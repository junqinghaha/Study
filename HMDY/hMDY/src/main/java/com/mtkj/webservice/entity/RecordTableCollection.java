package com.mtkj.webservice.entity;

import java.util.ArrayList;
import java.util.List;

public class RecordTableCollection {
	public String NetID;// { get; set; }

	private List<RecordTable> RecordTables = new ArrayList<RecordTable>();

	public String getNetID() {
		return NetID;
	}

	public void setNetID(String netID) {
		NetID = netID;
	}

	public List<RecordTable> getRecordTables() {
		return RecordTables;
	}

	public void setRecordTables(List<RecordTable> RecordTables) {
		this.RecordTables = RecordTables;
	}

}
