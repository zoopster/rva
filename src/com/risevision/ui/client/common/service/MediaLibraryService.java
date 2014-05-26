// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.MediaItemsInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("mediaLibrary")
public interface MediaLibraryService extends RemoteService {
	MediaItemsInfo getBucketItems(String bucketName) throws ServiceFailedException;
	void createBucket(String bucketName) throws ServiceFailedException;
//	void deleteMediaItem(String bucketName, String itemName) throws ServiceFailedException;
	void deleteMediaItems(String bucketName, ArrayList<String> itemNames) throws ServiceFailedException;
	String getSignedPolicy(String policyBase64);
}
