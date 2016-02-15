package com.android.uitils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tnt.hmdy.R;
import com.utils.log.MLog;

public class Util {

    private static final int CORE_POOL_SIZE = 30;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "thread #" + mCount.getAndIncrement());
        }
    };

    private static ExecutorService mExecutorService;
    
    public static String GetID(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

    /**
     * 获取线程池
     * 
     * @return
     */
    public static ExecutorService getExecutor() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(CORE_POOL_SIZE, sThreadFactory);
        }
        return mExecutorService;
    }

    private static final String dateFormat1 = "yyyy-MM-dd HH:mm:ss";

    public static long getTimeMillis(String dateString) throws ParseException {
        SimpleDateFormat format = getDateFormat(dateFormat1);
        return format.parse(dateString).getTime();
    }

    public static String getTimeString(long timeMillis) {
        SimpleDateFormat format = getDateFormat(dateFormat1);
        Date date = new Date(timeMillis);
        return format.format(date).toString();
    }

    public static SimpleDateFormat getDateFormat(String dateFormat) {
        synchronized (Util.class) {
            return new SimpleDateFormat(dateFormat, Locale.CHINA);
        }
    }

    public static String ConvertDateToString(Date date) {
        String strDateString = "";
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        strDateString = format1.format(date);
        return strDateString;
    }

    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String HANDSET_NAME = "handset01";

    public static String getHandsetName() {
        return HANDSET_NAME;
    }

    public static void SetHandSetName(Context context) {
        initHandSetName(context);
    }

    private static void initHandSetName(Context context) {
        String phone = "";
        try {
            phone = getLocalNumber(context);
        } catch (Exception e) {
            phone = null;
            Log.e("UpLocation", "获得号码失败: " + e.toString());
        }
    }

    private static String getLocalNumber(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = tManager.getLine1Number();
        if (number != null && number.length() > 4) {
            // 取后四位
            number = number.substring(number.length() - 4, number.length());
        } else {
            number = "0000";
        }
        return number;
    }

    /**
     * 获取sd卡路径
     * 
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 拨打电话
     * 
     * @param context
     * @param strTelNum
     */
    public static void CallMobile(Context context, String strTelNum) {
        // ACTION_DIAL&&ACTION_CALL
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strTelNum));
            context.startActivity(intent);

        } catch (Exception e) {
            MLog.e("changchun", "启动打电话失败");
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断字符串是否为数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 取出开头为数字的组成
     * 
     * @param str
     * @return
     */
    public static String getNotNumericInfo(String str) {

        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            String info = matcher.group(2);
            if (info.startsWith("_") || info.startsWith("-") || info.startsWith(".")) {
                info = info.substring(1, info.length());
            }
            return info;
        }
        return str;
    }

    /**
     * 消息提示
     * 
     * @param context
     * @param msg
     */
    public static void MsgBox(Context context, String msg) {
    	MsgBox(context, msg, Toast.LENGTH_SHORT);
    }
    
    /**
     * 消息提示
     * 
     * @param context
     * @param msg
     */
    public static void MsgBoxLong(Context context, String msg) {
    	MsgBox(context, msg, Toast.LENGTH_LONG);
    }
    
    /**
     * 消息提示
     * 
     * @param context
     * @param msg
     */
    private static void MsgBox(Context context, String msg, int timeStyle) {
    	// Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    	// Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
    	Toast toast = new Toast(context);
    	TextView view = new TextView(context);
    	view.setText(msg);
    	// view.setBackgroundColor(Color.BLACK);
    	view.setBackgroundResource(R.drawable.bk_toast);
    	view.setTextSize(context.getResources().getDimension(R.dimen.text_size_tost));
    	view.setPadding(16, 8, 16, 8);
    	view.setTextColor(Color.WHITE);
    	// toast.setGravity(Gravity.CENTER, 0, 0);
    	toast.setView(view);
    	toast.setDuration(timeStyle);
    	toast.show();
    	// Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public static String getRandomString(int len) {
        String returnStr = "";
        char[] ch = new char[len];
        Random rd = new Random();
        for (int i = 0; i < len; i++) {
            ch[i] = (char) (rd.nextInt(9) + 65);
            ch[i] = encodeTable[rd.nextInt(36)];
        }
        returnStr = new String(ch);
        return returnStr;
    }

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    public static String genKey(String ser, String hw) {
        String key = "";
        try {
            key = Base32Encoder.encode(HmacSHA1Encrypt(ser, hw));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return key.substring(0, 8);
    }

    /***
     * 获得设备ID
     * 
     * @param context
     * @return
     */
    public static String getDevId(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 系统震动
     * 
     * @param context
     * @param milliseconds
     */
    public static void Vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 系统震动
     * 
     * @param context
     * @param pattern
     * @param isRepeat
     *            1:表示重复震动，-1表示不重复
     */
    public static void Vibrate(Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }


    /***
     * 文件名合法检查
     * 
     * @param fileName
     * @return
     */
    public static boolean isFileNameOk(String fileName) {
        return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }

    /***
     * 获取指定位置指定类型的文件
     * 
     * @param path
     *            指定位置
     * @param type
     *            指定类型
     * @return
     */
    public static List<String> getOptTypeFileOnOptPath(String path, String type) {
        List<String> pathList = new ArrayList<String>();
        File fileFolder = new File(path);
        if (!fileFolder.isDirectory()) {
            return null;
        } else {
            File[] files = fileFolder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String strPath = file.getAbsolutePath();
                    int nTypeSize = type.length();
                    String strExpend = strPath.substring(strPath.length() - nTypeSize, strPath.length());
                    boolean bCompare = false;
                    bCompare = strExpend.equalsIgnoreCase(type);
                    if (bCompare) {
                        pathList.add(strPath);
                    }
                }
            }
        }
        return pathList;
    }

    /***
     * 获取指定目录下的所有文件夹
     * 
     * @param strPath
     * @return
     */
    public static List<String> getAllFolderInPath(String strPath) {
        List<String> pathList = new ArrayList<String>();
        File fileFolder = new File(strPath);
        if (!fileFolder.isDirectory()) {
            return null;
        } else {
            File[] files = fileFolder.listFiles();
            for (File file : files) {
                if (!file.isFile()) {
                    String strPath1 = file.getAbsolutePath();
                    pathList.add(strPath1);
                }
            }
        }
        return pathList;
    }

    /***
     * 获取指定目录下的所有文件
     * 
     * @param strPath
     * @return
     */
    public static List<String> getAllFilesInPath(String strPath) {
        List<String> pathList = new ArrayList<String>();
        File fileFolder = new File(strPath);
        if (!fileFolder.isDirectory()) {
            return null;
        } else {
            File[] files = fileFolder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String strPath1 = file.getAbsolutePath();

                    if (!strPath1.contains(".ovr")) {
                        pathList.add(strPath1);
                    }
                }
            }
        }
        return pathList;
    }

    /**
     * 根据文件全称获得文件名
     * 
     * @param fileAbsolutePath
     * @return
     */
    public static String GetFileNameByWholePath(String fileAbsolutePath) {
        int startIndex = fileAbsolutePath.lastIndexOf("/");
        int endIndex = fileAbsolutePath.lastIndexOf(".");
        return fileAbsolutePath.substring(startIndex + 1, endIndex);
    }

    /***
     * 读取文件的内容转化为文本类型
     * 
     * @param filepath
     * @return
     */
    public static String readFile(String filepath) {
        String path = filepath;
        if (null == path) {
            return null;
        }

        String filecontent = null;
        File f = new File(path);
        if (f != null && f.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return null;
            }

            CharBuffer cb;
            try {
                cb = CharBuffer.allocate(fis.available());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }

            InputStreamReader isr;
            try {
                isr = new InputStreamReader(fis, "gb2312");
                try {
                    if (cb != null) {
                        isr.read(cb);
                    }
                    filecontent = new String(cb.array());
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return filecontent;
    }

    public static void ShowMsg(Context context, String strMsg) {
        Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据dip返回当前设备上的px值
     * 
     * @param context
     * @param dip
     * @return
     */
    public static int dipToPx(Context context, int dip) {
        int px = 0;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        px = (int) (dip * density);
        // Print.println("pxToDip px = " + px);
        return px;
    }

    /****
     * 保留小数点后N位
     * 
     * @param dx
     * @return
     */
    public static double Round2(int n, double dx) {
        String strFormat = "#.";
        for (int i = 0; i < n; i++) {
            strFormat += "0";
        }
        DecimalFormat df = new DecimalFormat(strFormat);
        double d = Double.parseDouble(df.format(dx));
        return d;
    }

    // 把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat(dateFormat1);
        return df.parse(strDate);
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    public static void GetandSaveCurrentImage(Activity mContext) {
        // 构建Bitmap
        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        // 获取屏幕
        View decorview = mContext.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();
        // 图片存储路径
        String SavePath = getSDCardPath();
        // 保存Bitmap
        try {
            File path = new File(SavePath);
            // 文件
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String filepath = SavePath + "/"+sdf.format(new Date())+".png";
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                MsgBox(mContext, "截屏文件已保存至SDCard目录下");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /***
     * 判断一个字符串是否在一个数组中
     * @param arrays
     * @param strText
     * @return
     */
    public static boolean isInStringArray(String[] arrays,String strText){
        boolean bRes=false;
        if(arrays==null||arrays.length<1){
            return bRes;
        }
        for (String str : arrays) {
            if(str.equalsIgnoreCase(strText)){
                bRes=true;
                break;
            }
        }
        return bRes;
    }
    
    /**
	 * 号码检查
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "---");
		return m.matches();
	}

	/**
	 * 座机号检查
	 * 
	 * @return
	 */
	public static boolean isTelNo(String telNo) {
		// -------------手机号码，以1开始，13,15,18,19,为合法，后根9位数字------
		// String regEx="[1]{1}[3,5,8,6]{1}[0-9]{9}"; //表示a或f
		// boolean p = Pattern.compile(regEx).matcher("13558842633").find();
		// -------------电话号码,以0开始,不含括号----------------------------------------------
		String tregEx = "[0]{1}[0-9]{2,3}-[0-9]{7,8}"; // 表示a或f 0832-80691990
		return Pattern.compile(tregEx).matcher(telNo).find();
	}

}
