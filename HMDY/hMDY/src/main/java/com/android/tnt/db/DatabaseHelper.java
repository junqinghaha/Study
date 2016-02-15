package com.android.tnt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.tnt.config.Constants;
import com.android.tnt.config.PathConstant;
import com.android.tnt.db.columns.HuoDanColumns;
import com.utils.log.MLog;

import java.io.File;

/**
 * 数据库操作
 * 
 * @author TNT
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String TAG = Constants.TAG + "-DatabaseHelper";

	public static final int DATABASE_VERSION = 11;

	private static DatabaseHelper mDbHelper = null;

	public static DatabaseHelper getInstance(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DatabaseHelper(context);
		}
		return mDbHelper;
	}

	public DatabaseHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, PathConstant.DatabasePath + File.separator
				+ Database.DATABASE_NAME, null, DATABASE_VERSION);
		MLog.d(TAG, PathConstant.DatabasePath + File.separator
				+ Database.DATABASE_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		//货单表
		db.execSQL("create table if not exists " + Database.HD_TABLE + " ("
				+ HuoDanColumns._ID + " integer primary key autoincrement,"
				+ HuoDanColumns.ID + " varchar(64),"
				+ HuoDanColumns.THDH + " varchar(64),"
				+ HuoDanColumns.THDH_SHOW + " varchar(64),"
				+ HuoDanColumns.DESTINATION + " varchar(128),"
				+ HuoDanColumns.CUSTOMER + " varchar(64),"
				+ HuoDanColumns.CUSTOMER_ID + " varchar(64),"
				+ HuoDanColumns.ORIGIN + " varchar(128),"
				+ HuoDanColumns.DATE_INFO + " date,"
				+ HuoDanColumns.SEND_DATE + " date,"
				+ HuoDanColumns.NUMBER + " integer,"
				+ HuoDanColumns.ADDRESS_DETAILS + " varchar(256),"
				+ HuoDanColumns.CONSIGNEE + " varchar(64),"
				+ HuoDanColumns.SERVICE_TYPE + " varchar(64),"
				+ HuoDanColumns.START_PHONE + " varchar(64),"
				+ HuoDanColumns.END_PHONE + " varchar(64),"
				+ HuoDanColumns.SOURCE + " varchar(64),"
				+ HuoDanColumns.STATE + " varchar(64));");
		db.setVersion(DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists " + Database.HD_TABLE);
		onCreate(db);
	}
}
