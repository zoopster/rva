package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.UserAttribute;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.info.UsersInfo;

@Deprecated
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
		
		protected void onResponseImpl(JSOModel jso) {
			parseUsersData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getUsers(UsersInfo usersInfo, AsyncCallback<UsersInfo> callback) {
		String action = "/company/" + usersInfo.getCompanyId() + "/users";
		String method = "GET"; 
		String query = createQuery(usersInfo, userSearchCategories);
		
		UsersDataResponse response = new UsersDataResponse(usersInfo, callback);
		
		getData(action, method, query, response);
	}

	protected void parseUsersData(JSOModel jsUsers, UsersDataResponse response) {
		JsArray<JSOModel> rows = jsUsers.getArray("rows");
		
		UsersInfo usersInfo = response.usersInfo;
		
		ArrayList<UserInfo> users = usersInfo.getUsers();		
		if (usersInfo.getPage() == 0) {
			users = new ArrayList<UserInfo>();
		}
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			UserInfo user = new UserInfo();
			int j = 0;
			
			user.setId(column.get(headerEntries.get(j++).col).get("v"));
			user.setCompany(column.get(headerEntries.get(j++).col).get("v"));
			user.setUserName(column.get(headerEntries.get(j++).col).get("v"));
			user.setFirstName(column.get(headerEntries.get(j++).col).get("v"));
			user.setLastName(column.get(headerEntries.get(j++).col).get("v"));
			user.setTelephone(column.get(headerEntries.get(j++).col).get("v"));
			user.setEmail(column.get(headerEntries.get(j++).col).get("v"));
			
			ArrayList<String> rolesList = new ArrayList<String>();
			String[] roles = column.get(headerEntries.get(j++).col).get("v", "").split(",");
			for (String role: roles) {
				rolesList.add(role);
			}
			user.setRoles(rolesList);
			
			user.setShowTutorial(column.get(headerEntries.get(j++).col).getBoolean("v"));
			user.setStatus(column.get(headerEntries.get(j++).col).getInt("v"));
			user.setLastLogin(column.get(headerEntries.get(j++).col).getDate("v"));
			user.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
			user.setChangedDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			users.add(user);
		}
		
		usersInfo.setUsers(users);
//		usersInfo.setCurrentPage(1);
//		usersInfo.setPageCount(1);
		
		response.callback.onSuccess(usersInfo);
	}
	
}
