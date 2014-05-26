// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

import com.risevision.common.client.info.PlaylistInfo;


@SuppressWarnings("serial")
public class PlaylistsInfo extends GridInfo implements Serializable {
	private String companyId;
	private ArrayList<PlaylistInfo> playlists;
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public ArrayList<PlaylistInfo> getPlaylists() {
		return playlists;
	}
	
	public void setPlaylists(ArrayList<PlaylistInfo> playlists) {
		this.playlists = playlists;
	}
	
	public void clearData(){
		playlists = null;
	}
}
