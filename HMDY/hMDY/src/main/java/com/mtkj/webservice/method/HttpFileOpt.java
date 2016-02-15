package com.mtkj.webservice.method;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.text.TextUtils;

import com.android.uitils.FileUtil;
import com.mtkj.webservice.method.MyUpFileEntity.ProgressListener;
import com.utils.log.MLog;

/***
 * HTTP上传文件
 * 
 * @author 周涛涛
 * 
 */
public class HttpFileOpt {
	private long totalSize = 0;

	/** 上传监听 **/
	private OnUpInfoListener listener = null;

	// ------------------------

	public HttpFileOpt() {
	}

	public HttpFileOpt(OnUpInfoListener listener) {
		setOnUpInfoListener(listener);
	}

	/***
	 * 设置上传状态监听
	 * 
	 * @param listener
	 */
	public void setOnUpInfoListener(OnUpInfoListener listener) {
		this.listener = listener;
	}

	/***
	 * 上传文件到服务器
	 * 
	 * @return
	 */
	public String UploadFile(String url, File file) {
		String strResult = "";

		try {
			HttpPost httpPost = new HttpPost(url);
			HttpClient httpClient = new DefaultHttpClient();// .newInstance("");
			// httpGet.setEntity(new UrlEncodedFormEntity(lstPostParams,
			// HTTP.UTF_8));
			MyUpFileEntity fileEntity = new MyUpFileEntity(file, "*/*", new ProgressListener() {

				@Override
				public void transferred(long num) {
					if (listener != null) {
						listener.onFileUploading((int) ((num / (float) totalSize) * 100));
					}
				}
			});
//			MyUpBytesEntity byteEntity = new MyUpBytesEntity(FileUtil.File2byte(file.getAbsolutePath()), new ProgressListener() {
//				
//				@Override
//				public void transferred(long num) {
//					if (listener != null) {
//						listener.onFileUploading((int) ((num / (float) totalSize) * 100));
//					}
//				}
//			});
			totalSize = fileEntity.getContentLength();
			httpPost.setEntity(fileEntity);
			//httpGet.setEntity(byteEntity);
			// httpGet.setEntity(new FileEntity(file, "*/*"));
			// HttpClient httpClient = new DefaultHttpClient();
			// 发送请求获得响应对象
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获取响应消息实体
				HttpEntity entity = response.getEntity();
				// 获取输入流
				InputStream inputStream = entity.getContent();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

				String line = "";

				while (null != (line = bufferedReader.readLine())) {
					strResult += line;
				}
			}
			// 结束上传
			if (this.listener != null) {
				this.listener.onFileUploadied(file, strResult);
			}

		} catch (MalformedURLException e) {
			MLog.i("HttpClientOpt", e.toString());
		} catch (IOException e1) {
			MLog.i("HttpClientOpt", e1.toString());
		}

		return strResult;
	}

	/***
	 * 下载文件
	 * 
	 * @param path
	 *            下载文件存放在sd卡上的目录，例如"temp/"
	 * @param fileName
	 *            下载的文件存放名，例如"img.png"
	 * @return 返回-1下载文件出错，返回0下载成功，返回1文件已经存在
	 */
	public int downFile(String httpUrl, String path, String fileName) {
		try {
			InputStream is = null;
			String fileType = "";
			if (new File(path, fileName).exists()) {
				return 1;
			} else {
				// inputStream = 上个从网络上获得的输入流
				// is = getInputStreamFromUrl();
				URL url = null;
				HttpURLConnection httpURLConnection = null;
				try {
					url = new URL(httpUrl);
					httpURLConnection = (HttpURLConnection) url.openConnection();
					if (fileName != null && !fileName.contains(".")) {
						fileType = getFileName(httpURLConnection.getContentType());
					}
					is = httpURLConnection.getInputStream();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (is == null) {
					return -1;
				}
				if (!TextUtils.isEmpty(fileType)) {
					fileName = fileName + fileType;
				}
				if (!FileUtil.WriteFileToSDCard(path, new File(path, fileName), is)) {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	/***
	 * 下载文件
	 * 
	 * @param path
	 *            下载文件存放在sd卡上的目录，例如"temp/"
	 * @param fileName
	 *            下载的文件存放名，例如"img.png"
	 * @return 返回-1下载文件出错，返回0下载成功，返回1文件已经存在
	 */
	public File downFile2(String httpUrl, String path, String fileName) {
		File file = null;
		try {
			InputStream is = null;
			String fileType = "";
			if (new File(path, fileName).exists()) {
				return new File(path, fileName);
			} else {
				// inputStream = 上个从网络上获得的输入流
				// is = getInputStreamFromUrl();
				URL url = null;
				HttpURLConnection httpURLConnection = null;
				try {
					url = new URL(httpUrl);
					httpURLConnection = (HttpURLConnection) url.openConnection();
					if (fileName != null && !fileName.contains(".")) {
						fileType = getFileName(httpURLConnection.getContentType());
					}
					is = httpURLConnection.getInputStream();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (is == null) {
					return null;
				}
				if (!TextUtils.isEmpty(fileType)) {
					fileName = fileName + fileType;
				}
				if (FileUtil.WriteFileToSDCard(path, new File(path, fileName), is)) {
					file = new File(path, fileName);
				} else {
					file = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	private String getFileName(String contentType) {
		String type = ".txt";
		if (contentType != null) {
			if (contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.template")) {
				type = ".docx";

			} else if (contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
				type = ".docx";

			} else if (contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
				type = ".pptx";

			} else if (contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.presentationml.slideshow")) {
				type = ".ppsx";

			} else if (contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.presentationml.template")) {
				type = ".pptx";

			} else if (contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
				type = ".xlsx";

			} else if (contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.template")) {
				type = ".xltx";

			} else if (contentType.equalsIgnoreCase("application/msword")) {
				type = ".doc";

			} else if (contentType.equalsIgnoreCase("Model/vnd.dwf")) {
				type = ".dwf";

			} else if (contentType.equalsIgnoreCase("application/x-dwf")) {
				type = ".dwf";

			} else if (contentType.equalsIgnoreCase("application/x-dwg")) {
				type = ".dwg";

			} else if (contentType.equalsIgnoreCase("application/x-jpg")) {
				type = ".jpg";

			} else if (contentType.equalsIgnoreCase("image/jpeg")) {
				type = ".jpg";

			} else if (contentType.equalsIgnoreCase("application/x-png")) {
				type = ".png";

			} else if (contentType.equalsIgnoreCase("image/png")) {
				type = ".png";

			} else if (contentType.equalsIgnoreCase("application/pdf")) {
				type = ".pdf";

			} else if (contentType.equalsIgnoreCase("application-powerpoint")) {
				type = ".pps";

			} else if (contentType.equalsIgnoreCase("applications-powerpoint")) {
				type = ".ppt";

			} else if (contentType.equalsIgnoreCase("application/x-ppt")) {
				type = ".ppt";

			} else if (contentType.equalsIgnoreCase("application/-excel")) {
				type = ".xls";

			} else if (contentType.equalsIgnoreCase("application/x-xls")) {
				type = ".xls";

			} else if (contentType.equalsIgnoreCase("application/vnd.ms-excel")) {
				type = ".xls";

			} else if (contentType.equalsIgnoreCase("application/x-excel")) {
				type = ".xls";

			} else if (contentType.equalsIgnoreCase("application/x-xlw")) {
				type = ".xlw";

			} else if (contentType.equalsIgnoreCase("text/xml")) {
				type = ".xlm";

			} else if (contentType.equalsIgnoreCase("application/x-ppt")) {
				type = ".ppt";

			} else if (contentType.equalsIgnoreCase("application/vnd.ms-powerpoint")) {
				type = ".ppt";

			} else if (contentType.equalsIgnoreCase("application/x-shockwave-flash")) {
				type = ".swf";
			}
		}
		return type;
	}

	/***
	 * 获取从服务器返回来的输入流
	 * 
	 * @return
	 */
	private InputStream getInputStreamFromUrl(String httpUrl) {
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		InputStream is = null;
		try {
			url = new URL(httpUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			is = httpURLConnection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	/***
	 * 获取从服务器返回来的调用结果
	 * 
	 * @return
	 */
	private String getResultFromUrl(String httpUrl) {
		InputStream is = getInputStreamFromUrl(httpUrl);
		int ch;
		StringBuffer b = new StringBuffer();
		try {
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b.toString();
	}

	/***
	 * 文件上传状态监听
	 * 
	 * @author TNT
	 * 
	 */
	public interface OnUpInfoListener {
		/***
		 * 开始上传准备
		 * 
		 * @param totalCount
		 */
		public void onStartUpFile(int totalCount);

		/***
		 * 上传中
		 * 
		 * @param index
		 */
		public void onFileUploading(int index);

		/***
		 * 读取结果中
		 * 
		 */
		public void onGettingResult();

		/***
		 * 上传完毕
		 * 
		 * @param file
		 *            :上传的文件
		 * @param result
		 *            :结果
		 */
		public void onFileUploadied(File file, String result);
	}

}
