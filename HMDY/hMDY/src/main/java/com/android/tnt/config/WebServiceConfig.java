package com.android.tnt.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.util.Log;

import com.android.uitils.FileUtil;
import com.utils.log.MLog;

/***
 * 网络配置
 * 
 * @author TNT
 * 
 */
public class WebServiceConfig {

	private static final String TAG = "WebServiceConfig";

	/** 更新地址 */
	public static String UpdateFileURL = "http://116.90.85.94:8010/update.xml";
	//public static String UpdateFileURL = "http://115.28.66.223:8010/update.xml";

	// 调试模式
	public static boolean IsDebugMode = true;

	public static String SERVICE_HEADER = "http://";

	/** 地址1 */
	public static String Address1 = "116.90.85.94:8010";//
	//public static String Address1 = "115.28.66.223:8010";//
	/** 地址2 */
	public static String Address2 = "116.90.85.94:8010";//
	//public static String Address2 = "115.28.66.223:8010";//
	/** 地址1 */
	public static String Address3 = "116.90.85.94:8010";//
	//public static String Address3 = "115.28.66.223:8010";//
	/** 地址2 */
	public static String Address4 = "116.90.85.94:8010";//
	//public static String Address4 = "115.28.66.223:8010";//
//	/** 地址1 */
//	public static String Address1 = "222.168.83.35:70";//
//	/** 地址2 */
//	public static String Address2 = "222.168.83.35:70";//
//	/** 地址1 */
//	public static String Address3 = "222.168.83.35:70";//
//	/** 地址2 */
//	public static String Address4 = "222.168.83.35:70";//
	//http://localhost:6134/ServiceEngineHandler.ashx
	/** 地址1--路径--公共接口 */
	public static String Servic1 = "/api/";
	/** 地址2--路径--项目接口 */
	public static String Servic2 = "/api/";
	/** 地址3--路径--排查接口 */
	public static String Servic3 = "/api/";
	/** 地址4--路径 --隐患接口 */
	public static String Servic4 = "/api/";

	/** 命名空间1 **/
	public static String Namespace1 = "http://tempuri.org/";
	/** 命名空间2 **/
	public static String Namespace2 = "http://tempuri.org/";
	/** 命名空间3 **/
	public static String Namespace3 = "http://tempuri.org/";
	/** 命名空间4 **/
	public static String Namespace4 = "http://tempuri.org/";

	/** ActionHeader1 **/
	public static String ActionHeader1 = Namespace1 + "ICommonService/";
	/** ActionHeader2 **/
	public static String ActionHeader2 = Namespace2 + "IProjectService/";
	/** ActionHeader3 **/
	public static String ActionHeader3 = Namespace1 + "ICheckService/";
	/** ActionHeader4 **/
	public static String ActionHeader4 = Namespace2 + "IHiddenService/";

	/** 地址1 **/
	public static String ServerAddress1 = SERVICE_HEADER + Address1 + Servic1;//
	/** 地址2 **/
	public static String ServerAddress2 = SERVICE_HEADER + Address2 + Servic2;
	/** 地址3 **/
	public static String ServerAddress3 = SERVICE_HEADER + Address3 + Servic3;//
	/** 地址4 **/
	public static String ServerAddress4 = SERVICE_HEADER + Address4 + Servic4;
	/** 文件接口地址 **/
	public static String ServerAddress5 = SERVICE_HEADER + Address1 + Servic1;

	// public static String SEVICE5 = "/FileTransferHandler.ashx";//
	// ?Method=UpLoadFile&
	// public static String UP_FILE_METHOD = "UpLoadFile";

	/****
	 * 文件上传地址
	 */
	public static String UP_FILE_URL = ServerAddress5 + "?Method=UpLoadFile&";
	/****
	 * 获取隐患文件列表地址
	 */
	public static String GET_TROUBLE_FILE = ServerAddress5
			+ "?Method=GetFileInfoByClassCode&";
	/****
	 * 根据ID下载文件
	 */
	public static String FILE_DOWNLOAD = ServerAddress5
			+ "?Method=GetFileDataByFileID&";
	/****
	 * 根据名称下载文件
	 */
	public static String FILE_DOWNLOAD_BY_NAME = ServerAddress5
			+ "?Method=GetFileDataByFileName&";

	/****
	 * 刷新地址
	 */
	public static void RefreshAddress() {
		// ServerAddress1 = SERVICE_HEADER + Address1 + Servic1;
		// ServerAddress2 = SERVICE_HEADER + Address2 + Servic2;
		// ServerAddress3 = SERVICE_HEADER + Address3 + Servic3;
		// ServerAddress4 = SERVICE_HEADER + Address4 + Servic4;
		// UP_FILE_URL = SERVICE_HEADER + Address1
		// + "/FileTransferHandler.ashx?Method=UpLoadFile&";
		// GET_TROUBLE_FILE = SERVICE_HEADER + Address1
		// + "/FileTransferHandler.ashx?Method=GetFileInfoByClassCode&";
		// FILE_DOWNLOAD = SERVICE_HEADER + Address1
		// + "/FileTransferHandler.ashx?Method=GetFileDataByFileID&";
	}

	public static void refreshFileAddress5() {
		UP_FILE_URL = ServerAddress5 + "?Method=AddPicture&";
		GET_TROUBLE_FILE = ServerAddress5 + "?Method=GetFileInfoByClassCode&";
		FILE_DOWNLOAD = ServerAddress5 + "?Method=GetFileDataByFileID&";
		FILE_DOWNLOAD_BY_NAME = ServerAddress5 + "?Method=GetFileDataByFileName&";
	}

	public static void initSetting() {
		File file = PathManager.GetSettingFile();
		if (file != null && file.exists()) {
			int i = 1;
			try {
				BufferedReader bReader = null;
				if (FileUtil.IsUTF8(file)) {
					bReader = new BufferedReader(new InputStreamReader(
							new FileInputStream(file), "UTF-8"));
				} else {
					bReader = new BufferedReader(new InputStreamReader(
							new FileInputStream(file), "GB2312"));
				}

				String str2 = bReader.readLine();
				while (str2 != null) {
					if (i == 1) {// 第一行
						String[] arrStrings = str2.split("##");
						if (arrStrings != null && arrStrings.length > 0) {
							try {
								ServerAddress1 = arrStrings[1];
							} catch (Exception e) {
								MLog.e(TAG, "地址更新错误:" + e.toString());
							}
						}
					} else if (i == 2 && str2 != null && str2.contains("##")) {// 第二行
						String[] arrStrings = str2.split("##");
						if (arrStrings != null && arrStrings.length > 0) {
							try {
								ServerAddress2 = arrStrings[1];
							} catch (Exception e) {
								MLog.e(TAG, "地址更新错误:" + e.toString());
							}
						}
					} else if (i == 3 && str2 != null && str2.contains("##")) {// 第三行
						String[] arrStrings = str2.split("##");
						if (arrStrings != null && arrStrings.length > 0) {
							try {
								ServerAddress3 = arrStrings[1];
							} catch (Exception e) {
								MLog.e(TAG, "地址更新错误:" + e.toString());
							}
						}
					} else if (i == 4 && str2 != null && str2.contains("##")) {// 第四行
						String[] arrStrings = str2.split("##");
						if (arrStrings != null && arrStrings.length > 0) {
							try {
								ServerAddress4 = arrStrings[1];
							} catch (Exception e) {
								MLog.e(TAG, "地址更新错误:" + e.toString());
							}
						}
					} else if (i == 5 && str2 != null && str2.contains("##")) {// 第五行
						String[] arrStrings = str2.split("##");
						if (arrStrings != null && arrStrings.length > 0) {
							try {
								ServerAddress5 = arrStrings[1];
								refreshFileAddress5();
							} catch (Exception e) {
								MLog.e(TAG, "地址更新错误:" + e.toString());
							}
						}
					}else if (i == 6 && str2 != null && str2.contains("##")) {// 第6行
						String[] arrStrings = str2.split("##");
						if (arrStrings != null && arrStrings.length > 0) {
							try {
								UpdateFileURL = arrStrings[1];
							} catch (Exception e) {
								MLog.e(TAG, "地址更新错误:" + e.toString());
							}
						}
					}
					str2 = bReader.readLine();
					i++;
				}
			} catch (Exception localException) {
				Log.e(TAG, localException.toString());
			}
		} else {
			MLog.e(TAG, "未发现配置文件");
		}
	}
}
