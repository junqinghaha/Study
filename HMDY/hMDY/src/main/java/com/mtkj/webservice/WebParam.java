package com.mtkj.webservice;

/**
 * WebService 参数类，封装webservice调用时需要传入的参数名和参数值
 * @author Napoleon
 *
 */
public class WebParam {

	private String parName = null;
	private Object parValue = null;
	
	public WebParam(){
		
	}
	public WebParam(String name,Object value){
		parName  = name;
		parValue = value;
	}
	
	@Override
	public String toString() {
		return "WebParam [parName=" + parName + ", parValue=" + parValue + "]";
	}
	/**
	 * @return the parName
	 */
	public String getParName() {
		return parName;
	}
	/**
	 * @param parName the parName to set
	 */
	public void setParName(String parName) {
		this.parName = parName;
	}
	/**
	 * @return the parValue
	 */
	public Object getParValue() {
		return parValue;
	}
	/**
	 * @param parValue the parValue to set
	 */
	public void setParValue(Object parValue) {
		this.parValue = parValue;
	}
	
	
}
