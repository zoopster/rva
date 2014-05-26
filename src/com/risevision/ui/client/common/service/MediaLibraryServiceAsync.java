package com.risevision.ui.client.common.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.ui.client.common.info.MediaItemsInfo;

/**
 * The async counterpart of <code>MediaLibraryService</code>.
 */
public interface MediaLibraryServiceAsync {
	void getBucketItems(String bucketName, AsyncCallback<MediaItemsInfo> callback);
	void createBucket(String bucketName, AsyncCallback<Void> callback);
//	void deleteMediaItem(String bucketName, String itemName, AsyncCallback<Void> callback);
	void deleteMediaItems(String bucketName, ArrayList<String> itemNames, AsyncCallback<Void> callback);
	void getSignedPolicy(String policyBase64, AsyncCallback<String> callback);
	
}
