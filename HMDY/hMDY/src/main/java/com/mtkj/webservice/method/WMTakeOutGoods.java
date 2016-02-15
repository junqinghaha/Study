package com.mtkj.webservice.method;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.text.TextUtils;

import com.android.tnt.config.Constants;
import com.android.tnt.db.columns.HuoDanColumns;
import com.android.tnt.db.entity.HuoDanEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mtkj.webservice.BaseWebserviceMothod;
import com.mtkj.webservice.WebParam;
import com.mtkj.webservice.WebServiceInfo;
import com.utils.log.MLog;

/**
 * 获取数据
 * 
 * @author TNT
 * 
 */
public class WMTakeOutGoods extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeOutGoods";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	public WMTakeOutGoods() {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoods);
		// TODO Auto-generated constructor stub
		isPost = false;
	}

	public WMTakeOutGoods(String index, String pageSize, String status, String startTime, String endTime) throws JSONException {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoods);
		isPost = false;
		setParams(index, pageSize, status, startTime, endTime);
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

	public void setParams(String index, String pageSize, String status, String startTime, String endTime) throws JSONException {
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
		webParams.add(new WebParam("PageIndex", index));
		webParams.add(new WebParam("PageSize", pageSize));
		if (!TextUtils.isEmpty(status)) {
			if (status.equalsIgnoreCase(HuoDanColumns.STATE_VALUE.STATE_DONE) || status.equalsIgnoreCase(HuoDanColumns.STATE_VALUE.STATE_DONE + ",")) {
				webParams.add(new WebParam("Status", 10));
			} else if (status.equalsIgnoreCase(HuoDanColumns.STATE_VALUE.STATE_UNDO) || status.equalsIgnoreCase(HuoDanColumns.STATE_VALUE.STATE_UNDO + ",")) {
				webParams.add(new WebParam("Status", 5));
			}
		}
		if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
			webParams.add(new WebParam("StartDate", startTime));
			webParams.add(new WebParam("EndDate", endTime));
		}
	}

	@Override
	public Object parseWebResult(Object result) {
		List<HuoDanEntity> lstResults = new ArrayList<HuoDanEntity>();
		this.returnValue = result;
		this.resultValue = (String) result;
		try {
			if (!TextUtils.isEmpty(resultValue)) {
				JsonArray arrDatas = new Gson().fromJson(this.resultValue, JsonArray.class);
				// JSONArray arrDatas = new JSONArray(result);
				for (int i = 0; i < arrDatas.size(); i++) {
					// String newValue =
					// arrDatas.get(i).toString().replaceAll("\\/Date\\(", "");
					// newValue = newValue.replaceAll("-0000\\)\\/", "");
					HuoDanEntity data = new Gson().fromJson(arrDatas.get(i).toString(), HuoDanEntity.class);
					if (data != null && data.getCode() != null) {
						lstResults.add(data);
					}
				}
			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return lstResults;
	}
}
