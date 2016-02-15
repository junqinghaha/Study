package com.android.uitils;

import java.net.URLEncoder;
import java.security.InvalidKeyException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/***
 * 参数名称 参数含义 参数样例
 * <hr>
 * areaid 区域 id 单区域:101010100 多区域:101010100|101010200
 * <hr>
 * type 数据类型 指数:
 * <hr>
 * index_f(基础)、index_v(常规) 3 天常规预报(24 小时):forecast_f(基础)、 forecast_v (常规)
 * <hr>
 * date 客户端日期 按照格式 yyyyMMddHHmm 获取客户端当前时间
 * <hr>
 * appid 固定分配的型 xx: 1234567890
 * <hr>
 * SmartWeatherAPI Open 版接口使用说明书 <Lite> 3 号标识 传递参数时:截取 appid 的前 6 位 生成公钥时:取完整的
 * <hr>
 * appid key 令牌 由公钥(public_key)和私钥(private_key)通过 固定算法加密生成
 * 
 * @author TNT
 * 
 */
public class WeatherApiUtil {
	private static final char last2byte = (char) Integer
			.parseInt("00000011", 2);
	private static final char last4byte = (char) Integer
			.parseInt("00001111", 2);
	private static final char last6byte = (char) Integer
			.parseInt("00111111", 2);
	private static final char lead6byte = (char) Integer
			.parseInt("11111100", 2);
	private static final char lead4byte = (char) Integer
			.parseInt("11110000", 2);
	private static final char lead2byte = (char) Integer
			.parseInt("11000000", 2);
	private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };

	/***
	 * 获得调用url
	 * 
	 * @param appid
	 * @param key
	 * @param type
	 * @param date
	 * @return
	 */
	public static String getHtmlURL(String areaId, String appid,
			String appid_pre6, String key, String type, String date) {
		// 需要加密的数据
		String data = "http://open.weather.com.cn/data/?areaid=" + areaId
				+ "&type=" + type + "&date=" + date + "&appid=" + appid_pre6
				+ "&key=" + buildKeyValues(key, areaId, appid, type, date) + "";
		// 密钥
		// String key = "xxxxx_SmartWeatherAPI_xxxxxxx";
		return data;
	}

	/***
	 * 获得调用url
	 * 
	 * @param appid
	 * @param key
	 * @param type
	 * @param date
	 * @return
	 */
	public static String buildKeyValues(String privateKey, String areaId,
			String appid, String type, String date) {
		// 需要加密的数据
		String data = "http://open.weather.com.cn/data/?areaid=" + areaId
				+ "&type=" + type + "&date=" + date + "&appid=" + appid;// +
																		// "&key="
																		// + key
																		// + "";
		// 密钥
		// String key = "xxxxx_SmartWeatherAPI_xxxxxxx";
		return standardURLEncoder(data, privateKey);
	}

	public static String standardURLEncoder(String data, String key) {
		byte[] byteHMAC = null;
		String urlEncoder = "";
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			if (byteHMAC != null) {
				// String oauth = new Hex().encode(byteHMAC);
				String oauth = encode(byteHMAC);
				if (oauth != null) {
					urlEncoder = URLEncoder.encode(oauth, "utf8");
				}
			}
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return urlEncoder;
	}

	public static String encode(byte[] from) {
		StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
		int num = 0;
		char currentByte = 0;
		for (int i = 0; i < from.length; i++) {
			num = num % 8;
			while (num < 8) {
				switch (num) {
				case 0:
					currentByte = (char) (from[i] & lead6byte);
					currentByte = (char) (currentByte >>> 2);
					break;
				case 2:
					currentByte = (char) (from[i] & last6byte);
					break;
				case 4:
					currentByte = (char) (from[i] & last4byte);
					currentByte = (char) (currentByte << 2);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead2byte) >>> 6;
					}
					break;
				case 6:
					currentByte = (char) (from[i] & last2byte);
					currentByte = (char) (currentByte << 4);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead4byte) >>> 4;
					}
					break;
				}
				to.append(encodeTable[currentByte]);
				num += 6;
			}
		}
		if (to.length() % 4 != 0) {
			for (int i = 4 - to.length() % 4; i > 0; i--) {
				to.append("=");
			}
		}
		return to.toString();
	}

	public static void main(String[] args) {
		try {

			// 需要加密的数据
			String data = "http://open.weather.com.cn/data/?areaid=xxxxxxxxxx&type=xxxxxxxx&date=xxxxxxxxx&appid=xxxxxxx";
			// 密钥
			String key = "xxxxx_SmartWeatherAPI_xxxxxxx";

			String str = standardURLEncoder(data, key);

			System.out.println(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
