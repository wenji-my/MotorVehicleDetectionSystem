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
 * @������SocketClient
 * @author:�㶫��ʤ�Ƽ����޹�˾
 * @ʵ�ֹ��ܣ�socket��������
 * @�������ڣ�2016.07.12
 */
@SuppressLint("HandlerLeak")
public class SocketClient implements Runnable {
	private Socket socket;
	private Handler handler;// ��UI�̷߳�����Ϣ��Handler����
	public Handler revHandler;// ����UI�̵߳���Ϣ��Handler����
	private BufferedReader br = null;
	private OutputStream os = null;
	private String ip;// IP��ַ
	private int portNumber;// �˿ں�
	private int timeout;// ��ʱʱ��
	private static boolean isStart;
	private int SOCKET_TOAST_MESSAGE = StaticConst.SOCKETCLIENT_TOAST_MESSAGE;// �����ݽ���ȷ����
	private int SOCKET_RECEIVE_MESSAGE = StaticConst.SOCKETCLIENT_RECEIVE_MESSAGE;// ������Ϣ
	private int SOCKET_SEND_MESSAGE = StaticConst.SOCKETCLIENT_SEND_MESSAGE;// ������Ϣ
	private int SOCKET_BROKEN_NETWORK_SHOW = StaticConst.INSTRUMENTDETECTION_BROKEN_NETWORK_SHOW;// socket�Ͽ�
	private int SOCKET_CONNET_NETWORK_DISMISS = StaticConst.INSTRUMENTDETECTION_CONNET_NETWORK_DISMISS;// socket����

	public SocketClient(Handler handler, String ip, int portNumber, int timeout) {
		Log.i("AAA", "**��ʼ������**");
		this.handler = handler;
		this.ip = ip;
		this.portNumber = portNumber;
		this.timeout = timeout;
	}

	@Override
	public void run() {

		Log.i("AAA", "**����run**");
		isStart = true;
		while (isStart) {
			try {
				Log.i("AAA", "**����Socket**");
				socket = new Socket(ip, portNumber);

//				socket.setSoTimeout(timeout);	//���ղ�������������

				handler.sendEmptyMessage(SOCKET_CONNET_NETWORK_DISMISS);
				isStart = false;
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				os = socket.getOutputStream();

				// ����һ���߳�����ȡ��������Ӧ������
				new Thread() {
					public void run() {
						String content = null;
						try {
							while ((content = br.readLine()) != null) {
								// ��ȡ����
								Log.d("AAA", "������������content��"+ content);
								handler.sendEmptyMessage(SOCKET_TOAST_MESSAGE);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}.start();
				Looper.prepare();
				// �����û��跢�͵� ����
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
				// �������ӳ�ʱ�׳��쳣
				Log.i("AAA", "�������ӳ�ʱ:e2");
			} catch (Exception e3) {
				e3.printStackTrace();
				Log.i("AAA", "���������쳣:e3");
				handler.sendEmptyMessage(SOCKET_BROKEN_NETWORK_SHOW);
			}
		}
	}

	/**
	 * �ж�Socket�Ƿ�����
	 */
	public Boolean isConnectSocket(Socket socket) {
		if (!socket.isClosed()) {
			if (socket.isConnected()) {
				return true;// ������
			}
		}
		return false;// �ر�
	}
	
	public static boolean setStart(boolean is){
		isStart = is;
		return isStart;
	}
}
