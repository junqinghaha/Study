package com.mtkj.webservice.entity;

import java.util.ArrayList;
import java.util.List;


public class RecordEntity {

	public String GUID;// { get; set; }
	public double X;// { get; set; }
	public double Y;// { get; set; }

	private List<FieldEntity> Fields = new ArrayList<FieldEntity>();

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public List<FieldEntity> getFields() {
		return Fields;
	}

	public void setFields(List<FieldEntity> Fields) {
		this.Fields = Fields;
	}

}
