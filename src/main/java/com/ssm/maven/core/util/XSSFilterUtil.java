package com.ssm.maven.core.util;

public class XSSFilterUtil {
	private static String characterParams = "',@,|,<![CDATA[&]]>,#,$,%,\",\\',"
			+ "\\\",<![CDATA[<]]>,>,\\,Eval,Document,Cookie,Javascript,Script,onerror";
	
	public static String[] getStrArrays() {
		return characterParams.split(",");
	}
	
	public static boolean doFilter(String filter) {
		boolean status = false;  
		String[] strArrays = getStrArrays();
		for (int i = 0; i < strArrays.length; i++) {
			if(filter.indexOf(strArrays[i])>=0) {
				status = true;
				break;
			}
			if(status) {
				break;
			}
		}
		return status;
	}
}
