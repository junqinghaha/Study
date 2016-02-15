package com.android.tnt.db.entity;

import java.io.Serializable;
import java.util.Date;

import android.text.TextUtils;

import com.android.uitils.MDateUtils;

/***
 * 货单实体
 * 
 * @author TNT
 * 
 */
public class HuoDanEntity implements Serializable {
	/** 提货单号 **/
	public String Code = "";
	/** 提货单号1--用于显示 **/
	public String Code1 = "";
	/** 目的地 **/
	public String EndStation = "";
	/** 目的地 **/
	public String EndStationId = "";
	/** 始发站 **/
	public String StartStation = "";
	/** 始发站 **/
	public String StartStationId = "";
	/** 件数 **/
	public int Num;
	/**  **/
	private String CustomName;
	/**  **/
	private String CustomerID;
	/** 详细地址 **/
	public String Address = "";
	/** 收货人 **/
	public String Person = "";
	/** 服务方式 **/
	public String ServiceType = "";
	/** 发件人联系方式 */
	public String startPhone = "";
	/** 收货人联系方式 */
	public String endPhone = "";
	/** 来源 */
	public String Source = "";
	/** 当前状态 */
	public String Status = "";
	/** 用户ID */
	private String UserID;
	/** 用户名称 */
	private String UserName;
	/** 发送日期 */
	private String SendDate;
	/** 日期 */
	private String Date;
	/** 发送日期 */
	private Date SendDate1;
	/** 日期 */
	private Date Date1;
	/** 唯一编号 */
	private String ID;

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getEndStation() {
		return EndStation;
	}

	public void setEndStation(String endStation) {
		EndStation = endStation;
	}

	public String getEndStationId() {
		return EndStationId;
	}

	public void setEndStationId(String endStationId) {
		EndStationId = endStationId;
	}

	public String getStartStationId() {
		return StartStationId;
	}

	public void setStartStationId(String startStationId) {
		StartStationId = startStationId;
	}

	public String getStartStation() {
		return StartStation;
	}

	public void setStartStation(String startStation) {
		StartStation = startStation;
	}

	public int getNum() {
		return Num;
	}

	public void setNum(int num) {
		Num = num;
	}

	public String getCustomName() {
		return CustomName;
	}

	public void setCustomName(String customName) {
		CustomName = customName;
	}

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPerson() {
		return Person;
	}

	public void setPerson(String person) {
		Person = person;
	}

	public String getServiceType() {
		return ServiceType;
	}

	public void setServiceType(String serviceType) {
		ServiceType = serviceType;
	}

	public String getStartPhone() {
		return startPhone;
	}

	public void setStartPhone(String startPhone) {
		this.startPhone = startPhone;
	}

	public String getEndPhone() {
		return endPhone;
	}

	public void setEndPhone(String endPhone) {
		this.endPhone = endPhone;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public Date getSendDate() {
		if(SendDate1 != null){
			return SendDate1;
		}else if(!TextUtils.isEmpty(SendDate)){
			SendDate1 = MDateUtils.GetTheDate(MDateUtils.FORMAT_DATE_1, SendDate);
		}
		return SendDate1;
	}

	public void setSendDate(Date sendDate) {
		SendDate1 = sendDate;
	}

	public void setSendDate(String sendDate) {
		this.SendDate = sendDate;
		SendDate1 = MDateUtils.GetTheDate(MDateUtils.FORMAT_DATE_1, sendDate);
	}

	public Date getDate() {
		if(Date1 != null){
			return Date1;
		}else if(!TextUtils.isEmpty(Date)){
			Date1 = MDateUtils.GetTheDate(MDateUtils.FORMAT_DATE_1, SendDate);
		}
		return Date1;
	}

	public void setDate(Date date) {
		this.Date1 = date;
	}

	public void setDate(String date) {
		this.Date = date;
		this.Date1 = MDateUtils.GetTheDate(MDateUtils.FORMAT_DATE_1, date);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getCode1() {
		return Code1;
	}

	public void setCode1(String code1) {
		Code1 = code1;
	}

}
