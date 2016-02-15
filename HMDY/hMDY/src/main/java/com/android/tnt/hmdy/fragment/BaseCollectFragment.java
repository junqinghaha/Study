package com.android.tnt.hmdy.fragment;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.tnt.config.Constants;
import com.android.tnt.hmdy.R;
import com.android.tnt.hmdy.adapter.MSpinnerAdapter;
import com.android.uitils.Util;
import com.utils.log.MLog;

/***
 * @author 作者 周涛涛
 * @version 创建时间：2015年3月25日 类说明 碎片基类
 */

public abstract class BaseCollectFragment extends BaseFragment {

	private static SharedPreferences spHistroyData;
	
	private final String TAG = "BaseCollectFragment";
	
	protected String taskId = "";
	protected String tableName = "";
	protected String tableAlias = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		spHistroyData = getActivity().getSharedPreferences("MyHistoryData", 0);
		super.onCreate(savedInstanceState);
		try {
			taskId = getArguments().getString(Constants.KEY.DATA1);
			tableName = getArguments().getString(Constants.KEY.DATA2);
			tableAlias = getArguments().getString(Constants.KEY.DATA3);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	// 解决childfragemanager初始化问题 java.lang.IllegalStateException: No activity
	// http://blog.csdn.net/leewenjin/article/details/19410949
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 */
	protected void showMessage(String msg) {
		Util.MsgBox(getActivity(), msg);
		// Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 信息提示
	 * 
	 * @param msgID
	 */
	protected void showMessage(int msgID) {
		String msg = getResources().getString(msgID);
		showMessage(msg);
	}

	/**
	 * 信息显示
	 * 
	 * @param title
	 * @param msg
	 */
	protected void showMsgDlg(String title, String msg) {
		new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(msg).setPositiveButton(R.string.ok, null).show();
	}

	
	/**
	 * 初始化Spinner
	 * 
	 * @param key
	 * @param rootGroup
	 * @param spinnerId
	 */
	public Spinner InitSpinner(Spinner spinner, final String[] arrSpinnerDatas, final String[] arrSpinnerValues) {
		// 设置风格
		BaseAdapter adapter = new MSpinnerAdapter(getActivity(), arrSpinnerDatas);
		spinner.setAdapter(adapter);
		spinner.setTag(arrSpinnerValues);
		adapter.notifyDataSetChanged();
		spinner.invalidate();
		return spinner;
	}
	
	public String getSpinnerData(Spinner spinner) {
		String data = "";
		if (spinner != null) {
			if (spinner.getTag() != null) {
				String[] arrValues = (String[]) spinner.getTag();
				data = arrValues[spinner.getSelectedItemPosition()] + "";
			}
		}
		return data;
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
	 * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param auto
	 *            要操作的AutoCompleteTextView
	 */
	public void initAutoComplete(String field, AutoCompleteTextView auto) {
		try {
			String[] hisArrays = getFieldHistInputData(field);
			if (hisArrays != null) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, hisArrays);
				// 只保留最近的5条的记录
				if (hisArrays.length > 10) {
					String[] newArrays = new String[10];
					System.arraycopy(hisArrays, 0, newArrays, 0, 10);
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
		} catch (Exception e) {
			MLog.e(TAG, "initAutoComplete--" + e.toString());
		}
	}

	/**
	 * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param value
	 *            记录值
	 */
	public static void saveAutoCompleteHistory(String field, AutoCompleteTextView auto) {
		if (spHistroyData != null && auto != null) {
			String value = auto.getText().toString().trim();
			if(!TextUtils.isEmpty(value)){
				String longhistory = spHistroyData.getString(field, "");
				if (!longhistory.contains(value + ",")) {
					StringBuilder sb = new StringBuilder(longhistory);
					sb.insert(0, value + ",");
					spHistroyData.edit().putString(field, sb.toString()).commit();
				}
			}
		}
	}
	
	private TextView mDateTextView = null;
	private TextView mTimeTextView = null;
	private String mDateTmp = "";
	private String mTimeTmp = "";
	private int mHour;
	private int mMinute;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	private int curShowType = 0;

	public interface SHOW_TYPE {
		public int TYPE_GPS = 1;
		public int TYPE_DATE_E = 2;
		public int TYPE_DATE_E_NO_YEAR = 3;
		public int TYPE_DATE_CN = 4;
		public int TYPE_DATE_CN_NO_YEAR = 5;
	}


	/** 时间对话框ID **/
	final int TIME_DIALOG_ID = 0x101;
	/** 日期对话框ID **/
	final int DATE_DIALOG_ID = 0x201;

	/***
	 * 初始化默认时间值
	 */
	private void timeValuesInit() {
		// 初始化时间
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		if (mMinute < 10) {
			mTimeTmp = mHour + ":0" + mMinute + ":00";
		} else {
			mTimeTmp = mHour + ":" + mMinute + ":00";
		}

		mDateTmp = mYear + "-" + (mMonth + 1) + "-" + mDay;
	}

	/***
	 * 点击选择日期
	 * 
	 * @param v
	 */
	public void onDateClick(View v) {
		onDateClick(v, SHOW_TYPE.TYPE_DATE_E);
	}

	/***
	 * 点击选择日期
	 * 
	 * @param v
	 */
	public void onDateClick(View v, int showType) {
		this.curShowType = showType;
		if(v.getTag() != null){
			mDateTextView = (TextView) v.getTag();
		}else{
			mDateTextView = (TextView) v;
		}
		if (mDateTextView.getText().toString().trim().contains(" ")) {
			String[] arrDates = mDateTextView.getText().toString().trim().split(" ");
			mDateTmp = arrDates[0];
			if(arrDates.length > 1){
				mTimeTmp = arrDates[1];
			}
		}else{
			mDateTmp = mDateTextView.getText().toString().trim();
		}
		initDate(mDateTmp);

		showDialog(DATE_DIALOG_ID);
	}

	/***
	 * 点击选择时间
	 * 
	 * @param v
	 */
	public void onTimeClick(View v) {
		//mTimeTextView = (TextView) v.getTag();
		if(v.getTag() != null){
			mTimeTextView = (TextView) v.getTag();
		}else{
			mTimeTextView = (TextView) v;
		}
		if (mTimeTextView.getText().toString().trim().contains(" ")) {
			String[] arrDates = mTimeTextView.getText().toString().trim()
					.split(" ");
			mDateTmp = arrDates[0];
			mTimeTmp = arrDates[1];
		}
		initTime(mTimeTmp);
		showDialog(TIME_DIALOG_ID);
	}


	/***
	 * 初始化年月日
	 * 
	 * @param date
	 */
	private void initDate(String date) {
		if (date != null ) {
			if(date.contains("年")){
				date = date.replace("年", "-");
			}
			if(date.contains("月")){
				date = date.replace("月", "-");
			}
			if(date.contains("日")){
				date = date.replace("日", "-");
			}
			if(date.contains("-")){
				try {
					if(date.endsWith("-")){
						date = date.substring(0, date.length()-1);
					}
					if(curShowType != SHOW_TYPE.TYPE_DATE_CN_NO_YEAR){
						String[] arrDatas = date.split("-");
						mYear = Integer.parseInt(arrDatas[0]);
						mMonth = Integer.parseInt(arrDatas[1]) - 1;
						mDay = Integer.parseInt(arrDatas[2]);
					}else{
						String[] arrDatas = date.split("-");
						if(arrDatas.length == 2){
							mMonth = Integer.parseInt(arrDatas[0]) - 1;
							mDay = Integer.parseInt(arrDatas[1]);
						}else if(arrDatas.length == 3){
							mMonth = Integer.parseInt(arrDatas[1]) - 1;
							mDay = Integer.parseInt(arrDatas[2]);
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else if(date.contains("/")){
				try {
					String[] arrDatas = date.split("/");
					mYear = Integer.parseInt(arrDatas[0]);
					mMonth = Integer.parseInt(arrDatas[1]) - 1;
					mDay = Integer.parseInt(arrDatas[2]);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}


	/***
	 * 初始化时分
	 * 
	 * @param time
	 */
	private void initTime(String time) {
		if (time != null && time.contains(":")) {
			try {
				String[] arrDatas = time.split(":");
				mHour = Integer.parseInt(arrDatas[0]);
				mMinute = Integer.parseInt(arrDatas[1]);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	/***
	 * 显示对话框
	 * 
	 * @param id
	 */
	public void showDialog(int id) {
		Dialog dialog = getDateTimeDlg(id);
		prepareDialog(id, dialog);
		dialog.show();
	}

	/***
	 * 获得对话框
	 * 
	 * @param id
	 * @return
	 */
	protected Dialog getDateTimeDlg(int id) {
		try {
			switch (id) {
			case TIME_DIALOG_ID:
				return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						mHour = hourOfDay;
						mMinute = minute;
						if (mTimeTextView != null) {
							if (mMinute < 10) {
								mTimeTmp = mHour + ":0" + mMinute + ":00";
							} else {
								mTimeTmp = mHour + ":" + mMinute + ":00";
							}
							mTimeTextView.setText(mDateTmp + " " + mTimeTmp);
						}
					}
				}, mHour, mMinute, true);
			case DATE_DIALOG_ID:
				return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						if(curShowType != SHOW_TYPE.TYPE_DATE_CN_NO_YEAR){
							mYear = year;
						}
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						if (mDateTextView != null) {
							if(curShowType == SHOW_TYPE.TYPE_DATE_CN){
								mDateTmp = mYear + "年" + (mMonth + 1) + "月" + mDay + "日";
							}else if(curShowType == SHOW_TYPE.TYPE_DATE_CN_NO_YEAR){
								mDateTmp = (mMonth + 1) + "月" + mDay + "日";
							}else{
								mDateTmp = mYear + "-" + (mMonth + 1) + "-" + mDay;
							}
							mDateTextView.setText(mDateTmp + " " + mTimeTmp);
						}
					}
				}, mYear, mMonth, mDay);
			}
		} catch (Exception e) {
			Log.e("mucai", "时间对话框错误信息：" + e.toString());
		}
		return null;
	}

	protected void prepareDialog(int id, Dialog dialog) {
		switch (id) {
		case TIME_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}
}
