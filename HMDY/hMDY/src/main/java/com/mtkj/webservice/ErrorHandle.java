/**
 * 
 */
package com.mtkj.webservice;

import java.util.HashMap;

/**
 * @author TNT
 * 
 */
public class ErrorHandle {

	public static HashMap<Integer, String> ErrorHashMap = new HashMap<Integer, String>();

	public final static int ERROR_CODE_1 = -1;
	public final static int ERROR_CODE_2 = -2;
	public final static int ERROR_CODE_3 = -3;
	public final static int ERROR_CODE_4 = -4;
	public final static int ERROR_CODE_5 = -5;

	public final static String ERROR_INFO_1 = "";
	public final static String ERROR_INFO_2 = "";
	public final static String ERROR_INFO_3 = "";
	public final static String ERROR_INFO_4 = "";
	public final static String ERROR_INFO_5 = "";

	static {
		ErrorHashMap.put(ERROR_CODE_1, ERROR_INFO_1);
		ErrorHashMap.put(ERROR_CODE_2, ERROR_INFO_2);
		ErrorHashMap.put(ERROR_CODE_3, ERROR_INFO_3);
		ErrorHashMap.put(ERROR_CODE_4, ERROR_INFO_4);
		ErrorHashMap.put(ERROR_CODE_5, ERROR_INFO_5);
	}

	public String getErrorInfo(String result) {
		return "";
	}

	public String getErrorInfo(int errorCode) {
		return ErrorHashMap.get(errorCode);
	}

}
