package com.mtkj.webservice.entity;

import java.util.ArrayList;
import java.util.List;

public class RecordTable {

	public String TableName;// { get; set; }

	private List<RecordEntity> Records = new ArrayList<RecordEntity>();

	public String getTableName() {
		return TableName;
	}

	public void setTableName(String tableName) {
		TableName = tableName;
	}

	public List<RecordEntity> getRecords() {
		return Records;
	}

	public void setRecords(List<RecordEntity> Records) {
		this.Records = Records;
	}
}
