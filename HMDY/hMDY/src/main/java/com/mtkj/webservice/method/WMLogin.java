package com.mtkj.webservice.method;

import org.json.JSONException;

import android.text.TextUtils;

import com.android.tnt.config.Constants;
import com.android.tnt.config.SysConfig;
import com.google.gson.Gson;
import com.mtkj.webservice.BaseWebserviceMothod;
import com.mtkj.webservice.WebParam;
import com.mtkj.webservice.WebServiceInfo;
import com.mtkj.webservice.entity.UserEntity;
import com.utils.log.MLog;

/**
 * 反馈--登录
 * 
 * @author TNT
 * 
 */
public class WMLogin extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-LOGIN";
	/***
	 * 区域类型
	 */
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	public WMLogin() {
		super(WebServiceInfo.METHOD_NAME.LOGIN);
		// TODO Auto-generated constructor stub
		isPost = true;
	}

	public WMLogin(String userid, String pwd, String devId) throws JSONException {
		super(WebServiceInfo.METHOD_NAME.LOGIN);
		isPost = true;
		setParams(userid, pwd, devId);
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

	public void setParams(String userid, String pwd, String devId) throws JSONException {
		webParams.clear();
		// LoginEntity entity = new LoginEntity();
		// if (TextUtils.isEmpty(pwd)) {
		// entity.NetID = userid;
		// entity.Type = 1;
		// } else {
		// entity.NetID = userid;
		// entity.Psw = pwd;
		// entity.Type = 2;
		// }
		// String str = new Gson().toJson(entity, LoginEntity.class);
		webParams.add(new WebParam("Pwd", pwd));
		webParams.add(new WebParam("Name", userid));
	}

	@Override
	public Object parseWebResult(Object result) {
		// TODO Auto-generated method stub
		boolean bRt = false;
		this.returnValue = result;
		try {
			UserEntity entity = new Gson().fromJson(((String) result), UserEntity.class);
			if (entity != null && !TextUtils.isEmpty(entity.getStationName())) {
				SysConfig.curUser = entity;
				SysConfig.UserName = SysConfig.curUser.getUserName();
				bRt = true;
			} else {
				resultReason = result + "";
			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return bRt;
	}
}
