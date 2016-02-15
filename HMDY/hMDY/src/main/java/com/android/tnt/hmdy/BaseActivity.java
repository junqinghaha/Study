package com.android.tnt.hmdy;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.android.tnt.config.Constants;
import com.android.tnt.config.SysConfig;
import com.android.uitils.Util;
import com.mtkj.broadcast.NetWorkConstans;
import com.utils.log.MLog;

public class BaseActivity extends FragmentActivity {

	/** 当前app的实例 */
	protected MApplication mApp;
	protected ActionBar mActionBar;
	protected String TAG = Constants.APPNAME + "-";

	private final static String RESTART_INTENT_KEY = "RESTART_INTENT_KEY";
	private final static int CRASHED_CODE = 0x911;
	private PendingIntent m_restartIntent = null;

	private boolean isGpsChecked = false;
	private boolean isNetWorkChecked = false;

	protected PowerManager powerManager = null;
	protected WakeLock wakeLock = null;
	protected boolean isWakeUp = false;// 屏幕是否常亮

	// 重启处理
	private UncaughtExceptionHandler m_handler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// 01-错误消息
			MLog.e(TAG, "uncaught exception is catched!");
			MLog.e(TAG, "错误信息:" + FormatStackTrace(ex));
			MLog.e(TAG, "错误信息2：" + ex.toString());
			MLog.e(TAG, "错误信息：getMessage：" + ex.getMessage());
			MLog.e(TAG, "错误信息：getLocalizedMessage：" + ex.getLocalizedMessage());

			// 02-重启程序
			// AlarmManager mgr = (AlarmManager)
			// getSystemService(Context.ALARM_SERVICE);
			// mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
			// m_restartIntent);
			// showMessage("程序异常退出");
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(0);
		}
	};

	private void restartOpt() {
		Intent intent = getIntent();
		int code = intent.getIntExtra(RESTART_INTENT_KEY, 0);
		if (CRASHED_CODE == code) {
			MLog.d(TAG, "So sorry that the application crashed.");
		}

		intent = new Intent(BaseActivity.this, LoginActivity.class);
		intent.putExtra(RESTART_INTENT_KEY, CRASHED_CODE);
		m_restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public String FormatStackTrace(Throwable throwable) {
		if (throwable == null)
			return "";
		String rtn = throwable.getStackTrace().toString();
		try {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			throwable.printStackTrace(printWriter);
			printWriter.flush();
			writer.flush();
			rtn = writer.toString();
			rtn = rtn.replaceAll("at ", "\r\n" + "at ");
			rtn = rtn.replaceAll("Caused by", "\r\n" + "Caused by");
			printWriter.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
		}
		return rtn;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TAG += this.getClass().getName();
		mApp = (MApplication) getApplicationContext();
		mActionBar = getActionBar();

		MLog.d(TAG, "【界面状态】-->onCreate");
		// restartOpt();
		Thread.setDefaultUncaughtExceptionHandler(m_handler);
		initDatas();
		initViews();
		enableHomeBackFuction(true);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	public void setActionbarTitle(String title, String subTitle) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
			actionBar.setSubtitle(subTitle);
		}
	}
	
	public void setIsCheckGps(boolean isCheck) {
		this.isGpsChecked = isCheck;
	}

	public void setIsCheckNetwork(boolean isCheck) {
		this.isNetWorkChecked = isCheck;
	}

	public void enableHomeBackFuction(boolean bEnable) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(bEnable);// 显示系统返回箭头
			// actionBar.setHomeButtonEnabled(true); //不显示箭头小符号,适用于自定义返回箭头情况
			// actionBar.setHomeButtonEnabled(bEnable);
		}
	}

	public void setHomeBackIcon() {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			//actionBar.setIcon(R.drawable.icon_home_back2);
			//actionBar.setIcon(null);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			BaseActivity.this.finish();
		}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		MLog.d(TAG, "【界面状态】-->onBackPressed");
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MLog.d(TAG, "【界面状态】-->onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		MLog.d(TAG, "【界面状态】-->onPause");
		super.onPause();
		if (isWakeUp) {
			closeWakeUp();
		}
	}

	/**
	 * 初始化数据
	 */
	protected void initDatas() {
		String lastErrorString = mApp.getData(Constants.KEY.ERROR_INFO, "");
		if (!lastErrorString.equalsIgnoreCase("")) {
			mApp.setData(Constants.KEY.ERROR_INFO, "");
		}
	}

	/**
	 * 初始化控件
	 */
	protected void initViews() {

	}

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 */
	protected void showMessage(String msg) {
		Util.MsgBox(this, msg);
		// Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 信息提示
	 * 
	 * @param msgID
	 */
	protected void showMessage(int msgID) {
		String msg = getResources().getString(msgID);
		showMessage(msg);
	}

	/**
	 * 信息显示
	 * 
	 * @param title
	 * @param msg
	 */
	protected void showMsgDlg(String title, String msg) {
		new AlertDialog.Builder(BaseActivity.this).setTitle(title)
				.setMessage(msg).setPositiveButton(R.string.ok, null).show();
	}


	public void enableWackUpActivity(boolean bEnable) {
		if (bEnable) {
			isWakeUp = true;
			openWakeUp();
		} else {
			isWakeUp = false;
			closeWakeUp();
		}
	}

	private void openWakeUp() {
		if (this.powerManager == null) {
			this.powerManager = (PowerManager) this
					.getSystemService(Context.POWER_SERVICE);
		}
		if (this.wakeLock == null) {
			this.wakeLock = this.powerManager.newWakeLock(
					PowerManager.FULL_WAKE_LOCK, "TNT");
			// this.wakeLock = this.powerManager.newWakeLock(
			// PowerManager.FULL_WAKE_LOCK, "My Lock");
		}
		if (!this.wakeLock.isHeld()) {
			this.wakeLock.acquire();
		}
	}

	private void closeWakeUp() {
		if (this.wakeLock != null) {
			if (this.wakeLock.isHeld()) {
				this.wakeLock.release();
				// this.powerManager = null;
				this.wakeLock = null;
			}
		}
	}
}
