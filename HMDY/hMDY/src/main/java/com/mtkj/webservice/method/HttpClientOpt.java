package com.mtkj.webservice.method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.net.http.AndroidHttpClient;

import com.android.tnt.config.SysConfig;
import com.mtkj.webservice.HttpOperate;
import com.mtkj.webservice.WebParam;
import com.utils.log.MLog;

/***
 * 封装 http请求的操作类
 * 
 * @author 周涛涛
 * 
 */
public class HttpClientOpt {
	/** 发送http请求对象 */
	private HttpGet mHttpGet = null;
	/** http客户端对象 */
	private HttpClient mHttpClient = null;
	/** 接收http的响应 */
	private HttpResponse mHttpResponse = null;
	/** http实体 */
	private HttpEntity mHttpEntity = null;
	/** 请求URL */
	private String mStrURL = null;
	/** 获取http响应的输入流 */
	private InputStream mInputStream = null;
	/** 上传到服务器本地文件的路径 */
	private String mStrUploadFilePath = null;
	/** 上传后在服务器上的名称 */
	private String mNewName = "image.jpg";

	private long totalSize = 0;

	public HttpClientOpt(String strURL) {
		this.mStrURL = strURL;
		mHttpGet = new HttpGet(mStrURL);
		mHttpClient = new DefaultHttpClient();
	}

	public HttpClientOpt(String strURL, String methedName, List<WebParam> lstParams) {
		this.mStrURL = strURL;
		mHttpGet = new HttpGet(mStrURL);
		mHttpClient = new DefaultHttpClient();
	}

	public HttpClientOpt(String strURL, String file, String strFileName) {
		this.mStrURL = strURL;
		this.mStrUploadFilePath = file;
		this.mNewName = strFileName;
		mHttpGet = new HttpGet(mStrURL);
		mHttpClient = new DefaultHttpClient();
	}

	// public HttpClientOpt(String strURL) {
	// this.mStrURL = strURL;
	// }

	public String GetHttpRequest(String url, List<WebParam> lstParams) {
		String result = "";
		try {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(url);
			// urlBuilder.append("?");
			// urlBuilder.append("Command=");
			// urlBuilder.append("Method=");
			// urlBuilder.append(mMethodName);
			//urlBuilder.append("?UserId=" + SysConfig.curUser.getUserID());
			if ((null != lstParams) && lstParams.size() > 0) {
				int i = 0;
				for (WebParam params : lstParams) {
					if (i == 0) {
						urlBuilder.append("?");
					} else {
						urlBuilder.append('&');
					}
					try {
						urlBuilder.append(URLEncoder.encode(params.getParName(), "UTF-8")).append('=').append(URLEncoder.encode((params.getParValue() + ""), "UTF-8"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
				}
			}

			HttpGet httpGet = new HttpGet(urlBuilder.toString());
			// AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
			// HttpClient httpClient = new DefaultHttpClient();
			// 发送请求获得响应对象
			if (HttpOperate.httpClient == null) {
				HttpOperate.httpClient = new DefaultHttpClient();
			}
			HttpResponse response = HttpOperate.httpClient.execute(httpGet);
			// 获取响应消息实体
			HttpEntity entity = response.getEntity();
			// 获取输入流
			InputStream inputStream = entity.getContent();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String line = "";

			while (null != (line = bufferedReader.readLine())) {
				result += line;
			}
			// httpClient.close();
		} catch (Exception e) {
			// TODO: handle exception
			MLog.e("ERROR", "ERROR:" + e.toString());
		}
		return result;
	}

	/***
	 * 发送http请求
	 * 
	 * @return 返回响应字符串
	 */
	public String PostHttpRequest(String url, List<WebParam> lstParams) {
		String result = "";
		try {
			List<NameValuePair> lstPostParams = new ArrayList<NameValuePair>();
			if ((null != lstParams) && lstParams.size() > 0) {
				for (WebParam params : lstParams) {
					lstPostParams.add(new BasicNameValuePair(params.getParName(), params.getParValue() + ""));
				}
			}
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(lstPostParams, HTTP.UTF_8));
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
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
					result += line;
				}
			}
			httpClient.close();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 发送http请求
	 * 
	 * @return 返回响应字符串
	 */
	public String PostHttpRequest() {
		String result = "";
		try {
			// 发送请求获得响应对象
			mHttpResponse = mHttpClient.execute(mHttpGet);
			// 获取响应消息实体
			mHttpEntity = mHttpResponse.getEntity();
			// 获取输入流
			mInputStream = mHttpEntity.getContent();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream));

			String line = "";

			while (null != (line = bufferedReader.readLine())) {
				result += line;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
