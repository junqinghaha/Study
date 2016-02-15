package com.android.tnt.db.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.android.tnt.db.DBM;
import com.android.tnt.db.Database;
import com.android.tnt.db.columns.HuoDanColumns;
import com.android.tnt.db.entity.HuoDanEntity;
import com.android.uitils.MDateUtils;

/***
 * 货单数据管理
 * 
 * @author TNT
 * 
 */
public class HuoDanDBManager extends DBM {

	public HuoDanDBManager(Context context) {
		super(context, Database.HD_TABLE);
	}

	public String[][] getAllShowDatas(List<String> lstIDs) {
		return cursorToStringArr(query(null, null, null, null, null, HuoDanColumns._ID + " asc"), lstIDs);
	}

	public String[][] getAllShowDatasWithFliter(String[] arrFileds, String[] arrValues, String dateField, String minDate, String maxDate, List<String> lstIDs) throws Exception{
		return cursorToStringArr(query(null, getFilterSql(arrFileds, arrValues, dateField, minDate, maxDate), null, null, null, HuoDanColumns._ID + " asc"), lstIDs);
	}

	private String getFilterSql(String[] arrFileds, String[] arrValues, String dateField, String minDate, String maxDate) {
		String filter = "";
		for (int i = 0; i < arrFileds.length; i++) {
			if (!TextUtils.isEmpty(arrFileds[i])) {
				String value = arrValues[i];
				if(!TextUtils.isEmpty(value)){
					if(value.endsWith(",")){
						value = value.substring(0, value.length()-1);
					}
				}
				if(!TextUtils.isEmpty(value)){
					if(value.contains(",")){
						value = value.replaceAll(",", "','");
					}
					filter += arrFileds[i] + " IN('" + value + "') and ";
				}
			}
		}
		if (!TextUtils.isEmpty(dateField) && !TextUtils.isEmpty(minDate) && !TextUtils.isEmpty(maxDate)) {
			filter += dateField + ">='" + minDate + "' and " + dateField + "<='" + maxDate + "'";
		} else {
			filter = filter.substring(0, filter.length() - 5);
		}
		return filter;
	}

	public HuoDanEntity getEntity(String id) {
		List<HuoDanEntity> lstData = cursorToEntity(query(null, HuoDanColumns.ID + " = ?", new String[] { id }, null, null, null));
		if (lstData != null && lstData.size() > 0) {
			return lstData.get(0);
		}
		return null;
	}

	/**
	 * 获得分组数据
	 * 
	 * @param field
	 * @return
	 */
	public String[] getGroupData(String field) {
		List<String> lstDatas = new ArrayList<String>();
		if (field != null) {
			if(field.equalsIgnoreCase(HuoDanColumns.STATE)){
				lstDatas.add(HuoDanColumns.STATE_VALUE.STATE_UNDO);
				lstDatas.add(HuoDanColumns.STATE_VALUE.STATE_DONE);
			}else{
				Cursor cursor = query(new String[] { field }, null, null, field, null, HuoDanColumns._ID + " desc");
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					do {
						String data = cursor.getString(cursor.getColumnIndex(field));
						if (!TextUtils.isEmpty(data)) {
							lstDatas.add(data);
						}
					} while (cursor.moveToNext());
					
					cursor.close();
				}
			}
		}
		String[] arrResult = new String[lstDatas.size()];
		return lstDatas.toArray(arrResult);
	}

	// 写入
	public void insertOneHuoDan(List<HuoDanEntity> lstDatas) {
		if (lstDatas != null) {
			for (HuoDanEntity entity : lstDatas) {
				insertOneHuoDan(entity);
			}
		}
	}

	// 写入
	public boolean insertOneHuoDan(HuoDanEntity entity) {
		boolean bRt = false;
		if (entity != null) {
			ContentValues values = new ContentValues();
			values.put(HuoDanColumns.ID, entity.getID());
			values.put(HuoDanColumns.THDH, entity.getCode());
			// 设置code1
			if (TextUtils.isEmpty(entity.getCode1())) {
				entity.setCode1(entity.getCode());
			}
			values.put(HuoDanColumns.THDH_SHOW, entity.getCode1());
			values.put(HuoDanColumns.ADDRESS_DETAILS, entity.getAddress());
			values.put(HuoDanColumns.CONSIGNEE, entity.getPerson());
			if(entity.getDate() != null){
				values.put(HuoDanColumns.DATE_INFO, MDateUtils.dateFormat(entity.getDate(), MDateUtils.FORMAT_GPS));
			}else{
				values.put(HuoDanColumns.DATE_INFO, MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_GPS));
			}
			if(entity.getSendDate() != null){
				values.put(HuoDanColumns.SEND_DATE, MDateUtils.dateFormat(entity.getSendDate(), MDateUtils.FORMAT_GPS));
			}else{
				values.put(HuoDanColumns.SEND_DATE, MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_GPS));
			}
			values.put(HuoDanColumns.DESTINATION, entity.getEndStation());
			values.put(HuoDanColumns.NUMBER, entity.getNum());
			values.put(HuoDanColumns.CUSTOMER, entity.getCustomName());
			values.put(HuoDanColumns.CUSTOMER_ID, entity.getCustomerID());
			values.put(HuoDanColumns.ORIGIN, entity.getStartStation());
			values.put(HuoDanColumns.SERVICE_TYPE, entity.getServiceType());
			if (entity.getStatus().toLowerCase().equalsIgnoreCase(HuoDanColumns.STATE_VALUE.STATE_UNDO_VALUE)) {
				values.put(HuoDanColumns.STATE, HuoDanColumns.STATE_VALUE.STATE_UNDO);
			} else if (entity.getStatus().toLowerCase().equalsIgnoreCase(HuoDanColumns.STATE_VALUE.STATE_DONE_VALUE)) {
				values.put(HuoDanColumns.STATE, HuoDanColumns.STATE_VALUE.STATE_DONE);
			} else {
				values.put(HuoDanColumns.STATE, entity.getStatus());
			}
			if (!isExist(HuoDanColumns.ID, entity.getID())) {
				if (insert(null, values) >= 0) {
					bRt = true;
				}
			} else {
				if (update(values, HuoDanColumns.ID, entity.getID()) >= 0) {
					bRt = true;
				}
			}
		}
		return bRt;
	}

	// 是否存在
	public boolean isExsits(String huoDanNumber) {
		return isExist(HuoDanColumns.THDH, huoDanNumber);
	}

	// 删除
	public int deleteEntity(String[] codes) {
		int nCount = 0;
		if(codes != null && codes.length > 0){
			for(String id:codes){
				if(deleteEntity(id)){
					nCount++;
				}
			}
		}
		return nCount;
	}
	
	// 删除
	public boolean deleteEntity(String code) {
		boolean bRt = false;
		if (code != null) {
			if (isExsits(code)) {
				if (delete(HuoDanColumns.THDH, code) >= 0) {
					bRt = true;
				}
			}
		}
		return bRt;
	}
	
	// 删除
	public boolean deleteEntity(HuoDanEntity entity) {
		boolean bRt = false;
		if (entity != null) {
			if (isExsits(entity.getCode())) {
				if (delete(HuoDanColumns.THDH, entity.getCode()) >= 0) {
					bRt = true;
				}
			}
		}
		return bRt;
	}

	// 更新
	public int updateEntity(HuoDanEntity entity) {
		return 0;
	}

	// 更新
	public int updateEntityStatus(String code, String status) {
		ContentValues values = new ContentValues();
		values.put(HuoDanColumns.STATE, status);
		return update(values, HuoDanColumns.THDH + "=" + code);
	}

	public List<HuoDanEntity> cursorToEntity(Cursor cursor) {
		List<HuoDanEntity> lstDatas = new ArrayList<HuoDanEntity>();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				HuoDanEntity entity = new HuoDanEntity();
				entity.setAddress(cursor.getString(cursor.getColumnIndex(HuoDanColumns.ADDRESS_DETAILS)));
				entity.setPerson(cursor.getString(cursor.getColumnIndex(HuoDanColumns.CONSIGNEE)));
				String dataTimeString = cursor.getString(cursor.getColumnIndex(HuoDanColumns.DATE_INFO));
				if (!TextUtils.isEmpty(dataTimeString)) {
					SimpleDateFormat format = new SimpleDateFormat(MDateUtils.FORMAT_DATE_4);
					Date date = null;
					try {
						date = format.parse(dataTimeString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					entity.setDate(date);
				}
				dataTimeString = cursor.getString(cursor.getColumnIndex(HuoDanColumns.SEND_DATE));
				if (!TextUtils.isEmpty(dataTimeString)) {
					SimpleDateFormat format = new SimpleDateFormat(MDateUtils.FORMAT_DATE_4);
					Date date = null;
					try {
						date = format.parse(dataTimeString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					entity.setSendDate(date);
				}
				entity.setEndStation(cursor.getString(cursor.getColumnIndex(HuoDanColumns.DESTINATION)));
				entity.setNum(cursor.getInt(cursor.getColumnIndex(HuoDanColumns.NUMBER)));
				entity.setStartStation(cursor.getString(cursor.getColumnIndex(HuoDanColumns.ORIGIN)));
				entity.setServiceType(cursor.getString(cursor.getColumnIndex(HuoDanColumns.SERVICE_TYPE)));
				entity.setCustomName(cursor.getString(cursor.getColumnIndex(HuoDanColumns.CUSTOMER)));
				entity.setCustomerID(cursor.getString(cursor.getColumnIndex(HuoDanColumns.CUSTOMER_ID)));
				entity.setServiceType(cursor.getString(cursor.getColumnIndex(HuoDanColumns.SERVICE_TYPE)));
				entity.setCode(cursor.getString(cursor.getColumnIndex(HuoDanColumns.THDH)));
				entity.setCode1(cursor.getString(cursor.getColumnIndex(HuoDanColumns.THDH_SHOW)));
				entity.setID(cursor.getString(cursor.getColumnIndex(HuoDanColumns.ID)));
				lstDatas.add(entity);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return lstDatas;
	}

	public String[][] entityToStringArr(List<HuoDanEntity> lstDatas) {
		String[][] arrDatas = null;
		if (lstDatas != null && lstDatas.size() > 0) {
			arrDatas = new String[lstDatas.size() + 1][HuoDanColumns.ALL_FILED_COUNT];
			// 设置首行
			for (int k = 0; k < HuoDanColumns.ALL_FILED_COUNT; k++) {
				arrDatas[0][k] = HuoDanColumns.AllFieldAlias[k];
			}
			// 内容
			for (int i = 0; i < lstDatas.size(); i++) {
				arrDatas[i + 1][0] = lstDatas.get(i).getCode();
				arrDatas[i + 1][1] = lstDatas.get(i).getEndStation();
				arrDatas[i + 1][2] = lstDatas.get(i).getPerson();
				arrDatas[i + 1][3] = lstDatas.get(i).getNum() + "";
				arrDatas[i + 1][4] = lstDatas.get(i).getServiceType();
				arrDatas[i + 1][5] = lstDatas.get(i).getAddress();
				arrDatas[i + 1][6] = MDateUtils.dateFormat(lstDatas.get(i).getDate(), MDateUtils.FORMAT_DATE_4);
				arrDatas[i + 1][7] = lstDatas.get(i).getStartStation();
			}
		} else {
			arrDatas = new String[1][HuoDanColumns.ALL_FILED_COUNT];
			// 设置首行
			for (int k = 0; k < HuoDanColumns.ALL_FILED_COUNT; k++) {
				arrDatas[0][k] = HuoDanColumns.AllFieldAlias[k];
			}
		}
		return arrDatas;
	}

	public String[][] cursorToStringArr(Cursor cursor, List<String> lstIds) {
		String[][] arrDatas = null;
		if (cursor != null && cursor.getCount() > 0) {
			if (lstIds == null) {
				lstIds = new ArrayList<String>();
			}
			lstIds.clear();

			arrDatas = new String[cursor.getCount() + 1][HuoDanColumns.ALL_FILED_COUNT];
			// 设置首行
			for (int k = 0; k < HuoDanColumns.ALL_FILED_COUNT; k++) {
				arrDatas[0][k] = HuoDanColumns.AllFieldAlias[k];
			}
			// 所有内容
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String id = cursor.getString(cursor.getColumnIndex(HuoDanColumns.ID));
				lstIds.add(id);
				for (int h = 0; h < HuoDanColumns.ALL_FILED_COUNT; h++) {
					if (h != HuoDanColumns.DATE_FIELD_INDEX) {
						if (h == 0) {
							String value = cursor.getString(cursor.getColumnIndex(HuoDanColumns.AllFields[h]));
							// if (value.length() > 0) {
							// value = value.substring(0, 7);
							// }
							if (!TextUtils.isEmpty(value)) {
								arrDatas[i + 1][h] = value;
							} else {
								arrDatas[i + 1][h] = "";
							}
						} else {
							String value = cursor.getString(cursor.getColumnIndex(HuoDanColumns.AllFields[h]));
							if (!TextUtils.isEmpty(value)) {
								arrDatas[i + 1][h] = value;
							} else {
								arrDatas[i + 1][h] = "";
							}
						}
					} else {
						String dataTimeString = cursor.getString(cursor.getColumnIndex(HuoDanColumns.DATE_INFO));
						if (!TextUtils.isEmpty(dataTimeString)) {
							SimpleDateFormat format = new SimpleDateFormat(MDateUtils.FORMAT_DATE_4);
							Date date = null;
							try {
								date = format.parse(dataTimeString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							arrDatas[i + 1][h] = MDateUtils.dateFormat(date, MDateUtils.FORMAT_DATE_4);
						} else {
							arrDatas[i + 1][h] = "";
						}
					}
				}
			}
			cursor.close();
		} else {
			arrDatas = new String[1][HuoDanColumns.ALL_FILED_COUNT];
			// 设置首行
			for (int k = 0; k < HuoDanColumns.ALL_FILED_COUNT; k++) {
				arrDatas[0][k] = HuoDanColumns.AllFieldAlias[k];
			}
		}
		return arrDatas;
	}

}
