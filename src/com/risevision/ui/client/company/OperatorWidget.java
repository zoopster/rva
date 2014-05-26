package com.risevision.ui.client.company;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.directory.CompanyDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.CompaniesInfo;
import com.risevision.ui.client.common.info.CompanyInfo;

@Deprecated
public class OperatorWidget extends HorizontalPanel {
	private static OperatorWidget instance;
	
	private RpcCallBackHandler rpcPnoCallBackHandler = new RpcCallBackHandler();

	private Label companyLabel = new Label();
//	private Anchor changeCompanyLink = new Anchor("Change");
	
//	private Command selectCommand;
	private String operatorId;

	public OperatorWidget() {
		add(companyLabel);
//		add(new SpacerWidget());
//		add(changeCompanyLink);
		
		styleControls();
		initActions();
	}
	
	private void styleControls() {
//		companyLabel.addStyleName("rdn-OverflowElipsis");
//		companyLabel.setWidth("175px");
	}
	
	private void initActions() {
//		changeCompanyLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				OperatorSelectWidget.getInstance().show();
//			}
//		});
		
//		selectCommand = new Command() {
//			@Override
//			public void execute() {
//				updateWidget();
//			}
//		};
	}
	
	public static OperatorWidget getInstance() {
		try {
			if (instance == null)
				instance = new OperatorWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public void loadPnoCompanies(CompanyInfo companyInfo) {
		if (RiseUtils.strIsNullOrEmpty(companyInfo.getParentId())) {
			companyLabel.setText("");
			return;
		}
		
		operatorId = companyInfo.getParentId();
		
		loadOperatorName();
		
//		OperatorSelectWidget.getInstance().init(companyInfo, selectCommand);
	}

//	private void updateWidget() {
//		CompanyInfo parentCompany = OperatorSelectWidget.getInstance().getCurrentCompany();
//		
//		if (parentCompany != null) {
//			companyLabel.setText(parentCompany.getName());
//			operatorId = parentCompany.getId();
//		}
//	}

	public String getOperatorId() {
		return operatorId;
	}
	
	private void loadOperatorName() {
		CompanyDataController controller = CompanyDataController.getInstance();
		String companyId = null;
		
		if (UserAccountController.getInstance().getUserInfo() != null) {
			companyId = UserAccountController.getInstance().getUserInfo().getCompany();
		}

		controller.getNetworkOperator(companyId, operatorId, rpcPnoCallBackHandler);
	}
	
	class RpcCallBackHandler extends RiseAsyncCallback<CompaniesInfo> {
		public void onFailure() {

		}
		
		public void onSuccess(CompaniesInfo result) {
			if (result != null && result.getCompanies() != null) {
				for (CompanyInfo company: result.getCompanies()) {
					companyLabel.setText(company.getName());
					return;
				}
			}
		}
	}
}