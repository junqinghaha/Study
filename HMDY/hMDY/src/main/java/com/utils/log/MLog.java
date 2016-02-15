package com.utils.log;

import android.util.Log;

/**
 * 日志记录
 * 
 * @author TNT
 * 
 */
public class MLog {

	// 不再记录日志
	public final static int FLAG_NO_LOG = 0x0000;
	// 记录日志
	public final static int FLAG_LOG_CAT = 0x0001;
	// 写日志到SD卡
	public final static int FLAG_WRITE_SD = 0x0002;
	// 写日志到SD卡和Logcat
	public final static int FLAG_LOGCAT_AND_SD = 0x0003;

	// 当前日志标志
	public static int currentFlag = FLAG_LOGCAT_AND_SD;

	public static int getCurrentFlag() {
		return currentFlag;
	}

	public static void setCurrentFlag(int currentFlag) {
		MLog.currentFlag = currentFlag;
	}

	public static void i(String tag, String msg) {

		switch (currentFlag) {
		case FLAG_NO_LOG:
			break;
		case FLAG_LOG_CAT:
			Log.i(tag, msg);
			break;
		case FLAG_WRITE_SD:
			SDLog.i(tag, msg);
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.i(tag, msg);
			SDLog.i(tag, msg);
			break;
		}
	}
	
	public static void i(String tag, String msg, boolean logcatOnly) {
		
		switch (currentFlag) {
		case FLAG_NO_LOG:
			break;
		case FLAG_LOG_CAT:
			Log.i(tag, msg);
			break;
		case FLAG_WRITE_SD:
			if(!logcatOnly){
				SDLog.i(tag, msg);
			}
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.i(tag, msg);
			if(!logcatOnly){
				SDLog.i(tag, msg);
			}
			break;
		}
	}
	
	public static void u(String tag, String msg) {

		switch (currentFlag) {
		case FLAG_NO_LOG:
			break;
		case FLAG_LOG_CAT:
			Log.i(tag, msg);
			break;
		case FLAG_WRITE_SD:
			SDLog.u(tag, msg);
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.i(tag, msg);
			SDLog.u(tag, msg);

			break;
		}
	}

	public static void w(String tag, String msg) {
		switch (currentFlag) {
		case FLAG_NO_LOG:
			break;
		case FLAG_LOG_CAT:
			Log.w(tag, msg);
			break;
		case FLAG_WRITE_SD:
			SDLog.w(tag, msg);
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.w(tag, msg);
			SDLog.w(tag, msg);
			break;
		}
	}

	public static void e(String tag, String msg) {
		switch (currentFlag) {
		case FLAG_NO_LOG:
		case FLAG_LOG_CAT:
		case FLAG_WRITE_SD:
		case FLAG_LOGCAT_AND_SD:
			Log.e(tag, msg);
			SDLog.e(tag, msg);
			break;
		}
//		switch (currentFlag) {
//		case FLAG_NO_LOG:
//			break;
//		case FLAG_LOG_CAT:
//			Log.e(tag, msg);
//			break;
//		case FLAG_WRITE_SD:
//			SDLog.e(tag, msg);
//			break;
//		case FLAG_LOGCAT_AND_SD:
//			Log.e(tag, msg);
//			SDLog.e(tag, msg);
//			break;
//		}
	}

	public static void d(String tag, String msg) {
		switch (currentFlag) {
		case FLAG_NO_LOG:
			break;
		case FLAG_LOG_CAT:
			Log.d(tag, msg);
			break;
		case FLAG_WRITE_SD:
			SDLog.d(tag, msg);
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.d(tag, msg);
			SDLog.d(tag, msg);
			break;
		}
	}
	
	public static void net(String tag, String msg, boolean writeToSd) {
		switch (currentFlag) {
		case FLAG_NO_LOG:
			if(writeToSd){
				SDLog.d(tag, msg);
			}
			break;
		case FLAG_LOG_CAT:
			Log.d(tag, msg);
			if(writeToSd){
				SDLog.d(tag, msg);
			}
			break;
		case FLAG_WRITE_SD:
			SDLog.d(tag, msg);
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.d(tag, msg);
			SDLog.d(tag, msg);
			break;
		}
	}
	
	public static void d(String tag, String msg, boolean logcatOnly) {
		switch (currentFlag) {
		case FLAG_NO_LOG:
			break;
		case FLAG_LOG_CAT:
			Log.d(tag, msg);
			break;
		case FLAG_WRITE_SD:
			if(!logcatOnly){
				SDLog.d(tag, msg);
			}
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.d(tag, msg);
			if(!logcatOnly){
				SDLog.d(tag, msg);
			}
			break;
		}
	}

	public static void v(String tag, String msg) {
		switch (currentFlag) {
		case FLAG_NO_LOG:
			break;
		case FLAG_LOG_CAT:
			Log.v(tag, msg);
			break;
		case FLAG_WRITE_SD:
			SDLog.v(tag, msg);
			break;
		case FLAG_LOGCAT_AND_SD:
			Log.v(tag, msg);
			SDLog.v(tag, msg);
			break;
		}
	}
}
