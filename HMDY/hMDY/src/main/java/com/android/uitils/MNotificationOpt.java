/***
 * 	android 状态栏 的相关操作
 * 
 *  创建通知消息的步骤：
 *  1.获取NotificationManager的一个引用
 *  2.实例化Notification
 *  3.定义Notification的扩展消息和Intent：
 *  4.将Notification传递给NotificationManager
 */
package com.android.uitils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

public class MNotificationOpt {

	private Context m_context = null;
	private NotificationManager m_NotificationManager = null;

	public MNotificationOpt(Context context) {
		m_context = context;
		// 1.获取NotificationManager的一个引用
		m_NotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * 发送常规通知信息到状态栏
	 * 
	 * @param id
	 *            An identifier for this notification unique within your
	 *            application
	 * @param imagId
	 *            通知图标
	 * @param tickerText
	 *            状态栏通知内容
	 * @param contentTitle
	 *            通知内容的标题
	 * @param contentText
	 *            通知内容
	 * @param contentIntent
	 *            点击通知后的操作（跳转操作）
	 */
	public void sendNotificationNomal(int id, int imagId, String tickerText,
			String contentTitle, String contentText, PendingIntent contentIntent) {
		// 获得当前时间
		long when = System.currentTimeMillis();

		// 2.实例化Notification
		Notification notification = new Notification(imagId, tickerText, when);

		// 3.定义Notification的扩展消息和Intent：
		Context context = m_context.getApplicationContext();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// 4.将Notification传递给NotificationManager
		m_NotificationManager.notify(id, notification);
	}

	/**
	 * 发送常规通知信息到状态栏
	 * 
	 * @param id
	 *            An identifier for this notification unique within your
	 *            application
	 * @param imagId
	 *            通知图标
	 * @param tickerText
	 *            状态栏通知内容
	 * @param contentTitle
	 *            通知内容的标题
	 * @param contentText
	 *            通知内容
	 * @param contentIntent
	 *            点击通知后的操作（跳转操作）
	 */
	public void sendTaskNotification(int id, int imagId, String tickerText,
			String contentTitle, String contentText, PendingIntent contentIntent) {

		// 获得当前时间
		long when = System.currentTimeMillis();

		// 2.实例化Notification
		Notification notification = new Notification(imagId, tickerText, when);

		notification.defaults = Notification.DEFAULT_ALL;

		// 3.定义Notification的扩展消息和Intent：
		Context context = m_context.getApplicationContext();
		notification.flags |= Notification.FLAG_INSISTENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// notification.flags |= Notification.DEFAULT_ALL;
		// notification.flags |= Notification.DEFAULT_LIGHTS;
		// notification.flags |= Notification.DEFAULT_VIBRATE;
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// 4.将Notification传递给NotificationManager
		m_NotificationManager.notify(id, notification);
	}

	public void clearNotification(int id) {
		m_NotificationManager.cancel(id);
	}

}
