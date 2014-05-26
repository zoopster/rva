package com.risevision.ui.client.common.widgets;


public class TimeZoneWidget extends RiseListBox{
	public TimeZoneWidget() {
		loadData();
	}

	private void loadData() {
		addItem("< Select Time Zone >", "");
		addItem("(GMT -12:00) International Dateline West", "-720");
		addItem("(GMT -11:00) Midway Island, Samoa", "-660");
		addItem("(GMT -10:00) Hawaii", "-600");
		addItem("(GMT -09:00) Alaska", "-540");
		addItem("(GMT -08:00) Pacific Time (US & Canada); Tijuana", "-480");
		addItem("(GMT -07:00) Mountain Time (US & Canada)", "-420");
		addItem("(GMT -06:00) Central Time (US & Canada)", "-360");
		addItem("(GMT -05:00) Eastern Time (US & Canada)", "-300");
		addItem("(GMT -04:00) Atlantic Time (Canada)", "-240");
		addItem("(GMT -03:30) NewfoundLand Time (Canada)", "-210");
		addItem("(GMT -03:00) Buenos Aires, Georgetown", "-180");
		addItem("(GMT -02:00) Mid-Atlantic", "-120");
		addItem("(GMT -01:00) Cape Verde Is.", "-60");
		addItem("(GMT  00:00) Dublin, Edinburgh, Lisbon, London", "0");
		addItem("(GMT +01:00) Amsterdam, Berlin, Bern, Rome, Paris, Stockholm, Vienna", "60");
		addItem("(GMT +02:00) Athens, Bucharest, Istanbul, Minsk", "120");
		addItem("(GMT +03:00) Moscow, St. Petersburg, Volgograd", "180");
		addItem("(GMT +03:30) Tehran", "210");
		addItem("(GMT +04:00) Abu Dhabi, Muscat", "240");
		addItem("(GMT +04:30) Kabul", "270");
		addItem("(GMT +05:00) Islamabad, Karachi, Tashkent", "300");
		addItem("(GMT +05:30) Calcutta, Chennai, Mumbai,New Delhi", "330");
		addItem("(GMT +05:45) Kathmandu", "345");
		addItem("(GMT +06:00) Astana,Almaty, Dhaka, Novosibirsk", "360");
		addItem("(GMT +06:30) Rangoon (Yangon, Burma)", "390");
		addItem("(GMT +07:00) Bangkok, Hanoi, Jakarta", "420");
		addItem("(GMT +08:00) Beijing, Chongqing, Hong Kong, Urumqi", "480");
		addItem("(GMT +09:00) Osaka, Sapporo, Tokyo", "540");
		addItem("(GMT +09:30) Adelaide, Darwin", "570");
		addItem("(GMT +10:00) Canberra, Melbourne, Sydney, Vladvostok", "600");
		addItem("(GMT +11:00) Magadan, Solomon Is., New Caledonia", "660");
		addItem("(GMT +12:00) Auckland, Fiji, Kamchatka, Marshall Is.", "720");
		addItem("(GMT +13:00) Nuku'alofa", "780");
		
		setSelectedIndex(0);
	}

}