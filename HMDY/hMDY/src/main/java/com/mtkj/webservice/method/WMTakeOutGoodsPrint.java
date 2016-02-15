package com.mtkj.webservice.method;

import org.json.JSONException;

import com.android.tnt.config.Constants;
import com.mtkj.webservice.BaseWebserviceMothod;
import com.mtkj.webservice.WebParam;
import com.mtkj.webservice.WebServiceInfo;
import com.utils.log.MLog;

/**
 * 打印
 * 
 * @author TNT
 * 
 */
public class WMTakeOutGoodsPrint extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeOutGoodsPrint";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	public WMTakeOutGoodsPrint() {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsPrint);
		// TODO Auto-generated constructor stub
		isPost = true;
	}

	public WMTakeOutGoodsPrint(String code) throws JSONException {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsPrint);
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
		webParams.clear();
		webParams.add(new WebParam("Code", code));
	}

	@Override
	public Object parseWebResult(Object result) {
		// TODO Auto-generated method stub
		boolean bRt = false;
		this.returnValue = result;
		try {
//			UserEntity entity = new Gson().fromJson(((String) result), UserEntity.class);
//			if (entity != null && !TextUtils.isEmpty(entity.getStationName())) {
//				SysConfig.curUser = entity;
//				SysConfig.UserName = SysConfig.curUser.getUserName();
//				bRt = true;
//			} else {
//				resultReason = result + "";
//			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return bRt;
	}
}
