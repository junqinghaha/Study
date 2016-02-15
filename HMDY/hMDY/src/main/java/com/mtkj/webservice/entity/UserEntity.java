package com.mtkj.webservice.entity;

public class UserEntity {

	private String UserID;
	private String UserName;
	private String StationID;
	private String StationName;

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

	public String getStationID() {
		return StationID;
	}

	public void setStationID(String stationID) {
		StationID = stationID;
	}

	public String getStationName() {
		return StationName;
	}

	public void setStationName(String stationName) {
		StationName = stationName;
	}

}
