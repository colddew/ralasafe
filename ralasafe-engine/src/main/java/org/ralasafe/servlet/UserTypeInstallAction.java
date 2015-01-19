/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
import org.ralasafe.ResourceConstants;
import org.ralasafe.SystemConstant;
import org.ralasafe.WebRalasafe;
import org.ralasafe.application.Application;
import org.ralasafe.application.ApplicationManager;
import org.ralasafe.metadata.user.UserMetadata;
import org.ralasafe.metadata.user.UserMetadataParser;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.userType.UserType;
import org.ralasafe.userType.UserTypeManager;
import org.ralasafe.util.Util;

public class UserTypeInstallAction extends Action {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (RalasafeController.isSecured()) {
			if (!WebRalasafe
					.hasPrivilege(req, Privilege.POLICY_ADMIN_ID)) {
				String errorMsg = Util.getMessage(req.getLocale(),ResourceConstants.NO_PRIVILEGE, 
													Util.getMessage(req.getLocale(), ResourceConstants.POLICY_ADMIN));
				req.setAttribute("errorMsg",errorMsg);
				throw new RalasafeException(errorMsg);
			}
		}

		Object[] values = extractValues(req);
		String op = (String) values[0];
		UserType userType = (UserType) values[1];
		userType.setName("ralasafe");

		Application app = new Application();
		app.setName("ralasafe");
		app.setDescription("ralasafe application");
		ArrayList userTypes = new ArrayList();
		userTypes.add(userType);
		app.setUserTypes(userTypes);

		UserTypeManager userTypeMng=Factory.getUserTypeManager();
		ApplicationManager appMng=Factory.getApplicationManager();
		
		if ("add".equalsIgnoreCase(op)) {
			try {
				userTypeMng.addUserType(userType);

				// Create application named ralasafe, and add this usertype to it
				try {
					appMng.addApplication(req.getLocale(), app);
				} catch (Exception e) {
					appMng.updateApplicatonUserType("ralasafe", userType);
				}
			} catch (EntityExistException e) {
			}
		} else if ("update".equalsIgnoreCase(op)) {
			userTypeMng.updateUserType(userType);
			appMng.updateApplicatonUserType("ralasafe", userType);
		}

		req.setAttribute("userType", userType);
		req.setAttribute("editable", Boolean.FALSE);
		WebUtil.forward( req, resp, "/ralasafe/userType/view.jsp");
	}

	/**
	 * 
	 * @param req
	 * @return Object[]{op<String>,userType<UserType>}
	 * @throws ServletException
	 */
	private Object[] extractValues(HttpServletRequest req)
			throws ServletException, IOException {
		String op = null;
		UserType userType = new UserType();
		
		String userTypeStoreDir=SystemConstant.getUserTypeStoreDir();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(userTypeStoreDir));
		ServletFileUpload upload = new ServletFileUpload(factory);

		List items;
		try {
			items = upload.parseRequest(req);
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}

		for (Iterator iter = items.iterator(); iter.hasNext();) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
				String fieldName = item.getFieldName();
				String value = item.getString();
/*				if( value!=null ) {
					value=new String(value.getBytes("ISO-8859-1"),"UTF-8");
				}*/

				if ("name".equalsIgnoreCase(fieldName)) {
					userType.setName(value);
				} else if ("desc".equalsIgnoreCase(fieldName)) {
					userType.setDesc(value);
				} else if ("op".equalsIgnoreCase(fieldName)) {
					op = value;
				}
			} else {
				String clientFile = item.getName();
				int index = clientFile.lastIndexOf("\\");
				if (index == -1) {
					index = clientFile.lastIndexOf("/");
				}
				String serverFile = userTypeStoreDir + File.separator
						+ sdf.format(new Date()) + "-"
						+ clientFile.substring(index + 1);
				userType.setSrcFile(serverFile);
				File f = new File(serverFile);
				;
				try {
					item.write(f);
				} catch (Exception e) {
					throw new ServletException(e);
				}

				UserMetadataParser parser = new UserMetadataParser();
				UserMetadata metadata = parser.parse(f.getAbsolutePath());
				userType.setUserMetadata(metadata);
			}
		}

		return new Object[] { op, userType };
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}