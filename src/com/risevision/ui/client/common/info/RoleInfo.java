package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

import com.risevision.core.api.types.UserRole;

@SuppressWarnings("serial")
public class RoleInfo implements Serializable {
	private String name;
	private String description;
	private String info;

	public RoleInfo() {
	}
	
	public RoleInfo(String name, String description, String info) {
		this.name = name;
		this.description = description;
		this.info = info;
	}
		
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getInfo() {
		return info;
	}
	
	public static ArrayList<String> getStandardRoles() {
		ArrayList<String> roles = new ArrayList<String>();
		
		roles.add(UserRole.CONTENT_EDITOR);
		roles.add(UserRole.CONTENT_PUBLISHER);
		roles.add(UserRole.DISPLAY_ADMINISTRATOR);
		roles.add(UserRole.USER_ADMINISTRATOR);
		
		return roles;
	}
}
