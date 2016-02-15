package com.android.tnt.hmdy;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/***
 * 电源管理操作
 * 
 * @author TNT
 * 
 */
public class PowerControl {

	protected PowerManager powerManager = null;
	protected WakeLock wakeLock = null;
	protected boolean isWakeUp = true;// 屏幕是否常亮

	public Context context = null;

	public PowerControl(Context context) {
		this.context = context;
	}

	public boolean isWakeUp() {
		return isWakeUp;
	}

	public void openWakeUp() {
		if (this.powerManager == null) {
			this.powerManager = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
		}
		if (this.wakeLock == null) {
			this.wakeLock = this.powerManager.newWakeLock(
					PowerManager.FULL_WAKE_LOCK, "TNT");
		}
		if (!this.wakeLock.isHeld()) {
			this.wakeLock.acquire();
			isWakeUp = true;
		}
	}

	public void closeWakeUp() {
		if (this.wakeLock != null) {
			if (this.wakeLock.isHeld()) {
				this.wakeLock.release();
				// this.powerManager = null;
				this.wakeLock = null;
				isWakeUp = false;
			}
		}
	}
}
