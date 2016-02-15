package com.android.tnt.hmdy.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.tnt.config.Constants;
import com.android.tnt.db.entity.HuoDanEntity;
import com.android.tnt.hmdy.MApplication;
import com.android.tnt.hmdy.R;
import com.android.uitils.MDateUtils;
import com.android.uitils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import zpSDK.zpSDK.zpSDK;

/***
 * 
 * 打印测试
 * 
 * @author TNT
 * 
 */
public class PrintSettingFragment extends BaseFragment {

	private final String TAG = "PrintSettingFragment";

	private final float FONT_LITTLE = 2.2f;
	private final float FONT_MENU = 3.0f;
	private final float FONT_CONTENT = 3.4f;
	private final float FONT_CONTENT_BIG = 5f;
	private final float FONT_TITLE = 5f;

	private final int ROW_1 = 18;
	private final int ROW_2 = 25;
	private final int ROW_3 = 33;
	private final int ROW_4 = 40;
	private final int ROW_5 = 48;

	private final int COL_1 = 1;
	private final int COL_2 = 13;
	private final int COL_2_2 = 8;
	private final int COL_3 = 31;
	private final int COL_4 = 41;
	private final int COL_4_2 = 45;

	private HuoDanEntity entity = null;

	public static BluetoothAdapter myBluetoothAdapter;
	public String SelectedBDAddress;

	List<Map<String, String>> list = null;
	ListView listView = null;
	
	StatusBox statusBox;

	private MApplication mApp = null;

	private View rootView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 获得本机蓝牙适配器对象引用
		if (myBluetoothAdapter == null) {
			toast("对不起 ，您的设备不具备蓝牙功能");
			getActivity().finish();
			return;
		}
		// 打开蓝牙
		openBL();

		// entity = (HuoDanEntity)
		// getArguments().getSerializable(Constants.KEY.DATA1);
		entity = new HuoDanEntity();
		entity.setAddress("打印测试--详细地址");
		entity.setCode("1049696736001");
		entity.setEndStation("打印测试--目的地");
		entity.setNum(2);
		entity.setPerson("某某某");
		entity.setServiceType("站点送货");
		entity.setStartStation("XXX营业部");

		mApp = (MApplication) getActivity().getApplication();
		SelectedBDAddress = mApp.getData(Constants.CONFIG_KEY.BLDevice, "");

		IntentFilter bluetoothFilter = new IntentFilter();
		bluetoothFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		bluetoothFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		getActivity().registerReceiver(BluetoothReciever, bluetoothFilter);

		// 蓝牙扫描相关设备
		IntentFilter btDiscoveryFilter = new IntentFilter();
		btDiscoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		btDiscoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		btDiscoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
		btDiscoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
		btDiscoveryFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		getActivity().registerReceiver(BTDiscoveryReceiver, btDiscoveryFilter);

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 刷新数据
		initDatas();
		// 刷新界面
		View rootView = inflater.inflate(R.layout.fragment_print_setting, container, false);
		// statusBox = new StatusBox(getActivity(), rootView);
		initViews(rootView);
		if(list != null && list.size() == 0 && listView != null){
			listView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					listView.invalidateViews();
				}
			}, 1000);
		}
		return rootView;
	}
	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(BluetoothReciever);
		super.onDestroy();
	}

	private void initDatas() {
		// TODO Auto-generated method stub
	}

	public boolean saveSetting() {
		boolean bRt = false;
		// SelectedBDAddress = mApp.getData(key, defValue)
		if (!TextUtils.isEmpty(SelectedBDAddress)) {
			mApp.setData(Constants.CONFIG_KEY.BLDevice, SelectedBDAddress);
			bRt = true;
		} else {
			Util.MsgBox(getActivity(), "请选择打印机");
		}

		return bRt;
	}

	public void refreshListView(){
		ListBluetoothDevice(this.rootView);
	}
	
	private void initViews(View rootView) {
		this.rootView = rootView;
		Button Button1 = (Button) rootView.findViewById(R.id.button1);
		statusBox = new StatusBox(getActivity(), Button1);
		Button1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				// Print1(SelectedBDAddress);
				Print(SelectedBDAddress, entity);
			}
		});
		if (!ListBluetoothDevice(rootView)) {
			// 开始扫描蓝牙设备
			dicoverBlDevice();
		}
		Button Button2 = (Button) rootView.findViewById(R.id.button2);
		Button2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				// Print2(SelectedBDAddress);
				dicoverBlDevice();
			}
		});
		Button Button3 = (Button) rootView.findViewById(R.id.button3);
		Button3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				// Print3(SelectedBDAddress);
			}
		});
		Button Button4 = (Button) rootView.findViewById(R.id.button4);
		Button4.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print4(SelectedBDAddress);
			}
		});
		Button Button5 = (Button) rootView.findViewById(R.id.button5);
		Button5.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print5(SelectedBDAddress);
			}
		});
		Button Button6 = (Button) rootView.findViewById(R.id.button6);
		Button6.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print6(SelectedBDAddress);
			}
		});

	}

	public boolean ListBluetoothDevice(View rootView) {
		list = new ArrayList<Map<String, String>>();
		listView = (ListView) rootView.findViewById(R.id.listView1);
		SimpleAdapter m_adapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_2, new String[] { "DeviceName", "BDAddress" }, new int[] { android.R.id.text1, android.R.id.text2 });
		listView.setAdapter(m_adapter);

		// if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) ==
		// null) {
		// toast("没有找到蓝牙适配器");//, Toast.LENGTH_LONG).show();
		// return false;
		// }

		// if (!myBluetoothAdapter.isEnabled()) {
		// Intent enableBtIntent = new
		// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		// startActivityForResult(enableBtIntent, 2);
		// }

		Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() <= 0) {
			return false;
		}
		for (BluetoothDevice device : pairedDevices) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("DeviceName", device.getName());
			map.put("BDAddress", device.getAddress());
			// device.getBondState()
			list.add(map);
		}
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				SelectedBDAddress = list.get(arg2).get("BDAddress");
				if (((ListView) arg0).getTag() != null) {
					((View) ((ListView) arg0).getTag()).setBackgroundDrawable(null);
				}
				((ListView) arg0).setTag(arg1);
				arg1.setBackgroundColor(getResources().getColor(R.color.alice_blue));
			}
		});
		listView.invalidateViews();
		return true;
	}

	public boolean OpenPrinter(String BDAddress) {

		if (BDAddress == "" || BDAddress == null) {
			Util.MsgBoxLong(getActivity(), "没有选择打印机");// ,
														// Toast.LENGTH_LONG).show();
			return false;
		}
		BluetoothDevice myDevice;
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			Util.MsgBoxLong(getActivity(), "读取蓝牙设备错误");// ,
														// Toast.LENGTH_LONG).show();
			return false;
		}
		myDevice = myBluetoothAdapter.getRemoteDevice(BDAddress);
		if (myDevice == null) {
			Util.MsgBoxLong(getActivity(), "读取蓝牙设备错误");// ,
														// Toast.LENGTH_LONG).show();
			return false;
		}
		if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
			Util.MsgBoxLong(getActivity(), zpSDK.ErrorMessage);// ,
																// Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public void Print(String BDAddress, HuoDanEntity entity) {
		statusBox.Show("正在打印...");
		int number = entity.getNum();
		String baseHuoDan = "1xxxx1xxxx";
		if (!TextUtils.isEmpty(entity.getCode())) {
			baseHuoDan = entity.getCode();
		}
		if (baseHuoDan.length() > 10) {
			baseHuoDan = baseHuoDan.substring(0, 10);
		}

		for (int i = 0; i < number; i++) {
			statusBox.Show("正在打印第" + (i + 1) + "张");

			if (!OpenPrinter(BDAddress)) {
				statusBox.Close();
				return;
			}

			if (!zpSDK.zp_page_create(80, 60)) {
				Toast.makeText(getActivity(), "创建打印页面失败", Toast.LENGTH_LONG).show();
				statusBox.Close();
				return;
			}
			zpSDK.TextPosWinStyle = false;
			zpSDK.zp_draw_text_ex(2, 7, "青鸟物流", "黑体", FONT_TITLE, 0, false, false, false);
			zpSDK.zp_draw_barcode(25, 0, baseHuoDan + getIndexValue(i + 1), zpSDK.BARCODE_TYPE.BARCODE_CODE128, 12, 2, 0);
			zpSDK.zp_draw_text_ex(25, 14, baseHuoDan + getIndexValue(i + 1), "黑体", FONT_LITTLE, 0, true, false, false);

			// 第一行
			zpSDK.zp_draw_text_ex(COL_1, ROW_1, "提货单", "黑体", FONT_MENU, 0, false, false, false);
			zpSDK.zp_draw_text_ex(COL_2_2, ROW_1, baseHuoDan, "黑体", FONT_CONTENT, 0, true, false, true);
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
			zpSDK.zp_draw_text_ex(COL_2_2, ROW_2, entity.getStartStation(), "黑体", FONT_CONTENT, 0, true, false, true);
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
			zpSDK.zp_draw_text_ex(COL_2_2, ROW_3, entity.getNum() + "-" + (i + 1), "黑体", FONT_CONTENT_BIG, 0, true, false, true);
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
				Toast.makeText(getActivity(), zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
			}
			zpSDK.zp_page_free();
			zpSDK.zp_close();
		}
		toast("打印完成");
		statusBox.Close();
	}

	private String getIndexValue(int i) {
		String data = "";
		if (i < 100) {
			data = (100 + i) + "";
			data = data.replaceFirst("1", "0");
		} else {
			data = i + "";
		}
		return data;
	}

	public void PrintTest(String BDAddress) {
		statusBox.Show("正在打印...");
		if (!OpenPrinter(BDAddress)) {
			statusBox.Close();
			return;
		}
		if (!zpSDK.zp_page_create(80, 63)) {
			Toast.makeText(getActivity(), "创建打印页面失败", Toast.LENGTH_LONG).show();
			statusBox.Close();
			return;
		}
		zpSDK.TextPosWinStyle = false;
		zpSDK.zp_draw_text_ex(2, 7, "青鸟物流", "黑体", FONT_TITLE, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 0, "1049696736001", zpSDK.BARCODE_TYPE.BARCODE_CODE128, 12, 3, 0);
		zpSDK.zp_draw_text_ex(25, 10, "1049696736001", "黑体", FONT_LITTLE, 0, true, false, false);

		// 第一行
		zpSDK.zp_draw_text_ex(0, 16, "提货单", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(10, 16, "1049696736001", "黑体", FONT_CONTENT, 0, true, false, true);
		zpSDK.zp_draw_text_ex(40, 16, "目的地", "黑体", FONT_MENU, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(50, 16, "日照市", "黑体", FONT_CONTENT_BIG, 0, true, false, true);// 第一行字体;;

		// 第二行
		zpSDK.zp_draw_text_ex(0, 24, "始发站", "黑体", FONT_MENU, 0, false, false, false); // 第一行字体;;;
		zpSDK.zp_draw_text_ex(10, 24, "久敬庄营业部", "黑体", FONT_CONTENT, 0, true, false, true);
		zpSDK.zp_draw_text_ex(40, 24, "日期", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(50, 24, "2015-06-26", "黑体", FONT_CONTENT, 0, true, false, true);

		// 第三行
		zpSDK.zp_draw_text_ex(0, 32, "件数", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(10, 32, "2", "黑体", FONT_CONTENT, 0, true, false, true);
		zpSDK.zp_draw_text_ex(40, 32, "收货人", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(50, 32, "爱青鸟", "黑体", FONT_CONTENT, 0, true, false, true);

		// 第四行
		zpSDK.zp_draw_text_ex(0, 40, "详细地址:", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(14, 40, "临新路与济南路 锦绣前程小区", "黑体", FONT_CONTENT, 0, true, false, true);

		// 第五行;
		zpSDK.zp_draw_text_ex(0, 48, "服务方式:", "黑体", FONT_MENU, 0, false, false, false);
		zpSDK.zp_draw_text_ex(14, 48, "站点送货", "黑体", FONT_CONTENT, 0, true, false, true);

		zpSDK.zp_page_print(false);
		zpSDK.zp_printer_status_detect();
		// zpSDK.zp_goto_mark_right(150);
		zpSDK.zp_goto_mark_label(100);
		if (zpSDK.zp_printer_status_get(8000) != 0) {
			Toast.makeText(getActivity(), zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
		}
		zpSDK.zp_page_free();
		zpSDK.zp_close();
		statusBox.Close();
	}

	// 走到黑标
	public void Print4(String BDAddress) {
		if (!OpenPrinter(BDAddress))
			return;
		zpSDK.zp_goto_mark_left(150);
		zpSDK.zp_close();
	}

	public void Print5(String BDAddress) {
		if (!OpenPrinter(BDAddress))
			return;
		zpSDK.zp_goto_mark_right(150);
		zpSDK.zp_close();

	}

	public void Print6(String BDAddress) {
		if (!OpenPrinter(BDAddress))
			return;
		zpSDK.zp_goto_mark_label(150);
		zpSDK.zp_close();
	}// print6

	private void openBL() {
		boolean wasBtOpened = myBluetoothAdapter.isEnabled(); // 是否已经打开
		if (!wasBtOpened) {
			boolean result = myBluetoothAdapter.enable();
			if (result)
				toast("蓝牙打开操作成功");
			else
				toast("蓝牙打开失败");
		} else if (wasBtOpened)
			toast("蓝牙已经打开");
	}

	private void dicoverBlDevice() {
		statusBox.Show("正在搜索...");
		if (!myBluetoothAdapter.isDiscovering()) {
			Log.i(TAG, "btCancelDiscovery ### the bluetooth dont't discovering");
			myBluetoothAdapter.startDiscovery();
		} else {
			toast("搜索中");
		}
	}

	// 蓝牙开个状态以及扫描状态的广播接收器
	private BroadcastReceiver BluetoothReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
				Log.v(TAG, "### Bluetooth State has changed ##");

				int btState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);

				printBTState(btState);
			} else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(intent.getAction())) {
				Log.v(TAG, "### ACTION_SCAN_MODE_CHANGED##");
				int cur_mode_state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);
				int previous_mode_state = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);

				Log.v(TAG, "### cur_mode_state ##" + cur_mode_state + " ~~ previous_mode_state" + previous_mode_state);

			}
		}

	};

	// 蓝牙扫描时的广播接收器
	private BroadcastReceiver BTDiscoveryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())) {
				Log.v(TAG, "### BT ACTION_DISCOVERY_STARTED ##");
				statusBox.Show("正在搜索...");
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
				Log.v(TAG, "### BT ACTION_DISCOVERY_FINISHED ##");
				statusBox.Close();
			} else if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
				Log.v(TAG, "### BT BluetoothDevice.ACTION_FOUND ##");

				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (btDevice != null)
					Log.v(TAG, "Name : " + btDevice.getName() + " Address: " + btDevice.getAddress());

			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
				Log.v(TAG, "### BT ACTION_BOND_STATE_CHANGED ##");

				int cur_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
				int previous_bond_state = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE);

				Log.v(TAG, "### cur_bond_state ##" + cur_bond_state + " ~~ previous_bond_state" + previous_bond_state);
				if(rootView != null){
					//刷新
					ListBluetoothDevice(rootView);
				}
			}
		}

	};

	private void printBTState(int btState) {
		switch (btState) {
		case BluetoothAdapter.STATE_OFF:
			toast("蓝牙状态:已关闭");
			Log.v(TAG, "BT State ： BluetoothAdapter.STATE_OFF ###");
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
			toast("蓝牙状态:正在关闭");
			Log.v(TAG, "BT State :  BluetoothAdapter.STATE_TURNING_OFF ###");
			break;
		case BluetoothAdapter.STATE_TURNING_ON:
			toast("蓝牙状态:正在打开");
			Log.v(TAG, "BT State ：BluetoothAdapter.STATE_TURNING_ON ###");
			break;
		case BluetoothAdapter.STATE_ON:
			toast("蓝牙状态:已打开");
			Log.v(TAG, "BT State ：BluetoothAdapter.STATE_ON ###");
			refreshListView();
			break;
		default:
			break;
		}
	}

	private final int REQUEST_OPEN_BT_CODE = 1;
	private final int REQUEST_DISCOVERY_BT_CODE = 2;

	private void toast(String str) {
		Util.MsgBox(getActivity(), str);
	}

}
