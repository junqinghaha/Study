package com.mtkj.webservice.method;

import org.json.JSONException;

import android.text.TextUtils;

import com.android.tnt.config.Constants;
import com.android.tnt.db.entity.HuoDanEntity;
import com.mtkj.webservice.BaseWebserviceMothod;
import com.mtkj.webservice.WebParam;
import com.mtkj.webservice.WebServiceInfo;
import com.utils.log.MLog;

/**
 * 编辑
 * 
 * @author TNT
 * 
 */
public class WMTakeOutGoodsDelete extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeOutGoodsDelete";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;
	
	private String codes = null;

	public WMTakeOutGoodsDelete() {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsDelete);
		// TODO Auto-generated constructor stub
		isPost = true;
	}

	public WMTakeOutGoodsDelete(String codes) throws JSONException {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsDelete);
		isPost = true;
		setParams(codes);
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

	public void setParams(String codes) throws JSONException {
		webParams.clear();
		this.codes = codes;
		webParams.add(new WebParam("Codes", codes));
	}
	
	public void setParams(HuoDanEntity entity) throws JSONException {
		if(entity != null){
			this.codes = entity.getCode();
			webParams.clear();
			webParams.add(new WebParam("Codes", entity.getCode()));
		}
	}
	
	public String getCodes() {
		return this.codes;
	}

	@Override
	public Object parseWebResult(Object result) {
		// TODO Auto-generated method stub
		boolean bRt = false;
		this.returnValue = result;
		this.resultValue  = (String) result;
		try {
			if (TextUtils.isEmpty(resultValue) || resultValue.equalsIgnoreCase("删除成功")) {
				bRt = true;
			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return bRt;
	}
}
