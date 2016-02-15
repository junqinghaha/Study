package com.android.uitils;

import java.io.File;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.utils.log.MLog;

/***
 * android打开文件管理
 * 
 * @author TNT
 * 
 */
public class AndroidFileUtil {

	/***
	 * 打开文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static Intent openFile(String filePath) {

		File file = new File(filePath);
		if (!file.exists())
			return null;
		/* 取得扩展名 */
		String end = file
				.getName()
				.substring(file.getName().lastIndexOf(".") + 1,
						file.getName().length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			return getAudioFileIntent(filePath);
		} else if (end.equals("3gp") || end.equals("mp4") || end.equals("avi")) {
			return getVideoFileIntent(filePath);
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			return getImageFileIntent(filePath);
		} else if (end.equals("apk")) {
			return getApkFileIntent(filePath);
		} else if (end.equals("ppt") || end.equals("pptx") || end.equals("pps")
				|| end.equals("ppsx")) {
			return getPptFileIntent(filePath);
		} else if (end.equals("xls") || end.equals("xlsx")) {
			return getExcelFileIntent(filePath);
		} else if (end.equals("doc") || end.equals("docx")) {
			return getWordFileIntent(filePath);
		} else if (end.equals("pdf")) {
			return getPdfFileIntent(filePath);
		} else if (end.equals("chm")) {
			return getChmFileIntent(filePath);
		} else if (end.equals("txt")) {
			return getTextFileIntent(filePath, false);
		} else {
			return getAllIntent(filePath);
		}
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// Android获取一个用于打开APK文件的intent
	public static Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// Android获取一个用于打开VIDEO文件的intent
	public static Intent getVideoFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// Android获取一个用于打开AUDIO文件的intent
	public static Intent getAudioFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// Android获取一个用于打开Html文件的intent
	public static Intent getHtmlFileIntent(String param) {

		Uri uri = Uri.parse(param).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// Android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// Android获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// Android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// Android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// Android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// Android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(String param, boolean paramBoolean) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// Android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	/****
	 * 获得视频缩略图
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap CreateVideoThumbnail(Context context, File file) {
		Bitmap bitmap = null;
		android.media.MediaMetadataRetriever retriever = new android.media.MediaMetadataRetriever();
		try {// MODE_CAPTURE_FRAME_ONLY
				// retriever
				// .setMode(android.media.MediaMetadataRetriever.MODE_CAPTURE_FRAME_ONLY);
				// retriever.setMode(MediaMetadataRetriever.MODE_CAPTURE_FRAME_ONLY);
			retriever.setDataSource(context, Uri.fromFile(file));
			// bitmap = retriever.captureFrame();
			String timeString = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			long time = Long.parseLong(timeString) * 1000;
			Log.i("TAG", "time = " + time);
			bitmap = retriever.getFrameAtTime(time * 31 / 160); // 按视频长度比例选择帧
		} catch (IllegalArgumentException ex) {
			// Assume this is a corrupt video file
			ex.printStackTrace();
		} catch (RuntimeException ex) {
			// Assume this is a corrupt video file.
			ex.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
				// Ignore failures while cleaning up.
			}
		}
		return bitmap;
	}

	public static Bitmap CreateAlbumThumbnail(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			// retriever.setMode(MediaMetadataRetriever.MODE_GET_METADATA_ONLY);
			retriever.setDataSource(filePath);
			byte[] art = retriever.getEmbeddedPicture();// .extractAlbumArt();
			// 获取缩略图像
			BitmapFactory.Options localOptions = new BitmapFactory.Options();
			localOptions.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeByteArray(art, 0, art.length,
					localOptions);
			localOptions.inJustDecodeBounds = false;
			int i = (int) (localOptions.outHeight / 80.0F);
			if (i <= 0)
				i = 2;
			localOptions.inSampleSize = i;
			bitmap = BitmapFactory.decodeByteArray(art, 0, art.length,
					localOptions);
		} catch (IllegalArgumentException ex) {
		} catch (RuntimeException ex) {
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
				// Ignore failures while cleaning up.
			}
		}
		return bitmap;
	}

	public static Bitmap CreateImgThumbnail(File file) {
		Bitmap localBitmap = null;
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		try {
			localOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), localOptions);
			localOptions.inJustDecodeBounds = false;
			int i = (int) (localOptions.outHeight / 80.0F);
			if (i <= 0)
				i = 1;
			localOptions.inSampleSize = i;
			localBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
					localOptions);
			return localBitmap;
		} catch (Exception e) {
			MLog.e("changchun", "图片解析错误：" + e.toString());
			localOptions.inSampleSize = 5;
			localBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
					localOptions);
		}
		return localBitmap;
	}

	public static Bitmap createVideoThumbnail(Context context, Uri uri) {
		Bitmap bitmap = null;
		String className = "android.media.MediaMetadataRetriever";
		Object objectMediaMetadataRetriever = null;
		Method release = null;
		try {
			objectMediaMetadataRetriever = Class.forName(className)
					.newInstance();
			Method setModeMethod = Class.forName(className).getMethod(
					"setMode", int.class);
			setModeMethod.invoke(objectMediaMetadataRetriever, 4);

			Method setDataSourceMethod = Class.forName(className).getMethod(
					"setDataSource", Context.class, Uri.class);
			setDataSourceMethod.invoke(objectMediaMetadataRetriever, context,
					uri);

			Method captureFrameMethod = Class.forName(className).getMethod(
					"captureFrame");
			release = Class.forName(className).getMethod("release");

			bitmap = (Bitmap) captureFrameMethod
					.invoke(objectMediaMetadataRetriever);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (release != null) {
					release.invoke(objectMediaMetadataRetriever);
				}
			} catch (Exception e) {
				// Ignore failures while cleaning up.
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
