package com.android.tnt.db.columns;

import android.provider.BaseColumns;

/***
 * 货单表
 * 
 * @author TNT
 * 
 */
public class HuoDanColumns implements BaseColumns {

	public static final int ALL_FILED_COUNT = 9;
	public static final int DATE_FIELD_INDEX = 6;

	public interface SOURCE_VALUE {
		public String SOURCE_PHONE = "手机下单";
		public String SOURCE_MYSELF = "自取";
	}

	public interface STATE_VALUE {
		public String STATE_UNDO = "未取件";
		public String STATE_DONE = "已取件";
		public String STATE_UNDO_VALUE = "send";
		public String STATE_DONE_VALUE = "over";
	}

	/** 提货单号 **/
	public static final String THDH = "THDH";
	/** 用于显示的单号 **/
	public static final String THDH_SHOW = "THDH_SHOW";
	/** ID-- 唯一编号 */
	public static final String ID = "ID";
	/** 目的地 **/
	public static final String DESTINATION = "DESTINATION";
	/** 客户 **/
	public static final String CUSTOMER = "CUSTOMER";
	/** 客户编号 **/
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	/** 始发站 **/
	public static final String ORIGIN = "ORIGIN";
	/** 日期 **/
	public static final String DATE_INFO = "DATE_INFO";
	/** 日期 **/
	public static final String SEND_DATE = "SEND_DATE";

	/** 件数 **/
	public static final String NUMBER = "NUMBER";
	/** 详细地址 **/
	public static final String ADDRESS_DETAILS = "ADDRESS_DETAILS";
	/** 收货人 **/
	public static final String CONSIGNEE = "CONSIGNEE";
	/** 服务方式 **/
	public static final String SERVICE_TYPE = "SERVICE_TYPE";
	/** 下单人联系方式 **/
	public static final String START_PHONE = "START_PHONE";
	/** 收货人联系方式 **/
	public static final String END_PHONE = "END_PHONE";
	/** 来源：手机下单、自取 **/
	public static final String SOURCE = "SOURCE";
	/** 当前状态：已取件、未取件 **/
	public static final String STATE = "STATE";

	public static final String[] AllFieldAlias = new String[] { "提货单号", "目的地", "收货人", "件数", "服务方式", "详细地址", "日期", "始发站", "客户", "状态" };
	public static final String[] AllFields = new String[] { THDH_SHOW, DESTINATION, CONSIGNEE, NUMBER, SERVICE_TYPE, ADDRESS_DETAILS, DATE_INFO, ORIGIN, CUSTOMER, STATE };
}
