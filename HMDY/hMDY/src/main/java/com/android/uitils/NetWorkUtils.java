package com.android.uitils;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.utils.log.MLog;

public class NetWorkUtils {
	private Context context;
	private final static String MLog_TAG = "NetWorkUtils";

	/**
	 * 是否已连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) {

		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();

				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			MLog.v("error", e.toString());
		}
		return false;
	}

	/**
	 * 网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	

	/**
	 * 检测是否可连接到某地址
	 * 
	 * @param urlStr
	 * @return
	 */
	public static boolean IsConnectedAbleTheAddress(String urlStr) {
		return IsConnect(urlStr) != null;
	}

	/**
	 * 功能：检测当前URL是否可连接或是否有效, 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
	 * 
	 * @param urlStr
	 *            指定URL网络地址 url = new URL("http://www.baidu.com");
	 * @return URL
	 * 
	 */
	public static synchronized URL IsConnect(String urlStr) {
		int counts = 0;
		URL url = null;
		HttpURLConnection con;
		int state = -1;
		if (urlStr == null || urlStr.length() <= 0) {
			return null;
		}
		while (counts < 5) {
			try {
				url = new URL(urlStr);
				con = (HttpURLConnection) url.openConnection();
				state = con.getResponseCode();
				MLog.i(MLog_TAG, counts + "= " + state);
				if (state == 200) {
					MLog.i(MLog_TAG, "URL可用！");
				}
				break;
			} catch (Exception ex) {
				counts++;
				MLog.e(MLog_TAG, "URL不可用，连接第 " + counts + " 次");
				// System.out.println("URL不可用，连接第 " + counts + " 次");
//				urlStr = null;
				url = null;
				continue;
			}
		}
		return url;
	}

}
