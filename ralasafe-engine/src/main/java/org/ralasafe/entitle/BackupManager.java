/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.io.OutputStream;
import java.util.Collection;

import org.ralasafe.EntityExistException;

public interface BackupManager {
	public Backup addBackup(Backup backup) throws EntityExistException;

	public void updateBackup(Backup backup);

	public Collection getBackups();
	
	public Backup getBackup(int id);

	public void exportBackup(int id, OutputStream out);
}
