package com.risevision.ui.client.common.lists;

import java.util.ArrayList;
import java.util.Collections;

public class SearchSortController {

	public static ArrayList<SearchSortable> search(ArrayList<? extends SearchSortable> items, String query) {
		ArrayList<SearchSortable> result = new ArrayList<SearchSortable>();

		for (SearchSortable item : items) {
			if (item.search(query)) {
				result.add(item);
			}
		}

		return result;
	}

	public static ArrayList<SearchSortable> sort(ArrayList<? extends SearchSortable> list, String column, boolean direction) {		
		ArrayList<SearchSortable> result = new ArrayList<SearchSortable>(list);

		try {
			sort(result, column, 0, result.size() - 1);
		}
		catch (Exception e) {

		}
		
		if (!direction) {
			Collections.reverse(result);
		}
		
		return result;
	}

	
	/*
	 * @(#)MergeSortAlgorithm.java	1.0 95/06/23 Jason Harrison
	 *
	 * Copyright (c) 1995 University of British Columbia
	 *
	 * Permission to use, copy, modify, and distribute this software
	 * and its documentation for NON-COMMERCIAL purposes and without
	 * fee is hereby granted provided that this copyright notice
	 * appears in all copies. Please refer to the file "copyright.html"
	 * for further important copyright and licensing information.
	 *
	 * UBC MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
	 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
	 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
	 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UBC SHALL NOT BE LIABLE FOR
	 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
	 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
	 */

	/**
	 * A merge sort demonstration algorithm
	 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
	 *
	 * @author Jason Harrison@cs.ubc.ca
	 * @version 	1.1, 12 Jan 1998
	 */
	// URL - http://home.westman.wave.ca/~rhenry/sort/src/MergeSortAlgorithm.java
	private static void sort(ArrayList<SearchSortable> list, String column, int lo0, int hi0) throws Exception {
		int lo = lo0;
		int hi = hi0;
		if (lo >= hi) {
			return;
		}
		int mid = (lo + hi) / 2;

		/*
		 * Partition the list into two lists and sort them recursively
		 */
		sort(list, column, lo, mid);
		sort(list, column, mid + 1, hi);

		/*
		 * Merge the two sorted lists
		 */
		int end_lo = mid;
		int start_hi = mid + 1;
		while ((lo <= end_lo) && (start_hi <= hi)) {
			// a[lo] < a[start_hi]
			if (list.get(lo).compare(list.get(start_hi), column) < 0) {
				lo++;
			} else {
				/*
				 * a[lo] >= a[start_hi] The next element comes from the second
				 * list, move the a[start_hi] element into the next position and
				 * shuffle all the other elements up.
				 */
				SearchSortable T = list.get(start_hi);
				for (int k = start_hi - 1; k >= lo; k--) {
					list.set(k + 1, list.get(k));
				}
				list.set(lo, T);
				lo++;
				end_lo++;
				start_hi++;
			}
		}
	}

}
