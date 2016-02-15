package com.utils.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

public class ApkRunnable implements Runnable {

	public static final int DOWN_UPDATE = 1;
	public static final int DOWN_OVER = 2;

	private Handler mHandler = null;
	private String mStrUrl = null;
	private String strApkName = null;

	private boolean interceptFlag = false;

	public ApkRunnable(Handler handler, String strUrl, String strName) {
		mHandler = handler;
		mStrUrl = strUrl;
		strApkName = strName;
	}

	public void Cancel() {
		interceptFlag = true;
	}

	public void run() {
		try {
			URL url = new URL(mStrUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000);
			// conn.setReadTimeout(100);
			conn.connect();
			int length = conn.getContentLength();
			InputStream is = conn.getInputStream();

			// 创建输出流
			File ApkFile = new File(strApkName);
			FileOutputStream fos = new FileOutputStream(ApkFile);

			int count = 0;
			byte buf[] = new byte[1024];

			do {

				int numread = is.read(buf);
				count += numread;

				if (numread <= 0) {

					// 关闭文件
					fos.close();
					is.close();

					// 下载完成通知安装
					if (mHandler != null) {
						Message msg = mHandler.obtainMessage(DOWN_OVER,
								strApkName);
						mHandler.sendMessage(msg);
					}
					break;

				} else {

					fos.write(buf, 0, numread);

					// 更新进度
					if (mHandler != null) {
						Message msg = mHandler.obtainMessage(DOWN_UPDATE,
								length, count);
						mHandler.sendMessage(msg);
					}
				}
			} while (!interceptFlag);// 点击取消就停止下载.

			fos.close();
			is.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
};
