package com.mtkj.webservice;

import java.util.HashMap;

/***
 * web服务常量
 * 
 * @author TNT
 * 
 */
public class WebServiceInfo {
	public static final int MSG_WS = 0x01; // messageWhat
	public static final int MSG_FAIL = 0x02; // MsgArg
	public static final int MSG_SUCCESS = 0x03;
	public static final int MSG_FAIL_MSG = 0x04;
	public static final String VALUE = "Value"; // 键
	public static final String COMAND_NAME = "COMAND_NAME";

	public static final String ERROR = "error";
	public static final String NO_CONNECTION = "no_connection";

	public static final int SERVICE_TYPE_HTTP = 1;
	public static final int SERVICE_TYPE_WEBSERVICE = 2;

	public static int ServiceType = SERVICE_TYPE_HTTP;

	// /**通用Method调用参数名称*/
	// String GENERAL_PARAMS_NAME = "jsonInfo";

	/***
	 * webmetod名称
	 * 
	 * @author TNT
	 * 
	 */
	public interface METHOD_NAME {
		String LOGIN = "Login"; // 登录接口
		String TakeOutGoods = "TakeOutGoods"; //查询提货单
		String StartStation = "StartStation"; //始发站
		String EndStation = "endStation"; //终点站
		String Customer = "Customer"; //收货人
		String TakeOutGoodsOver = "TakeOutGoods/Over"; //提货完成
		String TakeOutGoodsAdd = "TakeOutGoods/Add"; //添加
		String TakeOutGoodsEdit = "TakeOutGoods/Edit"; //编辑
		String TakeOutGoodsDelete = "TakeOutGoods/Delete"; //删除
		String TakeOutGoodsPrint = "TakeOutGoods/print"; //打印
		String TakeOutGoodsGetByCode = "TakeOutGoods/GetByCode"; //获得打印单
	}

	/**
	 * webmothod返回结果
	 * 
	 * @author TNT
	 * 
	 */
	public interface WEB_RESULT {
		/**
		 * 调用WebMethod成功
		 */
		String SUCESS = "0";
		/***
		 * 无
		 */
		String FAILED = "-1";
		/***
		 * 验证失败
		 */
		String FAILED_2 = "-2";
	}
	
	/**
	 * webmothod返回结果
	 * 
	 * @author TNT
	 * 
	 */
	public interface WEB_RESULT_VALUE {
		/**
		 * 调用WebMethod成功
		 */
		int SUCESS = 1;
		/***
		 * 失败
		 */
		int FAILED = 0;
		/***
		 * 其它
		 */
		int OTHER = -999;
	}

	/**
	 * webmothod返回结果
	 * 
	 * @author TNT
	 * 
	 */
	public interface WEB_RESULT_FORMAT_NAMES {
		/**
		 * 调用结果代码
		 */
		String RESULT_CODE = "ErrorCode";
		/***
		 * 原因
		 */
		String REASON = "reason";
		/***
		 * 结果数据
		 */
		String RESULT = "Result";
		/**
		 * 调用结果代码
		 */
		String Success  = "Success";
		/***
		 * 原因
		 */
		String Msg  = "Msg";
		/***
		 * 结果数据
		 */
		String Data  = "Data";
	}

	public static HashMap<Integer, String> ErrorInfos = new HashMap<Integer, String>();
	static {
		ErrorInfos.put(1, "上报记录--JSON对象为空");
		ErrorInfos.put(2, "上报记录--网点ID为空");
		ErrorInfos.put(3, "上报记录--表集合为空");
		ErrorInfos.put(4, "上报记录--表名为空，或者对应的记录集合为空（无对象）");
		ErrorInfos.put(5, "上报记录--未找到对应数据集");
		ErrorInfos.put(6, "上报记录--guid 为空，或者单条无字段信息");
		ErrorInfos.put(7, "上报记录--http 传入的Records 参数值为空");
		ErrorInfos.put(8, "上报记录--存在插入错误的数据，存储在 ErrorRecordTables");
		ErrorInfos.put(10, "上报记录--其他错误");
		ErrorInfos.put(21, "上报照片--recordID 为空");
		ErrorInfos.put(22, "上报照片--fileName 为空");
		ErrorInfos.put(23, "上报照片--fileType为空");
		ErrorInfos.put(24, "上报照片--tableName 为空");
		ErrorInfos.put(25, "上报照片--author 为空");
		ErrorInfos.put(26, "上报照片--takedatetime 为空");
		ErrorInfos.put(27, "上报照片--remark 为空");
		ErrorInfos.put(28, "上报照片--获取照片字节出错");
		ErrorInfos.put(29, "上报照片--插入照片出错");
		ErrorInfos.put(30, "上报照片--其它错误");
		ErrorInfos.put(41, "修改记录--json对象为null");
		ErrorInfos.put(42, "修改记录--网点ID为空");
		ErrorInfos.put(43, "修改记录--表集合为空");
		ErrorInfos.put(44, "修改记录--表名为空，或者对应的记录集合为空（无对象）");
		ErrorInfos.put(45, "修改记录--未找到对应数据集");
		ErrorInfos.put(46, "修改记录--guid 为空，或者单条无字段信息");
		ErrorInfos.put(47, "修改记录--http 传入的Records 参数值为空");
		ErrorInfos.put(48, "修改记录--存在插入错误的数据，存储在 ErrorRecordTables ");
		ErrorInfos.put(49, "修改记录--其他错误");
		ErrorInfos.put(50, "查询任务--无未接收的任务");
		ErrorInfos.put(51, "查询任务--NetID为空");
		ErrorInfos.put(52, "查询任务--其它错误");
		ErrorInfos.put(60, "上报地块--http 传入的参数值为空");
		ErrorInfos.put(61, "上报地块--对象为空");
		ErrorInfos.put(62, "上报地块--guid空");
		ErrorInfos.put(63, "上报地块--netid 空");
		ErrorInfos.put(64, "上报地块--种类空");
		ErrorInfos.put(65, "上报地块--品种空");
		ErrorInfos.put(66, "上报地块--地势空");
		ErrorInfos.put(67, "上报地块--gps数据量不对");
		ErrorInfos.put(68, "上报地块--netid 未找到对应对象");
		ErrorInfos.put(69, "上报地块--其他错误（服务端添加失败）");
		ErrorInfos.put(70, "上报gps轨迹--http传入参数为空");
		ErrorInfos.put(71, "上报gps轨迹--对象为空");
		ErrorInfos.put(72, "上报gps轨迹--netid为空");
		ErrorInfos.put(73, "上报gps轨迹--gps列表为空");
		ErrorInfos.put(78, "上报gps轨迹--netid 未找到对应对象");
		ErrorInfos.put(79, "上报gps轨迹--服务端其他错误");
		ErrorInfos.put(80, "上报咨询服务--http传入参数为空");
		ErrorInfos.put(82, "上报咨询服务--consult 空");
		ErrorInfos.put(83, "上报咨询服务--netid空");
		ErrorInfos.put(84, "上报咨询服务--ConsultInfo空");
		ErrorInfos.put(85, "上报咨询服务--Guid空");
		ErrorInfos.put(86, "上报咨询服务--netid 未找到对象");
		ErrorInfos.put(89, "上报咨询服务--服务器其他错误");
		ErrorInfos.put(100, "查询咨询信息--http 传入参数空");
		ErrorInfos.put(101, "查询咨询信息--ConsultQueryResultEntity 空");
		ErrorInfos.put(102, "查询咨询信息--netid空");
		ErrorInfos.put(103, "查询咨询信息--netid未找到");
		ErrorInfos.put(104, "查询咨询信息--查询成功，但是结果为空");
		ErrorInfos.put(109, "查询咨询信息--服务端其他错误");
		ErrorInfos.put(110, "更新任务状态--http 传入参数空");
		ErrorInfos.put(111, "更新任务状态--更新对象列表为空");
		ErrorInfos.put(112, "更新任务状态--服务端其他错误");
		ErrorInfos.put(120, "登录任务--http 传入空参数");
		ErrorInfos.put(121, "登录任务-- LoginEntity 解析为空");
		ErrorInfos.put(126, "登录任务--服务端其他错误");
		ErrorInfos.put(127, "登录任务--Type 参数不正确");
		ErrorInfos.put(128, "登录任务--密码不正确");
		ErrorInfos.put(129, "登录任务--用户名不正确");
	}

}
