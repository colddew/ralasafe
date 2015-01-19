/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.RalasafeException;
import org.ralasafe.SystemConstant;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.JavaBeanColumnAdapter;
import org.ralasafe.db.JavaBeanObjectNewer;
import org.ralasafe.db.SelectCondition;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableNewer;
import org.ralasafe.db.impl.TableDeletorImpl;
import org.ralasafe.db.impl.TableSaverImpl;
import org.ralasafe.db.impl.TableSelectorImpl;
import org.ralasafe.db.impl.TableUpdatorImpl;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.ZipUtil;

public class BackupManagerImpl implements BackupManager {
	private static Log log=LogFactory.getLog( BackupManagerImpl.class );
//	private static final String BACKUP_TABLE = "backup";
	private static String UPDATE_CONTENT_SQL;
	private static String SELECT_CONTENT_SQL;

//	private String appName;
	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdatorImpl updator;
	private TableDeletorImpl deleter;
	private Comparator comp;

	public BackupManagerImpl() {
		String appName="Ralasafe";
		UPDATE_CONTENT_SQL = "UPDATE " + appName+"_backup" + " SET content=? WHERE id=? ";
		SELECT_CONTENT_SQL = "SELECT content from " + appName+"_backup" + " WHERE id=? ";
		
		TableNewer tableNewer = new TableNewer();
		tableNewer.setTableName(appName+"_backup");
		tableNewer.setColumnNames(new String[] { "id", "description",
				"createTime" });
		tableNewer.setIdColumnNames(new String[] { "id" });
		tableNewer.setMappingClass(Backup.class.getName());
		tableNewer.put("id", new JavaBeanColumnAdapter("id", "int"));
		tableNewer.put("description", new JavaBeanColumnAdapter("description",
				"java.lang.String"));
		tableNewer.put("createTime", new JavaBeanColumnAdapter("createTime",
				"java.util.Date"));
		tableNewer.setId(DBPower.getTableId(null, tableNewer.getTableName()));
		table = tableNewer.getTable();
		selector = new TableSelectorImpl();
		selector.setObjectNewer(new JavaBeanObjectNewer(tableNewer
				.getMappingClass()));
		saver = new TableSaverImpl();
		updator = new TableUpdatorImpl();
		deleter = new TableDeletorImpl();
		selector.setTable(table);
		saver.setTable(table);
		updator.setTable(table);
		deleter.setTable(table);
		comp = new Comparator() {
			public int compare(Object o1, Object o2) {
				Backup q1 = (Backup) o1;
				Backup q2 = (Backup) o2;
				return q2.getCreateTime().compareTo(q1.getCreateTime());
			}
		};
	}

	public Backup addBackup(Backup backup) throws EntityExistException {
		backup.setId(newBackupId());
		Connection conn = null;
		try {
			conn = DBPower.getConnection(table.getId());
			conn.setAutoCommit(false);
			backup.setCreateTime(new Date());
			saver.save(conn, backup);
			importBackup(conn, backup);
			conn.commit();
		} catch (SQLException e) {
			log.error( "", e );
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error( "", e1 );
				throw new RalasafeException(e1);
			}
		} finally {
			DBUtil.close(conn);
		}

		return backup;
	}

	private void importBackup(Connection conn, Backup backup)
			throws DBLevelException {
		PreparedStatement pstmt = null;
		try {
			// create tmp zip
			String tempZip = SystemConstant.getRepositoryDir()
					+ new Random().nextLong() + ".zip";
			String xmlDir = SystemConstant.getRepositoryDir() + "ralasafe";
			ZipUtil.zip(tempZip, xmlDir);

			// update clob
			File zipFile = new File(tempZip);
			InputStream zipIn = new FileInputStream(zipFile);
			pstmt = conn.prepareStatement(UPDATE_CONTENT_SQL);
			pstmt.setBinaryStream(1, zipIn, (int) zipFile.length());
			pstmt.setInt(2, backup.getId());
			pstmt.executeUpdate();
			zipIn.close();

			// delete tmp zip
			zipFile.delete();

		} catch (Exception e) {
			log.error( "", e );
			throw new RalasafeException(e);
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public void updateBackup(Backup backup) {
		try {
			Backup old = getBackup(backup.getId());
			old.setDescription(backup.getDescription());
			backup = old;
			updator.updateByIdColumns(backup);
		} catch (EntityExistException e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}
	}

	public Collection getBackups() {
		Collection backups = selector.select(new SelectCondition(), null);
		SortedSet result = Collections.synchronizedSortedSet(new TreeSet(comp));
		result.addAll(backups);
		return result;
	}

	public void exportBackup(int id, OutputStream out) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBPower.getConnection(table.getId());
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(SELECT_CONTENT_SQL);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob(1);
				InputStream in = blob.getBinaryStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				in.close();
			}
			conn.commit();
		} catch (Exception e) {
			log.error( "", e );
		} finally {
			DBUtil.close(pstmt, conn);
		}
	}

	private int newBackupId() {
		try {
			int id = DBUtil.getSequenceNextVal(table, "id");

			while (id <= 0) {
				id = DBUtil.getSequenceNextVal(table, "id");
			}
			return id;
		} catch (SQLException e) {
			log.error( "", e );
			throw new DBLevelException(e);
		}
	}

	public Backup getBackup(int id) {
		Backup backup = new Backup();
		backup.setId(id);
		return (Backup) selector.selectByIdColumns(backup);
	}
}
