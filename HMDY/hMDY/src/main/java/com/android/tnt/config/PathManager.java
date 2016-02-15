package com.android.tnt.config;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import android.content.Context;

/**
 * 职责： 路径管理
 * 
 * @author TNT
 * 
 */
public class PathManager {

	/**
	 * 初始化程序目录
	 * 
	 * @throws IOException
	 */
	public static void initWorkPath(Context context) throws IOException {
		// 工作目录
		File file = new File(PathConstant.AppWorkPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 许可目录
		file = new File(PathConstant.ConfigPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 许可目录
		file = new File(PathConstant.DatabasePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}


	/**
	 * 获得配置文件初始数据
	 * 
	 * @return
	 */
	public static File GetSettingFile() {
		File[] arrFiles = getFiles(PathConstant.ConfigPath, new String[] { PathConstant.SETTING_FILE_NAME });
		if (arrFiles != null && arrFiles.length > 0)
			return arrFiles[0];
		return null;
	}

	/**
	 * 获得指定格式的文件
	 * 
	 * @param path
	 *            遍历的目录
	 * @param formates
	 *            要获取的文件格式
	 * @return
	 */
	public static File[] getFiles(String path, final String[] formates) {
		if (path != null && !path.equalsIgnoreCase("")) {
			return new File(path).listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					// TODO Auto-generated method stub
					for (String str : formates) {
						if (filename.endsWith(str)) {
							return true;
						}
					}
					return false;
				}
			});
		}
		return null;
	}
}
