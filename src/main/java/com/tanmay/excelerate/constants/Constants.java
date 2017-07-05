package com.tanmay.excelerate.constants;

import java.util.HashMap;

/**
 * @author : tanmay
 * @created : 05-Jul-2017
 */
public class Constants {
	public static final HashMap<String, String> REPORT_TYPE_NAME_MAPPING;
	
	static {
		REPORT_TYPE_NAME_MAPPING = new HashMap<String, String>();
		REPORT_TYPE_NAME_MAPPING.put("m", "MONTHLY");
		REPORT_TYPE_NAME_MAPPING.put("d", "DAILY");
		REPORT_TYPE_NAME_MAPPING.put("w", "WEEKLY");
		REPORT_TYPE_NAME_MAPPING.put("mw", "WEEKLY");
	}
}
