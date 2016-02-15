package com.utils.update;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.tnt.config.PathConstant;
import com.android.tnt.hmdy.R;

public class DownApkManager {

	private Context mContext = null;

	private ApkRunnable apkRunnable = null;

	private Dialog downloadDialog = null;
	private ProgressBar mProgress = null;
	private TextView tipProgress = null;
	private TextView downSize = null;

	public DownApkManager(Context context) {
		this.mContext = context;
	}

	private void showDownloadDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("软件升级");
		builder.setCancelable(false);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_layout, null);

		mProgress = (ProgressBar) v.findViewById(R.id.progressBar);
		tipProgress = (TextView) v.findViewById(R.id.tipProgress);
		downSize = (TextView) v.findViewById(R.id.downSize);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				apkRunnable.Cancel();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
	}

	// 下载apk
	public void DownloadApk(String strUrl, String strName) {

		// 获取下载的APk存储路径
		// File file = Environment.getExternalStorageDirectory();
		String path = PathConstant.AppWorkPath;
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
		String strApkName = path + File.separator + strName;

		// 存在则删除之前
		if (new File(strApkName).exists()) {
			new File(strApkName).delete();
		}

		// 显示进度对话框
		showDownloadDialog();

		// 启动下载线程
		DownloadHandler hanlder = new DownloadHandler();
		apkRunnable = new ApkRunnable(hanlder, strUrl, strApkName);
		Thread downLoadThread = new Thread(apkRunnable);
		downLoadThread.start();
	}

	// 安装apk
	private void installApk(String apkName) {
		File apkfile = new File(apkName);

		if (apkfile.exists()) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
					"application/vnd.android.package-archive");
			mContext.startActivity(i);
		}
	}

	class DownloadHandler extends Handler {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ApkRunnable.DOWN_UPDATE:
				if (mProgress != null) {
					// 更新进度
					int nPos = (int) (((float) msg.arg2) / ((float) msg.arg1) * 100);
					mProgress.setProgress(nPos);
					tipProgress.setText(nPos + "%");

					// 更新下在大小:
					String strSum = FromatFileSize(msg.arg1);
					String strLen = FromatFileSize(msg.arg2);
					downSize.setText("已完成：" + strLen + "/" + strSum);
				}
				break;
			case ApkRunnable.DOWN_OVER:
				downloadDialog.dismiss();

				// 2012/01/10android自带判断
				// //判断是否设置了“只允许安装Market源提供的安装程序”
				// int result =
				// Settings.Secure.getInt(mContext.getContentResolver(),
				// Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
				// if(0 == result){
				// IsAllowInstallDlg(R.string.install_allowed);
				// }

				installApk(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	}

	private String FromatFileSize(int nSize) {

		String strNum = null;
		if (nSize < 1024) {
			strNum = Integer.toString(nSize) + "B";
			return strNum;
		} else if (nSize < 1024 * 1024) {
			return Integer.toString((int) (nSize / (1024))) + "K";
		} else if (nSize < 1024 * 1024 * 1024) {
			strNum = String.format("%.2fM", ((double) nSize) / (1024 * 1024));
			return strNum;
		} else {
			strNum = String.format("%.2fG", ((double) nSize)
					/ (1024 * 1024 * 1024));
			return strNum;
		}
	}

	// private void IsAllowInstallDlg(int strId) {
	// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	// builder.setTitle(R.string.install_forbid);
	// builder.setPositiveButton(R.string.txtSetting, new
	// DialogInterface.OnClickListener(){
	//
	// public void onClick(DialogInterface dialog, int which) {
	// Intent intent = new Intent();
	// intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
	// mContext.startActivity(intent);
	// }
	//
	// });
	// builder.setNegativeButton(R.string.txtCancel, null);
	// builder.setMessage(strId);
	// builder.show();
	// }

}
