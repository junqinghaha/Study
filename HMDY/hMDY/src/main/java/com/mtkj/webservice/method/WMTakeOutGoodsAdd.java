package com.mtkj.webservice.method;

import java.util.Random;
import java.util.UUID;

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
 * 提货完成
 * 
 * @author TNT
 * 
 */
public class WMTakeOutGoodsAdd extends BaseWebserviceMothod {

	private final static String TAG = Constants.TAG + "-WMTakeOutGoodsOver";
	protected int type;
	/** 保存的数据 */
	public String jsonData = null;

	public WMTakeOutGoodsAdd() {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsAdd);
		// TODO Auto-generated constructor stub
		isPost = true;
	}

	public WMTakeOutGoodsAdd(HuoDanEntity entity) throws JSONException {
		super(WebServiceInfo.METHOD_NAME.TakeOutGoodsAdd);
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
		// webParams.add(new WebParam("Source", entity.getSource()));
		// webParams.add(new WebParam("Status", entity.getStatus()));
		//webParams.add(new WebParam("Code", entity.getCode()));
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
		String data = "";
		this.returnValue = result;
		this.resultValue = (String) result;
		try {
			if (!TextUtils.isEmpty(this.resultValue)) {
				data = this.resultValue;
			} else {
				// data = UUID.randomUUID().toString();
				// Random random= new Random();
				int i = (int) (Math.random() * 900) + 100;
				data = "1000" + i;
				resultReason = result + "";
			}
		} catch (Exception e) {
			MLog.e(TAG, "解析失败2:" + e.toString());
			resultReason = e.toString();
		}
		return data;
	}
}
