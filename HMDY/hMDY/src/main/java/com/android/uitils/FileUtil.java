package com.android.uitils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	// private Context context;
	// public FileUtil(Context paramContext) {
	// this.context = paramContext;
	// }
	private static final String TAG = FileUtil.class.getName();

	public static String GetFromAssets(Context context, String paramString) {
		String str1 = null;
		try {
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(context.getResources().getAssets()
							.open(paramString)));
			StringBuffer localStringBuffer = new StringBuffer();
			String str2 = localBufferedReader.readLine();
			while (str2 != null) {
				localStringBuffer.append(str2.trim());
				str2 = localBufferedReader.readLine();
			}
			str1 = localStringBuffer.toString();
		} catch (Exception localException) {
			str1 = null;
		}
		return str1;
	}

	/**
	 * 读取sd卡文件内容
	 * 
	 * @param strFilePath
	 * @return
	 */
	public static String GetFromSDFile(String strFilePath) {
		if (IsUTF8(strFilePath)) {
			return GetFromSDFile(strFilePath, "UTF-8");
		} else {
			return GetFromSDFile(strFilePath, "GB2312");
		}
	}

	/**
	 * 读取sd卡文件内容
	 * 
	 * @param strFilePath
	 * @param enCoding
	 *            "GB2312" utf8
	 * @return
	 */
	public static String GetFromSDFile(String strFilePath, String enCoding) {
		String str1 = null;
		File file = new File(strFilePath);
		try {
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), enCoding));
			StringBuffer localStringBuffer = new StringBuffer();
			String str2 = localBufferedReader.readLine();
			while (str2 != null) {
				localStringBuffer.append(str2.trim());
				str2 = localBufferedReader.readLine();
			}
			str1 = localStringBuffer.toString();

			str1 = new String(str1.getBytes(), "UTF-8");

		} catch (Exception localException) {
			str1 = null;
		}
		return str1;
	}

	/**
	 * 转换 String为inputeStream
	 * 
	 * @param s
	 * @return
	 */
	public static InputStream GetStringInputStream(String s) {
		if (s != null && !s.equals("")) {
			try {
				return new ByteArrayInputStream(s.getBytes());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 删除文件 如果该文件时目录，删除该目录以及该目录下的子目录
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		deleteFile(new File(path));
	}

	private static void copy(InputStream is, OutputStream os)
			throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		int len;
		byte[] buffer = new byte[1024];
		while ((len = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.flush();
	}

	/**
	 * 向sd卡写入文件
	 * 
	 * @param strFilePath
	 * @param strFileName
	 * @param in
	 * @return
	 */
	public static boolean WriteFileToSDCard(String strFilePath, File desFile,
			InputStream in) throws IOException {
		boolean bRet = false;
		if (in == null || desFile == null) {
			return bRet;
		}

		try {
			// 0判断文件是否存在，存在则删除后加载
			// File file = new File(strFilePath + File.separator + strFileName);
			if (desFile.exists()) {
				desFile.delete();
			} else {
				File pathFile = new File(strFilePath);
				if (!pathFile.exists()) {
					pathFile.mkdirs();
				}
			}

			FileOutputStream outStream = null;

			try {
				outStream = new FileOutputStream(desFile);
				copy(in, outStream);
				bRet = true;
			} catch (FileNotFoundException e) {
				Log.e(TAG, "file not found");
				throw e;
			} catch (IOException e) {
				Log.e(TAG, "ioexception");
				throw e;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					if (bRet) {
						throw e;
					}
				}
				try {
					if (outStream != null) {
						outStream.close();
					}
				} catch (IOException e) {
					if (bRet) {
						throw e;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bRet;
	}

	/**
	 * 文件夹复制
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public static boolean copyDirectory(File srcFile, File destFile)
			throws IOException {
		boolean flag = false;
		File files[] = srcFile.listFiles();
		for (File file : files) {
			File f = new File(destFile.getAbsolutePath() + File.separator
					+ file.getName());
			if (file.isDirectory()) {
				f.mkdirs();
				flag = copyDirectory(file, f);
			} else {
				flag = copyFile(file, f);
			}
			if (!flag) {
				return flag;
			}
		}
		return flag;
	}

	public static boolean copyDirectory(String srcFile, String destFile)
			throws IOException {
		File src = new File(srcFile);
		File dest = new File(destFile);
		if (dest.exists()) {
			deleteFile(dest);
		} else {
			dest.mkdirs();
		}
		return copyDirectory(src, dest);
	}

	/**
	 * 文件复制
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public static boolean copyFile(String srcFile, String destFile)
			throws IOException {
		return copyFile(new File(srcFile), new File(destFile));
	}

	/**
	 * 文件复制
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public static boolean copyFile(File srcFile, File destFile)
			throws IOException {
		boolean flag = false;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(srcFile);
			os = new FileOutputStream(destFile);
			copy(is, os);
			flag = true;
		} catch (FileNotFoundException e) {
			Log.e(TAG, "file not found");
		} catch (IOException e) {
			Log.e(TAG, "ioexception");
		} finally {
			os.close();
			is.close();
		}
		return flag;
	}

	/**
	 * 复制工程资源文件到指定文件
	 * 
	 * @param context
	 * @param resourceID
	 * @param destFile
	 * @return
	 */
	public static void copyFile(Context context, int resourceID, File destFile)
			throws IOException {
		boolean error = false;
		InputStream is = context.getResources().openRawResource(resourceID);
		OutputStream os = null;
		try {
			os = new FileOutputStream(destFile);
			copy(is, os);
		} catch (FileNotFoundException e) {
			error = true;
			Log.e(TAG, "file not found");
			throw e;
		} catch (IOException e) {
			error = true;
			Log.e(TAG, "ioexception");
			throw e;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				if (!error) {
					throw e;
				}
			}
			try {
				os.close();
			} catch (IOException e) {
				if (!error) {
					throw e;
				}
			}
		}
	}

	/**
	 * 复制工程资源文件到指定文件
	 * 
	 * @param context
	 * @param resourceID
	 * @param destFile
	 * @return
	 */
	public static void copyFile(Context context, String assetResName,
			File destFile) throws IOException {
		boolean error = false;
		InputStream is = context.getResources().getAssets().open(assetResName);
		OutputStream os = null;
		try {
			os = new FileOutputStream(destFile);
			copy(is, os);
		} catch (FileNotFoundException e) {
			error = true;
			Log.e(TAG, "file not found");
			throw e;
		} catch (IOException e) {
			error = true;
			Log.e(TAG, "ioexception");
			throw e;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				if (!error) {
					throw e;
				}
			}
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				if (!error) {
					throw e;
				}
			}
		}
	}

	/**
	 * 向sd卡写入文件
	 * 
	 * @param strFilePath
	 * @param strFileName
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean CopySDFileToSD(String srcFile, String destFilePath,
			File destFile) throws IOException {
		String content = GetFromSDFile(srcFile);
		return WriteFileToSDCard(destFilePath, destFile, content);

	}

	/**
	 * 向sd卡写入文件
	 * 
	 * @param strFilePath
	 * @param strFileName
	 * @param in
	 * @return
	 */
	public static void CopyImgFileToSD(String srcFile, String destFilePath,
			File desFile) {
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeFile(srcFile);
		} catch (OutOfMemoryError e) {
			BitmapFactory.Options localOptions = new BitmapFactory.Options();
			localOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(srcFile, localOptions);
			localOptions.inJustDecodeBounds = false;
			int i = (int) (localOptions.outHeight / 800.0F);
			if (i <= 0)
				i = 4;
			localOptions.inSampleSize = i;
			bmp = BitmapFactory.decodeFile(srcFile, localOptions);
		}

		stroageImgToSD(bmp, destFilePath, desFile);
	}

	public static void stroageImgToSD(Bitmap bitmap, String path, File desFile) {
		if (path != null && bitmap != null && desFile != null) {
			if (!new File(path).exists()) {
				new File(path).mkdirs();
			}
			if (!desFile.exists()) {
				try {
					desFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(desFile);
					bitmap.compress(CompressFormat.JPEG, 90, fos);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

	/**
	 * 向sd卡写入文件
	 * 
	 * @param strFilePath
	 * @param strFileName
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean WriteFileToSDCard(String strFilePath, File desFile,
			String content) throws IOException {
		boolean bRet = false;
		if (content != null) {
			ByteArrayInputStream in = new ByteArrayInputStream(
					content.getBytes());
			bRet = WriteFileToSDCard(strFilePath, desFile, in);
		}

		return bRet;
	}

	public static boolean IsExist(String strPath) {
		return new File(strPath).exists();
	}

	/**
	 * 删除文件 如果该文件时目录，删除该目录以及该目录下的子目录
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File childFile : files) {
					deleteFile(childFile);
				}
				file.delete();
			}
		}
	}

	/**
	 * 判断SDCard是否挂载
	 * 
	 * @return
	 */
	public static boolean IsSDCardExsit() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 根据文件全称获得文件名
	 * 
	 * @param fileAbsolutePath
	 * @return
	 */
	public static String GetFileNameByWholePath(String fileAbsolutePath) {
		int startIndex = fileAbsolutePath.lastIndexOf("/");
		int endIndex = fileAbsolutePath.lastIndexOf(".");
		return fileAbsolutePath.substring(startIndex + 1, endIndex);
	}

	/**
	 * 根据文件全称获得文件名+扩展名
	 * 
	 * @param fileAbsolutePath
	 * @return
	 */
	public static String GetFileNameByWholePathWithExName(
			String fileAbsolutePath) {
		int startIndex = fileAbsolutePath.lastIndexOf("/");
		int endIndex = fileAbsolutePath.length();
		return fileAbsolutePath.substring(startIndex + 1, endIndex);
	}

	public static boolean IsUTF8(String path) {
		boolean bRt = false;
		if (path != null) {
			bRt = IsUTF8(new File(path));
		}
		return bRt;
	}

	public static boolean IsUTF8(File file) {
		boolean bRt = false;
		if (file != null && file.exists()) {
			InputStream in;
			try {
				in = new java.io.FileInputStream(file);
				byte[] b = new byte[3];
				in.read(b);
				in.close();
				if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
					bRt = true;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bRt;
	}

	/***
	 * 获取指定位置，指定类型的文件
	 * @param path
	 * @param type
	 * @return
	 */
	public static List<String> getOptTypeFileOnOptPath(String path, String type)
	{
		List<String> pathList = new ArrayList<String>();
		File fileFolder = new File(path);
		if (!fileFolder.isDirectory())
		{
			return null;
		} else
		{
			File[] files = fileFolder.listFiles();
			for (File file : files)
			{
				if (file.isFile())
				{
					String strPath = file.getAbsolutePath();
					int nTypeSize = type.length();
					String strExpend = strPath.substring(strPath.length()
							- nTypeSize, strPath.length());
					boolean bCompare = false;
					bCompare = strExpend.equalsIgnoreCase(type);
					if (bCompare)
					{
						pathList.add(strPath);
					}
				}
			}
		}
		return pathList;
	}
}
