package com.camera.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SocketManager {
	
	public static final int PACKAGE_SEND_FAIL = -1;
	public static final int PACKAGE_SEND_SUCCESS = 1;
	public static final int TIME_OUT = 0;
	
	private static final String tag = "ServerActivity";
	private static final String HOST = "112.125.33.161";
	private static final int PORT = 10808;
	
	/** �ͻ���SOCKET����*/
	private Socket socket;
	/** ����ͨ�Ŵ������*/
	private Handler handler;
	/** Socket->InputStream����*/
	private InputStream in;
	/** Socket->OutputStream����*/
	private OutputStream out;
	/** Ҫ���͸�������������*/
	private byte[] dataBuf;
	/** ��������ʱҪ��ȡ��BUF����*/
	private int bufLength = 0;
	/** ���ظ�Handler����Ϣ*/
	private Message msg;
	private Context context;
	

	public SocketManager(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
		//��SOCKET�׽���
		handler.post(receiveThread);
		receiveThread.start();
	}
	
	/**
	 * �׽��ַ����߳�
	 */
	private Thread sendThread = new Thread() {
		@Override
		public void run(){
			try {
				//�ͻ��˷�������
				out = socket.getOutputStream();
				out.write(dataBuf, 0, bufLength);
				out.close();
				handler.removeCallbacks(this);
			} catch (Exception e) {
				Toast.makeText(context, "��������ʧ�ܣ�", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * �׽��ֽ����߳�
	 */
	private Thread receiveThread = new Thread() {
		@Override
		public void run(){
			try {
				//���������
				in = socket.getInputStream();
				byte[] recDataBuf = new byte[14];
				while(true) {
					int length = in.read(recDataBuf);
					//������ȷ�������ͳɹ�
					if(recDataBuf[1] == 0x4F && recDataBuf[2] == 0x4B) {
						msg.what = PACKAGE_SEND_SUCCESS;
						handler.sendMessage(msg);
						
					//������ȷ��������ʧ��
					} else if(recDataBuf[1] == 0X45 && recDataBuf[2] == 0X52) {
						msg.what = PACKAGE_SEND_FAIL;
						handler.sendMessage(msg);
					}
					//��ӡ���ص�����
					for(int i = 0; i < length; i++){
						Log.e("recDataBuf["+i+"]=", Integer.toHexString((int)recDataBuf[i]));
					}
				}
			} catch (Exception e) {
				Toast.makeText(context, "���շ�������ݳ����쳣��", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * ��SOCKET�׽���
	 */
	public void openSocketThread() {
		try {
			socket = new Socket(HOST,PORT);
			out = socket.getOutputStream();
		} catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "���ӷ�����ʧ�ܣ�", Toast.LENGTH_SHORT);
		}
	}
	
	public void send(byte[] dataBuf, int length) {
		//�����˴�������
		this.dataBuf = dataBuf;
		this.bufLength = length;
		handler.post(sendThread);
		sendThread.start();
	}

}
