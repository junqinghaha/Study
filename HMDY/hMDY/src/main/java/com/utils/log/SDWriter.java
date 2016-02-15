package com.utils.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.text.format.Time;

import com.android.tnt.config.Constants;
import com.android.tnt.config.PathConstant;

/**
 * 写入文本信息到sdcard
 * 
 * @author TNT
 * 
 */
public class SDWriter {

	private FileWriter writer = null;

	private static SDWriter instance = null;
	private Time time = new Time();
	public String logFilePath = null;

	/**
	 * 获得日志全路径
	 * 
	 * @return
	 */
	public String getLogFilePath() {
		return logFilePath;
	}

	/**
	 * 设置日志全路径
	 * 
	 * @param logFilePath
	 *            例如：/sdcard/log/log1.txt
	 */
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
		initWriter();
	}

	/**
	 * 初始化日志默认路径
	 */
	private void initLogPath() {
		time.setToNow();
		String strTime = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault()).format(time.toMillis(false));
		String folder = PathConstant.AppWorkPath + File.separator + "log";
		if (!new File(folder).exists()) {
			new File(folder).mkdirs();
		}
		logFilePath = folder + File.separator + "MLog_" + strTime + ".txt";
	}

	private SDWriter() {
		initLogPath();
		initWriter();
	}

	/**
	 * 初始化写对象
	 */
	private void initWriter() {
		try {
			if (writer != null) {// 关闭
				writer.flush();
				writer.close();
				writer = null;
			}
			if (logFilePath != null) {
				writer = new FileWriter(new File(logFilePath));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static SDWriter GetInstance() {
		if (instance == null) {
			instance = new SDWriter();
		}

		return instance;
	}

	public void CloseLog() {

		try {
			// output.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 写入日志
	 * 
	 * @param str
	 */
	public void WriteTxtLog(String str) {

		time.setToNow();
		String strTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(time.toMillis(false));
		strTime = "\r\n时间:----" + strTime + "---" + "\r\n";

		try {
			if (writer != null && Constants.IsDebugMode) {
				writer.write(strTime);
				writer.write(str + "\r\n");
				writer.flush();
				// writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
