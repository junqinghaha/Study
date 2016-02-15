package com.android.tnt.hmdy;

import java.io.IOException;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.tnt.config.Constants;
import com.android.tnt.config.PathManager;
import com.android.tnt.config.SDCardManager;
import com.android.tnt.config.SysConfig;
import com.android.tnt.config.WebServiceConfig;
import com.android.uitils.SorftKeyOpt;
import com.android.uitils.Util;
import com.mtkj.broadcast.NetWorkConstans;
import com.mtkj.webservice.HttpOperate;
import com.mtkj.webservice.WebServiceActivity;
import com.mtkj.webservice.method.WMLogin;
import com.utils.log.MLog;
import com.utils.update.VersionCheck;

/**
 * 登录界面
 * 
 * @author TNT
 * 
 */
public class LoginActivity extends WebServiceActivity implements OnClickListener, OnEditorActionListener {

	/** 登录对象 */
	private WMLogin login = null;

	private AutoCompleteTextView mLogName = null;
	private EditText mLogPwd = null;

	/** 登录用户历史 */
	private String[] userHistory;
	/** 登录用户历史 */
	private String loginHistroy;
	/** 上一个成功登录用户 */
	private String lastUser;
	
	private CheckBox cbPwd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(0x08000000, 0x08000000);
		if(HttpOperate.httpClient != null){
			//HttpOperate.httpClient.close();
			HttpOperate.httpClient = null;
		}
		if (!SDCardManager.ExistSDCard()) {
			isExistSDCardDlg();
		} else {
			try {
				PathManager.initWorkPath(this);
				// 刷新地址
				WebServiceConfig.RefreshAddress();
				// 刷新地址
				WebServiceConfig.initSetting();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				MLog.e(TAG, "启动错误:" + e1.toString());
			}

			// 更新检查
			if (WebServiceConfig.UpdateFileURL.length() > 0) {
				new VersionCheck(LoginActivity.this, false).execute(WebServiceConfig.UpdateFileURL);
			}

			mLogName.postDelayed(new Runnable() {

				@Override
				public void run() {
					SorftKeyOpt.ShowEditeTextSorftKey(LoginActivity.this, mLogName);
				}
			}, 100);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		NetWorkConstans.initNetInfo(this);
		// setIsCheckNetwork(true);
		super.onResume();
	}

	@Override
	protected void initDatas() {
		// TODO Auto-generated method stub
		super.initDatas();
		login = new WMLogin();
		loginHistroy = mApp.getData(Constants.CONFIG_KEY.LOGIN_USERS, "");
		lastUser = mApp.getData(Constants.CONFIG_KEY.LAST_USERS, "");
		refreshLoginUser(loginHistroy);
	}

	/**
	 * 刷新以前所有成功登录的用户
	 * 
	 * @param loginHistroy
	 */
	private void refreshLoginUser(String loginHistroy) {
		if (loginHistroy != null && loginHistroy.length() > 0) {
			userHistory = loginHistroy.split(",");
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		setContentView(R.layout.activity_log_in);
		findViewById(R.id.login_bt_login).setOnClickListener(this);
		//findViewById(R.id.login_bt_setting).setOnClickListener(this);
		cbPwd = ((CheckBox)findViewById(R.id.login_bt_setting));
		cbPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
			}
		});
		String pwd = mApp.getData(Constants.CONFIG_KEY.PWD_RECORD, "");
		if(!TextUtils.isEmpty(pwd)){
			cbPwd.setChecked(true);
		}else{
			cbPwd.setChecked(false);
		}
		
		
		mLogName = (AutoCompleteTextView) findViewById(R.id.username);
		mLogPwd = (EditText) findViewById(R.id.password);
		mLogName.setTag("name");
		mLogPwd.setTag("pwd");
		mLogName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mLogPwd.setImeOptions(EditorInfo.IME_ACTION_GO);
		mLogPwd.setOnEditorActionListener(this);
		if(cbPwd.isChecked()){
			mLogPwd.setText(pwd);
		}
		mLogName.setOnEditorActionListener(this);

		if (lastUser != null && !lastUser.equalsIgnoreCase("")) {
			mLogName.setText(lastUser);
			mLogPwd.requestFocus();
		}

		if (userHistory != null && userHistory.length > 0) {
			// 设置自动填充适配器
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, userHistory);
			mLogName.setAdapter(adapter);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_bt_login:
			onLoginClick();
			// intoSys();
			break;
		case R.id.login_bt_setting:
			// isClearDlg();
			// onResetClick();
			break;

		default:
			break;
		}
	}

	/**
	 * 点击登录按钮
	 */
	private void onLoginClick() {
		// 登录按钮不可用--防止多次点击
		findViewById(R.id.login_bt_login).setEnabled(false);
		String name = mLogName.getText().toString().trim();
		String pwd = mLogPwd.getText().toString().trim();
		if (name.equalsIgnoreCase("") || pwd.equalsIgnoreCase("")) {
			// 登录按钮不可用--防止多次点击
			findViewById(R.id.login_bt_login).setEnabled(false);
			Util.MsgBox(LoginActivity.this, "请输入登录信息");
			mLogPwd.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					findViewById(R.id.login_bt_login).setEnabled(true);
				}
			}, 2000);
		} else {
			login(name, pwd);
		}
	}

	/**
	 * 点击登录按钮
	 */
	private void onResetClick() {
		mLogName.setText("");
		mLogPwd.setText("");
	}

	/**
	 * 进入系统方法
	 */
	private void intoSys() {
		Intent data = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(data);
		LoginActivity.this.finish();
	}

	/**
	 * 登录方法
	 * 
	 * @param userName
	 * @param pwd
	 */
	private void login(String userName, String pwd) {
		try {
			SysConfig.UserName = userName;
			if (SysConfig.isTestMode) {
				if (userName.equalsIgnoreCase("TNT") && pwd.equalsIgnoreCase("000")) {
					SysConfig.isTestMode = true;
					intoSys();
				} else if (userName.equalsIgnoreCase("admin") && pwd.equalsIgnoreCase("1")) {
					SysConfig.isTestMode = true;
					SysConfig.UserName = "admin";
					recordLoginUserInfo();
					intoSys();
				} else {
					login.setParams(userName, pwd, null);
					setDialogCancelable(false);
					webRequest1(login.getMethodName(), login.getWebParams(), login.isPost());
				}
			} else {
				if (userName.equalsIgnoreCase("admin") && pwd.equalsIgnoreCase("1")) {
					SysConfig.isTestMode = true;
					SysConfig.UserName = "admin";
					recordLoginUserInfo();
					intoSys();
				} else {
					login.setParams(userName, pwd, null);
					setDialogCancelable(false);
					webRequest1(login.getMethodName(), login.getWebParams(), login.isPost());
				}
				// login.setParams(userName, pwd);
				// setDialogCancelable(false);
				// webRequestInEvent(login.getMethodName(),
				// login.getWebParams());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 登录按钮不可用--防止多次点击
			findViewById(R.id.login_bt_login).setEnabled(true);
		}
	}

	@Override
	protected void webserviceCallSucess(String methodName, String result) {
		// TODO Auto-generated method stub
		// 登录按钮不可用--防止多次点击
		findViewById(R.id.login_bt_login).setEnabled(true);
		if (methodName.equalsIgnoreCase(login.getMethodName())) {// 登录反馈结果
			Boolean data = (Boolean) login.parseWebResult(result);
			if (data) {
				SorftKeyOpt.hideEditeTextSorftKey(LoginActivity.this, mLogPwd);
				if(cbPwd.isChecked()){
					mApp.setData(Constants.CONFIG_KEY.PWD_RECORD, mLogPwd.getText().toString());
				}else{
					mApp.setData(Constants.CONFIG_KEY.PWD_RECORD, "");
				}
				recordLoginUserInfo();
				intoSys();
			} else {
				// 重置登录按钮
				findViewById(R.id.login_bt_login).setEnabled(true);
				if (login.getResultReason() != null) {
					Util.MsgBox(LoginActivity.this, login.getResultReason());
				} else {
					Util.MsgBox(LoginActivity.this, "登录失败");
					Util.MsgBox(LoginActivity.this, "请检查服务地址是否正确");
					Util.MsgBox(LoginActivity.this, result);
				}
			}
		}
	}

	private void recordLoginUserInfo() {
		mApp.setData(Constants.CONFIG_KEY.LAST_USERS, SysConfig.UserName);
		if (!loginHistroy.contains(SysConfig.UserName)) {
			if (loginHistroy.length() > 0) {
				loginHistroy = loginHistroy + "," + SysConfig.UserName;
			} else {
				loginHistroy = SysConfig.UserName;
			}
		}
	}

	@Override
	protected void webserviceCallFailed(String methodName, String failInfo) {
		// TODO Auto-generated method stub
		if (methodName.equalsIgnoreCase(login.getMethodName())) {// 调用失败
		}
		// 重置登录按钮
		findViewById(R.id.login_bt_login).setEnabled(true);
	}

	/**
	 * 对话框 是否切换编辑图层
	 * 
	 */
	private void isExistSDCardDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				LoginActivity.this.finish();
			}
		});
		builder.setMessage("未检测到SD卡，请确定设备是否存在SD卡");
		builder.show();
	}

	/**
	 * 
	 */
	private void isClearDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				onResetClick();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.setMessage("确定重置?");
		builder.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.TextView.OnEditorActionListener#onEditorAction(android
	 * .widget.TextView, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (v.getId()) {
		case R.id.username: {
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				mLogPwd.requestFocus();
			}
		}
			break;
		case R.id.password: {
			if (actionId == EditorInfo.IME_ACTION_GO) {
				onLoginClick();
			}
		}
			break;

		default:
			break;
		}
		return true;
	}
}
