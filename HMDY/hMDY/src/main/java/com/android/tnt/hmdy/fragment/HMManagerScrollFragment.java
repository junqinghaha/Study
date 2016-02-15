package com.android.tnt.hmdy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.tnt.config.SysConfig;
import com.android.tnt.db.manager.HuoDanDBManager;
import com.android.tnt.hmdy.R;
import com.android.uitils.Util;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.samples.adapters.MatrixTableAdapter;
import com.inqbarna.tablefixheaders.samples.adapters.SampleTableAdapter;
import com.utils.log.MLog;

/***
 * 
 * 货码打印
 * 
 * @author TNT
 * 
 */
public class HMManagerScrollFragment extends BaseFragment {

	private final String TAG = "HMManagerFragment";

	PullToRefreshScrollView mPullRefreshScrollView;

	private List<String> lstIDs = new ArrayList<String>();
	ScrollView mScrollView;

	private PullToRefreshListView mPullRefreshListView;
	// 提货单表数据
	private String[][] mDatas = new String[][] { { "      提 货 单 号      ", "目 的 地", "始 发 站", "日 期", "数 量", "收 货 人", "详 细 地 址", "服 务 方 式" }, { "1049696736001", "日照市", "久经庄营业部", "2015-06-06", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736002", "日照市", "久经庄营业部", "2015-06-07", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736003", "日照市", "久经庄营业部", "2015-06-08", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736004", "日照市", "久经庄营业部", "2015-06-09", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736005", "日照市", "久经庄营业部", "2015-06-10", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736006", "日照市", "久经庄营业部", "2015-06-11", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736007", "日照市", "久经庄营业部", "2015-06-12", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" },
			{ "1049696736008", "日照市", "久经庄营业部", "2015-06-13", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736009", "日照市", "久经庄营业部", "2015-06-14", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736010", "日照市", "久经庄营业部", "2015-06-15", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736011", "日照市", "久经庄营业部", "2015-06-16", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736012", "日照市", "久经庄营业部", "2015-06-17", "2", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736013", "日照市", "久经庄营业部", "2015-06-18", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736014", "日照市", "久经庄营业部", "2015-06-19", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736015", "日照市", "久经庄营业部", "2015-06-20", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" },
			{ "1049696736016", "日照市", "久经庄营业部", "2015-06-21", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736017", "日照市", "久经庄营业部", "2015-06-22", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736018", "日照市", "久经庄营业部", "2015-06-23", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736019", "日照市", "久经庄营业部", "2015-06-24", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736020", "日照市", "久经庄营业部", "2015-06-25", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" }, { "1049696736021", "日照市", "久经庄营业部", "2015-06-26", "3", "爱青鸟", "临新路与济南路 锦绣前程小区", "站点送货" } };

	private HuoDanDBManager dbm = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbm = new HuoDanDBManager(getActivity());
		if (!SysConfig.isTestMode) {
			mDatas = dbm.getAllShowDatas(lstIDs);
		}
		int length = mDatas.length;
		MLog.d(TAG, "数据长度:" + length);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 刷新数据
		initDatas();
		// 刷新界面
		View rootView = inflater.inflate(R.layout.fragment_hm_manager, container, false);
		initViews(rootView);
		return rootView;
	}

	private void initDatas() {
		// TODO Auto-generated method stub

	}

	private void initViews(View rootView) {
		initScrollTestView(rootView);
	}

	private void initScrollTestView(View rootView) {
		mPullRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetDataTask().execute();
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();

		TableFixHeaders tableFixHeaders = (TableFixHeaders) rootView.findViewById(R.id.table);
		MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(getActivity(), new String[][] { { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" }, { "Lorem", "sed", "do", "eiusmod", "tempor", "incididunt" }, { "ipsum", "irure", "occaecat", "enim", "laborum", "reprehenderit" }, { "dolor", "fugiat", "nulla", "reprehenderit", "laborum", "consequat" }, { "sit", "consequat", "laborum", "fugiat", "eiusmod", "enim" }, { "amet", "nulla", "Excepteur", "voluptate", "occaecat", "et" }, { "consectetur", "occaecat", "fugiat", "dolore", "consequat", "eiusmod" }, { "adipisicing", "fugiat", "Excepteur", "occaecat", "fugiat", "laborum" }, { "elit", "voluptate", "reprehenderit", "Excepteur", "fugiat", "nulla" }, });
		// tableFixHeaders.setAdapter(matrixTableAdapter);
		tableFixHeaders.setAdapter(new MyAdapter(getActivity()));
	}

	private void initListTestView(View rootView) {

		mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_list);

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
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

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
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
			TableFixHeaders tableFixHeaders = (TableFixHeaders) convertView.findViewById(R.id.table);
			MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(getActivity(), new String[][] { { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" }, { "Lorem", "sed", "do", "eiusmod", "tempor", "incididunt" }, { "ipsum", "irure", "occaecat", "enim", "laborum", "reprehenderit" }, { "dolor", "fugiat", "nulla", "reprehenderit", "laborum", "consequat" }, { "sit", "consequat", "laborum", "fugiat", "eiusmod", "enim" }, { "amet", "nulla", "Excepteur", "voluptate", "occaecat", "et" }, { "consectetur", "occaecat", "fugiat", "dolore", "consequat", "eiusmod" }, { "adipisicing", "fugiat", "Excepteur", "occaecat", "fugiat", "laborum" }, { "elit", "voluptate", "reprehenderit", "Excepteur", "fugiat", "nulla" }, });
			// tableFixHeaders.setAdapter(matrixTableAdapter);
			tableFixHeaders.setAdapter(new MyAdapter(getActivity()));
			return convertView;
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

		public MyAdapter(Context context) {
			super(context);

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
			return 7;
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
			}
			return width5;
		}

		@Override
		public int getHeight(int row) {
			return height;
		}

		@Override
		public View getView(int row, int column, View converView, ViewGroup parent) {
			View view = super.getView(row, column, converView, parent);
			if (row >= -1 && column == -1) {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Util.MsgBox(getActivity(), "点击");
					}
				});
				view.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						Util.MsgBox(getActivity(), "长按");
						// TODO Auto-generated method stub
						return true;
					}
				});
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

}
