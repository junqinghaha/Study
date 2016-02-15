package com.android.tnt.hmdy.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.android.tnt.hmdy.PrintSettingActivity;
import com.android.tnt.hmdy.R;
import com.android.tnt.view.NumberSelectedView;
import com.android.uitils.PrintUtil;
import com.android.uitils.Util;
import com.mtkj.webservice.WebServiceFragment;
import com.mtkj.webservice.method.WMTakeCustomerStation;
import com.mtkj.webservice.method.WMTakeEndStation;
import com.mtkj.webservice.method.WMTakeOutGoodsGetByCode;
import com.mtkj.webservice.method.WMTakeStartStation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/***
 * 
 * 添加
 * 
 * @author TNT
 * 
 */
public class PrintOneFragment extends WebServiceFragment implements View.OnClickListener {

	private final String TAG = "PrintOneFragment";

	private static SharedPreferences spHistroyData;
	private HuoDanDBManager dbm = null;
	private HuoDanEntity entity = null;
	private boolean isAdd = true;

	private View rootView = null;
	private Spinner spinner01 = null;
	private Spinner spinner02 = null;
	private Spinner spinner03 = null;

	// private Spinner spinner04 = null;
	// private Spinner spinner05 = null;
	// private Spinner spinner06 = null;

	private WMTakeOutGoodsGetByCode mGetByCode = null;

	private NumberSelectedView numberSelectView = null;
	private NumberSelectedView numberStartSelectView = null;
	private NumberSelectedView numberEndSelectView = null;

	private WMTakeStartStation startStationWebservice = null;
	private WMTakeEndStation endStationWebservice = null;
	private WMTakeCustomerStation customerWebservice = null;

	private String[] arrSp4Names = new String[] {};
	private String[] arrSp4Values = new String[] {};
	private HashMap<String, String> map4 = new HashMap<String, String>();

	private String[] arrSp5Names = new String[] {};
	private String[] arrSp5Values = new String[] {};
	private HashMap<String, String> map5 = new HashMap<String, String>();

	private String[] arrSp6Names = new String[] {};
	private String[] arrSp6Values = new String[] {};
	private HashMap<String, String> map6 = new HashMap<String, String>();

	public interface RequestInfo {
		public int Request_01 = 1;
		public int Request_02 = 2;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbm = new HuoDanDBManager(getActivity());

		mGetByCode = new WMTakeOutGoodsGetByCode();
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
		rootView = inflater.inflate(R.layout.fragment_hm_print_one, container, false);
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

	public void printOpt() {
		if (isDataOk()) {
			if (isAdd) {
				saveHistoryInfo();
				int startIndex = numberStartSelectView.getTextValue();
				int endIndex = numberEndSelectView.getTextValue();
				goodsPrint(getNewEntity(), startIndex, endIndex);
			} else {
				saveHistoryInfo();
				int startIndex = numberStartSelectView.getTextValue();
				int endIndex = numberEndSelectView.getTextValue();
				goodsPrint(getEditEntity(), startIndex, endIndex);
			}
		}
	}

	/***
	 * 提货完成操作
	 * 
	 * @return
	 */
	private void goodsPrint(HuoDanEntity entity, int nIndex, int endIndex) {
		if (entity != null) {
			MApplication app = (MApplication) getActivity().getApplication();
			String blDeviceAdress = app.getData(Constants.CONFIG_KEY.BLDevice, "");
			if (!TextUtils.isEmpty(blDeviceAdress)) {
				if (PrintUtil.isPrinterOk(getActivity(), blDeviceAdress)) {
					for (int i = nIndex; i < (endIndex + 1); i++) {
						PrintUtil.PrintOne(getActivity(), blDeviceAdress, entity, i);
					}
				} else {
					// Util.MsgBox(getActivity(), "请确认打印设备状态");
				}
			} else {
				new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("当前打印机未设置，是否现在设置？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(), PrintSettingActivity.class);
						getActivity().startActivity(intent);
						// Intent intent = new Intent(getActivity(),
						// PrintActivity.class);
						// Bundle data = new Bundle();
						// data.putSerializable(Constants.KEY.DATA1, entity);
						// intent.putExtras(data);
						// getActivity().startActivity(intent);
					}
				}).setNegativeButton("取消", null).create().show();
			}
		} else {
			Util.MsgBox(getActivity(), "数据为空");
		}
	}

	private boolean save(HuoDanEntity entity) {
		return dbm.insertOneHuoDan(entity);
	}

	private void saveHistoryInfo() {
		// saveHistory(HuoDanColumns.DESTINATION, getEditTextValue(R.id.et_01));
		// saveHistory(HuoDanColumns.ORIGIN, getEditTextValue(R.id.et_02));
		saveHistory(HuoDanColumns.THDH, getEditTextValue(R.id.et_00));
		saveHistory(HuoDanColumns.CONSIGNEE, getEditTextValue(R.id.et_04));
		saveHistory(HuoDanColumns.ADDRESS_DETAILS, getEditTextValue(R.id.et_05));
	}

	private void initViews(View rootView) {
		rootView.findViewById(R.id.button1).setOnClickListener(this);

		// 货单号
		String defValue = "";
		if (entity != null) {
			defValue = entity.getCode();
		}
		InitAutoCompleteTextView(HuoDanColumns.THDH, rootView, R.id.et_00, defValue);

		// 目的地
		defValue = "";
		if (entity != null) {
			defValue = entity.getEndStation();
		}
		InitAutoCompleteTextView(HuoDanColumns.DESTINATION, rootView, R.id.et_01, arrSp4Names, arrSp4Values, defValue);
		rootView.findViewById(R.id.et_01).setEnabled(false);
		// spinner04 = (Spinner) rootView.findViewById(R.id.sp_04);
		// InitSpinner(rootView, R.id.sp_04, arrSp4Names, arrSp4Values,
		// defValue);

		// 始发站
		defValue = "";
		if (entity != null) {
			defValue = entity.getStartStation();
		}
		InitAutoCompleteTextView(HuoDanColumns.ORIGIN, rootView, R.id.et_02, arrSp5Names, arrSp5Values, defValue);
		rootView.findViewById(R.id.et_02).setEnabled(false);
		// spinner05 = (Spinner) rootView.findViewById(R.id.sp_05);
		// InitSpinner(rootView, R.id.sp_05, arrSp5Names, arrSp5Values,
		// defValue);

		// 件数
		defValue = "1";
		if (entity != null) {
			defValue = entity.getNum() + "";
		}
		int num = 1;
		try {
			num = Integer.parseInt(defValue);
		} catch (Exception e) {
			// TODO: handle exception
		}
		numberSelectView = (NumberSelectedView) rootView.findViewById(R.id.et_03);
		numberSelectView.init(num, 1, -1, 1);
		numberSelectView.setEnabled(false);

		numberStartSelectView = (NumberSelectedView) rootView.findViewById(R.id.et_03_3);
		if (entity != null) {
			numberStartSelectView.init(0, 1, entity.getNum(), 1);
		} else {
			numberStartSelectView.init(0, 1, -1, 1);
		}
		numberEndSelectView = (NumberSelectedView) rootView.findViewById(R.id.et_03_4);
		if (entity != null) {
			numberEndSelectView.init(0, 1, entity.getNum(), 1);
		} else {
			numberEndSelectView.init(0, 1, -1, 1);
		}
		// InitAutoCompleteTextView(HuoDanColumns.NUMBER, rootView, R.id.et_03,
		// defValue);

		// 收货人
		defValue = "";
		if (entity != null) {
			defValue = entity.getPerson() + "";
		}
		InitAutoCompleteTextView(HuoDanColumns.CONSIGNEE, rootView, R.id.et_04, defValue);
		rootView.findViewById(R.id.et_04).setEnabled(false);

		// 客户
		defValue = "";
		if (entity != null) {
			defValue = entity.getCustomName() + "";
		}
		InitAutoCompleteTextView(HuoDanColumns.CONSIGNEE, rootView, R.id.et_06, arrSp6Names, arrSp6Values, defValue);
		rootView.findViewById(R.id.et_06).setEnabled(false);
		// spinner06 = (Spinner) rootView.findViewById(R.id.sp_06);
		// InitSpinner(rootView, R.id.sp_06, arrSp6Names, arrSp6Values,
		// defValue);

		// 详细地址
		defValue = "";
		if (entity != null) {
			defValue = entity.getAddress() + "";
		}
		InitAutoCompleteTextView(HuoDanColumns.ADDRESS_DETAILS, rootView, R.id.et_05, defValue);
		rootView.findViewById(R.id.et_05).setEnabled(false);

		// 货单状态
		defValue = "未取件";
		if (entity != null) {
			defValue = entity.getStatus() + "";
		}
		spinner01 = (Spinner) rootView.findViewById(R.id.sp_01);
		InitSpinner(rootView, R.id.sp_01, new String[] { "未取件", "已取件" }, new String[] { "new", "over" }, defValue);
		rootView.findViewById(R.id.sp_01).setEnabled(false);

		// 服务方式
		defValue = "站点送货";
		if (entity != null) {
			defValue = entity.getServiceType() + "";
		}
		spinner02 = (Spinner) rootView.findViewById(R.id.sp_02);
		rootView.findViewById(R.id.sp_02).setEnabled(false);
		InitSpinner(rootView, R.id.sp_02, new String[] { "站点送货", "站点自提", "中心自提" }, new String[] { "站点送货", "站点自提", "中心自提" }, defValue);

		// 服务方式
		defValue = "手机下单";
		if (entity != null) {
			defValue = entity.getSource() + "";
		}
		spinner03 = (Spinner) rootView.findViewById(R.id.sp_03);
		InitSpinner(rootView, R.id.sp_03, new String[] { "手机下单" }, new String[] { "手机下单" }, defValue);
		rootView.findViewById(R.id.sp_03).setEnabled(false);
	}

	public HuoDanEntity getNewEntity() {
		entity = new HuoDanEntity();
		// entity.setEndStation(getEditTextValue(R.id.et_01));
		entity.setCode(getEditTextValue(R.id.et_00));
		String name = getEditTextValue(R.id.et_01);
		String value = map4.get(name);
		if (value == null) {
			value = "";
		}
		entity.setEndStationId(value);
		entity.setEndStation(name);
		// entity.setEndStationId(getSpinnerValue(spinner04));
		// entity.setEndStation(arrSp4Names[spinner04.getSelectedItemPosition()]);

		// entity.setStartStation(getEditTextValue(R.id.et_02));
		name = getEditTextValue(R.id.et_02);
		value = map5.get(name);
		if (value == null) {
			value = "";
		}
		entity.setStartStationId(value);
		entity.setStartStation(name);
		// entity.setStartStationId(getSpinnerValue(spinner05));
		// entity.setStartStation(arrSp5Names[spinner05.getSelectedItemPosition()]);
		int number = Integer.parseInt(numberSelectView.getText());
		entity.setNum(number);
		entity.setPerson(getEditTextValue(R.id.et_04));

		name = getEditTextValue(R.id.et_06);
		value = map6.get(name);
		if (value == null) {
			value = "";
		}
		entity.setCustomerID(value);
		entity.setCustomName(name);
		// entity.setCustomerID(getSpinnerValue(spinner06));
		// entity.setCustomName(arrSp6Names[spinner06.getSelectedItemPosition()]);
		entity.setAddress(getEditTextValue(R.id.et_05));
		entity.setServiceType(getSpinnerValue(spinner02));
		// entity.setStatus(getSpinnerValue(spinner01));
		// entity.setSource(getSpinnerValue(spinner03));
		return entity;
	}

	public HuoDanEntity getEditEntity() {
		// entity = new HuoDanEntity();
		// entity.setEndStation(getEditTextValue(R.id.et_01));
		entity.setCode(getEditTextValue(R.id.et_01));

		String name = getEditTextValue(R.id.et_01);
		String value = map4.get(name);
		if (value == null) {
			value = "";
		}
		entity.setEndStationId(value);
		entity.setEndStation(name);
		// entity.setEndStationId(getSpinnerValue(spinner04));
		// entity.setEndStation(arrSp4Names[spinner04.getSelectedItemPosition()]);

		// entity.setStartStation(getEditTextValue(R.id.et_02));
		name = getEditTextValue(R.id.et_02);
		value = map5.get(name);
		if (value == null) {
			value = "";
		}
		entity.setStartStationId(value);
		entity.setStartStation(name);
		// entity.setStartStationId(getSpinnerValue(spinner05));
		// entity.setStartStation(arrSp5Names[spinner05.getSelectedItemPosition()]);
		int number = Integer.parseInt(numberSelectView.getText());
		entity.setNum(number);
		entity.setPerson(getEditTextValue(R.id.et_04));

		name = getEditTextValue(R.id.et_06);
		value = map6.get(name);
		if (value == null) {
			value = "";
		}
		entity.setCustomerID(value);
		entity.setCustomName(name);
		// entity.setCustomerID(getSpinnerValue(spinner06));
		// entity.setCustomName(arrSp6Names[spinner06.getSelectedItemPosition()]);
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
		if (isCodeOk()) {
			if (isDestinationDataOk()) {
				if (isOriginDataOk()) {
					if (isNumberDataOk()) {
						if (isNumberOk()) {
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
								// Util.MsgBox(getActivity(), "请选择客户信息");
							}
						} else {
							// Util.MsgBox(getActivity(), "请填补打页码");
						}
					} else {
						Util.MsgBox(getActivity(), "请填数量");
					}
				} else {
					// Util.MsgBox(getActivity(), "请填始发站");
				}
			} else {
				// Util.MsgBox(getActivity(), "请填写目的地");
			}
		} else {
			Util.MsgBox(getActivity(), "请填写货单号");
		}

		return bRt;
	}

	private boolean isCodeOk() {
		return !TextUtils.isEmpty(getEditTextValue(R.id.et_00));
	}

	private boolean isDestinationDataOk() {
		String name = getEditTextValue(R.id.et_01);
		String value = map4.get(name);
		if (!TextUtils.isEmpty(name)) {// && !TextUtils.isEmpty(value)) {
			return true;
		} else {
			if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(value)) {
				Util.MsgBox(getActivity(), "请选择正确的目的地");
			} else {
				Util.MsgBox(getActivity(), "请选择目的地");
			}
		}
		return false;
		// return !TextUtils.isEmpty(getSpinnerValue(spinner04));
	}

	private boolean isOriginDataOk() {
		String name = getEditTextValue(R.id.et_02);
		String value = map5.get(name);
		if (!TextUtils.isEmpty(name)) {// && !TextUtils.isEmpty(value)) {
			return true;
		} else {
			if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(value)) {
				Util.MsgBox(getActivity(), "请选择正确的始发站");
			} else {
				Util.MsgBox(getActivity(), "请选择始发站");
			}
		}
		return false;
		// return !TextUtils.isEmpty(getSpinnerValue(spinner05));
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

	private boolean isNumberOk() {
		boolean bRt = false;
		if (isStartNumberChildDataOk() && isEndNumberChildDataOk()) {
			int valueStart = numberStartSelectView.getTextValue();
			int valueEnd = numberEndSelectView.getTextValue();
			if (valueStart <= valueEnd) {
				bRt = true;
			} else {
				Util.MsgBoxLong(getActivity(), "起始页码不可大于终止页码");
			}
		}
		return bRt;
	}

	private boolean isStartNumberChildDataOk() {
		boolean bRt = false;
		int value = -1;
		String data = numberStartSelectView.getText().trim();// (R.id.et_03);//
		// TextUtils.isEmpty(;
		if (!TextUtils.isEmpty(data)) {
			try {
				value = Integer.parseInt(data);
				if (value > 0) {
					int num = 0;
					if (entity != null) {
						num = entity.getNum();
					} else {
						num = numberSelectView.getTextValue();
					}
					if (value <= num) {
						if (value > 0) {
							bRt = true;
						} else {
							Util.MsgBox(getActivity(), "补打页码不能为0");
						}
					} else {
						Util.MsgBox(getActivity(), "不能大于总数：" + entity.getNum());
					}
				} else {
					Util.MsgBox(getActivity(), "起始页未设置");
				}
			} catch (Exception e) {
				Util.MsgBox(getActivity(), "补打数值错误");
			}
		} else {
			Util.MsgBox(getActivity(), "补打数值不能未空");
		}
		return bRt;
	}

	private boolean isEndNumberChildDataOk() {
		boolean bRt = false;
		int value = -1;
		String data = numberEndSelectView.getText().trim();// (R.id.et_03);//
		// TextUtils.isEmpty(;
		if (!TextUtils.isEmpty(data)) {
			try {
				value = Integer.parseInt(data);
				if (value > 0) {
					int num = 0;
					if (entity != null) {
						num = entity.getNum();
					} else {
						num = numberSelectView.getTextValue();
					}
					if (value <= num) {
						if (value > 0) {
							bRt = true;
						} else {
							Util.MsgBox(getActivity(), "补打页码不能为0");
						}
					} else {
						Util.MsgBox(getActivity(), "不能大于总数：" + entity.getNum());
					}
				} else {
					Util.MsgBox(getActivity(), "终止页未设置");
				}

			} catch (Exception e) {
				Util.MsgBox(getActivity(), "补打数值错误");
			}
		} else {
			Util.MsgBox(getActivity(), "补打数值不能未空");
		}
		return bRt;
	}

	private boolean isConsigneeDataOk() {
		return !TextUtils.isEmpty(getEditTextValue(R.id.et_04));
	}

	private boolean isCustomerDataOk() {
		return true;
		// String name = getEditTextValue(R.id.et_06);
		// String value = map6.get(name);
		// if (!TextUtils.isEmpty(name)){//&& !TextUtils.isEmpty(value)) {
		// return true;
		// } else {
		// if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(value)) {
		// Util.MsgBox(getActivity(), "请选择正确的客户信息");
		// } else {
		// Util.MsgBox(getActivity(), "请选择客户");
		// }
		// }
		// return false;
		// return !TextUtils.isEmpty(getSpinnerValue(spinner06));
	}

	private boolean isAdressDataOk() {
		return !TextUtils.isEmpty(getEditTextValue(R.id.et_05));
	}

	private String getEditTextValue(int viewId) {
		try {
			if (rootView != null) {
				AutoCompleteTextView tv01 = (AutoCompleteTextView) rootView.findViewById(viewId);
				return tv01.getText().toString().trim();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
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
	 * 初始化EditeText
	 * 
	 * @param key
	 * @param rootGroup
	 * @param editeTextId
	 * @param defValue
	 */
	public void InitAutoCompleteTextView(final String key, View rootGroup, int editeTextId, String[] arrDatas, String[] arrValues, String defValue) {
		AutoCompleteTextView editText = (AutoCompleteTextView) rootGroup.findViewById(editeTextId);
		// 设置默认值
		if (defValue != null && !defValue.equalsIgnoreCase("")) {
			editText.setText(defValue);
		} else {
			editText.setText("");
		}
		// String[] arrHists = getFieldHistInputData(key);
		initAutoComplete(arrDatas, arrValues, key, editText);
	}

	/**
	 * 初始化Spinner
	 * 
	 * @param rootGroup
	 * @param spinnerId
	 */
	public Spinner InitSpinner(View rootGroup, int spinnerId, String[] arrStringDatas, String[] arrValues, String defValue) {
		Spinner spinner = (Spinner) rootGroup.findViewById(spinnerId);
		spinner.setTag(arrValues);
		// 设置风格
		BaseAdapter adapter = new MSpinnerAdatpter(spinner, getActivity(), arrStringDatas);
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

	/**
	 * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param auto
	 *            要操作的AutoCompleteTextView
	 */
	private void initAutoComplete(String[] arrDatas, String[] arrValues, String field, final AutoCompleteTextView auto) {
		if (arrDatas != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrDatas);
			auto.setAdapter(adapter);
			// auto.setDropDownHeight(350);
			auto.setThreshold(1);
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
		private Spinner spinner = null;

		public MSpinnerAdatpter(Spinner spinner, Context context, int arrId) {
			this.spinner = spinner;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arrDataStrings = context.getResources().getStringArray(arrId);
		}

		public MSpinnerAdatpter(Spinner spinner, Context context, String[] arrDatas) {
			this.spinner = spinner;
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
				if (this.spinner != null && !this.spinner.isEnabled()) {
					if (tView1.isEnabled()) {
						tView1.setEnabled(false);
						tView1.setTextColor(Color.GRAY);
					}
				} else {
					if (!tView1.isEnabled()) {
						tView1.setEnabled(true);
						tView1.setTextColor(Color.BLACK);
					}
				}
			}
			return convertView;
		}

	}

	private void toOpt(int optCode, Bundle data) {
		this.mFragmentlistener.onFragmentRequest(PrintOneFragment.this, optCode, data);
	}

	@Override
	protected void webserviceCallSucess(String methodName, String result) {
		// TODO Auto-generated method stub
		if (methodName.equalsIgnoreCase(endStationWebservice.getMethodName())) {
			String data = (String) endStationWebservice.parseWebResult(result);
			if (!TextUtils.isEmpty(data)) {
				refreshSpiner4Data(data);
				saveSpConfig(Constants.CONFIG_KEY.END_STATION, data);
				// InitSpinner(rootView, R.id.sp_04, arrSp4Names, arrSp4Values,
				// "");
				InitAutoCompleteTextView(HuoDanColumns.DESTINATION, rootView, R.id.et_01, arrSp4Names, arrSp4Values, getEditTextValue(R.id.et_01));
				webRequest(startStationWebservice.getMethodName(), startStationWebservice.getWebParams(), startStationWebservice.isPost());
			}

		} else if (methodName.equalsIgnoreCase(startStationWebservice.getMethodName())) {
			String data = (String) startStationWebservice.parseWebResult(result);
			if (!TextUtils.isEmpty(data)) {
				refreshSpiner5Data(data);
				saveSpConfig(Constants.CONFIG_KEY.START_STATION, data);
				// InitSpinner(rootView, R.id.sp_05, arrSp5Names, arrSp5Values,
				// "");
				InitAutoCompleteTextView(HuoDanColumns.ORIGIN, rootView, R.id.et_02, arrSp5Names, arrSp5Values, getEditTextValue(R.id.et_02));
				webRequest(customerWebservice.getMethodName(), customerWebservice.getWebParams(), customerWebservice.isPost());
			}

		} else if (methodName.equalsIgnoreCase(customerWebservice.getMethodName())) {
			String data = (String) customerWebservice.parseWebResult(result);
			if (!TextUtils.isEmpty(data)) {
				refreshSpiner6Data(data);
				// InitSpinner(rootView, R.id.sp_06, arrSp6Names, arrSp6Values,
				// "");
				InitAutoCompleteTextView(HuoDanColumns.CONSIGNEE, rootView, R.id.et_06, arrSp6Names, arrSp6Values, getEditTextValue(R.id.et_06));
				saveSpConfig(Constants.CONFIG_KEY.CUSTOMER, data);
			}
		} else if (methodName.equalsIgnoreCase(mGetByCode.getMethodName())) {
			List<HuoDanEntity> lstData = (List<HuoDanEntity>) mGetByCode.parseWebResult(result);
			// int updateCount = lstData.size();
			if (lstData != null && lstData.size() > 0) {
				entity = lstData.get(0);
				initViews(rootView);
			} else {
				Util.MsgBox(getActivity(), "无此货单");
			}
			// Util.MsgBox(getActivity(), "更新" + updateCount + "条数据");
		}
	}

	private void refreshSpiner4Data(String jsonString) {
		try {
			JSONArray arrDatas = new JSONArray(jsonString);
			arrSp4Names = new String[arrDatas.length()];
			arrSp4Values = new String[arrDatas.length()];
			map4.clear();
			for (int i = 0; i < arrDatas.length(); i++) {
				JSONObject jObj = arrDatas.optJSONObject(i);
				arrSp4Names[i] = jObj.optString("StationName");
				arrSp4Values[i] = jObj.optString("StationID");
				map4.put(arrSp4Names[i], arrSp4Values[i]);
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
			map5.clear();
			for (int i = 0; i < arrDatas.length(); i++) {
				JSONObject jObj = arrDatas.optJSONObject(i);
				arrSp5Names[i] = jObj.optString("StationName");
				arrSp5Values[i] = jObj.optString("StationID");
				map5.put(arrSp5Names[i], arrSp5Values[i]);
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
			map6.clear();
			for (int i = 0; i < arrDatas.length(); i++) {
				JSONObject jObj = arrDatas.optJSONObject(i);
				arrSp6Names[i] = jObj.optString("Name");
				arrSp6Values[i] = jObj.optString("ID");
				map6.put(arrSp6Names[i], arrSp6Values[i]);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			if (isCodeOk()) {
				try {
					mGetByCode.setParams(getEditTextValue(R.id.et_00));
					webRequest(mGetByCode.getMethodName(), mGetByCode.getWebParams(), false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}

}
