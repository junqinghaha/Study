package com.android.uitils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.utils.log.MLog;

public class WeatherGetAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private static final String TAG = "WeatherGetAsyncTask";

	/*** 申请的APPID */
	public final static String APPID = "8116cee059fce287";
	/*** 申请的APPID-前6位 */
	public final static String APPID_PRE6 = "8116ce";
	/*** 申请的private_key */
	public final static String KEY = "099bda_SmartWeatherAPI_9eace7b";
	/*** 基础URL */
	public final static String URL = "http://open.weather.com.cn/data/";

	/** 当前请求类型编码 **/
	public String WeatherTypeCode = "index_f";

	public final String[] typeCodes = new String[] { "forecast_f", "forecast_v", "index_f", "index_v" };
	public final String[] typeNames = new String[] { "3天24小时-基础区域", "3天24小时-常规区域", "指数-基础区域", "指数-常规区域" };

	/**
	 * 查询的天气类型
	 * 
	 * @author TNT
	 * 
	 */
	public interface WEATHER_TYPE {
		/** 3天24小时-基础区域 **/
		public int TYPE_BASE_3DAY = 0;
		/** 3天24小时-常规区域 **/
		public int TYPE_NORMAL_3DAY = 1;
		/** 指数-基础区域 **/
		public int TYPE_BASE_ZHISHU = 2;
		/** 指数-常规区域 **/
		public int TYPE_NORMAL_ZHISHU = 3;
	}
	
	/**
	 * 添加结果解析字段
	 * 
	 * @author TNT
	 * 
	 */
	public interface RESULT_NAME {
		/** 白天天气现象编号**/
		public String TIANQI_DAY_CODE = "fa";
		/** 夜间天气现象编号**/
		public String TIANQI_NIGHT_CODE = "fb";
		/** 白天天气温度编号**/
		public String TEMPERATURE_DAY_CODE = "fc";
		/** 夜间天气现象编号**/
		public String TEMPERATURE_NIGHT_CODE = "fd";
		/** 白天风向编号**/
		public String WIND_DIRECTION_DAY_CODE = "fe";
		/** 夜间风向编号**/
		public String WIND_DIRECTION_NIGHT_CODE = "ff";
		/** 白天风力编号**/
		public String WIND_POWER_DAY_CODE = "fg";
		/** 夜间风力编号**/
		public String WIND_POWER_NIGHT_CODE = "fh";
	}


	/***
	 * 查询结果
	 * 
	 * @author TNT
	 * 
	 */
	public interface OnWeatherGetingListener {
		public void onResult(JSONObject jsonResult);
	}

	ProgressDialog progressDialog = null;
	Context context = null;

	private OnWeatherGetingListener listener = null;

	public WeatherGetAsyncTask(Context context, int weatherType, OnWeatherGetingListener listener) {
		this.context = context;
		this.listener = listener;
		try {
			WeatherTypeCode = typeCodes[weatherType];
		} catch (Exception e) {
			WeatherTypeCode = "index_f";
		}
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		// String url = WeatherApiUtil.getHtmlURL(params[0], params[1],
		// params[2], params[3], params[4], params[5]);
		String url = WeatherApiUtil.getHtmlURL(params[0], APPID, APPID_PRE6, KEY, WeatherTypeCode, MDateUtils.GetCurrentFormatTime(MDateUtils.FILE_NAME_FORMAT2));
		return httpGet(url);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setMessage("数据请求中...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if (listener != null) {
			listener.onResult(result);
		}
	}

	private JSONObject httpGet(String httpUrl) {
		// String httpUrl =
		// "http://192.168.1.90:8080/AndroidLogin/loginAction.do?method=login&username="+username+"&password="+password;
		// 创建httpRequest对象
		HttpGet httpRequest = new HttpGet(httpUrl);
		try {
			// 取得HttpClient对象
			HttpClient httpclient = new DefaultHttpClient();
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				HttpEntity entity = httpResponse.getEntity();
				String strResult = EntityUtils.toString(entity, "UTF-8");
				return new JSONObject(strResult);
				// textView.setText(strResult);
			} else {
				// textView.setText("请求错误!");
				MLog.e(TAG, "error:" + "请求错误");
				return null;
			}
		} catch (ClientProtocolException e) {
			MLog.e(TAG, "error:" + e.toString());
			return null;
		} catch (IOException e) {
			MLog.e(TAG, "error:" + e.toString());
			return null;
		} catch (Exception e) {
			MLog.e(TAG, "error:" + e.toString());
			return null;
		}
	}
}
