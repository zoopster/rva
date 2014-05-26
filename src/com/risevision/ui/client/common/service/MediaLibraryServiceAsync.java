// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

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
