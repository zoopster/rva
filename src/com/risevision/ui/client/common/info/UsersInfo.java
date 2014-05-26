package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UsersInfo extends ScrollingGridInfo implements Serializable {
	private String companyId;
	private ArrayList<UserInfo> users;
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public ArrayList<UserInfo> getUsers() {
		return users;
	}
	
	public void setUsers(ArrayList<UserInfo> users) {
		this.users = users;
	}
	
	public void clearData(){
		users = null;
	}
}
