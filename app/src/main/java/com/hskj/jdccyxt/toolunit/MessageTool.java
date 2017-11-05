package com.hskj.jdccyxt.toolunit;

import android.os.Handler;
import android.os.Message;

public class MessageTool {
	/**
	 * 消息发送
	 * 
	 * @param constant
	 *            :信息指定编号
	 * @param matter
	 *            :信息内容
	 * @param handler
	 */
	public static void setmessage(int constant, String matter, Handler handler) {
		Message msg = new Message();
		msg.what = constant;
		msg.obj = matter;
		handler.sendMessage(msg);
	}

}
