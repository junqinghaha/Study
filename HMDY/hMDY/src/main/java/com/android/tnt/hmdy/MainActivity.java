package com.android.tnt.hmdy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tnt.config.Constants;
import com.android.tnt.hmdy.fragment.BaseFragment.OnFragmentListener;
import com.android.tnt.hmdy.fragment.HMManagerFragment;
import com.android.tnt.hmdy.fragment.LeftFragment;
import com.android.tnt.hmdy.fragment.RightFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements OnFragmentListener {

	private SlidingMenu _SlidingMenu; // 侧边栏菜单
	private LeftFragment _LeftFragment; // 左侧菜单Fragment
	private RightFragment _RightFragment; // 右侧菜单Fragment

	private HMManagerFragment mainFragment = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainFragment = new HMManagerFragment();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, mainFragment).commitAllowingStateLoss();
		}

		initSlidingMenu();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mainFragment.isOnBackOk()) {
			isExitSys();
		}
	}

	/**
	 * 是否退出系统
	 * 
	 */
	private void isExitSys() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Log.i(TAG, "退出系统");
				// moveTaskToBack(true);
				finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.setMessage("确定退出系统？");
		builder.show();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		switch (arg0) {
		case Constants.REQUEST.REQUEST_ADD:
			if (arg1 == RESULT_OK) {
				// mainFragment.refresh();
				mainFragment.refreshWebData();
			}
			break;
		// case Constants.REQUEST.REQUEST_EDIT:
		// if (arg1 == RESULT_OK) {
		// // mainFragment.refresh();
		// mainFragment.refresh();
		// }
		// break;
		default:
			break;
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	/**
	 * 初始化侧边栏菜单
	 */
	private void initSlidingMenu() {
		_LeftFragment = new LeftFragment(); // 创建左边菜单Fragment
		_RightFragment = new RightFragment();// 创建右边菜单Fragment
		_RightFragment.setOnFragmentListener(this);

		_SlidingMenu = getSlidingMenu(); // 由于Activity继承自SlidingFragmentActivity,所以直接获取当前的侧边栏菜单

		_SlidingMenu.setMode(SlidingMenu.LEFT_RIGHT); // 设置侧边栏菜单为左右模式
		_SlidingMenu.setBehindWidthRes(R.dimen.left_menu_width); // 设置左边菜单的宽度,该值为左菜单展开的宽度
		_SlidingMenu.setShadowDrawable(R.drawable.shadow); // 设置左菜单的阴影
		_SlidingMenu.setShadowWidth(10); // 设置阴影宽度
		_SlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE); // 设置侧边栏菜单触摸模式为全屏模式

		setBehindContentView(R.layout.left_menu_layout); // 设置左菜单的默认VIEW
		getSupportFragmentManager().beginTransaction().replace(R.id.leftMenu, _LeftFragment).commit(); // 将左菜单默认VIEW替换为左菜单Fragment

		_SlidingMenu.setSecondaryMenu(R.layout.right_menu_layout); // 设置右菜单默认VIEW
		_SlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright); // 设置右菜单阴影
		_SlidingMenu.setRightBehindWidthRes(R.dimen.right_menu_width); // 设置右菜单的宽度,该值为右菜单展开的宽度
		getSupportFragmentManager().beginTransaction().replace(R.id.rightMenu, _RightFragment).commit(); // 将右菜单默认VIEW替换为右菜单Fragment

		// _SlidingMenu.set

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_add:
			if(mainFragment.isMoreOptMode()){
				mainFragment.cancelMoreOptMode();
			}
			Intent intent = new Intent(MainActivity.this, AddActivity.class);
			startActivityForResult(intent, Constants.REQUEST.REQUEST_ADD);
			break;
		case R.id.action_filter: {
			if(mainFragment.isMoreOptMode()){
				mainFragment.cancelMoreOptMode();
			}
			if (!_SlidingMenu.isSecondaryMenuShowing()) {
				_SlidingMenu.showSecondaryMenu();
				_RightFragment.refreshView();
			} else {
				_SlidingMenu.showContent();
			}
		}
			break;
		case R.id.action_refresh: {
			if(mainFragment.isMoreOptMode()){
				mainFragment.cancelMoreOptMode();
			}
			mainFragment.refreshWebData();
		}
			break;
		case R.id.action_print_one: {
			// 进入补打界面
			// Util.MsgBox(MainActivity.this, "功能更新中");
			startActivity(new Intent(MainActivity.this, PrintOneActivity.class));
		}
			break;
		case R.id.action_print_setting: {
			startActivity(new Intent(MainActivity.this, PrintSettingActivity.class));
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
		if (fragment.getClass().equals(_RightFragment.getClass())) {
			if (optCode == 1) {
				if (_SlidingMenu.isSecondaryMenuShowing()) {
					_SlidingMenu.showContent();
				}
				// 根据过滤条件刷新数据
				mainFragment.refreshWebData();
			} else if (optCode == 0) {
				if (_SlidingMenu.isSecondaryMenuShowing()) {
					_SlidingMenu.showContent();
				}
				// 根据过滤条件刷新数据
				mainFragment.refreshWebData();
			}
		}
	}

}
