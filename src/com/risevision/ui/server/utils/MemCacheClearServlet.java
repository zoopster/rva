// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.risevision.ui.server.data.DataService;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

@SuppressWarnings("serial")
public class MemCacheClearServlet extends HttpServlet {
	protected static final Logger log = Logger.getAnonymousLogger();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		CacheFactory cacheFactory;
		try {
			cacheFactory = CacheManager.getInstance().getCacheFactory();
			Cache cache;
			cache = cacheFactory.createCache(Collections.emptyMap());
			
			cache.clear();
			
			DataService.getInstance().saveConfig();
		} catch (CacheException e) {
			log.log(Level.SEVERE, "Error instantiating Cache");
		}
		log.log(Level.WARNING, "Cleared the Cache");
	}

}
