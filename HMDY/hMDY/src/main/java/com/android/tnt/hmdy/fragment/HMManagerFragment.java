package com.android.tnt.hmdy.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.tnt.config.Constants;
import com.android.tnt.config.SysConfig;
import com.android.tnt.config.WebServiceConfig;
import com.android.tnt.db.columns.HuoDanColumns;
import com.android.tnt.db.entity.HuoDanEntity;
import com.android.tnt.db.manager.HuoDanDBManager;
import com.android.tnt.hmdy.AddActivity;
import com.android.tnt.hmdy.MApplication;
import com.android.tnt.hmdy.PrintSettingActivity;
import com.android.tnt.hmdy.R;
import com.android.uitils.MDateUtils;
import com.android.uitils.PrintUtil;
import com.android.uitils.Util;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.samples.adapters.MatrixTableAdapter;
import com.inqbarna.tablefixheaders.samples.adapters.SampleTableAdapter;
import com.mtkj.webservice.WebServiceFragment;
import com.mtkj.webservice.method.WMTakeOutGoods;
import com.mtkj.webservice.method.WMTakeOutGoodsDelete;
import com.mtkj.webservice.method.WMTakeOutGoodsOver;
import com.utils.log.MLog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * 
 * 货码打印
 * 
 * @author TNT
 * 
 */
public class HMManagerFragment extends WebServiceFragment implements View.OnClickListener {

	private final String TAG = "HMManagerFragment";

	private PullToRefreshListView mPullRefreshListView;

	private WMTakeOutGoods mWMTakeOutgoods = null;

	private WMTakeOutGoodsDelete mWMTakeOutGoodsDelete = new WMTakeOutGoodsDelete();

	private WMTakeOutGoodsOver mWMTakeOutgoodsOver = null;

	private MyAdapter adapter = null;

	private ViewGroup moreOptViewGroup = null;

	private List<String> lstIds = new ArrayList<String>();
	private HashMap<Integer, Boolean> mapSelectRows = new HashMap<Integer, Boolean>();

	private boolean isMoreOpt = false;

	// 提货单表数据
	private String[][] mDatas = new String[][] { { "      提 货 单 号      ", "目 的 地", "始 发 站", "日 期", "数 量", "收 货 人", "详 细 地 址", "服 务 方 式" }, { "1049696736001", "日照市", "久经庄营业部", "2015-06-06", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736002", "日照市", "久经庄营业部", "2015-06-07", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736003", "日照市", "久经庄营业部", "2015-06-08", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736004", "日照市", "久经庄营业部", "2015-06-09", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736005", "日照市", "久经庄营业部", "2015-06-10", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736006", "日照市", "久经庄营业部", "2015-06-11", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736007", "日照市", "久经庄营业部", "2015-06-12", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" },
			{ "1049696736008", "日照市", "久经庄营业部", "2015-06-13", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736009", "日照市", "久经庄营业部", "2015-06-14", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736010", "日照市", "久经庄营业部", "2015-06-15", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736011", "日照市", "久经庄营业部", "2015-06-16", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736012", "日照市", "久经庄营业部", "2015-06-17", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736013", "日照市", "久经庄营业部", "2015-06-18", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736014", "日照市", "久经庄营业部", "2015-06-19", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736015", "日照市", "久经庄营业部", "2015-06-20", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" },
			{ "1049696736016", "日照市", "久经庄营业部", "2015-06-21", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736017", "日照市", "久经庄营业部", "2015-06-22", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736018", "日照市", "久经庄营业部", "2015-06-23", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736019", "日照市", "久经庄营业部", "2015-06-24", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736020", "日照市", "久经庄营业部", "2015-06-25", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736021", "日照市", "久经庄营业部", "2015-06-26", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" } };

	private HuoDanDBManager dbm = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recoverFilterSetting();
		dbm = new HuoDanDBManager(getActivity());
		mWMTakeOutgoods = new WMTakeOutGoods();
		mWMTakeOutgoodsOver = new WMTakeOutGoodsOver();
		if (!SysConfig.isTestMode) {
			mDatas = dbm.getAllShowDatas(lstIds);
		}
		int length = mDatas.length;
		MLog.d(TAG, "数据长度:" + length);

		refreshDatas2();
	}

	public void refresh() {
		initDatas();
		// 加载更新数据
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	public void refreshWebData() {
		// new GetDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		refreshDatas2();
	}

	// private int refreshDatas1() {
	// int updateCount = 0;
	// if (mWMTakeOutgoods == null) {
	// mWMTakeOutgoods = new WMTakeOutGoods();
	// }
	// try {
	// mWMTakeOutgoods.setParams("0", "1000", );
	// HttpClientOpt httpOpt = new HttpClientOpt(getTakeOutgoodsURL());
	// String result = httpOpt.GetHttpRequest(getTakeOutgoodsURL(),
	// mWMTakeOutgoods.getWebParams());
	// if (!TextUtils.isEmpty(result)) {
	// try {
	// JSONObject json = new JSONObject(result);
	// if (json != null) {
	// boolean bResult =
	// json.optBoolean(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.Success, false);
	// String resultInfo =
	// json.optString(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.Msg, "");
	// String resultData =
	// json.optString(WebServiceInfo.WEB_RESULT_FORMAT_NAMES.Data, "");
	// if (bResult) {
	// List<HuoDanEntity> lstData = (List<HuoDanEntity>)
	// mWMTakeOutgoods.parseWebResult(resultData);
	// if (lstData != null) {
	// updateCount = lstData.size();
	// updateDataToDb(lstData);
	// }
	// } else {
	// Util.MsgBox(getActivity(), "" + resultInfo);
	// }
	// }
	// } catch (JSONException e1) {
	// e1.printStackTrace();
	// }
	// } else {
	// MLog.e(TAG, "请求结果未空");
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return updateCount;
	// }

	private void refreshDatas2() {
		if (mWMTakeOutgoods == null) {
			mWMTakeOutgoods = new WMTakeOutGoods();
		}
		try {
			MApplication app = (MApplication) getActivity().getApplication();

			String status = app.getData(HuoDanColumns.STATE, HuoDanColumns.STATE_VALUE.STATE_UNDO);
			String startTime = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4) + " 00:00:00");
			String endTime = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4) + " 23:59:59");
			if (TextUtils.isEmpty(startTime)) {
				startTime = MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4) + " 00:00:00";
				app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, startTime);
			}
			if (TextUtils.isEmpty(endTime)) {
				endTime = MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4) + " 23:59:59";
				app.setData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, endTime);
			}
			mWMTakeOutgoods.setParams("0", "1000", status, startTime, endTime);
			webRequest(mWMTakeOutgoods.getMethodName(), mWMTakeOutgoods.getWebParams(), false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateDataToDb(List<HuoDanEntity> lstData) {
		if (lstData != null) {
			HuoDanDBManager dbManager = new HuoDanDBManager(getActivity());
			dbManager.clear();
			dbManager.insertOneHuoDan(lstData);
		}
	}

	private String getTakeOutgoodsURL() {
		return WebServiceConfig.ServerAddress1 + mWMTakeOutgoods.getMethodName();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 刷新数据
		initDatas();
		// 刷新界面
		// View rootView = inflater.inflate(R.layout.fragment_hm_manager,
		// container, false);
		// /initViews2(rootView);
		View rootView = inflater.inflate(R.layout.table, container, false);
		initViews1(rootView);
		return rootView;
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		List<String> lstFields = new ArrayList<String>();
		List<String> lstFieldValues = new ArrayList<String>();
		MApplication app = (MApplication) getActivity().getApplication();
		boolean isFilter = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_FLAG, true);
		if (isFilter) {
			String fileds = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_NAME, HuoDanColumns.STATE);
			if (!TextUtils.isEmpty(fileds)) {
				String[] arrFields = fileds.split(",");
				if (arrFields != null && arrFields.length > 0) {
					for (String filed : arrFields) {
						if (!TextUtils.isEmpty(filed)) {
							if (filed.equalsIgnoreCase(HuoDanColumns.STATE)) {
								String filedValues = app.getData(filed, HuoDanColumns.STATE_VALUE.STATE_UNDO);
								if (!TextUtils.isEmpty(filedValues)) {
									lstFields.add(filed);
									lstFieldValues.add(filedValues);
								}
							} else {
								String filedValues = app.getData(filed, "");
								if (!TextUtils.isEmpty(filedValues)) {
									lstFields.add(filed);
									lstFieldValues.add(filedValues);
								}
							}
						}
					}
				}
			}
			String dateField = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_NAME, "");
			String dateFieldValueMin = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MIN, "");
			String dateFieldValueMax = app.getData(Constants.CONFIG_KEY.FLITER_FIELD_DATE_VALUE_MAX, "");
			if (lstFields.size() > 0 || (!TextUtils.isEmpty(dateField) && !TextUtils.isEmpty(dateFieldValueMin) && !TextUtils.isEmpty(dateFieldValueMax))) {
				String[] arrFieldNames = new String[lstFields.size()];
				String[] arrFieldValues = new String[lstFieldValues.size()];
				lstFields.toArray(arrFieldNames);
				lstFieldValues.toArray(arrFieldValues);
				try {
					mDatas = dbm.getAllShowDatasWithFliter(arrFieldNames, arrFieldValues, dateField, dateFieldValueMin, dateFieldValueMax, lstIds);
				} catch (Exception e) {
					Util.MsgBox(getActivity(), "过滤失败");
					mDatas = dbm.getAllShowDatas(lstIds);
					e.printStackTrace();
				}
			} else {
				mDatas = dbm.getAllShowDatas(lstIds);
			}
		} else {
			mDatas = dbm.getAllShowDatas(lstIds);
		}
	}

	@Override
	public void onDestroy() {
		recoverFilterSetting();
		super.onDestroy();
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
							app.setData(filed, HuoDanColumns.STATE_VALUE.STATE_UNDO + ",");
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

	private void initViews1(View rootView) {
		if (adapter == null) {
			adapter = new MyAdapter(getActivity());
		}
		moreOptViewGroup = (ViewGroup) rootView.findViewById(R.id.vg_more_opt_content);
		moreOptViewGroup.setVisibility(View.GONE);
		rootView.findViewById(R.id.textView0).setOnClickListener(this);
		rootView.findViewById(R.id.textView1).setOnClickListener(this);
		rootView.findViewById(R.id.textView2).setOnClickListener(this);
		rootView.findViewById(R.id.textView3).setOnClickListener(this);
		// adapter = new MyAdapter(getActivity());
		TableFixHeaders tableFixHeaders = (TableFixHeaders) rootView.findViewById(R.id.table);
		MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(getActivity(), new String[][] { { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" }, { "Lorem", "sed", "do", "eiusmod", "tempor", "incididunt" }, { "ipsum", "irure", "occaecat", "enim", "laborum", "reprehenderit" }, { "dolor", "fugiat", "nulla", "reprehenderit", "laborum", "consequat" }, { "sit", "consequat", "laborum", "fugiat", "eiusmod", "enim" }, { "amet", "nulla", "Excepteur", "voluptate", "occaecat", "et" }, { "consectetur", "occaecat", "fugiat", "dolore", "consequat", "eiusmod" }, { "adipisicing", "fugiat", "Excepteur", "occaecat", "fugiat", "laborum" }, { "elit", "voluptate", "reprehenderit", "Excepteur", "fugiat", "nulla" }, });
		// tableFixHeaders.setAdapter(matrixTableAdapter);
		tableFixHeaders.setAdapter(adapter);
	}

	private void initViews2(View rootView) {

		mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_list);

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				// new GetDataTask().execute();
			}
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// Toast.makeText(getActivity(), "End of List!",
				// Toast.LENGTH_SHORT).show();
			}
		});

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		/**
		 * Add Sound Event Listener
		 * 
		 * SoundPullEventListener<ListView> soundListener = new
		 * SoundPullEventListener<ListView>(getActivity());
		 * soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		 * soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		 * soundListener.addSoundEvent(State.REFRESHING,
		 * R.raw.refreshing_sound);
		 * mPullRefreshListView.setOnPullEventListener(soundListener);
		 */
		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		// actualListView.setAdapter(new MAdapter(this));
		mPullRefreshListView.setAdapter(new MAdapter(getActivity()));
	}

	// public class GetDataTask extends AsyncTask<Void, Void, String[]> {
	//
	// ProgressDialog progressDialog = null;
	//
	// @Override
	// protected void onPreExecute() {
	// progressDialog = new ProgressDialog(getActivity());
	// progressDialog.setMessage("数据更新中...");
	// progressDialog.setCancelable(false);
	// progressDialog.show();
	// super.onPreExecute();
	// }
	//
	// @Override
	// protected String[] doInBackground(Void... params) {
	// // Simulates a background job.
	// // try {
	// // Thread.sleep(4000);
	// // } catch (InterruptedException e) {
	// // }
	// int nCount = refreshDatas1();
	// return new String[nCount];
	// }
	//
	// @Override
	// protected void onPostExecute(String[] result) {
	// // mAdapter.notifyDataSetChanged();
	// // Call onRefreshComplete when the list has been refreshed.
	// if (mPullRefreshListView != null) {
	// mPullRefreshListView.onRefreshComplete();
	// }
	// if (progressDialog != null) {
	// progressDialog.dismiss();
	// }
	// if (result.length == 0) {
	// Util.MsgBox(getActivity(), "无更新");
	// } else {
	// Util.MsgBox(getActivity(), "更新数量 ：" + result.length);
	// // 重新读取数据库
	// initDatas();
	// // 加载更新数据
	// adapter.notifyDataSetChanged();
	// }
	//
	// super.onPostExecute(result);
	// }
	// }

	private void showOptDlg(final String id) {
		// String[] arrOpt = new String[] { "已收货", "修改数量", "打印" };
		final HuoDanEntity entity = dbm.getEntity(id);
		String[] arrOpt = new String[] { "已收货", "打印", "修改" };
		new AlertDialog.Builder(getActivity()).setTitle("单号:" + entity.getCode()).setItems(arrOpt, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case 0:// 已收货 // 提货完成
					if (entity != null) {
						goodsOverOpt(entity.getCode());
					}
					break;
				// case 1:// 修改数量
				//
				// break;
				case 1:// 打印
					goodsPrint(entity);
					break;
				case 2: {
					goodsEdite(entity);
				}
					break;

				default:
					break;
				}
			}
		}).create().show();
	}

	/***
	 * 提货完成操作
	 * 
	 * @return
	 */
	private void goodsOverOpt(String thdh) {
		try {
			mWMTakeOutgoodsOver = new WMTakeOutGoodsOver();
			mWMTakeOutgoodsOver.setParams(thdh);
			webRequest(mWMTakeOutgoodsOver.getMethodName(), mWMTakeOutgoodsOver.getWebParams(), true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showMorePanel() {
		if (moreOptViewGroup != null) {
			if (moreOptViewGroup.getVisibility() != View.VISIBLE) {
				moreOptViewGroup.setVisibility(View.VISIBLE);
			}
		}
	}

	private void hideMorePanel() {
		if (moreOptViewGroup != null) {
			if (moreOptViewGroup.getVisibility() != View.GONE) {
				moreOptViewGroup.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constants.REQUEST.REQUEST_EDIT:
			if (resultCode == Activity.RESULT_OK) {
				// mainFragment.refresh();
				refresh();
			}
			break;
		default:
			break;
		}
		// super.onActivityResult(arg0, arg1, arg2);
	}

	/***
	 * 提货完成操作
	 * 
	 * @return
	 */
	private void goodsEdite(HuoDanEntity entity) {
		Intent intent = new Intent(getActivity(), AddActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KEY.DATA1, entity);
		intent.putExtras(bundle);
		startActivityForResult(intent, Constants.REQUEST.REQUEST_EDIT);
	}

	private void goodsDelete(HuoDanEntity entity) throws JSONException {
		goodsDelete(entity.getCode());
	}

	private void goodsDelete(String codes) throws JSONException {
		mWMTakeOutGoodsDelete = new WMTakeOutGoodsDelete();
		mWMTakeOutGoodsDelete.setParams(codes);
		webRequest(mWMTakeOutGoodsDelete.getMethodName(), mWMTakeOutGoodsDelete.getWebParams(), mWMTakeOutGoodsDelete.isPost());
	}

	/***
	 * 提货完成操作
	 * 
	 * @return
	 */
	private void goodsPrint(HuoDanEntity entity) {
		if (entity != null) {
			MApplication app = (MApplication) getActivity().getApplication();
			String blDeviceAdress = app.getData(Constants.CONFIG_KEY.BLDevice, "");
			if (!TextUtils.isEmpty(blDeviceAdress)) {
				if (PrintUtil.isPrinterOk(getActivity(), blDeviceAdress)) {
					PrintUtil.Print(getActivity(), blDeviceAdress, entity);
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

	/***
	 * 提货完成操作
	 * 
	 * @return
	 */
	private void goodsPrint(List<HuoDanEntity> lstDatas) {
		if (lstDatas != null) {
			MApplication app = (MApplication) getActivity().getApplication();
			String blDeviceAdress = app.getData(Constants.CONFIG_KEY.BLDevice, "");
			if (!TextUtils.isEmpty(blDeviceAdress)) {
				if (PrintUtil.isPrinterOk(getActivity(), blDeviceAdress)) {
					for (HuoDanEntity entity : lstDatas) {
						PrintUtil.Print(getActivity(), blDeviceAdress, entity);
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

	private void showLongPressOptDlg(final String id) {
		final HuoDanEntity entity = dbm.getEntity(id);
		String[] arrOpt = new String[] { "修改", "删除" };
		new AlertDialog.Builder(getActivity()).setTitle("单号:" + entity.getCode1()).setItems(arrOpt, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case 0: {
					goodsEdite(entity);
				}
					break;
				case 1: {
					try {
						goodsDelete(entity);
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
		}).create().show();
	}

	private class MAdapter extends BaseAdapter {
		private Context context = null;
		private LayoutInflater mInflater = null;

		public MAdapter(Context context) {
			this.context = context;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.table, parent, false);
			}
			if (adapter == null) {
				adapter = new MyAdapter(getActivity());
			}
			// adapter = new MyAdapter(getActivity());
			TableFixHeaders tableFixHeaders = (TableFixHeaders) convertView.findViewById(R.id.table);
			MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(getActivity(), new String[][] { { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" }, { "Lorem", "sed", "do", "eiusmod", "tempor", "incididunt" }, { "ipsum", "irure", "occaecat", "enim", "laborum", "reprehenderit" }, { "dolor", "fugiat", "nulla", "reprehenderit", "laborum", "consequat" }, { "sit", "consequat", "laborum", "fugiat", "eiusmod", "enim" }, { "amet", "nulla", "Excepteur", "voluptate", "occaecat", "et" }, { "consectetur", "occaecat", "fugiat", "dolore", "consequat", "eiusmod" }, { "adipisicing", "fugiat", "Excepteur", "occaecat", "fugiat", "laborum" }, { "elit", "voluptate", "reprehenderit", "Excepteur", "fugiat", "nulla" }, });
			// tableFixHeaders.setAdapter(matrixTableAdapter);
			tableFixHeaders.setAdapter(adapter);
			return convertView;
		}
	}

	public boolean isOnBackOk() {
		if (isMoreOpt) {
			cancelMoreOptMode();
			return false;
		}
		return true;
	}

	public boolean isMoreOptMode() {
		return isMoreOpt;
	}

	public void cancelMoreOptMode() {
		isMoreOpt = false;
		hideMorePanel();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	public class MyAdapter extends SampleTableAdapter {

		private final int width;
		private final int width1;
		private final int width2;
		private final int width3;
		private final int width4;
		private final int width5;
		private final int width6;
		private final int width7;
		private final int width8;
		private final int height;

		private LayoutInflater inflater = null;

		public MyAdapter(Context context) {
			super(context);
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Resources resources = context.getResources();

			width = resources.getDimensionPixelSize(R.dimen.table_width);
			width1 = resources.getDimensionPixelSize(R.dimen.table_width1);
			width2 = resources.getDimensionPixelSize(R.dimen.table_width2);
			width3 = resources.getDimensionPixelSize(R.dimen.table_width3);
			width4 = resources.getDimensionPixelSize(R.dimen.table_width4);
			width5 = resources.getDimensionPixelSize(R.dimen.table_width5);
			width6 = resources.getDimensionPixelSize(R.dimen.table_width6);
			width7 = resources.getDimensionPixelSize(R.dimen.table_width7);
			width8 = resources.getDimensionPixelSize(R.dimen.table_width8);
			height = resources.getDimensionPixelSize(R.dimen.table_height);
		}

		@Override
		public int getRowCount() {
			return mDatas.length - 1;
		}

		@Override
		public int getColumnCount() {
			return 8;
		}

		@Override
		public int getWidth(int column) {
			switch (column) {
			case -1:
				return width1;
			case 0:
				return width2;
			case 1:
				return width3;
			case 2:
				return width4;
			case 3:
				return width5;
			case 4:
				return width6;
			case 5:
				return width7;
			case 6:
				return width8;
			default:
				return width;
			}
		}

		@Override
		public int getHeight(int row) {
			return height;
		}

		@Override
		public View getView(final int row, final int column, View converView, ViewGroup parent) {
			View view = null;
			if (row == -1) {// 标题
				view = super.getView(row, column, converView, parent);
			} else if (column == -1) {// 首列
				view = inflater.inflate(R.layout.layout_item_main, null);
				LinearLayout vgShow = (LinearLayout) view.findViewById(R.id.vg_main_text);
				final CheckBox checkbox = (CheckBox) view.findViewById(R.id.check);
				if (isMoreOpt) {
					if (checkbox.getVisibility() != View.VISIBLE) {
						checkbox.setVisibility(View.VISIBLE);
					}
					boolean isSelect = false;
					if (mapSelectRows.get(row) != null) {
						isSelect = mapSelectRows.get(row);
					}
					checkbox.setChecked(isSelect);
				} else {
					if (checkbox.getVisibility() != View.GONE) {
						checkbox.setVisibility(View.GONE);
					}
				}
				TextView textView = (TextView) view.findViewById(R.id.name);
				textView.setText(getCellString(row, column));
				vgShow.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Util.MsgBox(getActivity(), "点击打印");
						if (!isMoreOpt) {
							showOptDlg(lstIds.get(row));
						} else {
							checkbox.setChecked(!checkbox.isChecked());
							mapSelectRows.put(row, checkbox.isChecked());
						}
					}
				});
				vgShow.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// Util.MsgBox(getActivity(), "长按删除+修改");
						// TODO Auto-generated method stub
						// showLongPressOptDlg(lstIds.get(row));
						if (!isMoreOpt) {
							mapSelectRows.clear();
							isMoreOpt = true;
							mapSelectRows.put(row, true);
							showMorePanel();
							checkbox.setChecked(true);
							// 显示多操作按钮
							adapter.notifyDataSetChanged();

						}
						return true;
					}
				});
			} else {// 其它内容
				view = inflater.inflate(R.layout.layout_item_normal, null);
				TextView textView = (TextView) view.findViewById(R.id.name);
				textView.setText(getCellString(row, column));
				// view = super.getView(row, column, converView, parent);
			}
			return view;
		}

		@Override
		public String getCellString(int row, int column) {
			// return "Lorem (" + row + ", " + column + ")";
			if ((row + 1) < mDatas.length && (column + 1) < mDatas[row + 1].length) {
				return mDatas[row + 1][column + 1];
			}
			return "";
		}

		@Override
		public int getLayoutResource(int row, int column) {
			final int layoutResource;
			switch (getItemViewType(row, column)) {
			case 0:
				layoutResource = R.layout.item_table1_header;
				break;
			case 1:
				layoutResource = R.layout.item_table1;
				break;
			default:
				throw new RuntimeException("wtf?");
			}
			return layoutResource;
		}

		@Override
		public int getItemViewType(int row, int column) {
			if (row < 0) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}
	}

	@Override
	protected void webserviceCallSucess(String methodName, String result) {
		// TODO Auto-generated method stub
		if (methodName.equalsIgnoreCase(mWMTakeOutgoods.getMethodName())) {
			List<HuoDanEntity> lstData = (List<HuoDanEntity>) mWMTakeOutgoods.parseWebResult(result);
			if (lstData != null) {
				int updateCount = lstData.size();
				updateDataToDb(lstData);
				Util.MsgBox(getActivity(), "更新" + updateCount + "条数据");
			}
			refresh();
		} else if (methodName.equalsIgnoreCase(mWMTakeOutgoodsOver.getMethodName())) {
			if ((Boolean) mWMTakeOutgoodsOver.parseWebResult(result)) {
				try {
					String[] arrCode = mWMTakeOutgoodsOver.getCode().split(",");
					int nCount = 0;
					for (String code : arrCode) {
						if (dbm.updateEntityStatus(code, HuoDanColumns.STATE_VALUE.STATE_DONE) >= 0) {
							nCount++;
						}
					}
					// refreshWebData();
					// Util.MsgBox(getActivity(), "已删除" );
					Util.MsgBox(getActivity(), "已更新 ");// + nCount + "条记录");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			// 加载更新数据
			refresh();
		} else if (methodName.equalsIgnoreCase(mWMTakeOutGoodsDelete.getMethodName())) {
			if ((Boolean) mWMTakeOutGoodsDelete.parseWebResult(result)) {
				try {
					String codes = mWMTakeOutGoodsDelete.getCodes();
					if (!TextUtils.isEmpty(codes)) {
						String[] arrCodes = codes.split(",");
						int nCount = dbm.deleteEntity(arrCodes);
						// refreshWebData();
						// Util.MsgBox(getActivity(), "已删除" );//+ nCount +
						// "条记录");
						initDatas();
						adapter.notifyDataSetChanged();
						Util.MsgBox(getActivity(), "已删除" + nCount + "条记录");
					}
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	protected void webserviceCallFailed(String methodName, String failInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.textView0:{
				if(isMoreOpt){
					TextView textView = (TextView)v;
					if(textView.getText().toString().equalsIgnoreCase("全选")){
						textView.setText("全不选");
						mapSelectRows.clear();
						//isMoreOpt = true;
						for(int i=0; i<mDatas.length-1; i++){
							mapSelectRows.put(i, true);
						}
					}else{
						textView.setText("全选");
						mapSelectRows.clear();
						//isMoreOpt = true;
						for(int i=0; i<mDatas.length-1; i++){
							mapSelectRows.put(i, false);
						}
					}
					adapter.notifyDataSetChanged();
				}
			}break;
		case R.id.textView1:
			if (isMoreSelectOk()) {
				new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						moreDeleteOpt(v);
					}
				}).setNegativeButton("取消", null).create().show();
			}
			break;
		case R.id.textView2:
			if (isMoreSelectOk()) {
				morePrintOpt(v);
			}
			break;
		case R.id.textView3:
			if (isMoreSelectOk()) {
				moreOverOpt(v);
			}
			break;

		default:
			break;
		}
	}

	private boolean isMoreSelectOk() {
		boolean bRt = false;
		int nSize = 0;
		if (mapSelectRows.size() > 0) {
			Integer[] array = new Integer[mapSelectRows.size()];
			mapSelectRows.keySet().toArray(array);
			for (int data : array) {
				if (mapSelectRows.get(data)) {
					nSize++;
				}
			}
		}
		if (nSize > 0) {
			bRt = true;
		} else {
			Util.MsgBox(getActivity(), "当前未选择数据");
		}
		return bRt;
	}

	private void moreDeleteOpt(View v) {
		Integer[] array = new Integer[mapSelectRows.size()];
		mapSelectRows.keySet().toArray(array);
		String code = "";
		for (int i = 0; i < array.length; i++) {
			if (mapSelectRows.get(array[i])) {
				HuoDanEntity entity = dbm.getEntity(lstIds.get(array[i]));
				if (entity != null) {
					code += entity.getCode() + ",";
				}
			}
		}
		try {
			code = code.substring(0, code.length() - 1);
			goodsDelete(code);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		v.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cancelMoreOptMode();
			}
		}, 200);
	}

	private void morePrintOpt(View v) {
		List<HuoDanEntity> lstDatas = new ArrayList<HuoDanEntity>();
		Integer[] array = new Integer[mapSelectRows.size()];
		mapSelectRows.keySet().toArray(array);
		for (int i = 0; i < array.length; i++) {
			if (mapSelectRows.get(array[i])) {
				try {
					HuoDanEntity entity = dbm.getEntity(lstIds.get(array[i]));
					if (entity != null) {
						lstDatas.add(entity);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		goodsPrint(lstDatas);

		v.postDelayed(new Runnable() {
			@Override
			public void run() {
				cancelMoreOptMode();
			}
		}, 200);
	}

	private void moreOverOpt(View v) {
		Integer[] array = new Integer[mapSelectRows.size()];
		mapSelectRows.keySet().toArray(array);
		String code = "";
		for (int i = 0; i < array.length; i++) {
			if (mapSelectRows.get(array[i])) {
				HuoDanEntity entity = dbm.getEntity(lstIds.get(array[i]));
				if (entity != null) {
					code += entity.getCode() + ",";
				}
			}
		}
		code = code.substring(0, code.length() - 1);
		goodsOverOpt(code);

		v.postDelayed(new Runnable() {
			@Override
			public void run() {
				cancelMoreOptMode();
			}
		}, 200);
	}
}
