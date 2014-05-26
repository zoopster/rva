package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.DemoContentInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("company")
public interface CompanyService extends RemoteService {
	CompanyInfo getCompany(String companyId) throws ServiceFailedException;
	CompanyInfo saveCompany(CompanyInfo company) throws ServiceFailedException;
	void deleteCompany(String companyId) throws ServiceFailedException;
//	CompaniesInfo getCompanies(String parentCompanyId, CompaniesInfo companiesInfo) throws ServiceFailedException;
	
	CompanyInfo getCompanyByAuthKey(String companyAuthKey) throws ServiceFailedException;
	RpcResultInfo moveCompanyByAuthKey(String companyId, String companyAuthKey) throws ServiceFailedException;
	
//	ArrayList<CompanyInfo> getPnoCompanies(String companyId, String excludeCompanyId) throws ServiceFailedException;
	
//	CompanyInfo getPnoSettings(String companyId) throws ServiceFailedException;
	void savePnoSettings(CompanyInfo company) throws ServiceFailedException;
	
//	String getCompanyAuthKey(String companyId) throws ServiceFailedException;
	String resetCompanyAuthKey(String companyId) throws ServiceFailedException;
	String resetCompanyClaimId(String companyId) throws ServiceFailedException;

	String resetAlertsURL(String companyId) throws ServiceFailedException;
	
	DemoContentInfo getDemoContent(String companyId, int contentWidth, int contentHeight) throws ServiceFailedException;
//	ArrayList<DemoContentInfo> getDemoContent(String companyId) throws ServiceFailedException;
	RpcResultInfo saveDemoContent(String companyId, DemoContentInfo demoContent) throws ServiceFailedException;
	RpcResultInfo deleteDemoContent(String companyId, int contentWidth, int contentHeight) throws ServiceFailedException;
}
