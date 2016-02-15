package com.android.tnt.hmdy;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tnt.hmdy.fragment.AddFragment;
import com.android.tnt.hmdy.fragment.BaseFragment.OnFragmentListener;
import com.android.tnt.hmdy.fragment.PrintOneFragment;
import com.android.uitils.Util;

/***
 * 添加
 * 
 * @author TNT
 * 
 */
public class PrintOneActivity extends ActionBarActivity implements OnFragmentListener {

	private PrintOneFragment fragment = null;

	private MenuItem itemPrint = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		setTitle("补打一页");
		try {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
		fragment = new PrintOneFragment();
		if (getIntent() != null && getIntent().getExtras() != null) {
			fragment.setArguments(getIntent().getExtras());
		}
		fragment.setOnFragmentListener(this);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commitAllowingStateLoss();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print_one, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_refresh:
			fragment.refreshSpData();
			return true;
		case R.id.action_print:
			fragment.printOpt();
			return true;
			//itemPrint = item;
			//itemPrint.setActionView(R.layout.progressbar);
			//ProgressDialog protDialog = new ProgressDialog(PrintOneActivity.this);
			//new PrintTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		case android.R.id.home: {
			finish();
		}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class PrintTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// Simulate something long running
			
			return "";// PrintUtil.getPrinterState(BDAddress);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			itemPrint.collapseActionView(); // 这个方法需要 API 14 以上
			itemPrint.setActionView(null);
		}
	};

	@Override
	public void onFragmentRequest(Fragment fragment, int optCode, Bundle data) {
		// TODO Auto-generated method stub
		if (fragment.getClass().equals(fragment.getClass())) {
			if (optCode == AddFragment.RequestInfo.Request_01) {
				Util.MsgBox(PrintOneActivity.this, "打印完毕 ");
				// 保存成功
				Intent intent = new Intent();
				intent.putExtras(data);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}

}
