package com.utils.update;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.android.uitils.Util;

public class VersionCheck extends AsyncTask<String, String, Boolean> {

	private boolean bShowDialog = true;
	private Context mContext = null;
	private ProgressDialog progressDialog = null;

	private int nVersionCode = 0;
	private String strVersionName = "";
	private String strUrl = "";

	public VersionCheck(Context context, boolean bShow) {
		mContext = context;
		bShowDialog = bShow;
	}

	// 显示更新提示
	private void showUpdateNoticeDialog() {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("版本更新");
		builder.setMessage("检查到新的版本，是否立即下载更新？");
		builder.setCancelable(false);
		builder.setPositiveButton("立即下载", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				// 显示下载对话框
				DownApkManager apkDown = new DownApkManager(mContext);
				apkDown.DownloadApk(strUrl, "货码打印_V" + strVersionName + ".apk");
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});

		Dialog noticeDialog = builder.create();
		noticeDialog.setCancelable(false);
		noticeDialog.show();
	}

	protected void onPreExecute() {

		if (bShowDialog) {
			// 显示对话框
			progressDialog = new ProgressDialog(mContext);
			progressDialog.setMessage("检查更新...");
			progressDialog.show();
		}
	}

	public Boolean doInBackground(String... params) {

		if (params.length > 0) {

			String url = params[0];

			try {

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder docBuilder = factory.newDocumentBuilder();

				// 解析XMl
				Document document = docBuilder.parse(url);

				if (document != null) {
					Element root = document.getDocumentElement();

					String strCode = root.getAttribute("VersionCode");
					nVersionCode = Integer.parseInt(strCode);
					strVersionName = root.getAttribute("VersionName");

					// 获取http
					Node node = root.getFirstChild();
					strUrl = node.getNodeValue();

					return true;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void onPostExecute(Boolean data) {
		if (bShowDialog) {
			// 关闭对话框
			progressDialog.dismiss();
		}

		if (data) {

			try {
				PackageManager packMgr = mContext.getPackageManager();
				String packageName = mContext.getPackageName();

				PackageInfo packInfo = packMgr.getPackageInfo(packageName, 0);

				if (packInfo.versionCode < nVersionCode) {
					showUpdateNoticeDialog();
				} else {
					if (bShowDialog) {
						Util.MsgBox(mContext, "您目前的版本已是最新版本");
						// Toast.makeText(mContext, "您目前的版本已是最新版本",
						// Toast.LENGTH_SHORT).show();
					}
				}

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

		} else {
			if (bShowDialog) {
				Util.MsgBox(mContext, "网络连接异常");
				// Toast.makeText(mContext, "网络连接异常",
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

}
