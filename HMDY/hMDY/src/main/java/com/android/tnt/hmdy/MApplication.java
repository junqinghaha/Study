package com.android.tnt.hmdy;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.tnt.config.Constants;

public class MApplication extends Application {

	private static final String TAG = Constants.TAG + "-" + "MApplication";

	private SharedPreferences mSharedPreferences;

	public PowerControl mPowerControl = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// {{--20140116---解决No class Fund异常
		new EmptyTask().execute("");
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mPowerControl = new PowerControl(getApplicationContext());
		// 启动操作初始化
		// openWakeUp();
	}

	public void openWakeUp() {
		mPowerControl.openWakeUp();
	}

	public void closeWakeUp() {
		if (mPowerControl.isWakeUp()) {
			mPowerControl.closeWakeUp();
		}
	}

	// ---------------------------------------------------------------------------
	public String getData(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	public int getData(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	public long getData(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}

	public float getData(String key, float defValue) {
		return mSharedPreferences.getFloat(key, defValue);
	}

	public boolean getData(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public void setData(String key, Object o) {
		if (o != null) {
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			if (o instanceof Boolean) {
				editor.putBoolean(key, (Boolean) o);
			} else if (o instanceof Integer) {
				editor.putInt(key, (Integer) o);
			} else if (o instanceof Long) {
				editor.putLong(key, (Long) o);
			} else if (o instanceof Float) {
				editor.putFloat(key, (Float) o);
			} else if (o instanceof String) {
				editor.putString(key, (String) o);
			}
			boolean isok = editor.commit();
			if(!isok){
				Log.e(TAG, "error");
			}
		}
	}

	// ----------------------------文件夹目录--存储历史打开文件记录-----------------------------------------------
	/**
	 * 
	 */
	private SharedPreferences recentfile;

	public SharedPreferences getRencentFile() {
		if (recentfile == null) {
			recentfile = getSharedPreferences("navi_file", 0);
		}

		return recentfile;
	}

	// ---------------------------------------------------------------------------

	/**
	 * dp每毫米
	 */
	float dpPerMM = 0f;
	/***
	 * mm/dp
	 */
	float mmPerDp = 0f;

	/***
	 * 是否注册
	 */
	public boolean isRegistered = false;

	/**
	 * 初始化像素转换参数
	 * 
	 * @param metric
	 */
	public void initPixToMM(DisplayMetrics metric) {
		dpPerMM = (metric.densityDpi / 160) / (metric.xdpi * (1.0f / 25.4f));
		mmPerDp = (metric.xdpi * (1.0f / 25.4f)) / (metric.densityDpi / 160);
	}

	/**
	 * 解决Java.lang.NoClassDefFoundError: android/os/AsyncTask问题
	 * 
	 * @author TNT
	 * 
	 */
	public class EmptyTask extends AsyncTask<String, String, Boolean> {

		protected void onPreExecute() {
		}

		public Boolean doInBackground(String... params) {
			return true;
		}

		public void onPostExecute(Boolean data) {
		}
	}
}
