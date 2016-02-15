package com.android.uitils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.text.format.Time;

/**
 * 日期管理控件
 * 
 * @author TNT
 * 
 */
public class MDateUtils {



	/***
	 * gps格式
	 */
	public static String GPS_FAMATE = "yyyy-MM-dd HH:mm:ss";

	/***
	 * 文件命名
	 */
	public static String NAME_FAMATE = "yyyyMMddHHmmss";
	/***
	 * 文件命名
	 */
	public static String NAME_FAMATE_2 = "yyyyMMdd_HHmmss";
	/***
	 * 文件命名
	 */
	public static String NAME_FAMATE_3 = "yyyy年MM月dd-HH时mm分ss秒";
	
	public static String FILE_NAME_FORMAT = "yyyyMMddHHmmss";
	public static String FILE_NAME_FORMAT2 = "yyyyMMddHHmm";

	
	/**
	 * 常用格式1：yyyyMMddHHmmss 常用来作为名称(符合命名规则)
	 */
	public static final String FORMAT_NORMAL = "yyyyMMddHHmmss";
	/** GPS时间格式 yyyy-MM-dd HH:mm:ss **/
	public static final String FORMAT_GPS = "yyyy-MM-dd HH:mm:ss";
	/** 格式yyyy/MM/dd HH:mm:ss**/
	public static final String FORMAT_DATE_1 = "yyyy/MM/dd HH:mm:ss";
	/** 格式 yyyy/MM/dd**/
	public static final String FORMAT_DATE_2 = "yyyy/MM/dd";
	/** 格式 年月日**/
	public static final String FORMAT_DATE_3 = "yyyyMMdd";
	/** 格式 年月日**/
	public static final String FORMAT_DATE_4 = "yyyy-MM-dd";
	/** 格式 年月日 时：分：秒**/
	public static final String FORMAT_CN_1 = "yyyy年MM月dd日 HH:mm:ss";
	/** 格式 年月日**/
	public static final String FORMAT_CN_2 = "yyyy年MM月dd日";

	/**
	 * 
	 * @param datestr
	 *            日期字符串 yyyy-MM-dd
	 * @param day
	 *            相对天数，为正数表示之后，为负数表示之前
	 * @return 指定日期字符串n天之前或者之后的日期
	 */
	public static java.sql.Date GetBeforeAfterDate(String datestr, int day) {
		return GetBeforeAfterDate("yyyy-MM-dd", datestr, day);
	}

	/**
	 * 
	 * @param datestr
	 *            日期字符串 yyyy-MM-dd
	 * @param day
	 *            相对天数，为正数表示之后，为负数表示之前
	 * @return 指定日期字符串n天之前或者之后的日期
	 */
	public static java.sql.Date GetBeforeAfterDate(String format,
			String datestr, int day) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		java.sql.Date olddate = null;
		try {
			df.setLenient(false);
			olddate = new java.sql.Date(df.parse(datestr).getTime());
		} catch (ParseException e) {
			throw new RuntimeException("日期转换错误");
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(olddate);

		int Year = cal.get(Calendar.YEAR);
		int Month = cal.get(Calendar.MONTH);
		int Day = cal.get(Calendar.DAY_OF_MONTH);

		int NewDay = Day + day;

		cal.set(Calendar.YEAR, Year);
		cal.set(Calendar.MONTH, Month);
		cal.set(Calendar.DAY_OF_MONTH, NewDay);

		return new java.sql.Date(cal.getTimeInMillis());
	}
	
	/**
	 * 
	 * @param datestr
	 *            日期字符串 yyyy-MM-dd
	 * @param day
	 *            相对天数，为正数表示之后，为负数表示之前
	 * @return 指定日期字符串n天之前或者之后的日期
	 */
	public static Date GetTheDate(String format,
			String datestr) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			date = new java.util.Date(df.parse(datestr).getTime());
		} catch (ParseException e) {
			throw new RuntimeException("日期转换错误");
		}
		return date;
	}

	/**
	 * 
	 * @param startDate
	 *            日期毫秒数
	 * @param day
	 *            相对天数，为正数表示之后，为负数表示之前
	 * @return 指定日期字符串n天之前或者之后的日期毫秒数
	 */
	public static long GetBeforeAfterDate(long startDate, int day) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(startDate);

		int Year = cal.get(Calendar.YEAR);
		int Month = cal.get(Calendar.MONTH);
		int Day = cal.get(Calendar.DAY_OF_MONTH);

		int NewDay = Day + day;

		cal.set(Calendar.YEAR, Year);
		cal.set(Calendar.MONTH, Month);
		cal.set(Calendar.DAY_OF_MONTH, NewDay);

		return cal.getTimeInMillis();
	}

	/**
	 * 获得当前格式化的时间
	 * 
	 * @param format
	 *            时间格式,例如:%y%M%d%H%M%S
	 * @return
	 */
	public static String GetCurrentFormatTime(String format) {
		Time time = new Time();
		time.setToNow();
		return GetCurrentFormatTime(time.toMillis(false), format);
	}

	/**
	 * 获得当前格式化的时间
	 * 
	 * @param format
	 *            时间格式,例如:%y%m%d%H%M%S
	 * @return
	 */
	public static String GetCurrentFormatTime(Long longTime, String format) {
		if (longTime > 0) {
			return new SimpleDateFormat(format).format(longTime);
		} else {
			return "";
		}
	}

	/**
	 * 毫秒时间转字符串
	 * 
	 * @param timeStemp
	 * @return
	 */
	public static long FormateStringTimeToLong(String strDate,
			String formatString) {
		Date date = null;
		long dateLong = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null) {
			dateLong = date.getTime();
		}
		return dateLong;
	}
	

	/***
	 * 日期格式化
	 * 
	 * @param myDate
	 * @param fromatString
	 * @return
	 */
	public static String dateFormat(Date myDate, String fromatString) {
		return new SimpleDateFormat(fromatString).format(myDate);
	}

	/***
	 * 获得当前时间
	 */
	public static String getCurrentUtcTime() {
		Date l_datetime = new Date();
		//		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		DateFormat formatter = new SimpleDateFormat("HHmmss");
		TimeZone l_timezone = TimeZone.getTimeZone("GMT-0");
		formatter.setTimeZone(l_timezone);
		// String l_utc_date = formatter.format(l_datetime);
		// System.out.println(l_utc_date + " (Local)");
		return formatter.format(l_datetime);
	}

}
