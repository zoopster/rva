// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MediaItemsInfo extends ScrollingGridInfo implements Serializable {
	private String companyId;
	private ArrayList<MediaItemInfo> mediaItems;
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public ArrayList<MediaItemInfo> getMediaItems() {
		return mediaItems;
	}
	
	public void setMediaItems(ArrayList<MediaItemInfo> mediaItems) {
		this.mediaItems = mediaItems;
	}
	
	public void clearData(){
		mediaItems = null;
	}
}
