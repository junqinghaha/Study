package com.android.tnt.hmdy;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tnt.hmdy.fragment.PrintSettingFragment;
import com.android.uitils.Util;

/***
 * 打印页
 * 
 * @author TNT
 * 
 */
public class PrintSettingActivity extends ActionBarActivity {

	private PrintSettingFragment printFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_setting);
		printFragment = new PrintSettingFragment();
		// printFragment.setArguments(getIntent().getExtras());
		enableHomeBackFuction(true);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, printFragment).commitAllowingStateLoss();
		}
	}

	public void enableHomeBackFuction(boolean bEnable) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(bEnable);// 显示系统返回箭头
			actionBar.setHomeButtonEnabled(true); // 不显示箭头小符号,适用于自定义返回箭头情况
			// actionBar.setHomeButtonEnabled(bEnable);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
		}
			break;
		case R.id.action_save: {
			if (printFragment.saveSetting()) {
				Util.MsgBox(PrintSettingActivity.this, "已保存");
				finish();
			}
		}
			break;
		case R.id.action_refresh: {
			printFragment.refreshListView();
		}
		break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
