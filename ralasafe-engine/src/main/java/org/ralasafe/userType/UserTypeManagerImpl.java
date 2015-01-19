/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.userType;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
import org.ralasafe.application.Application;
import org.ralasafe.application.ApplicationUserType;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.FieldWhereElement;
import org.ralasafe.db.JavaBeanColumnAdapter;
import org.ralasafe.db.JavaBeanObjectNewer;
import org.ralasafe.db.SingleValueComparator;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableNewer;
import org.ralasafe.db.TableUpdator;
import org.ralasafe.db.impl.TableDeletorImpl;
import org.ralasafe.db.impl.TableSaverImpl;
import org.ralasafe.db.impl.TableSelectorImpl;
import org.ralasafe.db.impl.TableUpdatorImpl;
import org.ralasafe.metadata.user.UserMetadata;
import org.ralasafe.metadata.user.UserMetadataParser;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.StringUtil;

public class UserTypeManagerImpl implements UserTypeManager {
	private static Log logger=LogFactory.getLog( UserTypeManagerImpl.class );
	
	/**
	 * UserType cache store
	 */
	Map userTypeMap = new HashMap();
	private boolean changed=true;
	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdator updator;
	private TableDeletorImpl deletor;
	private FieldWhereElement nameWhereEmt;
	
	private TableDeletorImpl applicationUserTypeDeletor;
	private FieldWhereElement userTypeNameAppUserTypeTableWhereEmt;
	
	public UserTypeManagerImpl() {
		initUserTypeTableInfo();
		initApplicationUserTypeTableInfo();
	}

	private void initApplicationUserTypeTableInfo() {
		TableNewer newer = new TableNewer();
		newer.setTableName( "applicationusertype" );
		newer.setColumnNames(new String[] { "appName", "userTypeName","userMetadataStr" });
		newer.setIdColumnNames(new String[] { "appName", "userTypeName" } );
		//newer.setUniqueColumnNames(new String[] { "name" });
		newer.setMappingClass(ApplicationUserType.class.getName());
		newer.put("appName", new JavaBeanColumnAdapter("appName",
						"java.lang.String"));
		newer.put("userTypeName", new JavaBeanColumnAdapter("userTypeName",
				"java.lang.String"));
		newer.put("userMetadataStr", new JavaBeanColumnAdapter("userMetadataStr",
				"java.lang.String"));

		newer.setId(DBPower.getTableId(null, newer.getTableName()));
		Table appUserTypeTable = newer.getTable();
		applicationUserTypeDeletor = new TableDeletorImpl();
		applicationUserTypeDeletor.setTable(appUserTypeTable);
		
		userTypeNameAppUserTypeTableWhereEmt=new FieldWhereElement();
		userTypeNameAppUserTypeTableWhereEmt.setColumn( appUserTypeTable.getColumns()[1] );
		userTypeNameAppUserTypeTableWhereEmt.setCompartor( SingleValueComparator.EQUAL );
		userTypeNameAppUserTypeTableWhereEmt.setContextValue( true );
	}

	private void initUserTypeTableInfo() {
		TableNewer newer = new TableNewer();
		newer.setTableName( "usertype" );
		newer.setColumnNames(new String[] { "name", "description","userMetadataXML" });
		newer.setIdColumnNames(new String[] { "name" });
		//newer.setUniqueColumnNames(new String[] { "name" });
		newer.setMappingClass(UserType.class.getName());
		newer.put("name", new JavaBeanColumnAdapter("name",
						"java.lang.String"));
		newer.put("description", new JavaBeanColumnAdapter("desc",
				"java.lang.String"));
		newer.put("userMetadataXML", new JavaBeanColumnAdapter("userMetadataXML",
				"java.lang.String"));

		newer.setId(DBPower.getTableId(null, newer.getTableName()));
		table = newer.getTable();
		selector = new TableSelectorImpl();
		selector.setObjectNewer(new JavaBeanObjectNewer(newer.getMappingClass()));
		saver = new TableSaverImpl();
		updator = new TableUpdatorImpl();
		deletor = new TableDeletorImpl();
		selector.setTable(table);
		saver.setTable(table);
		updator.setTable(table);
		deletor.setTable(table);
		
		nameWhereEmt=new FieldWhereElement();
		nameWhereEmt.setColumn( table.getColumns()[0] );
		nameWhereEmt.setCompartor( SingleValueComparator.EQUAL );
		nameWhereEmt.setContextValue( true );
	}
	
	private synchronized void loadIntoMemory() {
		if( !changed ) {
			return;
		}
		
		Collection allUserTypes=selector.select( null, null );
		
		userTypeMap.clear();
		for( Iterator iter=allUserTypes.iterator(); iter.hasNext(); ) {
			UserType userType=(UserType) iter.next();
			extractUserMetadata( userType );
			userTypeMap.put( userType.getName(), userType );
		}
		
		changed=false;
	}
	
	private void preModify( UserType userType ) {
		String xml = getUserMetadataXML(userType);
		if( xml.length()>=1000&&xml.length()<=2000 ) {
			xml=StringUtils.rightPad( xml, 4000-xml.length() );
		}
		
		userType.setUserMetadataXML(xml);
	}
	
	public void addUserType(UserType userType) throws EntityExistException {
		loadIntoMemory();
		
		// check duplicated name
		if( userTypeMap.keySet().contains( userType.getName() ) ) {
			throw new EntityExistException("The name '" + userType.getName() + "' exists already.");
		}
		
		preModify( userType );	
		
		extractUserMetadata( userType );
		
		saver.save( userType );
		
		changed=true;
	}

	private String getUserMetadataXML(UserType userType) {
		int ch = 0;
		StringBuffer buf = new StringBuffer();
		try {
			FileReader reader = new FileReader(userType.getSrcFile());
			while ((ch = reader.read()) != -1) {
				buf.append((char) ch);
			}
			reader.close();
			return buf.toString();
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
	}

	public void deleteUserType(String name) {
		loadIntoMemory();
		
		Collection applications = getApplications(name);
		
		Connection conn=null;
		boolean autoCommit=true;
		try {
			conn=DBPower.getConnection( table.getId() );
			autoCommit=conn.getAutoCommit();
			conn.setAutoCommit( false );
			
			// drop application's user-role tables
			for(Iterator itr=applications.iterator();itr.hasNext();) {
				Application application = (Application) itr.next();
				String dropSql=DBUtil.userRoleTableDropSql(application.getName(), name);
				try {
					DBUtil.exec(conn, dropSql);
				} catch( SQLException e ) {
					logger.error( "Failed to delete table: " + dropSql );
				}				
			}
			
			// delete app-usertype relationship
			ApplicationUserType appUserTypeHint=new ApplicationUserType();
			appUserTypeHint.setUserTypeName( name );
			applicationUserTypeDeletor.delete( conn, userTypeNameAppUserTypeTableWhereEmt, appUserTypeHint );
			
			// delete UserType
			UserType userTypeHint=new UserType();
			userTypeHint.setName( name );
			deletor.deleteByIdColumns( conn, userTypeHint );
			
			conn.commit();
		} catch( SQLException e ) {
			DBUtil.rollback( conn );
			throw new DBLevelException( e );
		} finally {
			DBUtil.setCommitMode( conn, autoCommit );
			DBUtil.close( conn );
		}
		
		//userTypeMap.remove(name);
		changed=true;
		// notify Factory
		Factory.userTypeChanged(name);
	}

	public Collection getAllUserTypes() {
		loadIntoMemory();
		
		return userTypeMap.values();
	}

	public UserType getUserType(String name) {
		loadIntoMemory();
		
		return (UserType) userTypeMap.get(name);		
	}

	/**
	 * Extract UserMetadata from userType's xml definition,
	 * and validate the infomation
	 * @param userType
	 */
	private void extractUserMetadata(UserType userType) {
		// parse
		if (userType.getUserMetadataXML() != null) {
			UserMetadataParser parser = new UserMetadataParser();
			UserMetadata userMetadata=parser.parse(new ByteArrayInputStream(
					userType.getUserMetadataXML().getBytes()));
			
			String validInfo=userMetadata.getValidInfo();
			if( !StringUtil.isEmpty( validInfo ) ) {
				System.out.println( "Error: user metadata is invalid! name=" + userType.getName() );
				System.out.println( validInfo );
				System.out.println( "userMetadataStr=" );
				System.out.println( userType.getUserMetadataXML() );
				throw new RalasafeException( "User metadata is invalid! " + validInfo );
			}
			
			userType.setUserMetadata(userMetadata);
		}
	}

	public void updateUserType(UserType userType) {
		preModify( userType );
		
		extractUserMetadata( userType );
		
		try {
			updator.updateByIdColumns( userType );
		} catch( EntityExistException e ) {
			throw new DBLevelException( e );
		}
		
		changed=true;

		// notify Factory
		Factory.userTypeChanged(userType.getName());
	}

	public Collection getApplications(String userTypeName) {
		loadIntoMemory();
		
		Set applications = new HashSet();

		Iterator appItr = Factory.getApplicationManager().getAllApplications()
				.iterator();
		while (appItr.hasNext()) {
			Application application = (Application) appItr.next();
			Iterator itr = application.getUserTypes().iterator();
			while (itr.hasNext()) {
				UserType userType = (UserType) itr.next();
				if (userType.getName().equals(userTypeName))
					applications.add(application);
			}
		}
		return applications;
	}

	public UserType getUserTypeCopy(String name) {
		loadIntoMemory();
		
		UserType cacheOne=(UserType) userTypeMap.get( name );
		if( cacheOne==null ) {
			return null;
		} else {
			UserType copy=new UserType();
			copy.setDesc( cacheOne.getDesc() );
			copy.setName( cacheOne.getName() );
			copy.setSrcFile( cacheOne.getSrcFile() );
			copy.setUserMetadataXML( cacheOne.getUserMetadataXML() );
			extractUserMetadata( copy );
			
			return copy;
		}
	}

}
