package com.hskj.jdccyxt.toolunit;

import android.os.Handler;
import android.os.Message;

public class MessageTool {
	/**
	 * ��Ϣ����
	 * 
	 * @param constant
	 *            :��Ϣָ�����
	 * @param matter
	 *            :��Ϣ����
	 * @param handler
	 */
	public static void setmessage(int constant, String matter, Handler handler) {
		Message msg = new Message();
		msg.what = constant;
		msg.obj = matter;
		handler.sendMessage(msg);
	}

}
