package com.risevision.ui.client.common.lists;

public interface SearchSortable {
	public boolean search(String query);
	public int compare(SearchSortable item, String column);
}
