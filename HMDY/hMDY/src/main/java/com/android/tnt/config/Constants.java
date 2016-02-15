package com.android.tnt.config;

/**
 * 数据库表
 * 
 * @author TNT
 * 
 */
public class Constants {

	public static final String TAG = "HMDY";

	public static final String APPNAME = "HMDY";

	public static boolean IsDebugMode = true;

	/**Key */
	public interface KEY {
		String DATA = "data";
		String DATA1 = "data1";
		String DATA2 = "data2";
		String DATA3 = "data3";
		String DATA4 = "data4";
		
		String ERROR_INFO = "ERROR_INFO";
	}
	
	/** 所有shareprephrence保存的key */
	public interface CONFIG_KEY {
		String FLITER_FIELD_FLAG = "FLITER_FIELD_FLAG";
		String FLITER_FIELD_NAME = "FLITER_FIELD_NAME";
		String FLITER_FIELD_VALUES = "FIELD";
		String FLITER_FIELD_DATE_NAME = "DATE_FIELD";
		String FLITER_FIELD_DATE_VALUE_MIN = "DATE_VALUE_MIN";
		String FLITER_FIELD_DATE_VALUE_MAX = "DATE_VALUE_MAX";
		String GUID = "GUID";
		String LOGIN_USERS = "LOGIN_USERS";
		String LAST_USERS = "LAST_USERS";
		String BLDevice = "BLDevice";
		String START_STATION = "START_STATION";
		String END_STATION = "END_STATION";
		String CUSTOMER = "CUSTOMER";
		String PWD_RECORD = "PWD_RECORD";
	}
	
	public interface REQUEST {
		int REQUEST_ADD = 1;
		int REQUEST_EDIT = 2;
	}

}
