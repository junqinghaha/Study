package com.mtkj.webservice.method;

import org.json.JSONException;

import android.text.TextUtils;

import com.android.tnt.config.Constants;
import com.mtkj.webservice.BaseWebserviceMothod;
import com.mtkj.webservice.WebParam;
import com.mtkj.webservice.WebServiceInfo;
import com.utils.log.MLog;

/**
 * 提货完成
 * 
 * @author TNT
 * 
 */
public class WMTakeOutGoodsOver extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeOutGoodsOver";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	private String code = "";

	public WMTakeOutGoodsOver() {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsOver);
		// TODO Auto-generated constructor stub
		isPost = true;
	}

	public WMTakeOutGoodsOver(String code) throws JSONException {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsOver);
		isPost = true;
		setParams(code);
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

	public void setParams(String code) throws JSONException {
		this.code = code;
		webParams.clear();
		if (!TextUtils.isEmpty(code)) {
			webParams.add(new WebParam("Code", code));
		} else {
			webParams.add(new WebParam("Code", "0"));
		}
	}

	public String getCode() {
		return this.code;
	}

	@Override
	public Object parseWebResult(Object result) {
		// TODO Auto-generated method stub
		boolean bRt = false;
		this.returnValue = result;
		this.resultValue = (String) result;
		try {
			if (TextUtils.isEmpty(this.resultValue)) {
				bRt = true;
			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return bRt;
	}
}
