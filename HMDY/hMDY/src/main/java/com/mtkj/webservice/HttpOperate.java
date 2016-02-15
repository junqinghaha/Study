package com.mtkj.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.tnt.config.WebServiceConfig;
import com.mtkj.broadcast.NetWorkConstans;
import com.utils.log.MLog;

/**
 * Http请求操作 调用类
 * 
 * @author Napoleon
 * 
 */
public class HttpOperate implements Runnable {

	private static final String TAG = "WebServiceMLog";

	private String mURL = ""; // url
	private String mMethodName = null; // 方法名

	private Handler mHandler = null; // 句柄
	private List<WebParam> mParamList = null; // 参数列表

	private boolean mIsDebug = true; // 是否调试模式

	public static HttpClient httpClient = null;

	private boolean isPostType = false;

	/**
	 * 创建WebServiceOperate对象,设置默认的返回值类型是字符串
	 * 
	 * @param nameSpace
	 *            WebService名称空间
	 * @param url
	 *            WebServiceURL
	 * @param methodName
	 *            WebService方法名
	 */
	public HttpOperate(String url, String methodName) {
		mURL = url;
		mMethodName = methodName;
	}

	/**
	 * 设置返回消息的句柄
	 * 
	 * @param handler
	 *            消息句柄
	 */
	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public boolean isPostType() {
		return isPostType;
	}

	public void setPostType(boolean isPostType) {
		this.isPostType = isPostType;
	}

	/**
	 * 设置传入WebService方法的参数名和参数值
	 * 
	 * @param paramList
	 *            参数列表，WebParam类中封装了参数名和参数值
	 */
	public void setParameters(List<WebParam> paramList) {
		mParamList = paramList;
	}

	/**
	 * 是否调试状态
	 * 
	 * @param isDebug
	 */
	public void isDebug(boolean isDebug) {
		mIsDebug = isDebug;
	}

	private String CallWebservice() {
		String result = "";

		if (null == mMethodName || mMethodName.equalsIgnoreCase("")) {
			MLog.e(TAG, "名称空间或者方法名为空");
			return null;
		}
		String debugInfo = "";
		// 调用方法信息
		debugInfo += "\n" + mMethodName + "--url：" + mURL + "\n";
		debugInfo += "\n" + mMethodName + "--调用Web方法:" + mMethodName + "\n";
		debugInfo += "\n" + mMethodName + "--参数总数:" + mParamList.size() + "\n";

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(mURL);
		// urlBuilder.append("?");
		// urlBuilder.append("Command=");
		// urlBuilder.append("Method=");
		urlBuilder.append(mMethodName);
		List<NameValuePair> lstPostParams = new ArrayList<NameValuePair>();
		if (isPostType()) {
			if ((null != mParamList) && mParamList.size() > 0) {
				for (WebParam params : mParamList) {
					lstPostParams.add(new BasicNameValuePair(params.getParName(), params.getParValue() + ""));
				}
			}
		} else {
			if ((null != mParamList) && mParamList.size() > 0) {
				int i=0;
				for (WebParam params : mParamList) {
					if (i == 0) {
						urlBuilder.append("?");
					} else {
						urlBuilder.append('&');
					}
					try {
						// urlBuilder.append(params.getParName()).append('=').append((params.getParValue()
						// + ""));
						urlBuilder.append(URLEncoder.encode(params.getParName(), "UTF-8")).append('=').append(URLEncoder.encode((params.getParValue() + ""), "UTF-8"));
						// urlBuilder.append(URLEncoder.encode(params.getParName(),
						// "GBK")).append('=').append(URLEncoder.encode((params.getParValue()
						// + ""), "GBK"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
			}
		}

		try {
			// if
			// (!mMethodName.equalsIgnoreCase(WebServiceInfo.METHOD_NAME.LOGIN))
			// {
			if (isPostType()) {
				HttpPost httpPost = new HttpPost(urlBuilder.toString());
				httpPost.setEntity(new UrlEncodedFormEntity(lstPostParams, HTTP.UTF_8));
				if (httpClient == null) {
					// httpClient = AndroidHttpClient.newInstance("");
					httpClient = new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3600000);
				}
				// HttpClient httpClient = new DefaultHttpClient();
				// 发送请求获得响应对象
				HttpResponse response = httpClient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					// 获取响应消息实体
					HttpEntity entity = response.getEntity();
					// 获取输入流
					InputStream inputStream = entity.getContent();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

					String line = "";

					while (null != (line = bufferedReader.readLine())) {
						result += line;
					}
				}
				// httpClient.close();
			} else {
				HttpGet httpGet = new HttpGet(urlBuilder.toString());
				// if(httpClient == null){
				// httpClient = AndroidHttpClient.newInstance("");
				// }
				if (httpClient == null) {
					// httpClient = AndroidHttpClient.newInstance("");
					httpClient = new DefaultHttpClient();
				}
				// HttpClient httpClient = new DefaultHttpClient();
				// 发送请求获得响应对象
				HttpResponse response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					// 获取响应消息实体
					HttpEntity entity = response.getEntity();
					// 获取输入流
					InputStream inputStream = entity.getContent();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
					
					String line = "";
					
					while (null != (line = bufferedReader.readLine())) {
						result += line;
					}
				}else{
					result = "调用失败";
				}
				// httpClient.close();
			}

			// MLog.d(TAG, mMethodName + "-" + "调用结果" + "==" + result);

		} catch (IOException e1) {
			MLog.e(TAG, mMethodName + "--Webservice调用异常1" + e1.toString());
			e1.printStackTrace();
			result = "网络异常";
			// JSONObject jsonObject = new JSONObject();
			// try {
			// jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT_CODE,
			// -1);
			// jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.REASON,
			// "调用失败-IOException");
			// jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT,
			// "");
			// result = jsonObject.toString();
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		} catch (Exception e) {
			e.printStackTrace();
			MLog.e(TAG, mMethodName + "--Webservice调用异常3" + e.toString());
			result = "调用异常:" + e.toString();
			// try {
			// JSONObject jsonObject = new JSONObject();
			// jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT_CODE,
			// -1);
			// jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.REASON,
			// "调用失败-e:");
			// jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT,
			// "");
			// result = jsonObject.toString();
			// } catch (JSONException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
		}

		debugInfo += "\n" + mMethodName + "--调用结果:" + result;

		if (WebServiceConfig.IsDebugMode) {
			MLog.net(TAG, debugInfo, true);
		}

		return result;
	}

	public void run() {
		String result = "";
		if (NetWorkConstans.IsConnection) {
			result = CallWebservice();
		} else {
			result = WebServiceInfo.NO_CONNECTION;
		}
		Message msg = Message.obtain();
		if (null == result || result.equals(WebServiceInfo.ERROR) || result.equals(WebServiceInfo.NO_CONNECTION)) {
			Bundle bundle = new Bundle();
			if (result == null) {
				result = "";
			}
			bundle.putString(WebServiceInfo.VALUE, result);
			bundle.putString(WebServiceInfo.COMAND_NAME, mMethodName);
			msg.what = WebServiceInfo.MSG_WS;
			msg.arg1 = WebServiceInfo.MSG_FAIL;
			msg.setData(bundle);

		} else {
			boolean bResult = false;//
			String resultInfo = "";
			String resultData = "";
			int resultCode = 0;
			try {
				JSONObject json = new JSONObject(result);
				if (json != null) {
					bResult = json.optBoolean(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.Success, false);
					resultInfo = json.optString(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.Msg, "");
					resultData = json.optString(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.Data, "");
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if (bResult) {
				Bundle bundle = new Bundle();
				bundle.putString(WebServiceInfo.VALUE, resultData);
				bundle.putString(WebServiceInfo.COMAND_NAME, mMethodName);
				msg.what = WebServiceInfo.MSG_WS;
				msg.arg1 = WebServiceInfo.MSG_SUCCESS;
				msg.setData(bundle);

			} else {
				Bundle bundle = new Bundle();
				bundle.putString(WebServiceInfo.VALUE, resultInfo);
				bundle.putString(WebServiceInfo.COMAND_NAME, mMethodName);

				msg.what = WebServiceInfo.MSG_WS;
				msg.arg1 = WebServiceInfo.MSG_FAIL_MSG;
				msg.setData(bundle);
			}
		}
		if (null == mHandler) {
			MLog.e(TAG, "请设置消息句柄");
			return;
		}
		mHandler.sendMessage(msg);
	}
}
