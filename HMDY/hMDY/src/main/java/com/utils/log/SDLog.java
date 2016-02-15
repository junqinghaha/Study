package com.utils.log;

/**
 * SDCard日志写入
 * 
 * @author TNT
 * 
 */
public class SDLog {

	private static SDWriter writer = SDWriter.GetInstance();

	/**
	 * 一般信息
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		writer.WriteTxtLog("{i}$$" + tag + "&&" + msg);
	}
	
	/**
	 * 用户界面交互信息
	 * @param tag
	 * @param msg
	 */
	public static void u(String tag, String msg) {
		writer.WriteTxtLog("{u}$$" + tag + "&&" + msg);
	}

	/**
	 * 警示信息
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		writer.WriteTxtLog("{w}$$" + tag + "&&" + msg);
	}

	/**
	 * 错误信息
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		writer.WriteTxtLog("{e}$$" + tag + "&&" + msg);
	}

	/**
	 * 调试信息
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		writer.WriteTxtLog("{d}$$" + tag + "&&" + msg);
	}

	public static void v(String tag, String msg) {
		writer.WriteTxtLog("{v}$$" + tag + "&&" + msg);
	}

}
