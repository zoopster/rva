package com.risevision.ui.client;

import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Frame;
import com.risevision.ui.client.common.info.ManageSettingsInfo;

public class AdsenseBannerWidget extends Frame {
	private static AdsenseBannerWidget instance;
	
	private final String RISE_BANNER_ID = "ca-pub-2013654478569194";
	private final String RISE_BANNER_SLOT = "RVA_App";
//	private final String RISE_BANNER_SLOT = "RVA_Test";
	
	private String bannerId, bannerSlot;
//	private final String BANNER_HTML = "" +
//			"<html>" +
//			"<head>" +
//			"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
//			"<title></title>" +
//			"<script type='text/javascript' src='http://partner.googleadservices.com/gampad/google_service.js'></script>" +
//			"<script type='text/javascript'>" +
//			"GS_googleAddAdSenseService('%s1');" +
//			"GS_googleEnableAllServices();" +
//			"</script>" +
//			"<script type='text/javascript'>" +
//			"GA_googleAddSlot('%s1', '%s2');" +
//			"</script>" +
//			"<script type='text/javascript'>" +
//			"GA_googleFetchAds();" +
//			"</script>" +
//			"</head>" +
//			"<body style=\"margin:0px;\">" +
//			"<div>" +
//			"<script type='text/javascript'>" +
//			"GA_googleFillSlot('%s2');" +
//			"</script>" +
//			"</div>" +
//			"</body>" +
//			"</html>";
	
//	private Timer reloadTimer = new Timer() {
//		@Override
//		public void run() {
//			reloadItem();
//		}
//	};

	private AdsenseBannerWidget() {
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		getElement().setAttribute("scrolling", "no");
		setSize(ManageSettingsInfo.BANNER_WIDTH + "px", ManageSettingsInfo.BANNER_HEIGHT + "px");
	}

	public static AdsenseBannerWidget getInstance() {
		try {
			if (instance == null)
				instance = new AdsenseBannerWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public void renderItem() {
		renderItem(RISE_BANNER_ID, RISE_BANNER_SLOT);
		
//		reloadTimer.schedule(30 * 1000);
	}
	
	public void renderItem(String bannerId, String bannerSlot) {
		this.bannerId = bannerId;
		this.bannerSlot = bannerSlot;
		
		updateBanner();
	}
	
	public void updateBanner() {
		if (isVisible()) {
//			renderItem();
			
//			String htmlString = BANNER_HTML;
//			htmlString = htmlString.replace("%s1", bannerId);
//			htmlString = htmlString.replace("%s2", bannerSlot);
			
//			HtmlUtils.writeHtml(getElement(), htmlString);	
			
			setUrl("BannerPage.html?bannerId=" + bannerId + "&bannerSlot=" + bannerSlot + "&bannerUniqueId=" + (int)(Math.random() * 1000000) + "_" + new Date().getTime());
		}
	}
}
