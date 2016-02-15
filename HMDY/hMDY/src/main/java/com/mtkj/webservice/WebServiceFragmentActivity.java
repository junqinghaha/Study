package com.mtkj.webservice;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.tnt.config.WebServiceConfig;
import com.android.tnt.hmdy.BaseActivity;
import com.android.uitils.NetWorkUtils;
import com.android.uitils.Util;
import com.utils.log.MLog;

public abstract class WebServiceFragmentActivity extends BaseActivity {
	protected final static String TAG = "WebServiceActivity";
	// web请求过程中是否显示进度条
	private boolean isShowPrgBar = true;
	private Handler mHandler = null;
	private boolean isDlgCancelable = false;
	private ProgressDialog dialog = null;

	/**
	 * web请求结果返回函数
	 * 
	 * @param msgFlag
	 *            参考WebServiceOperate中的参数
	 * @param methodName
	 *            返回结果的函数名,msgFlag==WebServiceOperate.MSG_FAIL时为null
	 * @param result
	 *            返回结果，msgFlag==WebServiceOperate.MSG_FAIL时为null
	 * 
	 *            protected abstract void webserviceResult(int msgFlag, String
	 *            methodName, String result);
	 */

	/** webservice调用成功 */
	protected abstract void webserviceCallSucess(String methodName, String result);

	/** webservic调用失败 */
	protected abstract void webserviceCallFailed(String methodName, String failInfo);

	/**
	 * 设置是否显示web请求进度条界面,默认显示
	 * 
	 * @param isShowPrgBar
	 */
	public void setShowPrgBar(boolean isShowPrgBar) {
		this.isShowPrgBar = isShowPrgBar;
	}

	/**
	 * 公用接口
	 * 
	 * @param methodName
	 * @param params
	 */
	public void webRequest1(String methodName, List<WebParam> params, boolean isPost) {
		webRequest(WebServiceConfig.Namespace1, WebServiceConfig.ServerAddress1, methodName, WebServiceConfig.ActionHeader1 + methodName, params, isPost);
	}

	/**
	 * 项目接口
	 * 
	 * @param methodName
	 * @param params
	 */
	public void webRequest2(String methodName, List<WebParam> params, boolean isPost) {
		webRequest(WebServiceConfig.Namespace2, WebServiceConfig.ServerAddress2, methodName, WebServiceConfig.ActionHeader2 + methodName, params, isPost);
	}

	/**
	 * 排查接口
	 * 
	 * @param methodName
	 * @param params
	 */
	public void webRequest3(String methodName, List<WebParam> params, boolean isPost) {
		webRequest(WebServiceConfig.Namespace3, WebServiceConfig.ServerAddress3, methodName, WebServiceConfig.ActionHeader3 + methodName, params, isPost);
	}

	/**
	 * 隐患接口
	 * 
	 * @param methodName
	 * @param params
	 */
	public void webRequest4(String methodName, List<WebParam> params, boolean isPost) {
		webRequest(WebServiceConfig.Namespace4, WebServiceConfig.ServerAddress4, methodName, WebServiceConfig.ActionHeader4 + methodName, params, isPost);
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
	 * @param isPost
	 * @return
	 */
	protected void webRequest(String namespace, String url, String methodName, String soapAction, List<WebParam> params, boolean isPost) {
		if (!NetWorkUtils.isConnect(this)) {
			Util.MsgBox(this, "无网络链接");
		} else {
			if (params != null) {

				if (mHandler == null) {
					mHandler = new MHandler(Looper.myLooper());
				}
				if (isShowPrgBar) {
					// //{{{---webserviceTask--延迟太久问题
					if (dialog == null) {
						// 显示对话框
						dialog = new ProgressDialog(this);
						dialog.setMessage("正在执行请求...");
						dialog.setCancelable(isDlgCancelable);
						dialog.show();
					}
					webRequest(namespace, url, methodName, soapAction, params, mHandler, isPost);
				} else {
					webRequest(namespace, url, methodName, soapAction, params, mHandler, isPost);
				}
			} else {
				MLog.e(TAG, "web请求参数为null");
			}
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
		wo.setPostType(isPost);
		wo.setHandler(handler);
		wo.isDebug(true);
		Thread thread = new Thread(wo);
		thread.start();

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

	private class MHandler extends Handler {

		public MHandler(Looper myLooper) {
			super(myLooper);
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
					try {
						if (dialog != null) {
							dialog.dismiss();
							dialog = null;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				} else if (WebServiceInfo.MSG_FAIL == msg.arg1) {
					MLog.e(TAG, "网络服务调用失败");
					// Util.MsgBox(WebServiceFragmentActivity.this, "网络服务调用失败");
					Bundle bundle = msg.getData();
					String methodName = "";
					String value = "";
					if (bundle != null) {
						methodName = (String) bundle.getString(WebServiceInfo.COMAND_NAME);
						value = (String) bundle.getString(WebServiceInfo.VALUE);
					}
					if (value.equalsIgnoreCase("") || value.equals(WebServiceInfo.ERROR)) {
						Util.MsgBox(WebServiceFragmentActivity.this, "网络服务调用失败");
						webserviceCallFailed(methodName, WebServiceInfo.ERROR);

					} else if (value.equals(WebServiceInfo.NO_CONNECTION)) {
						Util.MsgBox(WebServiceFragmentActivity.this, "无网络连接");
						webserviceCallFailed(methodName, WebServiceInfo.NO_CONNECTION);

					} else {
						Util.MsgBox(WebServiceFragmentActivity.this, "网络服务调用失败");
						webserviceCallFailed(methodName, "网络服务调用失败");
					}
					// webserviceCallFailed(methodName, "网络服务调用失败");
					try {
						if (dialog != null) {
							dialog.dismiss();
							dialog = null;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
				break;
			}

		}

	}

}
