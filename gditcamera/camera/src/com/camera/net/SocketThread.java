package com.camera.net;

import java.net.Socket;

import android.os.Handler;

public class SocketThread extends Thread {
	
	private static final String tag = "ServerActivity";
	private static final String HOST = "112.125.33.161";
	private static final int PORT = 10808;
	
	/** �ͻ���SOCKET����*/
	private Socket clientSocket;
	/** ����ͨ�Ŵ������*/
	private Handler handler;
	

	public SocketThread(Handler handler){
		this.handler = handler;
	}
	
	@Override
	public void run() {
			try {
		
			//�����˴�������
			new Thread() {
				
				@Override
				public void run(){
					try {
						//�ͻ��˷�������
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			
			
			//��ȡ��������Ӧ��Ϣ
			new Thread() {
				
				@Override
				public void run(){
					try {
						//���շ��������
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
