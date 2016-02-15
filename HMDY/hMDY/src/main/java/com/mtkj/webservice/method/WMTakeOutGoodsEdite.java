package com.mtkj.webservice.method;

import org.json.JSONException;

import android.text.TextUtils;

import com.android.tnt.config.Constants;
import com.android.tnt.config.SysConfig;
import com.android.tnt.db.entity.HuoDanEntity;
import com.android.uitils.MDateUtils;
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
public class WMTakeOutGoodsEdite extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeOutGoodsOver";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	public WMTakeOutGoodsEdite() {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsEdit);
		// TODO Auto-generated constructor stub
		isPost = true;
	}

	public WMTakeOutGoodsEdite(HuoDanEntity entity) throws JSONException {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsEdit);
		isPost = true;
		setParams(entity);
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

	public void setParams(HuoDanEntity entity) throws JSONException {
		webParams.clear();
		// webParams.add(new WebParam("Code", entity.getCode()));
		if (!TextUtils.isEmpty(entity.getCode())) {
			webParams.add(new WebParam("Code", entity.getCode()));
		} else {
			webParams.add(new WebParam("Code", "0"));
		}
		webParams.add(new WebParam("ID", entity.getID()));
		webParams.add(new WebParam("Person", entity.getPerson()));
		webParams.add(new WebParam("StartStationID", entity.getStartStationId()));
		webParams.add(new WebParam("StartStation", entity.getStartStation()));
		webParams.add(new WebParam("EndStationID", entity.getEndStationId()));
		webParams.add(new WebParam("EndStation", entity.getEndStation()));
		webParams.add(new WebParam("CustomerID", entity.getCustomerID()));
		webParams.add(new WebParam("CustomName", entity.getCustomName()));
		webParams.add(new WebParam("Num", entity.getNum()));
		webParams.add(new WebParam("Address", entity.getAddress()));
		webParams.add(new WebParam("ServiceType", entity.getServiceType()));
		webParams.add(new WebParam("UserName", SysConfig.UserName));
		webParams.add(new WebParam("Date", MDateUtils.GetCurrentFormatTime(MDateUtils.GPS_FAMATE)));
		// webParams.add(new WebParam("Source", entity.getSource()));
	}

	@Override
	public Object parseWebResult(Object result) {
		// TODO Auto-generated method stub
		boolean bRt = false;
		this.returnValue = result;
		this.resultValue  = (String) result;
		try {
			if(TextUtils.isEmpty(resultValue)){
				bRt = true;
			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return bRt;
	}
}
