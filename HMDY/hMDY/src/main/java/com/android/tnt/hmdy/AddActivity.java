package com.android.tnt.hmdy;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tnt.config.Constants;
import com.android.tnt.db.entity.HuoDanEntity;
import com.android.tnt.hmdy.fragment.AddFragment;
import com.android.tnt.hmdy.fragment.BaseFragment.OnFragmentListener;
import com.android.uitils.Util;

/***
 * 添加
 * 
 * @author TNT
 * 
 */
public class AddActivity extends ActionBarActivity implements OnFragmentListener {

	private AddFragment fragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		if(getIntent() != null){
			Bundle bunle = getIntent().getExtras();//getArguments();
			if (bunle != null) {
				setTitle("编辑");
			}else{
				setTitle("添加");
			}
		}else{
			setTitle("添加");
		}
		try {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
		fragment = new AddFragment();
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
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_save:
			fragment.saveOpt();
			return true;
		case R.id.action_refresh:
			fragment.refreshSpData();
			return true;
		case android.R.id.home: {
			AddActivity.this.finish();
		}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFragmentRequest(Fragment fragment, int optCode, Bundle data) {
		// TODO Auto-generated method stub
		if (fragment.getClass().equals(fragment.getClass())) {
			if (optCode == AddFragment.RequestInfo.Request_01) {
				Util.MsgBox(AddActivity.this, "已保存");
				// 保存成功
				Intent intent = new Intent();
				intent.putExtras(data);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}

}
