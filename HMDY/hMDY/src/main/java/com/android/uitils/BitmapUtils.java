package com.android.uitils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Base64;

import com.utils.log.MLog;

public class BitmapUtils {

	private static final String TAG = "BitmapUntil---------------";

	public static Options getOptions(String path) {
		Options options = new Options();
		options.inJustDecodeBounds = true; // 只描边，不读取数�?
		BitmapFactory.decodeFile(path, options);
		return options;
	}

	/**
	 * 获得图像
	 * 
	 * @param path
	 * @param options
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap getBitmapByPath(String path, int screenWidth,
			int screenHeight) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		FileInputStream in = null;
		in = new FileInputStream(file);

		Options options = getOptions(path);
		if (options != null) {
			int maxSize = screenWidth > screenHeight ? screenWidth
					: screenHeight;
			int inSimpleSize = computeSampleSize(options, maxSize, screenWidth
					* screenHeight);
			options.inSampleSize = inSimpleSize; // 设置缩放比例
			options.inJustDecodeBounds = false;
		}
		Bitmap b = BitmapFactory.decodeStream(in, null, options);
		try {
			in.close();
		} catch (IOException e) {
			MLog.v(TAG, e.getMessage());
		}
		return b;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 将指定路径的照片转换为Base64编码的字符串
	 * 
	 * @param picPath
	 *            照片路径
	 * @param qualityValue
	 *            照片质量【0-100】
	 * @return 转换后的字符
	 */
	public static String PicToBase64String(String picPath, int picHeight,
			int qualityValue) {
		try {

			BitmapFactory.Options localOptions = new BitmapFactory.Options();
			localOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picPath, localOptions);
			localOptions.inJustDecodeBounds = false;
			int i = (int) (localOptions.outHeight / picHeight);
			if (i <= 0)
				i = 4;
			localOptions.inSampleSize = i;
			Bitmap bp = BitmapFactory.decodeFile(picPath, localOptions);

			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			// Bitmap bp = BitmapFactory.decodeFile(picPath);
			if (null != bp) {
				bp.compress(CompressFormat.JPEG, qualityValue, bStream);
				byte[] picByte = bStream.toByteArray();
				String pic = Base64.encodeToString(picByte, Base64.DEFAULT);
				return pic;
			} else {
				return null;
			}

		} catch (Exception e) {
			MLog.e("changchun", "图片转换失败--图片太大");
		}
		return null;
	}

	public static void Base64StringToPic(String picString, String picPath) {

		try {

			byte[] picByte = Base64.decode(picString, Base64.DEFAULT);

			Bitmap bp = BitmapFactory.decodeByteArray(picByte, 0,
					picByte.length);

			FileOutputStream fos = new FileOutputStream(picPath);
			bp.compress(CompressFormat.JPEG, 100, fos);
			fos.close();

		} catch (Exception e) {
			MLog.v(TAG, e.getMessage());
		}
	}
	
	public static String getPicBase64String(String picPath) {
		InputStream in = null;
		byte[] data = null;

		try {
			in = new FileInputStream(picPath);
			data = new byte[in.available()];
			in.read(data);
			in.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

		String picString = Base64.encodeToString(data, Base64.DEFAULT);
		return picString;
	}

	public static Bitmap getBitmap(String strImgPath) {
		Bitmap localBitmap = null;
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		try {
			localOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(strImgPath, localOptions);
			localOptions.inJustDecodeBounds = false;
			int i = (int) (localOptions.outHeight / 800.0F);
			if (i <= 0)
				i = 2;
			localOptions.inSampleSize = i;
			localBitmap = BitmapFactory.decodeFile(strImgPath, localOptions);
			return localBitmap;
		} catch (Exception e) {
			MLog.e("changchun", "图片解析错误：" + e.toString());
			localOptions.inSampleSize = 5;
			localBitmap = BitmapFactory.decodeFile(strImgPath, localOptions);
		}
		return localBitmap;
	}
}
