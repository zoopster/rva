package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.DemoContentInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

/**
 * The async counterpart of <code>CompanyService</code>.
 */
public interface CompanyServiceAsync {
	void getCompany(String companyId, AsyncCallback<CompanyInfo> callback);
	void saveCompany(CompanyInfo company, AsyncCallback<CompanyInfo> callback);
	void deleteCompany(String companyId, AsyncCallback<Void> callback);
//	void getCompanies(String parentCompanyId, CompaniesInfo companiesInfo, AsyncCallback<CompaniesInfo> callback);
	
	void getCompanyByAuthKey(String companyAuthKey, AsyncCallback<CompanyInfo> callback);
	void moveCompanyByAuthKey(String companyId, String companyAuthKey, AsyncCallback<RpcResultInfo> callback);
	
//	void getPnoCompanies(String companyId, String excludeCompanyId, AsyncCallback<ArrayList<CompanyInfo>> callback);

//	void getPnoSettings(String companyId, AsyncCallback<CompanyInfo> callback);
	void savePnoSettings(CompanyInfo company, AsyncCallback<Void> callback);
	
//	void getCompanyAuthKey(String companyId, AsyncCallback<String> callback);
	void resetCompanyAuthKey(String companyId, AsyncCallback<String> callback);
	void resetCompanyClaimId(String companyId, AsyncCallback<String> callback);

	void resetAlertsURL(String companyId, AsyncCallback<String> callback);
	
	void getDemoContent(String companyId, int contentWidth, int contentHeight, AsyncCallback<DemoContentInfo> callback);
//	void getDemoContent(String companyId, AsyncCallback<ArrayList<DemoContentInfo>> callback);
	void saveDemoContent(String companyId, DemoContentInfo demoContent, AsyncCallback<RpcResultInfo> callback);
	void deleteDemoContent(String companyId, int contentWidth, int contentHeight, AsyncCallback<RpcResultInfo> callback);
}
