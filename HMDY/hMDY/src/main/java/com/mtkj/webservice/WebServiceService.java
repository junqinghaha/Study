package com.mtkj.webservice;

import java.util.List;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.tnt.config.WebServiceConfig;
import com.utils.log.MLog;

public abstract class WebServiceService extends Service {
	private final static String TAG = "WebServiceActivity";
	private Handler mWebHandler = null;

	public void onCreate() {
		super.onCreate();

	}

	public Handler getHandler() {
		// Looper.prepare();
		if (mWebHandler == null) {
			Looper looper = Looper.myLooper();
			if (looper == null) {
				looper = Looper.getMainLooper();
				mWebHandler = new MHandler(looper);
			}
			if (looper != null) {
				mWebHandler = new MHandler(looper);
			} else {
				mWebHandler = new MHandler();
			}
		}
		return mWebHandler;
	}

	/** webservice调用成功 */
	protected abstract void webserviceCallSucess(String methodName, String result);

	/** webservic调用失败 */
	protected abstract void webserviceCallFailed(String methodName, String failInfo);

	/**
	 * 生产作业-web接口调用
	 * 
	 * @param methodName
	 * @param params
	 */
	public void webRequest(String methodName, List<WebParam> params, boolean isPost) {
		webRequest(WebServiceConfig.Namespace1, WebServiceConfig.ServerAddress1, methodName, WebServiceConfig.ActionHeader1 + methodName, params, isPost);
	}

	/**
	 * 网络请求
	 * 
	 * @param namespace
	 *            :http://ws.hbzf.hnjz.com
	 * @param url
	 *            :http://171.17.1.254/PDASystem/ProblemService.asmx
	 * @param methodName
	 *            :请求的函数名
	 * @param params
	 *            ：网络请求参数
	 * @return
	 */
	protected void webRequest(String namespace, String url, String methodName, String soapAction, List<WebParam> params, boolean isPost) {
		if (params != null) {
			if (mWebHandler == null) {
				// Looper.prepare();
				Looper looper = Looper.myLooper();
				if (looper == null) {
					looper = Looper.getMainLooper();
					mWebHandler = new MHandler(looper);
				}
				if (looper != null) {
					mWebHandler = new MHandler(looper);
				} else {
					mWebHandler = new MHandler();
				}
			}
			webRequest(namespace, url, methodName, soapAction, params, mWebHandler, isPost);
		} else {
			MLog.e(TAG, "web请求参数为null");
		}
	}

	private synchronized boolean webRequest(String namespace, String url, String methodName, String soapAction, List<WebParam> params, Handler handler, boolean isPost) {
		boolean bRt = false;
		switch (WebServiceInfo.ServiceType) {
		case WebServiceInfo.SERVICE_TYPE_HTTP:
			bRt = webRequestHttp(null, url, methodName, null, params, handler, isPost);
			break;
		case WebServiceInfo.SERVICE_TYPE_WEBSERVICE:
			bRt = webRequestWebservice(namespace, url, methodName, soapAction, params, handler, isPost);
			break;
		default:
			bRt = webRequestWebservice(namespace, url, methodName, soapAction, params, handler, isPost);
			break;
		}
		return bRt;
	}

	private synchronized boolean webRequestHttp(String namespace, String url, String methodName, String soapAction, List<WebParam> params, Handler handler, boolean isPost) {
		HttpOperate wo = new HttpOperate(url, methodName);
		wo.setParameters(params);
		wo.setHandler(handler);
		wo.setPostType(isPost);
		wo.isDebug(true);
		Thread thread = new Thread(wo);
		thread.start();
		return true;
	}

	private synchronized boolean webRequestWebservice(String namespace, String url, String methodName, String soapAction, List<WebParam> params, Handler handler, boolean isPost) {
		WebServiceOperate wo = new WebServiceOperate(namespace, url, methodName, soapAction);
		wo.setParameters(params);
		wo.setHandler(handler);
		wo.setPostType(isPost);
		wo.isDebug(true);
		Thread thread = new Thread(wo);
		thread.start();

		return true;
	}

	private class MHandler extends Handler {

		public MHandler(Looper myLooper) {
			super(myLooper);
		}

		public MHandler() {
			super();
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case WebServiceInfo.MSG_WS: {
				if (WebServiceInfo.MSG_SUCCESS == msg.arg1) {
					Bundle bundle = msg.getData();
					if (bundle != null) {
						String methodName = (String) bundle.getString(WebServiceInfo.COMAND_NAME);
						String result = bundle.getString(WebServiceInfo.VALUE);
						MLog.i(TAG, "网络反馈结果:" + result);
						webserviceCallSucess(methodName, result);
					}

				} else if (WebServiceInfo.MSG_FAIL == msg.arg1) {
					MLog.e(TAG, "网络服务调用失败");
					// Util.MsgBox(.this, "网络服务调用失败");
					Bundle bundle = msg.getData();
					String methodName = "";
					String value = "";
					if (bundle != null) {
						methodName = (String) bundle.getString(WebServiceInfo.COMAND_NAME);
						value = (String) bundle.getString(WebServiceInfo.VALUE);
					}
					if (value.equalsIgnoreCase("") || value.equals(WebServiceInfo.ERROR)) {
						webserviceCallFailed(methodName, WebServiceInfo.ERROR);

					} else if (value.equals(WebServiceInfo.NO_CONNECTION)) {
						webserviceCallFailed(methodName, WebServiceInfo.NO_CONNECTION);

					} else {
						webserviceCallFailed(methodName, "网络服务调用失败");
					}
				}
			}
				break;
			}

		}

	}

}
