// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("gadget")
public interface GadgetService extends RemoteService {
	//Gadget API
//	GadgetsInfo getGadgets(GadgetsInfo gadgetsInfo) throws ServiceFailedException;
//	GadgetsInfo getSharedGadgets(GadgetsInfo gadgetsInfo) throws ServiceFailedException;
	GadgetInfo getGadget(String companyId, String gadgetId) throws ServiceFailedException;
	GadgetInfo getGadget(String gadgetId) throws ServiceFailedException;
	RpcResultInfo putGadget(String companyId, GadgetInfo gadget) throws ServiceFailedException;
	RpcResultInfo deleteGadget(String companyId, String gadgetId) throws ServiceFailedException;

	String getGadgetXml(String gadgetUrl) throws Exception;
}
