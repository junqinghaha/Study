package com.mtkj.webservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * webservice调用基础方法
 * 
 * @author TNT
 * 
 */
public abstract class BaseWebserviceMothod {
	/** 方法名称 */
	protected String methodName = "";
	/*** 方法参数 */
	protected List<WebParam> webParams = new ArrayList<WebParam>();
	/*** 返回值 */
	protected Object returnValue = null;
	/*** 返回值-原因 */
	protected String resultReason = null;
	/*** 返回值-结果值 */
	protected String resultValue = null;
	/** 参数是否为jason格式 */
	protected boolean isJson = false;
	
	protected boolean isPost = false;
	
	

	public BaseWebserviceMothod(String methodName) {
		super();
		this.methodName = methodName;
	}

	public BaseWebserviceMothod(String methodName, List<WebParam> webParams) {
		this(methodName);
		if (webParams != null) {
			this.webParams = webParams;
		}
	}
	
//	public String WebParamsToString(){
//		String paramsString = "";
//		if(this.webParams != null && this.webParams.size() > 0){
//			for(WebParam param:this.webParams){
//				paramsString += param.getParName() +";;"+ param.getParValue();
//			}
//		}
//	}

	public boolean isPost() {
		return isPost;
	}

	public void setPost(boolean isPost) {
		this.isPost = isPost;
	}

	public abstract Object parseWebResult(Object result);

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<WebParam> getWebParams() {
		return webParams;
	}

	public void setWebParams(List<WebParam> webParams) {
		if (webParams != null) {
			this.webParams = webParams;
		}
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	/**
	 * @return the resultValue
	 */
	public String getResultValue() {
		return resultValue;
	}

	/**
	 * @param resultValue the resultValue to set
	 */
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

	/**
	 * @return the resultReason
	 */
	public String getResultReason() {
		return resultReason;
	}

	/**
	 * @param resultReason the resultReason to set
	 */
	public void setResultReason(String resultReason) {
		this.resultReason = resultReason;
	}

	/**
	 * 获得整形值
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public int getJsonIntData(JSONObject json, String key) {
		Integer data = 0;
		if (json != null) {
			Object object = getJsonData(json, key);
			if (object != null) {
				data = (Integer) object;
			}
		}
		return data;
	}

	/**
	 * 获得字符串值
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public String getJsonStringData(JSONObject json, String key) {
		String data = "";
		if (json != null) {
			Object object = getJsonData(json, key);
			if (object != null) {
				data = (String) object;
			}
		}
		return data;
	}
	
	/**
	 * 获得JsonArray
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public JSONArray getJsonArrayData(JSONObject json, String key) {
		JSONArray data = null;
		if (json != null) {
			Object object = getJsonData(json, key);
			if (object != null) {
				data = (JSONArray) object;
			}
		}
		return data;
	}

	/**
	 * 根据key获得对应的值
	 * 
	 * 排除key字母大小写不一致问题
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public Object getJsonData(JSONObject json, String key) {
		Object obj = null;
		if (json != null) {
			obj = json.opt(key);
			if (obj == null) {
				String newKey = getNewKey(json, key);
				if (newKey != null && newKey.length() > 0) {
					obj = getJsonData(json, newKey);
				}
			}
		}
		return obj;
	}

	/***
	 * 获得新的key
	 * 
	 * @param key
	 * @param json
	 * @return
	 */
	private String getNewKey(JSONObject json, String key) {
		if (json == null || key == null) {
			return null;
		}
		Iterator<String> datas = json.keys();
		String newKey = "";
		String oldKey = key.toUpperCase();
		while (datas.hasNext()) {
			newKey = datas.next();
			if (newKey.toUpperCase().equalsIgnoreCase(oldKey)) {
				return newKey;
			}
		}
		return null;
	}
}
