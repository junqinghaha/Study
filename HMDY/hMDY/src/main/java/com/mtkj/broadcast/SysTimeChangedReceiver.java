/**
 * 
 */
package com.mtkj.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.tnt.config.SysConfig;

/**
 * Android 利用广播BroadCast监听系统时间和日期的变化
 * 
 * @author 402-9
 * 
 */
public class SysTimeChangedReceiver extends BroadcastReceiver {
	public static final String ACTION_DATA_CHANGED = Intent.ACTION_DATE_CHANGED;
	public static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (ACTION_DATA_CHANGED.equals(intent.getAction())
				|| ACTION_TIME_CHANGED.equals(intent.getAction())) {
			//SysConfig.TimeDValue = 0;
		}
	}

}