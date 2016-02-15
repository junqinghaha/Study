package com.mtkj.webservice;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.tnt.config.WebServiceConfig;
import com.mtkj.broadcast.NetWorkConstans;
import com.utils.log.MLog;

/**
 * WebService 调用类
 * 
 * @author Napoleon
 * 
 */
public class WebServiceOperate implements Runnable {

	private static final String TAG = "WebServiceMLog";
	public static final String ERROR = "error";
	public static final String NO_CONNECTION = "no_connection";

	private String mNameSpace = ""; // 名称空间
	private String mURL = ""; // url
	private String mMethodName = null; // 方法名
	private String mSoapAction = null;

	private Handler mHandler = null; // 句柄
	private List<WebParam> mParamList = null; // 参数列表

	private boolean mIsDebug = true; // 是否调试模式
	
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
	public WebServiceOperate(String nameSpace, String url, String methodName, String soapAction) {
		mNameSpace = nameSpace;
		mURL = url;
		mSoapAction = soapAction;
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

		if (null == mNameSpace || mNameSpace.equalsIgnoreCase("") || null == mMethodName || mMethodName.equalsIgnoreCase("")) {
			MLog.e(TAG, "名称空间或者方法名为空");
			return null;
		}
		String debugInfo = "";
		// 调用方法信息
		debugInfo += "\n" + mMethodName + "--url：" + mURL + "\n";
		debugInfo += "\n" + mMethodName + "--调用Web方法:" + mMethodName + "\n";
		debugInfo += "\n" + mMethodName + "--参数总数:" + mParamList.size() + "\n";

		// mNameSpace = "";
		// mMethodName = "";
		// mSoapAction = "";
		SoapObject request = new SoapObject(mNameSpace, mMethodName);
		if ((null != mParamList) && mParamList.size() > 0) {
			String parName = "";
			String parValue = null;
			for (int i = 0; i < mParamList.size(); i++) {
				parName = mParamList.get(i).getParName();
				parValue = (String) mParamList.get(i).getParValue();
				request.addProperty(parName, parValue);

				// 记录debug日志
				// if (!mMethodName
				// .equalsIgnoreCase(WebServiceInfo.METHOD_NAME.LOGIN)) {
				debugInfo += "\n" + mMethodName + "--参数" + (i + 1) + ":" + parName + "==" + parValue + "\n";
				// MLog.d(TAG, mMethodName + "-" + parName + "==" +
				// parValue);
				// }
			}
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		// envelope.encodingStyle = "UTF-8";
		HttpTransportSE aht = new HttpTransportSE(mURL);
		// AndroidHttpTransport aht = new AndroidHttpTransport(mURL);
		// aht.getConnection().setConnectTimeOut(30000);
		aht.debug = mIsDebug;

		try {
			// aht.call(mSoapAction, envelope);
			aht.call(mNameSpace + mMethodName, envelope);
			// SoapPrimitive obj = (SoapPrimitive) envelope.getResponse();
			// String result = obj.toString();
			Object obj = envelope.getResponse();
			result = obj.toString();
			// MLog.d(TAG, mMethodName + "-" + "调用结果" + "==" + result);

		} catch (IOException e1) {
			MLog.e(TAG, mMethodName + "--Webservice调用异常1" + e1.toString());
			e1.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT_CODE, -1);
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.REASON, "调用失败-IOException");
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT, "");
				result = jsonObject.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (XmlPullParserException e1) {
			MLog.e(TAG, mMethodName + "--Webservice调用异常2" + e1.toString());
			e1.printStackTrace();
			// result = ERROR + ":" + e1.toString();
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT_CODE, -1);
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.REASON, "调用失败-XmlPullParserException");
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT, "");
				result = jsonObject.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			MLog.e(TAG, mMethodName + "--Webservice调用异常3" + e.toString());
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT_CODE, -1);
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.REASON, "调用失败-e:");
				jsonObject.put(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.RESULT, "");
				result = jsonObject.toString();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
			result = NO_CONNECTION;
		}
		Message msg = Message.obtain();
		if (null == result || result.equals(ERROR) || result.equals(NO_CONNECTION)) {
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
			Bundle bundle = new Bundle();
			bundle.putString(WebServiceInfo.VALUE, result);
			bundle.putString(WebServiceInfo.COMAND_NAME, mMethodName);

			msg.what = WebServiceInfo.MSG_WS;
			msg.arg1 = WebServiceInfo.MSG_SUCCESS;
			msg.setData(bundle);
		}
		if (null == mHandler) {
			MLog.e(TAG, "请设置消息句柄");
			return;
		}
		mHandler.sendMessage(msg);
	}
}
