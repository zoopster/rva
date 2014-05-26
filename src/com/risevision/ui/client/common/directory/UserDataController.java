package com.risevision.ui.client.common.directory;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.UserAttribute;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.info.UsersInfo;

public class UserDataController extends DataControllerBase {
	private static UserDataController instance;
	private final String[] userSearchCategories = new String[] {
			UserAttribute.FIRST_NAME,
			UserAttribute.LAST_NAME,
			UserAttribute.USERNAME,
			UserAttribute.EMAIL,
			UserAttribute.ROLES
			};
	
	public static UserDataController getInstance() {
		try {
			if (instance == null)
				instance = new UserDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (UserDataController) instance;
	}
	
	public UserDataController() {
		headerEntries.add(new HeaderEntry(UserAttribute.ID));
		headerEntries.add(new HeaderEntry(UserAttribute.COMPANY_ID));
		headerEntries.add(new HeaderEntry(UserAttribute.USERNAME));
		headerEntries.add(new HeaderEntry(UserAttribute.FIRST_NAME));
		headerEntries.add(new HeaderEntry(UserAttribute.LAST_NAME));
		headerEntries.add(new HeaderEntry(UserAttribute.TELEPHONE));
		headerEntries.add(new HeaderEntry(UserAttribute.EMAIL));
		headerEntries.add(new HeaderEntry(UserAttribute.ROLES));
		headerEntries.add(new HeaderEntry(UserAttribute.SHOW_TUTORIAL));
		headerEntries.add(new HeaderEntry(UserAttribute.STATUS));
		headerEntries.add(new HeaderEntry(UserAttribute.LAST_LOGIN));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
		
		mainSearchCategory = UserAttribute.USERNAME;
	}
	
	private class UsersDataResponse extends DataResponseBase {
		protected UsersInfo usersInfo;
		protected AsyncCallback<UsersInfo> callback;
		
		protected UsersDataResponse(UsersInfo usersInfo, AsyncCallback<UsersInfo> callback) {
			this.usersInfo = usersInfo;
			this.callback = callback;
		}
		
		protected void onResponseImpl(JavaScriptObject jsDataTable, String cursor) {
			usersInfo.setCursor(cursor);
			parseUsersData(jsDataTable, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getUsers(UsersInfo usersInfo, AsyncCallback<UsersInfo> callback) {
		String action = "/company/" + usersInfo.getCompanyId() + "/users";
		
		String query = createQuery(usersInfo, userSearchCategories);
		
		UsersDataResponse response = new UsersDataResponse(usersInfo, callback);
		
		getData(action, query, response);
	}

	protected void parseUsersData(JavaScriptObject jsDataTable, UsersDataResponse response) {
		
		UsersInfo usersInfo = response.usersInfo;
		
		ArrayList<UserInfo> users = usersInfo.getUsers();		
		if (usersInfo.getPage() == 0) {
			users = new ArrayList<UserInfo>();
		}
		
		for (int i = 0; i < getNumberOfRows(jsDataTable); i++) {
			
			UserInfo user = new UserInfo();
			int j = 0;
			
			user.setId(getValue(jsDataTable, i, headerEntries.get(j++).col));
			user.setCompany(getValue(jsDataTable, i, headerEntries.get(j++).col));
			user.setUserName(getValue(jsDataTable, i, headerEntries.get(j++).col));
			user.setFirstName(getValue(jsDataTable, i, headerEntries.get(j++).col));
			user.setLastName(getValue(jsDataTable, i, headerEntries.get(j++).col));
			user.setTelephone(getValue(jsDataTable, i, headerEntries.get(j++).col));
			user.setEmail(getValue(jsDataTable, i, headerEntries.get(j++).col));
			
			ArrayList<String> rolesList = new ArrayList<String>();
			String rolesString = getValue(jsDataTable, i, headerEntries.get(j++).col);
			if (rolesString == null) rolesString = "";
			String[] roles = rolesString.split(",");
			for (String role: roles) {
				rolesList.add(role);
			}
			user.setRoles(rolesList);
			
			user.setShowTutorial(getValueBoolean(jsDataTable, i, headerEntries.get(j++).col));
			user.setStatus(getValueInt(jsDataTable, i, headerEntries.get(j++).col));
			user.setLastLogin(getValueDate(jsDataTable, i, headerEntries.get(j++).col));
			user.setChangedBy(getValue(jsDataTable, i, headerEntries.get(j++).col));
			user.setChangedDate(getValueDate(jsDataTable, i, headerEntries.get(j++).col));
			
			users.add(user);
		}
		
		usersInfo.setUsers(users);
//		usersInfo.setCurrentPage(1);
//		usersInfo.setPageCount(1);
		
		response.callback.onSuccess(usersInfo);
	}
	
}
