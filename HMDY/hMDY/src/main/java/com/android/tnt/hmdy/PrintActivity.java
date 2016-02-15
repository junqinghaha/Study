package com.android.tnt.hmdy;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tnt.hmdy.fragment.PrintFragment;

/***
 * 打印页
 * 
 * @author TNT
 * 
 */
public class PrintActivity extends BaseActivity {

	private Fragment printFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print);
		printFragment = new PrintFragment();
		printFragment.setArguments(getIntent().getExtras());
		enableHomeBackFuction(true);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, printFragment).commitAllowingStateLoss();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print_test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
