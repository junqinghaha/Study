package com.android.tnt.hmdy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.tnt.config.Constants;
import com.android.tnt.db.columns.HuoDanColumns;
import com.android.tnt.db.manager.HuoDanDBManager;
import com.android.tnt.hmdy.MApplication;
import com.android.tnt.hmdy.R;
import com.android.uitils.MDateUtils;
import com.android.uitils.Util;

/***
 * 右边菜单栏
 * 
 * @author TNT
 * 
 */
public class RightFragment extends BaseCollectFragment implements View.OnClickListener {

	private String[] arrFliterFields = new String[] { HuoDanColumns.STATE, HuoDanColumns.ORIGIN, HuoDanColumns.DESTINATION,HuoDanColumns.CONSIGNEE,HuoDanColumns.CUSTOMER, HuoDanColumns.DATE_INFO };
	private String[] arrFliterFieldNames = new String[] { "状态", "始发站", "目的站", "收货人", "发货人", "日期" };
	private String[][] arrFliterFieldValuesDef = new String[][] { { "已取件", "未取件" }, { "无" } };
	private String[] DATA_INFO = new String[] { "今天", "三天以内", "一周以内" };

	private ListView lstView1 = null;
	private ListView lstView2 = null;

	private TextView textView1 = null;
	private TextView textView2 = null;
	private RadioGroup group = null;

	private String curFliterField = null;
	private String curFliterFieldValues = null;

	private String[] curArrItems = null;
	private boolean[] curArrCheckInfo = null;

	private ViewGroup vg01 = null;
	private ViewGroup vg02 = null;

	private MApplication app = null;

	View _RootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	public void refreshView() {
		try {
			if (app != null) {
				boolean bRt = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_FLAG, false);
				if (!bRt) {
					recoverFilterSetting();
				}
			}
			refreshFliterValues(curFliterField);
			refreshFiterView();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_RootView = inflater.inflate(R.layout.right_fragment_menu, null);
		_RootView.findViewById(R.id.textView0).setOnClickListener(this);
		_RootView.findViewById(R.id.textView3).setOnClickListener(this);

		vg01 = (ViewGroup) _RootView.findViewById(R.id.vg_01);
		vg02 = (ViewGroup) _RootView.findViewById(R.id.vg_02);
		vg01.setVisibility(View.VISIBLE);
		vg02.setVisibility(View.GONE);

		textView1 = (TextView) _RootView.findViewById(R.id.tv_startTime);
		textView2 = (TextView) _RootView.findViewById(R.id.tv_endTime);
		group = (RadioGroup) _RootView.findViewById(R.id.cb_group);
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.check_01:
					dateOtherShowSetting(0);
					break;
				case R.id.check_02:
					dateOtherShowSetting(-3);
					break;
				case R.id.check_03:
					dateOtherShowSetting(-7);
					break;
				default:
					break;
				}

			}
		});

		textView1.setText(MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4));
		textView2.setText(MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4));
		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);

		lstView1 = (ListView) _RootView.findViewById(R.id.listView1);
		lstView2 = (ListView) _RootView.findViewById(R.id.listView2);
		initListView1();
		initListView2();
		if (curArrItems == null || curArrItems.length == 0) {
			_RootView.findViewById(R.id.textView2).setVisibility(View.VISIBLE);
			lstView2.setVisibility(View.GONE);
		} else {
			_RootView.findViewById(R.id.textView2).setVisibility(View.GONE);
			lstView2.setVisibility(View.VISIBLE);
		}
		return _RootView;
	}

	private void recoverFilterSetting() {
		MApplication app = (MApplication) getActivity().getApplication();
		String fileds = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_NAME, HuoDanColumns.STATE);
		if (!TextUtils.isEmpty(fileds)) {
			String[] arrFields = fileds.split(",");
			if (arrFields != null && arrFields.length > 0) {
				for (String filed : arrFields) {
					if (!TextUtils.isEmpty(filed)) {
						if (filed.equalsIgnoreCase(HuoDanColumns.STATE)) {
							app.setData(filed, HuoDanColumns.STATE_VALUE.STATE_UNDO);
						} else {
							app.setData(filed, "");
						}
					}
				}
			}
			app.setData(Constants.CONFIG_KEY.FLITER_FIELD_NAME, HuoDanColumns.STATE);
		}
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_NAME, "");
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, "");
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, "");
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_FLAG, true);
	}

	private void initData() {
		app = (MApplication) getActivity().getApplication();
		curFliterField = arrFliterFields[0];
		// 获得当前过滤字段
		// String fieldsInfo =
		// app.getData(Constants.CONFIG_KEY.FLITER_FIELD_NAME, "");
		// if(!TextUtils.isEmpty(fieldsInfo)){
		// curFliterField = fieldsInfo.split(",")[0];
		// }
		// 获得当前过来字段的值，保存的字符串：例如： "已取件; 未取件;"
		// curFliterFieldValues =
		// app.getData(Constants.CONFIG_KEY.FLITER_FIELD_VALUES +
		// curFliterField, "");
		curFliterFieldValues = app.getData(curFliterField, "");

		HuoDanDBManager dbm = new HuoDanDBManager(getActivity());
		// 获得当前过滤字段所有的值
		curArrItems = dbm.getGroupData(curFliterField);
		// 对应的选择框
		curArrCheckInfo = new boolean[curArrItems.length];
		// 恢复历史过滤条件
		if (!TextUtils.isEmpty(curFliterFieldValues)) {
			for (int i = 0; i < curArrItems.length; i++) {
				if (curFliterFieldValues.contains(curArrItems[i] + ",")) {
					curArrCheckInfo[i] = true;
				}
			}
		}
	}

	private void initListView1() {
		lstView1.setItemsCanFocus(true);
		lstView1.setAdapter(new FocusAdapter(getActivity(), arrFliterFields, arrFliterFieldNames, curFliterField));
		// lstView1.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		lstView1.invalidateViews();
	}

	private void initListView2() {
		lstView2.setItemsCanFocus(false);
		// lstView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstView2.setAdapter(new CheckAdapter(getActivity(), curArrItems, curArrCheckInfo));
		lstView2.invalidateViews();
	}

	private void refreshFliterValues(String fliterField) {
		if (!TextUtils.isEmpty(fliterField)) {
			curFliterField = fliterField;
			curFliterFieldValues = app.getData(curFliterField, "");
			// curFliterFieldValues =
			// app.getData(Constants.CONFIG_KEY.FLITER_FIELD_VALUES +
			// curFliterField, "");
			if (!fliterField.equalsIgnoreCase(HuoDanColumns.DATE_INFO)) {
				HuoDanDBManager dbm = new HuoDanDBManager(getActivity());
				curArrItems = dbm.getGroupData(curFliterField);
				curArrCheckInfo = new boolean[curArrItems.length];
			} else {
				curArrItems = DATA_INFO;
				curArrCheckInfo = new boolean[DATA_INFO.length];
			}
			if (!TextUtils.isEmpty(curFliterFieldValues)) {
				for (int i = 0; i < curArrItems.length; i++) {
					if (curFliterFieldValues.contains(curArrItems[i] + ",")) {
						curArrCheckInfo[i] = true;
					}
				}
			}
			saveFliterInfo();
		}
	}

	private void refreshFiterView() {
		initListView2();
		if (curArrItems == null || curArrItems.length == 0) {
			_RootView.findViewById(R.id.textView2).setVisibility(View.VISIBLE);
			lstView2.setVisibility(View.GONE);
		} else {
			_RootView.findViewById(R.id.textView2).setVisibility(View.GONE);
			lstView2.setVisibility(View.VISIBLE);
		}
	}

	private void saveFliterInfo() {
		String fieldsInfo = "";
		if (!curFliterField.equalsIgnoreCase(HuoDanColumns.DATE_INFO)) {
			if (!TextUtils.isEmpty(curFliterFieldValues)) {
				fieldsInfo = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_NAME, "");
				if (!TextUtils.isEmpty(fieldsInfo) && !fieldsInfo.endsWith(",")) {
					fieldsInfo += ",";
				}
				if (!fieldsInfo.contains(curFliterField)) {
					fieldsInfo += curFliterField + ",";
				}
			} else {
				fieldsInfo = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_NAME, "");
				if (fieldsInfo.contains(curFliterField)) {
					fieldsInfo.replace(curFliterField + ",", "");
				}
			}
			app.setData(Constants.CONFIG_KEY.FLITER_FIELD_NAME, fieldsInfo);
			app.setData(curFliterField, curFliterFieldValues);
		} else {
			app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_NAME, curFliterField);
			// if (!TextUtils.isEmpty(curFliterFieldValues)) {
			// int nIndex = -1;
			// for (int i = 0; i < DATA_INFO.length; i++) {
			// if (curFliterFieldValues.contains(DATA_INFO[i])) {
			// nIndex = i;
			// break;
			// }
			// }
			// if (nIndex > -1) {
			// switch (nIndex) {
			// case 0:
			// date1Setting();
			// break;
			// case 1:
			// dateOtherSetting(-3);
			// break;
			// case 2:
			// dateOtherSetting(-7);
			// break;
			//
			// default:
			// break;
			// }
			// }
			// }

		}
		// app.setData(Constants.CONFIG_KEY.FLITER_FIELD_VALUES +
		// curFliterField, curFliterFieldValues);
	}

	private void date1Setting() {
		String strTodayDate = MDateUtils.GetCurrentFormatTime(System.currentTimeMillis(), MDateUtils.FORMAT_DATE_4);
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, strTodayDate + " 00:00:00");
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, strTodayDate + " 23:59:59");
	}

	private void dateOtherSetting(int day) {
		String strTodayDate = MDateUtils.GetCurrentFormatTime(System.currentTimeMillis(), MDateUtils.FORMAT_DATE_4);
		long data1 = MDateUtils.GetBeforeAfterDate(System.currentTimeMillis(), day);
		String startDaty = MDateUtils.GetCurrentFormatTime(data1, MDateUtils.FORMAT_DATE_4);
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, startDaty + " 00:00:00");
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, strTodayDate + " 23:59:59");
	}

	private void dateOtherShowSetting(int day) {
		String strTodayDate = MDateUtils.GetCurrentFormatTime(System.currentTimeMillis(), MDateUtils.FORMAT_DATE_4);
		long data1 = MDateUtils.GetBeforeAfterDate(System.currentTimeMillis(), day);
		String startDaty = MDateUtils.GetCurrentFormatTime(data1, MDateUtils.FORMAT_DATE_4);
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, startDaty + " 00:00:00");
		app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, strTodayDate + " 23:59:59");
		textView1.setText(startDaty);
		textView2.setText(strTodayDate);
	}

	class FocusAdapter extends BaseAdapter {
		private String[] arrDatas = null;
		private String[] arrDatasShow = null;
		private String focusFields = null;
		LayoutInflater inflater = null;

		public FocusAdapter(Context context, String[] arrDatas, String[] arrDatasShow, String focusFields) {
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.arrDatas = arrDatas;
			this.arrDatasShow = arrDatasShow;
			this.focusFields = focusFields;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.arrDatas.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.arrDatas[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub\
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.layout_focus_info, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.textView);
			textView.setText(this.arrDatasShow[position]);
			if (focusFields.equalsIgnoreCase(arrDatas[position])) {
				textView.setSelected(true);
			} else {
				textView.setSelected(false);
			}
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setSelected(true);
					if (!arrDatas[position].equalsIgnoreCase(HuoDanColumns.DATE_INFO)) {
						if (vg01.getVisibility() != View.VISIBLE) {
							vg01.setVisibility(View.VISIBLE);
						}
						if (vg02.getVisibility() != View.GONE) {
							vg02.setVisibility(View.GONE);
						}
						refreshFliterValues(arrDatas[position]);
						refreshFiterView();
						focusFields = arrDatas[position];
						notifyDataSetChanged();
					} else {
						focusFields = arrDatas[position];
						if (vg02.getVisibility() != View.VISIBLE) {
							vg02.setVisibility(View.VISIBLE);
						}
						if (vg01.getVisibility() != View.GONE) {
							vg01.setVisibility(View.GONE);
						}

					}
				}
			});

			return convertView;
		}
	}

	class CheckAdapter extends BaseAdapter {
		private String[] arrDatas = null;
		private boolean[] arrCheckInfos = null;
		LayoutInflater inflater = null;

		public CheckAdapter(Context context, String[] arrDatas, boolean[] arrCheckInfos) {
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.arrDatas = arrDatas;
			this.arrCheckInfos = arrCheckInfos;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.arrDatas.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.arrDatas[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub\
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.layout_check_info, null);
			}
			CheckBox textView = (CheckBox) convertView.findViewById(R.id.checkTextView);
			textView.setText(this.arrDatas[position]);
			textView.setChecked(this.arrCheckInfos[position]);
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!curFliterField.equalsIgnoreCase(HuoDanColumns.DATE_INFO)) {
						arrCheckInfos[position] = !arrCheckInfos[position];
						if (arrCheckInfos[position]) {
							if(!TextUtils.isEmpty(curFliterFieldValues) && !curFliterFieldValues.endsWith(",")){
								curFliterFieldValues += ",";								
							}
							if (!curFliterFieldValues.contains(arrDatas[position] + ",")) {
								curFliterFieldValues += arrDatas[position] + ",";
							}
						} else {
							if (curFliterFieldValues.contains(arrDatas[position] + ",")) {
								curFliterFieldValues = curFliterFieldValues.replace(arrDatas[position] + ",", "");
							}
						}
					} else {
						for (boolean bCheck : arrCheckInfos) {
							bCheck = false;
						}
						arrCheckInfos[position] = !arrCheckInfos[position];
						if (arrCheckInfos[position]) {
							if (!curFliterFieldValues.contains(arrDatas[position] + ",")) {
								curFliterFieldValues = arrDatas[position] + ",";
							}
						} else {
							if (curFliterFieldValues.contains(arrDatas[position] + ",")) {
								curFliterFieldValues = curFliterFieldValues.replace(arrDatas[position] + ",", "");
							}
						}
					}

					((CheckBox) v).setChecked(arrCheckInfos[position]);
					saveFliterInfo();
				}
			});

			return convertView;
		}
	}

	private boolean isDateTimeOk() {
		boolean bRt = false;
		long startTime = MDateUtils.FormateStringTimeToLong(textView1.getText().toString(), MDateUtils.FORMAT_DATE_4);
		long endTime = MDateUtils.FormateStringTimeToLong(textView2.getText().toString(), MDateUtils.FORMAT_DATE_4);
		if (startTime <= endTime) {
			bRt = true;
		} else {
			Util.MsgBoxLong(getActivity(), "起始时间不可大于终止时间");
		}
		return bRt;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView0:
			recoverFilterSetting();
			if (mFragmentlistener != null) {
				mFragmentlistener.onFragmentRequest(this, 0, null);
			}
			break;
		case R.id.textView3: {
			if (isDateTimeOk()) {
				MApplication app = (MApplication) getActivity().getApplication();
				app.setData(Constants.CONFIG_KEY.FLITER_FIELD_FLAG, true);
				saveFliterInfo();
				app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, textView1.getText().toString() + " 00:00:00");
				app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, textView2.getText().toString() + " 23:59:59");
				if (mFragmentlistener != null) {
					mFragmentlistener.onFragmentRequest(this, 1, null);
				}
			}
		}
			break;
		case R.id.tv_startTime: {// 起始时间
			onDateClick(v);
		}
			break;
		case R.id.tv_endTime: {// 终止时间
			onDateClick(v);
		}
			break;
		default:
			break;
		}
	}
}
