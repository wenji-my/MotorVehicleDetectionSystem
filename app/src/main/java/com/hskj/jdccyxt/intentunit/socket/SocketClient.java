package com.hskj.jdccyxt.intentunit.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import com.hskj.jdccyxt.toolunit.StaticConst;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * @类名：SocketClient
 * @author:广东泓胜科技有限公司
 * @实现功能：socket联网部分
 * @创建日期：2016.07.12
 */
@SuppressLint("HandlerLeak")
public class SocketClient implements Runnable {
	private Socket socket;
	private Handler handler;// 向UI线程发送消息的Handler对象
	public Handler revHandler;// 接收UI线程的消息的Handler对象
	private BufferedReader br = null;
	private OutputStream os = null;
	private String ip;// IP地址
	private int portNumber;// 端口号
	private int timeout;// 超时时间
	private static boolean isStart;
	private int SOCKET_TOAST_MESSAGE = StaticConst.SOCKETCLIENT_TOAST_MESSAGE;// 弹数据接收确定框
	private int SOCKET_RECEIVE_MESSAGE = StaticConst.SOCKETCLIENT_RECEIVE_MESSAGE;// 接收信息
	private int SOCKET_SEND_MESSAGE = StaticConst.SOCKETCLIENT_SEND_MESSAGE;// 发送信息
	private int SOCKET_BROKEN_NETWORK_SHOW = StaticConst.INSTRUMENTDETECTION_BROKEN_NETWORK_SHOW;// socket断开
	private int SOCKET_CONNET_NETWORK_DISMISS = StaticConst.INSTRUMENTDETECTION_CONNET_NETWORK_DISMISS;// socket连接

	public SocketClient(Handler handler, String ip, int portNumber, int timeout) {
		Log.i("AAA", "**初始化进来**");
		this.handler = handler;
		this.ip = ip;
		this.portNumber = portNumber;
		this.timeout = timeout;
	}

	@Override
	public void run() {

		Log.i("AAA", "**启动run**");
		isStart = true;
		while (isStart) {
			try {
				Log.i("AAA", "**启动Socket**");
				socket = new Socket(ip, portNumber);

//				socket.setSoTimeout(timeout);	//接收不到服务器数据

				handler.sendEmptyMessage(SOCKET_CONNET_NETWORK_DISMISS);
				isStart = false;
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				os = socket.getOutputStream();

				// 启动一条线程来读取服务器响应的数据
				new Thread() {
					public void run() {
						String content = null;
						try {
							while ((content = br.readLine()) != null) {
								// 读取数据
								Log.d("AAA", "服务器的数据content："+ content);
								handler.sendEmptyMessage(SOCKET_TOAST_MESSAGE);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}.start();
				Looper.prepare();
				// 接收用户需发送的 数据
				revHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.what == SOCKET_SEND_MESSAGE) {
							try {
								os.write(msg.obj.toString().getBytes("utf-8"));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				Looper.loop();
			} catch (SocketTimeoutException e2) {
				// 网络连接超时抛出异常
				Log.i("AAA", "网络连接超时:e2");
			} catch (Exception e3) {
				e3.printStackTrace();
				Log.i("AAA", "网络连接异常:e3");
				handler.sendEmptyMessage(SOCKET_BROKEN_NETWORK_SHOW);
			}
		}
	}

	/**
	 * 判断Socket是否连接
	 */
	public Boolean isConnectSocket(Socket socket) {
		if (!socket.isClosed()) {
			if (socket.isConnected()) {
				return true;// 连接中
			}
		}
		return false;// 关闭
	}
	
	public static boolean setStart(boolean is){
		isStart = is;
		return isStart;
	}
}
