/**
 * 
 */
package com.mtkj.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;

/**
 * Android 利用广播BroadCast监听网络的变化
 * 
 * @author 402-9
 * 
 */
public class NetWorkChangedReceiver extends BroadcastReceiver {
	public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			if (ACTION.equals(intent.getAction())) {
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				State wifiState = null;
				if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
					wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
				}
				State mobileState = null;
				if(ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_MOBILE)){
					if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
						mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
					}
				}

				// Intent intent2 = new Intent(context,
				// BroadCastActivity2_SMS.class);
				if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
					// context.startService(intent2);
					if (!NetWorkConstans.IsConnection) {
						Toast.makeText(context, "手机网络连接成功！", Toast.LENGTH_SHORT).show();
					}
					NetWorkConstans.IsConnection = true;

				} else if (wifiState != null && mobileState != null && State.CONNECTED == wifiState && State.CONNECTED != mobileState) {
					NetWorkConstans.IsConnection = true;
					if (!NetWorkConstans.IsConnection) {
						Toast.makeText(context, "无线网络连接成功！", Toast.LENGTH_SHORT).show();
					}

				} else if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED != mobileState) {
					if (NetWorkConstans.IsConnection) {
						Toast.makeText(context, "无网络...", Toast.LENGTH_SHORT).show();
					}
					NetWorkConstans.IsConnection = false;
					
				}else if(wifiState != null && mobileState == null && State.CONNECTED == wifiState){
					if (!NetWorkConstans.IsConnection) {
						Toast.makeText(context, "无线网络连接成功！", Toast.LENGTH_SHORT).show();
					}
					NetWorkConstans.IsConnection = true;
					
				}else if(wifiState == null && mobileState != null && State.CONNECTED == mobileState){
					if (!NetWorkConstans.IsConnection) {
						Toast.makeText(context, "手机网络连接成功！", Toast.LENGTH_SHORT).show();
					}
					NetWorkConstans.IsConnection = true;
				}else if(wifiState == null && mobileState == null){
					if (!NetWorkConstans.IsConnection) {
						Toast.makeText(context, "无网络...", Toast.LENGTH_SHORT).show();
					}
					NetWorkConstans.IsConnection = false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}