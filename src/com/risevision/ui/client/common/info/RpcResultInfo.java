package com.risevision.ui.client.common.info;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RpcResultInfo implements Serializable{

	private String Id;
//	private String parentId;
	private String errorMessage;

	public RpcResultInfo() {
	}
	
	public RpcResultInfo(String Id) {
		this.setId(Id);
	}

	public RpcResultInfo(String Id, String errorMessage) {
		this.setId(Id);
		this.setErrorMessage(errorMessage);
	}
	
//	public RpcResultInfo(String Id, String parentId, String errorMessage) {
//		this.setId(Id);
//		this.setParentId(parentId);
//		this.setErrorMessage(errorMessage);
//	}

	public void setId(String id) {
		Id = id;
	}

	public String getId() {
		return Id;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

//	public void setParentId(String parentId) {
//		this.parentId = parentId;
//	}
//
//	public String getParentId() {
//		return parentId;
//	}

}