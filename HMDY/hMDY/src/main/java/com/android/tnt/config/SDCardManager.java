package com.android.tnt.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * Sdcard管理类
 * 
 * @author TNT
 * 
 */
public class SDCardManager {

	/**
	 * 获得SD卡目录
	 * 
	 * @return
	 */
	public static String GetSDCardPath() {
		if (android.os.Build.VERSION.SDK_INT > 17) {// 4.4及以上
			return Environment.getExternalStorageDirectory().getAbsolutePath();

		} else {
			String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();

			// 获得所有外置sdcard路径
			List<String> lstSdCards = GetAllExtenalSDCards();

			// 将内置卡排到第一位
			List<String> newLst = new ArrayList<String>();
			newLst.add(sdCard);
			for (String lst : lstSdCards) {
				if (!lst.equalsIgnoreCase(sdCard)) {
					newLst.add(lst);
				}
			}
			lstSdCards.clear();
			lstSdCards.addAll(newLst);

			// 开始比较选择
			if (lstSdCards.size() == 1) {// 只有一个sdcard
				sdCard = lstSdCards.get(0);

			} else if (lstSdCards.size() > 1) {// 存在多个，容量大的作为sdcard路径
				int nIndex = 0;
				long maxSize = getSDAllSize(sdCard);
				long size = 0;
				// 循环获得最大总容量sdcard
				for (int i = 0; i < lstSdCards.size(); i++) {
					try {
						size = getSDAllSize(lstSdCards.get(i));
					} catch (Exception e) {
						size = 0;
					}
					if (size > maxSize) {
						nIndex = i;
						maxSize = size;
					}
				}
				sdCard = lstSdCards.get(nIndex);
			}
			Log.d("leadmap", "sdCard路径="+sdCard);
			return sdCard;
		}

	}

	/**
	 * 选择指定包含某个文件夹的SDCard
	 * 
	 * @param workFolder
	 * @return
	 */
	public static String GetSDCardPath(String workFolder) {

		String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();

		// 获得所有外置sdcard路径
		List<String> lstSdCards = GetAllExtenalSDCards();

		if (lstSdCards.size() == 1) {// 只有一个sdcard
			sdCard = lstSdCards.get(0);

		} else if (lstSdCards.size() > 1) {// 存在多个，容量大的作为sdcard路径
			int nIndex = -1;
			// 循环判断是否包含次文件夹
			for (int i = 0; i < lstSdCards.size(); i++) {
				if (isExistFolder(lstSdCards.get(i), workFolder)) {
					nIndex = i;
					break;
				}
			}
			if (nIndex != -1) {
				sdCard = lstSdCards.get(nIndex) + File.separator;
			} else {
				sdCard = GetSDCardPath();// 默认总容量大的为存储空间
			}
		}
		return sdCard;
	}

	/**
	 * 是否存在工作文件夹
	 * 
	 * @param sdPath
	 * @param folderName
	 * @return
	 */
	private static boolean isExistFolder(String sdPath, final String folderName) {
		boolean bRt = false;
		try {
			File[] files = new File(sdPath).listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					if (pathname.isDirectory() && pathname.getName().equals(folderName)) {
						return true;
					}
					return false;
				}
			});
			if (files != null && files.length == 1) {
				bRt = true;
			}
		} catch (Exception e) {
			bRt = false;
		}

		return bRt;
	}

	/**
	 * 获得所有外置sdcard
	 * 
	 * @return
	 */
	public static List<String> GetAllExtenalSDCards() {
		List<String> lstSdcards = new ArrayList<String>();
		// 得到路径
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;

				if (line.contains("fat")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						lstSdcards.add(columns[1]);
					}
				} else if (line.contains("fuse")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						lstSdcards.add(columns[1]);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstSdcards;
	}

	/**
	 * 获得SD卡剩余空间
	 * 
	 * @param sdCardPath
	 * @return
	 */
	public static long getSDFreeSize(String sdCardPath) {
		// 取得SD卡文件路径
		StatFs sf = new StatFs(sdCardPath);
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/***
	 * 获得SD卡全部空间
	 * 
	 * @param sdCardPath
	 * @return
	 */
	public static long getSDAllSize(String sdCardPath) {
		// 取得SD卡文件路径
		StatFs sf = new StatFs(sdCardPath);
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 是否存在sdcard
	 * 
	 * @return
	 */
	public static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 获得SDCard剩余空间
	 * 
	 * @param sdcard
	 * @return
	 */
	public static long getAvailableStorageSize(String sdcard) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = new File(sdcard);
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();

			long availableBlocks = sf.getAvailableBlocks();

			return availableBlocks * blockSize;
		} else {
			return 0;
		}
	}

}
