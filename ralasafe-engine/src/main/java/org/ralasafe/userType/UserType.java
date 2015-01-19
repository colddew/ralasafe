/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.userType;

import org.ralasafe.metadata.user.UserMetadata;

public class UserType {
	private String name;

	private String desc;

	private UserMetadata userMetadata;

	private String userMetadataXML;

	private String srcFile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public UserMetadata getUserMetadata() {
		return userMetadata;
	}

	public void setUserMetadata(UserMetadata userMetadata) {
		this.userMetadata = userMetadata;
	}

	public String getUserMetadataXML() {
		return userMetadataXML;
	}

	public void setUserMetadataXML(String userMetadataXML) {
		this.userMetadataXML = userMetadataXML;
	}

	public String toString() {
		return "name: " + this.name + "\n" + "desc: " + this.desc + "\n"
				+ "userMetadataXML: " + this.userMetadataXML;
	}

	public String getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}

}
