package com.android.uitils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.tnt.db.entity.HuoDanEntity;

import zpSDK.zpSDK.zpSDK;

/**
 * 
 * @author TNT
 * 
 */
public class PrintUtil {

	private final static double PAGE_HEIGHT = 60;//62.5;

	private final static float FONT_LITTLE = 2.2f;
	private final static float FONT_MENU = 3.0f;
	private final static float FONT_CONTENT = 3.4f;
	private final static float FONT_CONTENT_BIG = 5f;
	private final static float FONT_TITLE = 5f;

	private final static int ROW_1 = 18;
	private final static int ROW_2 = 25;
	private final static int ROW_3 = 33;
	private final static int ROW_4 = 40;
	private final static int ROW_5 = 48;

	private final static int COL_1 = 1;
	private final static int COL_2 = 13;
	private final static int COL_22 = 10;
	private final static int COL_3 = 32;
	private final static int COL_4 = 42;
	private final static int COL_4_2 = 45;

	public static boolean OpenPrinter(Context context, String BDAddress) {

		if (BDAddress == "" || BDAddress == null) {
			Util.MsgBoxLong(context, "没有选择打印机");// , Toast.LENGTH_LONG).show();
			return false;
		}
		BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			Util.MsgBoxLong(context, "读取蓝牙设备错误");// , Toast.LENGTH_LONG).show();
			return false;
		}
		BluetoothDevice myDevice = myBluetoothAdapter.getRemoteDevice(BDAddress);
		if (myDevice == null) {
			Util.MsgBoxLong(context, "读取蓝牙设备错误");// , Toast.LENGTH_LONG).show();
			return false;
		}
		// if(myDevice.getBondState() != BluetoothDevice.BOND_BONDING){
		// Util.MsgBoxLong(context, "请确认设备是否开启");//, Toast.LENGTH_LONG).show();
		// return false;
		// }
		if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
			Util.MsgBoxLong(context, zpSDK.ErrorMessage);// ,
															// Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public static boolean isPrinterOk(Context context, String BDAddress) {

		if (BDAddress == "" || BDAddress == null) {
			Util.MsgBoxLong(context, "没有选择打印机");// , Toast.LENGTH_LONG).show();
			return false;
		}
		BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			Util.MsgBoxLong(context, "读取蓝牙设备错误");// , Toast.LENGTH_LONG).show();
			return false;
		}
		BluetoothDevice myDevice = myBluetoothAdapter.getRemoteDevice(BDAddress);
		if (myDevice == null) {
			Util.MsgBoxLong(context, "读取蓝牙设备错误");// , Toast.LENGTH_LONG).show();
			return false;
		}
		// if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
		// Toast.makeText(context, zpSDK.ErrorMessage,
		// Toast.LENGTH_SHORT).show();
		// Util.MsgBoxLong(context, "请确认打印机链接状态");
		// return false;
		// }
		// if (myDevice.getBondState() != BluetoothDevice.BOND_BONDING) {
		// // Toast.LENGTH_LONG).show();.
		// Util.MsgBoxLong(context, "未发现打印机");// ,
		// // myDevice.connectGatt(context, true, new BluetoothGattCallback() {
		// //
		// // @Override
		// // public void onConnectionStateChange(BluetoothGatt gatt, int
		// status, int newState) {
		// // super.onConnectionStateChange(gatt, status, newState);
		// // if (status == BluetoothGatt.GATT_SUCCESS) {
		// // return true;
		// // }
		// // }
		// //
		// // });
		// return false;
		// }
		return true;
	}

	public static String getPrinterState(String BDAddress) {
		String state = "OK";
		if (BDAddress == "" || BDAddress == null) {
			state = "没有选择打印机";// );// , Toast.LENGTH_LONG).show();
		}
		BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			state = "读取蓝牙设备错误";// , Toast.LENGTH_LONG).show();
		}
		BluetoothDevice myDevice = myBluetoothAdapter.getRemoteDevice(BDAddress);
		if (myDevice == null) {
			state = "读取蓝牙设备错误";// , Toast.LENGTH_LONG).show();
		}
		if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
			state = "请确认打印机链接状态";
		}
		// if (myDevice.getBondState() != BluetoothDevice.BOND_BONDING) {
		// // Toast.LENGTH_LONG).show();.
		// Util.MsgBoxLong(context, "未发现打印机");// ,
		// // myDevice.connectGatt(context, true, new BluetoothGattCallback() {
		// //
		// // @Override
		// // public void onConnectionStateChange(BluetoothGatt gatt, int
		// status, int newState) {
		// // super.onConnectionStateChange(gatt, status, newState);
		// // if (status == BluetoothGatt.GATT_SUCCESS) {
		// // return true;
		// // }
		// // }
		// //
		// // });
		// return false;
		// }
		return state;
	}

	public static void Print(Context context, String BDAddress, HuoDanEntity entity) {
		// statusBox.Show("正在打印...");
		int number = entity.getNum();
		String baseHuoDan = "1xxxx1xxxx";
		if (!TextUtils.isEmpty(entity.getCode())) {
			baseHuoDan = entity.getCode();
		}
		if (baseHuoDan.length() > 10) {
			baseHuoDan = baseHuoDan.substring(0, 10);
		}
		for (int i = 0; i < number; i++) {
			// statusBox.Show("正在打印第" + (i + 1) + "张");

			if (!OpenPrinter(context, BDAddress)) {
				// statusBox.Close();
				return;
			}
			
			//zpSDK.zp_goto_mark_label(80);
			//zpSDK.zp_close();

			if (!zpSDK.zp_page_create(80, PAGE_HEIGHT)) {
				Toast.makeText(context, "创建打印页面失败", Toast.LENGTH_LONG).show();
				// statusBox.Close();
				return;
			}
			zpSDK.TextPosWinStyle = false;
			zpSDK.zp_draw_text_ex(2, 7, "青鸟物流", "黑体", FONT_TITLE, 0, false, false, false);
			zpSDK.zp_draw_barcode(25, 0, baseHuoDan + getIndexValue(i + 1), zpSDK.BARCODE_TYPE.BARCODE_CODE128, 12, 2, 0);
			zpSDK.zp_draw_text_ex(25, 14, baseHuoDan + getIndexValue(i + 1), "黑体", FONT_LITTLE, 0, true, false, false);

			// 第一行
			zpSDK.zp_draw_text_ex(COL_1, ROW_1, "提货单", "黑体", FONT_MENU, 0, false, false, false);
			zpSDK.zp_draw_text_ex(COL_22, ROW_1, baseHuoDan, "黑体", FONT_CONTENT, 0, true, false, true);
			zpSDK.zp_draw_text_ex(COL_3, ROW_1, "目的地", "黑体", FONT_MENU, 0, false, false, false);// 第一行字体;;
			if (!TextUtils.isEmpty(entity.getEndStation())) {
				if (entity.getEndStation().length() <= 6) {
					zpSDK.zp_draw_text_ex(COL_4, ROW_1, entity.getEndStation(), "黑体", FONT_CONTENT_BIG, 0, true, false, true);// 第一行字体;;
				} else if (entity.getEndStation().length() <= 11) {
					String textFirst = entity.getEndStation().substring(0, 6);
					String textSecond = entity.getEndStation().substring(6, entity.getEndStation().length());
					zpSDK.zp_draw_text_ex(COL_4, ROW_1, textFirst, "黑体", FONT_CONTENT_BIG, 0, true, false, true);// 第一行字体;;
					zpSDK.zp_draw_text_ex(COL_4_2, ROW_2, textSecond, "黑体", FONT_CONTENT_BIG, 0, true, false, true);// 第一行字体;;
				} else {
					String textFirst = entity.getEndStation().substring(0, 8);
					String textSecond = entity.getEndStation().substring(8, entity.getEndStation().length());
					zpSDK.zp_draw_text_ex(COL_4, ROW_1, textFirst, "黑体", FONT_CONTENT, 0, true, false, true);// 第一行字体;;
					zpSDK.zp_draw_text_ex(COL_4_2, ROW_2, textSecond, "黑体", FONT_CONTENT, 0, true, false, true);// 第一行字体;;
				}
			}

			// 第二行
			zpSDK.zp_draw_text_ex(COL_1, ROW_2, "始发站", "黑体", FONT_MENU, 0, false, false, false); // 第一行字体;;;
			zpSDK.zp_draw_text_ex(COL_22, ROW_2, entity.getStartStation(), "黑体", FONT_CONTENT, 0, true, false, true);
			// zpSDK.zp_draw_text_ex(40, 25, "日期", "黑体", FONT_MENU, 0, false,
			// false, false);
			// if (entity.getDate1() != null) {
			// zpSDK.zp_draw_text_ex(50, 25,
			// MDateUtils.dateFormat(entity.getDate1(),
			// MDateUtils.FORMAT_DATE_4), "黑体", FONT_CONTENT, 0, true, false,
			// true);
			// } else {
			// zpSDK.zp_draw_text_ex(50, 25,
			// MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4), "黑体",
			// FONT_CONTENT, 0, true, false, true);
			// }

			// 第三行
			zpSDK.zp_draw_text_ex(COL_1, ROW_3, "件数", "黑体", FONT_MENU, 0, false, false, false);
			zpSDK.zp_draw_text_ex(COL_22, ROW_3, entity.getNum() + "-" + (i + 1), "黑体", FONT_CONTENT_BIG, 0, true, false, true);
			zpSDK.zp_draw_text_ex(COL_3, ROW_3, "日期", "黑体", FONT_MENU, 0, false, false, false);
			if (entity.getDate() != null) {
				zpSDK.zp_draw_text_ex(COL_4, ROW_3, MDateUtils.dateFormat(entity.getDate(), MDateUtils.FORMAT_DATE_4), "黑体", FONT_CONTENT, 0, true, false, true);
			} else {
				zpSDK.zp_draw_text_ex(COL_4, ROW_3, MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4), "黑体", FONT_CONTENT, 0, true, false, true);
			}
			// zpSDK.zp_draw_text_ex(40, 33, "收货人", "黑体", FONT_MENU, 0, false,
			// false, false);
			// zpSDK.zp_draw_text_ex(50, 33, entity.getPerson(), "黑体",
			// FONT_CONTENT, 0, true, false, true);

			// 第四行
			zpSDK.zp_draw_text_ex(COL_1, ROW_4, "服务方式:", "黑体", FONT_MENU, 0, false, false, false);
			zpSDK.zp_draw_text_ex(COL_2, ROW_4, entity.getServiceType(), "黑体", FONT_CONTENT, 0, true, false, true);
			zpSDK.zp_draw_text_ex(COL_3, ROW_4, "收货人", "黑体", FONT_MENU, 0, false, false, false);
			zpSDK.zp_draw_text_ex(COL_4, ROW_4, entity.getPerson(), "黑体", FONT_CONTENT, 0, true, false, true);
			// zpSDK.zp_draw_text_ex(0, 40, "详细地址:", "黑体", FONT_MENU, 0, false,
			// false, false);
			// zpSDK.zp_draw_text_ex(14, 40, entity.getAddress(), "黑体",
			// FONT_CONTENT, 0, true, false, true);

			// 第五行
			// zpSDK.zp_draw_text_ex(0, 48, "服务方式:", "黑体", FONT_MENU, 0, false,
			// false, false);
			// zpSDK.zp_draw_text_ex(14, 48, entity.getServiceType(), "黑体",
			// FONT_CONTENT, 0, true, false, true);
			zpSDK.zp_draw_text_ex(COL_1, ROW_5, "详细地址:", "黑体", FONT_MENU, 0, false, false, false);
			zpSDK.zp_draw_text_ex(COL_2, ROW_5, entity.getAddress(), "黑体", FONT_CONTENT, 0, true, false, true);

			zpSDK.zp_page_print(false);
			zpSDK.zp_printer_status_detect();
			// zpSDK.zp_goto_mark_right(150);
			zpSDK.zp_goto_mark_label(150);
			if (zpSDK.zp_printer_status_get(8000) != 0) {
				Toast.makeText(context, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
			}
			zpSDK.zp_page_free();
//			zpSDK.zp_goto_mark_label(150);
			zpSDK.zp_close();
		}
		// toast("打印完成");
		// statusBox.Close();
	}

	public static void PrintOne(Context context, String BDAddress, HuoDanEntity entity, int childIndex) {
		// statusBox.Show("正在打印...");
		int number = entity.getNum();
		String baseHuoDan = "1xxxx1xxxx";
		if (!TextUtils.isEmpty(entity.getCode())) {
			baseHuoDan = entity.getCode();
		}
		if (baseHuoDan.length() > 10) {
			baseHuoDan = baseHuoDan.substring(0, 10);
		}

		int i = childIndex;
		// statusBox.Show("正在打印第" + (i + 1) + "张");

		if (!OpenPrinter(context, BDAddress)) {
			// statusBox.Close();
			return;
		}
		//滚动到标签
		//zpSDK.zp_goto_mark_label(80);

		if (!zpSDK.zp_page_create(80, PAGE_HEIGHT)) {
			Toast.makeText(context, "创建打印页面失败", Toast.LENGTH_LONG).show();
			// statusBox.Close();
			return;
		}
		zpSDK.TextPosWinStyle = false;
		zpSDK.zp_draw_text_ex(2, 7, "青鸟物流", "黑体", FONT_TITLE, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 0, baseHuoDan + getIndexValue(i), zpSDK.BARCODE_TYPE.BARCODE_CODE128, 12, 2, 0);
		zpSDK.zp_draw_text_ex(25, 14, baseHuoDan + getIndexValue(i), "黑体", FONT_LITTLE, 0, true, false, false);

		// 第一行
		zpSDK.zp_draw_text_ex(COL_1, ROW_1, "提货单", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(COL_2, ROW_1, baseHuoDan, "黑体", FONT_CONTENT, 0, true, false, true);
		zpSDK.zp_draw_text_ex(COL_3, ROW_1, "目的地", "黑体", FONT_MENU, 0, false, false, false);// 第一行字体;;
		if (!TextUtils.isEmpty(entity.getEndStation())) {
			if (entity.getEndStation().length() <= 6) {
				zpSDK.zp_draw_text_ex(COL_4, ROW_1, entity.getEndStation(), "黑体", FONT_CONTENT_BIG, 0, true, false, true);// 第一行字体;;
			} else if (entity.getEndStation().length() <= 11) {
				String textFirst = entity.getEndStation().substring(0, 6);
				String textSecond = entity.getEndStation().substring(6, entity.getEndStation().length());
				zpSDK.zp_draw_text_ex(COL_4, ROW_1, textFirst, "黑体", FONT_CONTENT_BIG, 0, true, false, true);// 第一行字体;;
				zpSDK.zp_draw_text_ex(COL_4_2, ROW_2, textSecond, "黑体", FONT_CONTENT_BIG, 0, true, false, true);// 第一行字体;;
			} else {
				String textFirst = entity.getEndStation().substring(0, 8);
				String textSecond = entity.getEndStation().substring(8, entity.getEndStation().length());
				zpSDK.zp_draw_text_ex(COL_4, ROW_1, textFirst, "黑体", FONT_CONTENT, 0, true, false, true);// 第一行字体;;
				zpSDK.zp_draw_text_ex(COL_4_2, ROW_2, textSecond, "黑体", FONT_CONTENT, 0, true, false, true);// 第一行字体;;
			}
		}

		// 第二行
		zpSDK.zp_draw_text_ex(COL_1, ROW_2, "始发站", "黑体", FONT_MENU, 0, false, false, false); // 第一行字体;;;
		zpSDK.zp_draw_text_ex(COL_2, ROW_2, entity.getStartStation(), "黑体", FONT_CONTENT, 0, true, false, true);
		// zpSDK.zp_draw_text_ex(40, 25, "日期", "黑体", FONT_MENU, 0, false,
		// false, false);
		// if (entity.getDate1() != null) {
		// zpSDK.zp_draw_text_ex(50, 25,
		// MDateUtils.dateFormat(entity.getDate1(),
		// MDateUtils.FORMAT_DATE_4), "黑体", FONT_CONTENT, 0, true, false,
		// true);
		// } else {
		// zpSDK.zp_draw_text_ex(50, 25,
		// MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4), "黑体",
		// FONT_CONTENT, 0, true, false, true);
		// }

		// 第三行
		zpSDK.zp_draw_text_ex(COL_1, ROW_3, "件数", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(COL_2, ROW_3, entity.getNum() + "-" + (i), "黑体", FONT_CONTENT_BIG, 0, true, false, true);
		zpSDK.zp_draw_text_ex(COL_3, ROW_3, "日期", "黑体", FONT_MENU, 0, false, false, false);
		if (entity.getDate() != null) {
			zpSDK.zp_draw_text_ex(COL_4, ROW_3, MDateUtils.dateFormat(entity.getDate(), MDateUtils.FORMAT_DATE_4), "黑体", FONT_CONTENT, 0, true, false, true);
		} else {
			zpSDK.zp_draw_text_ex(COL_4, ROW_3, MDateUtils.GetCurrentFormatTime(MDateUtils.FORMAT_DATE_4), "黑体", FONT_CONTENT, 0, true, false, true);
		}
		// zpSDK.zp_draw_text_ex(40, 33, "收货人", "黑体", FONT_MENU, 0, false,
		// false, false);
		// zpSDK.zp_draw_text_ex(50, 33, entity.getPerson(), "黑体",
		// FONT_CONTENT, 0, true, false, true);

		// 第四行
		zpSDK.zp_draw_text_ex(COL_1, ROW_4, "服务方式:", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(COL_2, ROW_4, entity.getServiceType(), "黑体", FONT_CONTENT, 0, true, false, true);
		zpSDK.zp_draw_text_ex(COL_3, ROW_4, "收货人", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(COL_4, ROW_4, entity.getPerson(), "黑体", FONT_CONTENT, 0, true, false, true);
		// zpSDK.zp_draw_text_ex(0, 40, "详细地址:", "黑体", FONT_MENU, 0, false,
		// false, false);
		// zpSDK.zp_draw_text_ex(14, 40, entity.getAddress(), "黑体",
		// FONT_CONTENT, 0, true, false, true);

		// 第五行
		// zpSDK.zp_draw_text_ex(0, 48, "服务方式:", "黑体", FONT_MENU, 0, false,
		// false, false);
		// zpSDK.zp_draw_text_ex(14, 48, entity.getServiceType(), "黑体",
		// FONT_CONTENT, 0, true, false, true);
		zpSDK.zp_draw_text_ex(COL_1, ROW_5, "详细地址:", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(COL_2, ROW_5, entity.getAddress(), "黑体", FONT_CONTENT, 0, true, false, true);

		zpSDK.zp_page_print(false);
		zpSDK.zp_printer_status_detect();
		// zpSDK.zp_goto_mark_right(150);
		zpSDK.zp_goto_mark_label(150);
		if (zpSDK.zp_printer_status_get(8000) != 0) {
			Toast.makeText(context, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
		}
		zpSDK.zp_page_free();
		zpSDK.zp_close();

		// toast("打印完成");
		// statusBox.Close();
	}

	private static String getIndexValue(int i) {
		String data = "";
		if (i < 100) {
			data = (100 + i) + "";
			data = data.replaceFirst("1", "0");
		} else {
			data = i + "";
		}
		return data;
	}
}
