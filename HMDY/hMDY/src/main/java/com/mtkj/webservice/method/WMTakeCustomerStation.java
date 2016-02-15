package com.mtkj.webservice.method;

import org.json.JSONException;

import android.text.TextUtils;

import com.android.tnt.config.Constants;
import com.mtkj.webservice.BaseWebserviceMothod;
import com.mtkj.webservice.WebServiceInfo;
import com.utils.log.MLog;

/**
 * 顾客获取
 * 
 * @author TNT
 * 
 */
public class WMTakeCustomerStation extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeCustomerStation";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	public WMTakeCustomerStation() {
		super(WebServiceInfo.METHOD_NAME.Customer);
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
