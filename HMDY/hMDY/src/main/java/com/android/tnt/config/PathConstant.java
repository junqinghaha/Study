package com.android.tnt.config;

import java.io.File;

/**
 * 职责： 路径常量
 * 
 * @author TNT
 * 
 */
public class PathConstant {
	/** SD卡目录 */
	public static String SDCardPath = SDCardManager.GetSDCardPath();

	/** 工作目录名称 */
	public final static String WORK_FOLDER_NAME = "hmdy";
	public final static String CONFIG_FOLDER_NAME = "config";
	/** 配置文件名称 */
	public final static String SETTING_FILE_NAME = "set.ini";
	/** 数据库目录名称 */
	public final static String DATABASE_FOLDER_NAME = "db";

	// 目录---------------------------------------------------------------------------目录
	/** 程序工作目录 */
	public static String AppWorkPath = SDCardPath + File.separator
			+ WORK_FOLDER_NAME;
	/** 数据库目录 */
	public static String DatabasePath = AppWorkPath + File.separator
			+ DATABASE_FOLDER_NAME;
	/** 配置目录 */
	public static String ConfigPath = AppWorkPath + File.separator
			+ CONFIG_FOLDER_NAME;

}
