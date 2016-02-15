package com.mtkj.webservice.method;

import org.json.JSONException;

import android.text.TextUtils;

import com.android.tnt.config.Constants;
import com.mtkj.webservice.BaseWebserviceMothod;
import com.mtkj.webservice.WebServiceInfo;
import com.utils.log.MLog;

/**
 * 终点站
 * 
 * @author TNT
 * 
 */
public class WMTakeEndStation extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeEndStation";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	public WMTakeEndStation() {
		super(WebServiceInfo.METHOD_NAME.EndStation);
		// TODO Auto-generated constructor stub
		isPost = false;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return jsonData;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.jsonData = data;
	}

	public void setParams() throws JSONException {
		webParams.clear();
	}

	@Override
	public Object parseWebResult(Object result) {
		String data = "";
		this.returnValue = result;
		this.resultValue = (String) result;
		try {
			if (!TextUtils.isEmpty(resultValue)) {
				data = this.resultValue;
			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return data;
	}
}
