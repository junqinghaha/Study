package com.android.tnt.hmdy.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.tnt.config.Constants;
import com.android.tnt.db.columns.HuoDanColumns;
import com.android.tnt.db.entity.HuoDanEntity;
import com.android.tnt.db.manager.HuoDanDBManager;
import com.android.tnt.hmdy.MApplication;
import com.android.tnt.hmdy.R;
import com.android.tnt.view.NumberSelectedView;
import com.android.uitils.Util;
import com.mtkj.webservice.WebServiceFragment;
import com.mtkj.webservice.method.WMTakeCustomerStation;
import com.mtkj.webservice.method.WMTakeEndStation;
import com.mtkj.webservice.method.WMTakeOutGoodsAdd;
import com.mtkj.webservice.method.WMTakeOutGoodsEdite;
import com.mtkj.webservice.method.WMTakeStartStation;

/***
 * 
 * 添加
 * 
 * @author TNT
 * 
 */
public class CopyOfAddFragment extends WebServiceFragment {

	private final String TAG = "AddFragment";

	private static SharedPreferences spHistroyData;
	private HuoDanDBManager dbm = null;
	private HuoDanEntity entity = null;
	private boolean isAdd = true;

	private View rootView = null;
	private Spinner spinner01 = null;
	private Spinner spinner02 = null;
	private Spinner spinner03 = null;

	private Spinner spinner04 = null;
	private Spinner spinner05 = null;
	private Spinner spinner06 = null;

	private NumberSelectedView numberSelectView = null;
	private WMTakeOutGoodsAdd addWebservice = null;
	private WMTakeOutGoodsEdite editWebservice = null;

	private WMTakeStartStation startStationWebservice = null;
	private WMTakeEndStation endStationWebservice = null;
	private WMTakeCustomerStation customerWebservice = null;

	private String[] arrSp4Names = new String[] {};
	private String[] arrSp4Values = new String[] {};

	private String[] arrSp5Names = new String[] {};
	private String[] arrSp5Values = new String[] {};

	private String[] arrSp6Names = new String[] {};
	private String[] arrSp6Values = new String[] {};

	public interface RequestInfo {
		public int Request_01 = 1;
		public int Request_02 = 2;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbm = new HuoDanDBManager(getActivity());
		addWebservice = new WMTakeOutGoodsAdd();
		editWebservice = new WMTakeOutGoodsEdite();

		startStationWebservice = new WMTakeStartStation();
		endStationWebservice = new WMTakeEndStation();
		customerWebservice = new WMTakeCustomerStation();

		spHistroyData = getActivity().getSharedPreferences("MyHistoryData", 0);
		Bundle bunle = getArguments();
		if (bunle != null) {
			entity = (HuoDanEntity) bunle.getSerializable(Constants.KEY.DATA1);
		}
		if (entity != null) {
			isAdd = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 刷新数据
		initDatas();
		// 刷新界面
		rootView = inflater.inflate(R.layout.fragment_hm_add, container, false);
		initViews(rootView);
		return rootView;
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		MApplication app = (MApplication) getActivity().getApplication();
		String endJson = app.getData(Constants.CONFIG_KEY.END_STATION, "");
		String startJson = app.getData(Constants.CONFIG_KEY.START_STATION, "");
		String customerJson = app.getData(Constants.CONFIG_KEY.CUSTOMER, "");
		if (TextUtils.isEmpty(endJson) || TextUtils.isEmpty(startJson) || TextUtils.isEmpty(customerJson)) {
			webRequest(endStationWebservice.getMethodName(), endStationWebservice.getWebParams(), endStationWebservice.isPost());
		} else {
			refreshSpiner4Data(endJson);
			refreshSpiner5Data(startJson);
			refreshSpiner6Data(customerJson);
		}
	}

	public void refreshSpData() {
		webRequest(endStationWebservice.getMethodName(), endStationWebservice.getWebParams(), endStationWebservice.isPost());
	}

	public void saveOpt() {
		if (isDataOk()) {
			if (isAdd) {
				saveHistoryInfo();
				// 启动网络请求
				try {
					addWebservice.setParams(getNewEntity());
					webRequest(addWebservice.getMethodName(), addWebservice.getWebParams(), true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				saveHistoryInfo();
				// 启动网络请求
				try {
					editWebservice.setParams(getNewEntity());
					webRequest(editWebservice.getMethodName(), editWebservice.getWebParams(), true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private boolean save(HuoDanEntity entity) {
		return dbm.insertOneHuoDan(entity);
	}

	private void saveHistoryInfo() {
		// saveHistory(HuoDanColumns.DESTINATION, getEditTextValue(R.id.et_01));
		// saveHistory(HuoDanColumns.ORIGIN, getEditTextValue(R.id.et_02));
		saveHistory(HuoDanColumns.CONSIGNEE, getEditTextValue(R.id.et_04));
		saveHistory(HuoDanColumns.ADDRESS_DETAILS, getEditTextValue(R.id.et_05));
	}

	private void initViews(View rootView) {
		// 目的地
		String defValue = "";
		if (entity != null) {
			defValue = entity.getEndStation();
		}
		// InitAutoCompleteTextView(HuoDanColumns.DESTINATION, rootView,
		// R.id.et_01, defValue);
		//spinner04 = (Spinner) rootView.findViewById(R.id.sp_04);
		//InitSpinner(rootView, R.id.sp_04, arrSp4Names, arrSp4Values, defValue);

		// 始发站
		defValue = "";
		if (entity != null) {
			defValue = entity.getStartStation();
		}
		// InitAutoCompleteTextView(HuoDanColumns.ORIGIN, rootView, R.id.et_02,
		// defValue);
		//spinner05 = (Spinner) rootView.findViewById(R.id.sp_05);
		//InitSpinner(rootView, R.id.sp_05, arrSp5Names, arrSp5Values, defValue);

		// 件数
		defValue = "1";
		if (entity != null) {
			defValue = entity.getNum() + "";
		}
		numberSelectView = (NumberSelectedView) rootView.findViewById(R.id.et_03);
		numberSelectView.init(1, 1, -1, 1);
		// InitAutoCompleteTextView(HuoDanColumns.NUMBER, rootView, R.id.et_03,
		// defValue);

		// 收货人
		defValue = "";
		if (entity != null) {
			defValue = entity.getPerson() + "";
		}
		InitAutoCompleteTextView(HuoDanColumns.CONSIGNEE, rootView, R.id.et_04, defValue);

		// 客户
		defValue = "";
		if (entity != null) {
			defValue = entity.getPerson() + "";
		}
		//spinner06 = (Spinner) rootView.findViewById(R.id.sp_06);
		//InitSpinner(rootView, R.id.sp_06, arrSp6Names, arrSp6Values, defValue);

		// 详细地址
		defValue = "";
		if (entity != null) {
			defValue = entity.getAddress() + "";
		}
		InitAutoCompleteTextView(HuoDanColumns.ADDRESS_DETAILS, rootView, R.id.et_05, defValue);

		// 货单状态
		defValue = "未取件";
		if (entity != null) {
			defValue = entity.getStatus() + "";
		}
		spinner01 = (Spinner) rootView.findViewById(R.id.sp_01);
		InitSpinner(rootView, R.id.sp_01, new String[] { "未取件", "已取件" }, new String[] { "new", "over" }, defValue);

		// 服务方式
		defValue = "站点送货";
		if (entity != null) {
			defValue = entity.getServiceType() + "";
		}
		spinner02 = (Spinner) rootView.findViewById(R.id.sp_02);
		InitSpinner(rootView, R.id.sp_02, new String[] { "站点送货", "站点自提", "中心自提" }, new String[] { "站点送货", "站点自提", "中心自提" }, defValue);

		// 服务方式
		defValue = "手机下单";
		if (entity != null) {
			defValue = entity.getSource() + "";
		}
		spinner03 = (Spinner) rootView.findViewById(R.id.sp_03);
		InitSpinner(rootView, R.id.sp_03, new String[] { "手机下单" }, new String[] { "手机下单" }, defValue);
	}

	public HuoDanEntity getNewEntity() {
		entity = new HuoDanEntity();
		// entity.setEndStation(getEditTextValue(R.id.et_01));
		entity.setEndStationId(getSpinnerValue(spinner04));
		entity.setEndStation(arrSp4Names[spinner04.getSelectedItemPosition()]);
		// entity.setStartStation(getEditTextValue(R.id.et_02));
		entity.setStartStationId(getSpinnerValue(spinner05));
		entity.setStartStation(arrSp5Names[spinner05.getSelectedItemPosition()]);
		int number = Integer.parseInt(numberSelectView.getText());
		entity.setNum(number);
		entity.setPerson(getEditTextValue(R.id.et_04));
		entity.setCustomerID(getSpinnerValue(spinner06));
		entity.setCustomName(arrSp6Names[spinner06.getSelectedItemPosition()]);
		entity.setAddress(getEditTextValue(R.id.et_05));
		entity.setServiceType(getSpinnerValue(spinner02));
		// entity.setStatus(getSpinnerValue(spinner01));
		// entity.setSource(getSpinnerValue(spinner03));
		return entity;
	}

	private String getSpinnerValue(Spinner spinner) {
		String value = "";
		if (spinner != null) {
			String[] datas = (String[]) spinner.getTag();
			value = datas[spinner.getSelectedItemPosition()];
		}
		return value;
	}

	public boolean isDataOk() {
		boolean bRt = false;
		if (isDestinationDataOk()) {
			if (isOriginDataOk()) {
				if (isNumberDataOk()) {
					if (isCustomerDataOk()) {
						if (isConsigneeDataOk()) {
							if (isAdressDataOk()) {
								bRt = true;
							} else {
								Util.MsgBox(getActivity(), "请填写详细地址");
							}
						} else {
							Util.MsgBox(getActivity(), "请填写收货人");
						}
					} else {
						Util.MsgBox(getActivity(), "请选择客户信息");
					}
				} else {
					Util.MsgBox(getActivity(), "请填数量");
				}
			} else {
				Util.MsgBox(getActivity(), "请填始发站");
			}
		} else {
			Util.MsgBox(getActivity(), "请填写目的地");
		}
		return bRt;
	}

	private boolean isDestinationDataOk() {
		// return !TextUtils.isEmpty(getEditTextValue(R.id.et_01));
		return !TextUtils.isEmpty(getSpinnerValue(spinner04));
	}

	private boolean isOriginDataOk() {
		// return !TextUtils.isEmpty(getEditTextValue(R.id.et_02));
		return !TextUtils.isEmpty(getSpinnerValue(spinner05));
	}

	private boolean isNumberDataOk() {
		int value = -1;
		String data = numberSelectView.getText().trim();// (R.id.et_03);//
														// TextUtils.isEmpty(;
		if (!TextUtils.isEmpty(data)) {
			try {
				value = Integer.parseInt(data);
			} catch (Exception e) {
			}
		}
		return value > 0;
	}

	private boolean isConsigneeDataOk() {
		return !TextUtils.isEmpty(getSpinnerValue(spinner06));
	}

	private boolean isCustomerDataOk() {
		return !TextUtils.isEmpty(getEditTextValue(R.id.et_04));
	}

	private boolean isAdressDataOk() {
		return !TextUtils.isEmpty(getEditTextValue(R.id.et_05));
	}

	private String getEditTextValue(int viewId) {
		AutoCompleteTextView tv01 = (AutoCompleteTextView) rootView.findViewById(viewId);
		return tv01.getText().toString();
	}

	/**
	 * 初始化EditeText
	 * 
	 * @param key
	 * @param rootGroup
	 * @param editeTextId
	 * @param defValue
	 */
	public void InitAutoCompleteTextView(final String key, View rootGroup, int editeTextId, String defValue) {
		AutoCompleteTextView editText = (AutoCompleteTextView) rootGroup.findViewById(editeTextId);
		// 设置默认值
		if (defValue != null && !defValue.equalsIgnoreCase("")) {
			editText.setText(defValue);
		} else {
			editText.setText("");
		}
		// String[] arrHists = getFieldHistInputData(key);
		initAutoComplete(key, editText);
	}

	/**
	 * 初始化Spinner
	 * 
	 * @param key
	 * @param rootGroup
	 * @param spinnerId
	 */
	public Spinner InitSpinner(View rootGroup, int spinnerId, String[] arrStringDatas, String[] arrValues, String defValue) {
		Spinner spinner = (Spinner) rootGroup.findViewById(spinnerId);
		spinner.setTag(arrValues);
		// 设置风格
		BaseAdapter adapter = new MSpinnerAdatpter(getActivity(), arrStringDatas);
		spinner.setAdapter(adapter);

		// 设置默认值
		if (defValue != null && !defValue.equalsIgnoreCase("")) {
			if (arrStringDatas != null && arrStringDatas.length > 0) {
				// int i = 0;
				int index = 0;
				for (int i = 0; i < arrStringDatas.length; i++) {
					if (arrStringDatas[i] != null && (arrStringDatas[i].equalsIgnoreCase(defValue) || arrValues[i].equalsIgnoreCase(defValue))) {
						index = i;
						break;
					}
					// i++;
				}
				spinner.setSelection(index, true);
			}
		}
		return spinner;
	}

	/**
	 * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param auto
	 *            要操作的AutoCompleteTextView
	 */
	private void initAutoComplete(String field, AutoCompleteTextView auto) {
		String[] hisArrays = getFieldHistInputData(field);
		if (hisArrays != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, hisArrays);
			// 只保留最近的50条的记录
			if (hisArrays.length > 100) {
				String[] newArrays = new String[100];
				System.arraycopy(hisArrays, 0, newArrays, 0, 100);
				adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, newArrays);
			}
			auto.setAdapter(adapter);
			// auto.setDropDownHeight(350);
			auto.setThreshold(1);
			auto.setCompletionHint("历史记录");
			auto.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					AutoCompleteTextView view = (AutoCompleteTextView) v;
					if (hasFocus) {
						view.showDropDown();
					}
				}
			});
		}
	}

	// 历史记录
	private String[] getFieldHistInputData(String fields) {
		if (spHistroyData == null) {
			spHistroyData = getActivity().getSharedPreferences("MyHistoryData", 0);
		}
		String history = spHistroyData.getString(fields, "");
		if (!history.equalsIgnoreCase("")) {
			return history.split(",");
		}
		return null;
	}

	/**
	 * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param value
	 *            记录值
	 */
	private static void saveHistory(String field, String value) {
		if (spHistroyData != null) {
			String longhistory = spHistroyData.getString(field, "");
			if (!longhistory.contains(value + ",")) {
				StringBuilder sb = new StringBuilder(longhistory);
				sb.insert(0, value + ",");
				spHistroyData.edit().putString(field, sb.toString()).commit();
			}
		}
	}

	/**
	 * spinner风格过滤器
	 * 
	 * @author TNT
	 * 
	 */
	public class MSpinnerAdatpter extends BaseAdapter {
		private LayoutInflater mInflater = null;
		private String[] arrDataStrings = null;

		public MSpinnerAdatpter(Context context, int arrId) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arrDataStrings = context.getResources().getStringArray(arrId);
		}

		public MSpinnerAdatpter(Context context, String[] arrDatas) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arrDataStrings = arrDatas;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (arrDataStrings == null) {
				return 0;
			}
			return arrDataStrings.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if (arrDataStrings == null) {
				return null;
			}
			return arrDataStrings[arg0];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (arrDataStrings != null && position < arrDataStrings.length) {
				convertView = mInflater.inflate(R.layout.item_spinner_list_show, parent, false);

				TextView tView1 = (TextView) convertView.findViewById(R.id.tv_spinner);
				tView1.setText(arrDataStrings[position]);
			}
			return convertView;
			// return super.getDropDownView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position < arrDataStrings.length) {
				convertView = mInflater.inflate(R.layout.item_spinner_show, parent, false);

				TextView tView1 = (TextView) convertView.findViewById(R.id.tv_spinner);
				tView1.setText(arrDataStrings[position]);
			}
			return convertView;
		}

	}

	private void toOpt(int optCode, Bundle data) {
		this.mFragmentlistener.onFragmentRequest(CopyOfAddFragment.this, optCode, data);
	}

	@Override
	protected void webserviceCallSucess(String methodName, String result) {
		// TODO Auto-generated method stub
		if (methodName.equalsIgnoreCase(addWebservice.getMethodName())) {
			//
			String code = (String) addWebservice.parseWebResult(result);
			if (!TextUtils.isEmpty(code)) {
				// entity.setCode(code);
				// entity.setID(UUID.randomUUID().toString());
				// save(entity);
				Bundle data = new Bundle();
				data.putString(Constants.KEY.DATA1, entity.getCode());
				// data.putString(Constants.KEY.DATA2, entity.getCode());
				toOpt(RequestInfo.Request_01, data);
			}
		} else if (methodName.equalsIgnoreCase(editWebservice.getMethodName())) {
			save(entity);
			Bundle data = new Bundle();
			data.putString(Constants.KEY.DATA1, entity.getCode());
			// data.putString(Constants.KEY.DATA2, entity.getCode());
			toOpt(RequestInfo.Request_01, data);
		} else if (methodName.equalsIgnoreCase(endStationWebservice.getMethodName())) {
//			String data = (String) endStationWebservice.parseWebResult(result);
//			if (!TextUtils.isEmpty(data)) {
//				refreshSpiner4Data(data);
//				saveSpConfig(Constants.CONFIG_KEY.END_STATION, data);
//				InitSpinner(rootView, R.id.sp_04, arrSp4Names, arrSp4Values, "");
//				webRequest(startStationWebservice.getMethodName(), startStationWebservice.getWebParams(), startStationWebservice.isPost());
//			}

		} else if (methodName.equalsIgnoreCase(startStationWebservice.getMethodName())) {
//			String data = (String) startStationWebservice.parseWebResult(result);
//			if (!TextUtils.isEmpty(data)) {
//				refreshSpiner5Data(data);
//				saveSpConfig(Constants.CONFIG_KEY.START_STATION, data);
//				InitSpinner(rootView, R.id.sp_05, arrSp5Names, arrSp5Values, "");
//				webRequest(customerWebservice.getMethodName(), customerWebservice.getWebParams(), customerWebservice.isPost());
//			}

		} else if (methodName.equalsIgnoreCase(customerWebservice.getMethodName())) {
//			String data = (String) customerWebservice.parseWebResult(result);
//			if (!TextUtils.isEmpty(data)) {
//				refreshSpiner6Data(data);
//				InitSpinner(rootView, R.id.sp_06, arrSp6Names, arrSp6Values, "");
//				saveSpConfig(Constants.CONFIG_KEY.CUSTOMER, data);
//			}
		}
	}

	private void refreshSpiner4Data(String jsonString) {
		try {
			JSONArray arrDatas = new JSONArray(jsonString);
			arrSp4Names = new String[arrDatas.length()];
			arrSp4Values = new String[arrDatas.length()];
			for (int i = 0; i < arrDatas.length(); i++) {
				JSONObject jObj = arrDatas.optJSONObject(i);
				arrSp4Names[i] = jObj.optString("StationName");
				arrSp4Values[i] = jObj.optString("StationID");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshSpiner5Data(String jsonString) {
		try {
			JSONArray arrDatas = new JSONArray(jsonString);
			arrSp5Names = new String[arrDatas.length()];
			arrSp5Values = new String[arrDatas.length()];
			for (int i = 0; i < arrDatas.length(); i++) {
				JSONObject jObj = arrDatas.optJSONObject(i);
				arrSp5Names[i] = jObj.optString("StationName");
				arrSp5Values[i] = jObj.optString("StationID");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshSpiner6Data(String jsonString) {
		try {
			JSONArray arrDatas = new JSONArray(jsonString);
			arrSp6Names = new String[arrDatas.length()];
			arrSp6Values = new String[arrDatas.length()];
			for (int i = 0; i < arrDatas.length(); i++) {
				JSONObject jObj = arrDatas.optJSONObject(i);
				arrSp6Names[i] = jObj.optString("Name");
				arrSp6Values[i] = jObj.optString("ID");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveSpConfig(String key, String json) {
		MApplication app = (MApplication) getActivity().getApplication();
		app.setData(key, json);
	}

	@Override
	protected void webserviceCallFailed(String methodName, String failInfo) {
		// TODO Auto-generated method stub

	}

}
