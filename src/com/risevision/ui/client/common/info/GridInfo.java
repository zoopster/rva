// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GridInfo implements Serializable {
	public static final String NUMBER = "number";
	public static final String BOOKMARK = "bookmark";
	public static final String CHECKBOX = "#checkbox#";

	public static final int SORTACTION = 0;
	public static final int PAGEACTION = 1;
	
	public static final int SELECTACTION = 2;
	public static final int SEARCHACTION = 3;
//	public static final int DELETEACTION = 3;
	public static final int CHECKALLACTION = 4;
	public static final int CHECKACTION = 5;

	public static final int DEFAULTPAGESIZE = 20;

	private String sortBy;
	private String searchFor;
	private Integer pageSize = DEFAULTPAGESIZE;
	private Integer pageCount;
	private Integer currentPage = 1;

	public Integer getFirstPage() {
		if (pageCount > 0 && this.getPrevPage() > 1)
			return 1;
		return 0;
	}

	public Integer getLastPage() {
		if (pageCount > 0 && currentPage + 1 < pageCount)
			return pageCount;
		return 0;
	}

	public Integer getPrevPage() {
		if (currentPage > 1)
			return currentPage - 1;
		return 0;
	}

	public Integer getNextPage() {
		if (currentPage < pageCount)
			return currentPage + 1;
		return 0;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSearchFor() {
		return searchFor;
	}

	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getPageCount() {
		return pageCount;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}
