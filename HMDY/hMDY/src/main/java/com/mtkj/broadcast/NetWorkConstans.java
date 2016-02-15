/**
 * 
 */
package com.mtkj.broadcast;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 网络信息
 * 
 * @author TNT
 * 
 */
public class NetWorkConstans {

	/***
	 * 网络连接是否打开
	 * 
	 * public static boolean IsOpenNetwork = false;
	 */

	/***
	 * 网络是否已连接
	 */
	public static boolean IsConnection = true;

	public static String NETWORK_STATE = "";
	public static String SINGL_STATE = "";
	public static String SINGL_VALUE = "";
	public static State wifiState = null;
	public static State mobileState = null;

	public static int curMobileNetType = -2;

	/** 未知 */
	public static final int NETWORKTYPE_UNKNOWN = -3;
	/** 飞行模式 */
	public static final int NETWORKTYPE_AIR_PLAN = -2;
	/** 网络关闭 */
	public static final int NETWORKTYPE_CLOSE = -1;
	/** 没有信号 */
	public static final int NETWORKTYPE_INVALID = 0;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 2;
	/** 3G和3G以上网络，或统称为快速网络 */
	public static final int NETWORKTYPE_3G = 3;
	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 4;

	public static boolean initNetInfo(Context context) {
		return refreshNetWorkValue(context);
	}

	/**
	 * 判断网络连接是否已开 2012-08-20 true 已打开 false 未打开
	 * */
	public static boolean isConn(Context context) {
		boolean bisConnFlag = false;
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
		}
		return bisConnFlag;
	}

	/**
	 * 是否为飞行模式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAirplaneModeOn(Context context) {
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	/**
	 * 刷新网络状态数据
	 * 
	 * @param context
	 * @return
	 */
	public static boolean refreshNetWorkValue(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		NetworkInfo mInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(mInfo != null){
			mobileState = mInfo.getState();
		}

		if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED == mobileState) {
			NetWorkConstans.IsConnection = true;
			// {{----修改----
			curMobileNetType = getNetWorkType(context);
			NETWORK_STATE = getNetName(curMobileNetType);
			if (SINGL_STATE != null && !SINGL_STATE.equalsIgnoreCase("")) {
				NETWORK_STATE = NETWORK_STATE + "," + SINGL_STATE;// + ","+
			}
			// // 丰富网络状态
			// NETWORK_STATE = getTypeName(cm.getNetworkInfo(
			// ConnectivityManager.TYPE_MOBILE).getSubtype());
			// -------------------------------------------------------}}

		} else if (wifiState != null && mobileState != null
				&& State.CONNECTED == wifiState
				&& State.CONNECTED != mobileState) {
			NetWorkConstans.IsConnection = true;
			curMobileNetType = NETWORKTYPE_WIFI;
			NETWORK_STATE = getNetName(curMobileNetType);

		} else if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED != mobileState) {
			if (isConn(context)) {
				curMobileNetType = NETWORKTYPE_INVALID;
			} else {
				if (isAirplaneModeOn(context)) {
					curMobileNetType = NETWORKTYPE_AIR_PLAN;
				} else {
					curMobileNetType = NETWORKTYPE_CLOSE;
				}
			}
			NETWORK_STATE = getNetName(curMobileNetType);
			NETWORK_STATE = NETWORK_STATE + ":" + SINGL_STATE;// + "," +
																// SINGL_VALUE;
		}else if(wifiState != null && mobileState == null && State.CONNECTED == wifiState){
			NetWorkConstans.IsConnection = true;
			curMobileNetType = NETWORKTYPE_WIFI;
			NETWORK_STATE = getNetName(curMobileNetType);
			
		}else if(wifiState == null && mobileState != null && State.CONNECTED == mobileState){
			NetWorkConstans.IsConnection = true;
			// {{----修改----
			curMobileNetType = getNetWorkType(context);
			NETWORK_STATE = getNetName(curMobileNetType);
			if (SINGL_STATE != null && !SINGL_STATE.equalsIgnoreCase("")) {
				NETWORK_STATE = NETWORK_STATE + "," + SINGL_STATE;// + ","+
			}
			// // 丰富网络状态
			// NETWORK_STATE = getTypeName(cm.getNetworkInfo(
			// ConnectivityManager.TYPE_MOBILE).getSubtype());
			// -------------------------------------------------------}}
		}else if(wifiState == null && mobileState == null){
			if (isConn(context)) {
				curMobileNetType = NETWORKTYPE_INVALID;
			} else {
				if (isAirplaneModeOn(context)) {
					curMobileNetType = NETWORKTYPE_AIR_PLAN;
				} else {
					curMobileNetType = NETWORKTYPE_CLOSE;
				}
			}
			NETWORK_STATE = getNetName(curMobileNetType);
			NETWORK_STATE = NETWORK_STATE + ":" + SINGL_STATE;// + "," +
																// SINGL_VALUE;
		}
		return IsConnection;
	}

	public static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
		case 14://TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case 15://TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case 13://TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 获取网络状态，wifi,wap,2g,3g.
	 * 
	 * @param context
	 *            上下文
	 * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}, *
	 *         {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}*
	 *         <p>
	 *         {@link #NETWORKTYPE_WIFI}
	 */

	public static int getNetWorkType(Context context) {
		int mNetWorkType = 0;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();

				mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
						: NETWORKTYPE_2G)
						: NETWORKTYPE_WAP;
			}
		} else {
			mNetWorkType = NETWORKTYPE_INVALID;
		}

		return mNetWorkType;
	}

	public static String getNetName(int type) {
		String info = "";
		switch (type) {
		case NETWORKTYPE_AIR_PLAN: {// 飞行模式
			info = "飞行模式";
		}
			break;
		case NETWORKTYPE_CLOSE: {
			info = "网络关闭";
		}
			break;
		case NETWORKTYPE_INVALID: {
			info = "没有信号";
		}
			break;
		case NETWORKTYPE_3G: {
			info = "3G";
		}
			break;
		case NETWORKTYPE_2G: {
			info = "2G";
		}
			break;
		case NETWORKTYPE_WAP: {
			info = "WAP";
		}
			break;
		case NETWORKTYPE_WIFI: {
			info = "WIFI";
		}
			break;
		default: {
			info = "UNKNOWN";
		}
			break;
		}
		return info;
	}

	public static String getTypeName(int subTypeId) {
		String info = "";
		switch (subTypeId) {
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			info = "联通3G";
			break;
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
			info = "2G_GPRS";
			break;
		case TelephonyManager.NETWORK_TYPE_CDMA:
			info = "2G_CDMA";
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			info = "3G_CDMA";
			break;
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			info = "1xRTT";
			break;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			info = "HSPA";
			break;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			info = "HSUPA";
			break;
		case TelephonyManager.NETWORK_TYPE_IDEN:
			info = "IDEN";
			break;
		case 14://TelephonyManager.NETWORK_TYPE_EHRPD:
			info = "EHRPD";
			break;
		case 15://TelephonyManager.NETWORK_TYPE_HSPAP:
			info = "HSPAP";
			break;
		case 13://TelephonyManager.NETWORK_TYPE_LTE:
			info = "LTE";
			break;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			info = "UNKNOWN";
			break;
		default:
			info = "UNKNOWN";
			break;
		}
		return info;
	}

	/*
	 * 打开设置网络界面
	 */
	public static void setNetworkMethod(final Context context) {
		// 提示对话框
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("网络设置提示")
				.setMessage("网络连接不可用,是否进行设置?")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						// 判断手机系统的版本 即API大于10 就是3.0或以上版本
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName component = new ComponentName(
									"com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(component);
							intent.setAction("android.intent.action.VIEW");
						}
						context.startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
	}

	// -----------------------信号强度--------------------

}
