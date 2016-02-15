package com.android.tnt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.tnt.config.Constants;

/**
 * 职能：实现数据操作的基本方法，主要作为父类，子类实现具体的数据增查改
 * 
 * @author TNT
 * 
 */
public class DBM {
	public final static String TAG = Constants.TAG + "-DBM";
	// 数据库助手对象
	protected DatabaseHelper mDbHelper = null;
	// 当前操作的数据表
	protected String tableName = null;

	public DBM(Context context, String table) {
		mDbHelper = DatabaseHelper.getInstance(context);
		this.tableName = table;
		if (table == null) {
			throw new RuntimeException("table should not be null");
		}
	}

	/**
	 * 获得数据表名
	 * 
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 增加一条数据
	 * 
	 * @param nullColumnHack
	 * @param values
	 * @return 插入的行id
	 */
	public long insert(String nullColumnHack, ContentValues values) {
		return mDbHelper.getWritableDatabase().insert(tableName,
				nullColumnHack, values);
	}

	/**
	 * 清空记录
	 */
	public void clear() {
		mDbHelper.getWritableDatabase().execSQL(
				"delete from '" + tableName + "'");
	}

	/**
	 * 删除一条记录
	 * 
	 * @param whereClause
	 *            删除条件，传入null则删除所有记录
	 * @param whereArgs
	 *            删除条件中的参数
	 * @return 删除的记录数
	 */
	public int delete(String whereClause, String[] whereArgs) {
		return mDbHelper.getWritableDatabase().delete(tableName, whereClause,
				whereArgs);
	}

	/**
	 * 删除一条记录
	 * 
	 * @param whereClause
	 *            删除条件，传入null则删除所有记录
	 * @param whereArgs
	 *            删除条件中的参数
	 * @return 删除的记录数
	 */
	public int delete(String[] fields, String[] fieldValues) {
		String whereClause = getWhereClauseTypeAnd(fields);
		return delete(whereClause, fieldValues);
	}

	/**
	 * 删除一条记录
	 * 
	 * @param whereClause
	 *            删除条件，传入null则删除所有记录
	 * @param whereArgs
	 *            删除条件中的参数
	 * @return 删除的记录数
	 */
	public int delete(String field, String fieldValue) {
		return delete(new String[] { field }, new String[] { fieldValue });
	}

	/**
	 * 是否为空表
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		Cursor cursor = query();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * 查询全部
	 * 
	 * @return
	 */
	public Cursor query() {
		return mDbHelper.getReadableDatabase().query(tableName, null, null,
				null, null, null, null);
	}

	/**
	 * 查询
	 * 
	 * @param fields
	 * @param fieldValues
	 * @return
	 */
	public Cursor query(String[] columns, String[] fields, String[] fieldValues) {
		String whereClause = getWhereClauseTypeAnd(fields);
		return query(columns, whereClause, fieldValues, null, null, null);
	}

	/**
	 * 查询
	 * 
	 * @param fields
	 * @param fieldValues
	 * @return
	 */
	public Cursor query(String[] fields, String[] fieldValues) {
		String whereClause = getWhereClauseTypeAnd(fields);
		return query(null, whereClause, fieldValues, null, null, null);
	}

	/**
	 * 查询
	 * 
	 * @param fields
	 * @param fieldValues
	 * @return
	 */
	public Cursor query(String[] fields, String[] fieldValues,
			String orderFileds) {
		String whereClause = getWhereClauseTypeAnd(fields);
		return query(null, whereClause, fieldValues, null, null, orderFileds);
	}

	/**
	 * 有条件的查询
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return Cursor
	 */
	public Cursor query(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		return mDbHelper.getReadableDatabase().query(tableName, columns,
				selection, selectionArgs, groupBy, having, orderBy);

	}

	/**
	 * 
	 * @param sql
	 *            查询语句
	 * @param selectionArgs
	 *            查询语句参数
	 * @return Cursor 查询返回的数据
	 */
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return mDbHelper.getReadableDatabase().rawQuery(sql, selectionArgs);
	}

	/**
	 * 更新记录
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return 更新记录数
	 */
	public int update(ContentValues values, String whereClause,
			String[] whereArgs) {
		return mDbHelper.getWritableDatabase().update(tableName, values,
				whereClause, whereArgs);

	}
	
	/**
	 * 更新记录
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return 更新记录数
	 */
	public int update(ContentValues values, String whereClause) {
		return mDbHelper.getWritableDatabase().update(tableName, values,
				whereClause, null);
		
	}

	/**
	 * 更新记录
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return 更新记录数
	 */
	public int update(ContentValues values, String field, String fieldValue) {
		String whereClause = field + "=?";
		return mDbHelper.getWritableDatabase().update(tableName, values,
				whereClause, new String[] { fieldValue });

	}

	/**
	 * 更新记录
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return 更新记录数
	 */
	public int update(ContentValues values, String[] fields,
			String[] fieldValues) {
		String whereClause = getWhereClauseTypeAnd(fields);
		return mDbHelper.getWritableDatabase().update(tableName, values,
				whereClause, fieldValues);

	}

	/**
	 * 是否存在
	 * 
	 * @param standardID
	 * @return
	 */
	public boolean isExist(String[] fields, String[] values) {
		boolean bRt = false;
		String querySql = getWhereClauseTypeAnd(fields);
		Cursor cursor = query(null, querySql, values, null, null, null);
		if (cursor.getCount() > 0) {
			bRt = true;
		}
		cursor.close();
		return bRt;
	}

	/**
	 * 是否存在
	 * 
	 * @param standardID
	 * @return
	 */
	public boolean isExist(String field, String value) {
		boolean bRt = false;
		Cursor cursor = query(null, field + "=?", new String[] { value }, null,
				null, null);
		if (cursor.getCount() > 0) {
			cursor.close();
			bRt = true;
		}
		return bRt;
	}

	public void beginTransaction() {
		mDbHelper.getWritableDatabase().beginTransaction();
	}

	public void setTransactionSuccessful() {
		mDbHelper.getWritableDatabase().setTransactionSuccessful();
	}

	public void endTransaction() {
		mDbHelper.getWritableDatabase().endTransaction();
	}

	protected String getWhereClauseTypeAnd(String[] fields) {
		String whereClause = "";
		for (String field : fields) {
			whereClause += field + "=? and ";
		}
		return whereClause.trim().substring(0, whereClause.length() - 4);
	}

	private String getWhereClauseTypeOr(String[] fields) {
		String whereClause = "";
		for (String field : fields) {
			whereClause += field + "=? or ";
		}
		return whereClause.trim().substring(0, whereClause.length() - 4);
	}

}
