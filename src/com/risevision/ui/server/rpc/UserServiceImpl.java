// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.rpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.restlet.data.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.Global;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.UserAttribute;
import com.risevision.core.api.types.UserStatus;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.EmailInfo;
import com.risevision.ui.client.common.info.PrerequisitesInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.SystemMessageInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.service.UserService;
import com.risevision.ui.server.RiseRemoteServiceServlet;
import com.risevision.ui.server.data.DataService;
import com.risevision.ui.server.data.PersistentConfigurationInfo;
import com.risevision.ui.server.data.PersistentOAuthInfo;
import com.risevision.ui.server.data.PersistentUserInfo;
import com.risevision.ui.server.data.PersistentOAuthInfo.OAuthType;
import com.risevision.ui.server.utils.ServerUtils;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RiseRemoteServiceServlet implements
		UserService {

	public PrerequisitesInfo getCurrent(String requestUrl) throws ServiceFailedException {
		PrerequisitesInfo prereqInfo = new PrerequisitesInfo();
		PersistentConfigurationInfo pConfig = DataService.getInstance().getConfig();
		PersistentOAuthInfo pOAuth = DataService.getInstance().getOAuth(OAuthType.user);
		PersistentUserInfo pUser = ServerUtils.getPersistentUser();
		
		prereqInfo.setConfiguration(pConfig.getConfigurationInfo());
		prereqInfo.setOAuth(pOAuth.getOAuthInfo());
		prereqInfo.setUserOAuth(pUser.getUserOAuthInfo());
		prereqInfo.setServerTimestamp(new Date().getTime());
		
//		if (!ServerUtils.isUserLoggedIn()) {
//			prereqInfo.setLoginUrl(ServerUtils.getLoginURL(requestUrl + (requestUrl.contains("#") ? "/" : "#") + "register"));
//			if (requestUrl.contains("#")) {
//				this.getThreadLocalRequest().getSession().setAttribute("urlHash", requestUrl.substring(requestUrl.indexOf("#")));
//			}
//		}
//		else {
			try {
				login(prereqInfo);
			}
			catch (ServiceFailedException e) {
				// Deleted user
				if (e.getReason() == ServiceFailedException.ENTITY_GONE) {
					prereqInfo.setCurrentUserStatus(PrerequisitesInfo.STATUS_DELETED);
				}
				// New user
				else if (e.getReason() == ServiceFailedException.NOT_FOUND) {
//					throw new ServiceFailedException(ServiceFailedException.AUTHENTICATION_FAILED);
					prereqInfo.setCurrentUserStatus(PrerequisitesInfo.STATUS_NEW);
				}
				// This case implies that the wrong account is being used to access the User entity
				else if (e.getReason() == ServiceFailedException.FORBIDDEN) {
					prereqInfo.setCurrentUserStatus(PrerequisitesInfo.STATUS_WRONG_USERNAME);
				}
				// OAuth error
				else if (e.getReason() == ServiceFailedException.AUTHENTICATION_FAILED) {
					throw e;
				}
				else {
					prereqInfo.setCurrentUserStatus(PrerequisitesInfo.STATUS_ERROR);
					return prereqInfo;
				}
			}
					
			if (prereqInfo.getCurrentUser() != null){
				// set last login
//				prereqInfo.getCurrentUser().setLastLogin(new Date());
				
				// save user's id and company id
//				if (RiseUtils.strIsNullOrEmpty(pUser.getId()) || RiseUtils.strIsNullOrEmpty(pUser.getCompany())) {
//					pUser.setId(prereqInfo.getCurrentUser().getId());
//					pUser.setCompany(prereqInfo.getCurrentUser().getCompany());
//					
//					DataService.getInstance().saveUser(pUser);
//				}
				
			}
			else { // unregistered user
				prereqInfo.setCurrentUser(new UserInfo());
				prereqInfo.getCurrentUser().setUserName(ServerUtils.getGoogleUsername());
				prereqInfo.getCurrentUser().setEmail(ServerUtils.getGoogleUsername());
				
				if (prereqInfo.getCurrentUserStatus() == PrerequisitesInfo.STATUS_OK) {
					prereqInfo.setCurrentUserStatus(PrerequisitesInfo.STATUS_NEW);
				}
			}
					
//			prereqInfo.setLogoutUrl(ServerUtils.getLogoutURL(requestUrl));
			
//		}
			
		return prereqInfo;

	}
	
	private void login(PrerequisitesInfo prereqInfo) throws ServiceFailedException {		
		String url = createLoginResource(ServerUtils.getGoogleUsername());
		Document d;

		d = get(url);

		if (d != null) {
			UserInfo user;

			user = parseFind(d);
			prereqInfo.setCurrentUser(user);
			
			CompanyInfo company;
			
			company = CompanyServiceImpl.DocToCompany(d);
			prereqInfo.setCurrentUserCompany(company);
				
			prereqInfo.setSystemMessages(parseSystemMessages(d));

		}
		
	}
	
	public UserInfo getUser(String companyId, String userId) throws ServiceFailedException {
		String url = createCompanyUserResource(userId,	companyId);
		Document d;
		UserInfo user;

		d = get(url);

		if (d != null)
		{
			user = parseFind(d);
			return user;
		}

		return null;
	}
	
//	public UserInfo getUser(String username) throws ServiceFailedException {		
//		String url = createLookupUserResource(username);
//		Document d;
//		UserInfo user;
//
//		d = get(url);
//
//		if (d != null)
//		{
//			user = parseFind(d);
//			return user;
//		}
//		
//		return null;
//	}

//	public UsersInfo getUsers(UsersInfo usersInfo) throws ServiceFailedException {
//		String bookmark = null;
//		
//		bookmark = getUsersMap(usersInfo);
//		
//		if (bookmark != null){
//			String url = createUsersResource(usersInfo.getCompanyId(), bookmark);
//			Document d;
//	
//			d = get(url);
//
//			if (d != null) {
//				usersInfo.setUsers(parseFindAll(d));
//				return usersInfo;
//			}
//		}
//
//		usersInfo.clearData();
//		return usersInfo;
//	}

//	public String getUsersMap(UsersInfo usersInfo) throws ServiceFailedException {
//		String url = createUsersGridResource(usersInfo);
//		Document d;
//
//		d = get(url);
//
//		if (d != null) {
//			return ServerUtils.getGridBookmark(d, (GridInfo) usersInfo);
//		}
//
//		return null;
//	}	

    public RpcResultInfo putUser(String companyId, UserInfo user) throws ServiceFailedException {
		// new user- generate userId
		if (user.getId() == null || user.getId().isEmpty())
			user.setId(UUID.randomUUID().toString());

		String url = createCompanyUserResource(user.getId(), companyId);

		Form form = new Form();
		
		// if Last Login is being updated, don't add anything else to form
//		if (user.getLastLogin() != null){
//			form.add(UserAttribute.LAST_LOGIN, ServerUtils.dateToRfc822(user.getLastLogin()));
//		}
//		else {
			form.add(UserAttribute.ID, user.getId());
			form.add(UserAttribute.COMPANY_ID, user.getCompany());

			form.add(UserAttribute.USERNAME, user.getUserName());
			form.add(UserAttribute.FIRST_NAME, user.getFirstName());
			form.add(UserAttribute.LAST_NAME, user.getLastName());
			form.add(UserAttribute.TELEPHONE, user.getTelephone());
			form.add(UserAttribute.EMAIL, user.getEmail());
			form.add(UserAttribute.MAIL_SYNC_ENABLED, user.isMailSyncEnabled() ? Global.TRUE: Global.FALSE);
	
			form.add(UserAttribute.ROLES, RiseUtils.listToString(user.getRoles(), ","));
	        form.add(UserAttribute.SHOW_TUTORIAL, user.isShowTutorial() ? Global.TRUE: Global.FALSE);

			if (user.getTermsAcceptedDate() != null) {
				form.add(UserAttribute.TERMS_ACCEPTANCE_DATE, ServerUtils.dateToRfc822(user.getTermsAcceptedDate()));
			}
			
			form.add(UserAttribute.STATUS, Integer.toString(user.getStatus()));
//		}
		
//		int status = put(url, form);
		
		try {
			put(url, form);
		} catch (ServiceFailedException e) {
			if (e.getReason() == ServiceFailedException.CONFLICT) {
				return new RpcResultInfo("");
			}
			else {
				throw e;
			}
		}
		
//		if (status != 409){
		return new RpcResultInfo(user.getId());
//		}	
//		else 
//		{
			// 400 status means Duplicate Username
//			return new RpcResultInfo("");
//		}
	}
    
	public RpcResultInfo updateLastLogin(String companyId, UserInfo user) throws ServiceFailedException {
		if (user.getId() == null || user.getId().isEmpty())
			return null;

		String url = createCompanyUserResource(user.getId(), companyId);

		Form form = new Form();
		
		// if Last Login is being updated, don't add anything else to form
		form.add(UserAttribute.LAST_LOGIN, ServerUtils.dateToRfc822(new Date()));
		
		if (user.getTermsAcceptedDate() != null) {
			form.add(UserAttribute.TERMS_ACCEPTANCE_DATE, ServerUtils.dateToRfc822(user.getTermsAcceptedDate()));
		}
		
		put(url, form);
		
		return new RpcResultInfo();
	}
	
	public RpcResultInfo deleteUser(String companyId, String userId) throws ServiceFailedException {
		if ((userId == null) || (userId.isEmpty()))
			return new RpcResultInfo("null", "User ID is null or empty");

		String url = createCompanyUserResource(userId, companyId);
		
		delete(url);
		return new RpcResultInfo();
	}
    
	@Override
	public void sendEmail(EmailInfo email) {
        Properties props = new Properties();
        Session session = Session.getInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            
            if (email.getFromString() != null && !email.getFromString().isEmpty()) {
            	msg.setFrom(new InternetAddress(email.getFromString()));
            }
            else {
	            String fromEmail = ServerUtils.getGoogleUsername();
	            if (fromEmail != null && RiseUtils.isEmail(fromEmail))
	            	msg.setFrom(new InternetAddress(fromEmail));
	            else
	            	msg.setFrom(new InternetAddress("noreply@risevision.com"));
            }
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getToAddressString()));

            msg.setSubject(email.getSubjectString());
            msg.setText(email.getMessageString());
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
			e.printStackTrace();
        } catch (MessagingException e) {
            // ...
			e.printStackTrace();
        }
	}
	
	public String getLogoutURL(String URL) {
		return ServerUtils.getLogoutURL(URL);
	}

//	public String getLoginURL(String URL) {
//		return ServerUtils.getLoginURL(URL);
//	}
	
	private String createLoginResource(String username) {
		// /users/login/?username=userName

		if (username != null) {
			return "/users/login?username=" + username;
		}
		else return "/users/login";
	}	
	
//	private String createLookupUserResource(String username) {
//		// /users/lookup/?username=userName
//
//		if (username != null) {
//			return "/users/lookup?username=" + username;
//		}
//		else return "/users/lookup";
//	}	
	
	private String createCompanyUserResource(String userId, String companyId) {
		return "/company/" + companyId + "/user/" + userId;
	}
	
//	private String createUsersGridResource(UsersInfo usersInfo) {
//		// -- /company/{companyId}/users/pagemap?sortBy=sortByFieldName&searchFor=searchString&pageSize=recordsPerPage
//		
//		String resourceUrl = "/company/" + usersInfo.getCompanyId() + "/users/pagemap?";
//		if (!RiseUtils.strIsNullOrEmpty(usersInfo.getSortBy())){
//			resourceUrl = resourceUrl + "sortBy=" + usersInfo.getSortBy() + "&";
//		}
//		else
//			resourceUrl = resourceUrl + "sortBy=" + UserAttribute.LAST_NAME + "&"; //sortBy is Required
//			
//		if (!RiseUtils.strIsNullOrEmpty(usersInfo.getSearchFor())){
//			resourceUrl = resourceUrl + "searchFor=" + usersInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(usersInfo.getPageSize());
//		
//		return resourceUrl;
//	}
	
//	public String createUsersResource(String companyId, String bookmark) {
//		return "/company/" + companyId + "/users?bookmark=" + bookmark;
//	}

	private UserInfo parseFind(Document doc) {

		try {
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("user");

			Node fstNode = nodeList.item(0);

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				UserInfo user = new UserInfo();
				Element fstElmnt = (Element) fstNode;

				user.setId(ServerUtils.getNode(fstElmnt, UserAttribute.ID));
				user.setUserName(ServerUtils.getNode(fstElmnt, UserAttribute.USERNAME));
//				user.setUserId(ServerUtils.getNode(fstElmnt, UserAttribute.USER_ID));
				user.setFirstName(ServerUtils.getNode(fstElmnt, UserAttribute.FIRST_NAME));
				user.setLastName(ServerUtils.getNode(fstElmnt, UserAttribute.LAST_NAME));
				user.setTelephone(ServerUtils.getNode(fstElmnt, UserAttribute.TELEPHONE));
				user.setEmail(ServerUtils.getNode(fstElmnt, UserAttribute.EMAIL));
				// set to "TRUE" by default
				user.setMailSyncEnabled(!Global.FALSE.equals(ServerUtils.getNode(fstElmnt, UserAttribute.MAIL_SYNC_ENABLED)));
				// tutorial set "TRUE" by default
				user.setShowTutorial(!Global.FALSE.equals(ServerUtils.getNode(fstElmnt, UserAttribute.SHOW_TUTORIAL)));
				
				user.setCompany(ServerUtils.getNode(fstElmnt, UserAttribute.COMPANY_ID));
				user.setStatus(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, UserAttribute.STATUS), UserStatus.ACTIVE));
				
				NodeList rolesNodeList = fstElmnt.getElementsByTagName(UserAttribute.ROLE);
				ArrayList<String> rolesList = new ArrayList<String>();
				for (int s = 0; s < rolesNodeList.getLength(); s++) {
					Node rolesNode = rolesNodeList.item(s);

					if (rolesNode.getNodeType() == Node.ELEMENT_NODE) {
						String role = rolesNode.getFirstChild().getNodeValue();

						rolesList.add(role);
					}
				}
				user.setRoles(rolesList);

				NodeList optionsNodeList = fstElmnt.getElementsByTagName(UserAttribute.OPTION);
				ArrayList<String> optionsList = new ArrayList<String>();
				for (int s = 0; s < optionsNodeList.getLength(); s++) {
					Node optionsNode = optionsNodeList.item(s);

					if (optionsNode.getNodeType() == Node.ELEMENT_NODE) {
						String option = optionsNode.getFirstChild().getNodeValue();

						optionsList.add(option);
					}
				}

				Date termsAcceptedDate = ServerUtils.strToDate(ServerUtils.getNode(fstElmnt,UserAttribute.TERMS_ACCEPTANCE_DATE));
				user.setTermsAccepted(termsAcceptedDate != null && termsAcceptedDate.getTime() != 0);
				user.setLastLogin(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt,UserAttribute.LAST_LOGIN)));
				
				user.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
				user.setChangedDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE)));
				
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	private ArrayList<UserInfo> parseFindAll(Document doc) {
//		ArrayList<UserInfo> users = new ArrayList<UserInfo>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName("user");
//
//			for (int s = 0; s < nodeList.getLength(); s++) {
//
//				Node fstNode = nodeList.item(s);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					UserInfo user = new UserInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					user.setId(ServerUtils.getNode(fstElmnt, UserAttribute.ID));
//					user.setUserName(ServerUtils.getNode(fstElmnt, UserAttribute.USERNAME));
//					user.setFirstName(ServerUtils.getNode(fstElmnt,	UserAttribute.FIRST_NAME));
//					user.setLastName(ServerUtils.getNode(fstElmnt, UserAttribute.LAST_NAME));
//					user.setCompany(ServerUtils.getNode(fstElmnt, UserAttribute.COMPANY));
//					user.setStatus(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, UserAttribute.STATUS), UserStatus.ACTIVE));
//
//					user.setLastLogin(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt,
//							UserAttribute.LAST_LOGIN)));
//
//					users.add(user);
//				}
//
//			}
//			return users;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	
	private ArrayList<SystemMessageInfo> parseSystemMessages(Document doc) {
//		<messages>
//			<message>
//			This is a &lt;u&gt;test&lt;/u&gt; message. For more detail see &lt;a href='https://docs.google.com/document/d/1uBBuuPwBJtSoCxvHcOxflvXpEMIRK-TqGoetxbRfGx0/edit?usp=sharing'&gt;here&lt;/a&gt;.
//			</message>
//		</messages>
		try {
			ArrayList<SystemMessageInfo> systemMessages = new ArrayList<SystemMessageInfo>();
			
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("message");
			
			for (int s = 0; s < nodeList.getLength(); s++) {
				Node messageNode = nodeList.item(s);

				if (messageNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) messageNode;
					
					SystemMessageInfo message = new SystemMessageInfo();

					message.setText(ServerUtils.getNode(fstElmnt, SystemMessageInfo.TEXT));
					message.setStartDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, SystemMessageInfo.START_DATE)));
					message.setEndDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, SystemMessageInfo.END_DATE)));

					systemMessages.add(message);
				}
			}
			
			return systemMessages;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
