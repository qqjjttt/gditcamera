package com.camera.util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.camera.vo.DataHead;

/**
 * �ͻ����̣߳��������߳��ϴ��ļ�
 * @author tian
 *
 */
public class ClientThread extends Thread {
	
	private Socket clientSocket;
	private InputStream fromServer;
	private OutputStream toServer;
	private DataHead dataHead;
	private DataInputStream dataIn;
	
	/**
	 * ���캯��
	 * @param socket ���������ӵ�socket
	 * @param dataHead ÿ�����ݰ��İ�ͷ
	 * @param dataIn ���ϴ����ļ���������
	 */
	public ClientThread(Socket socket,DataHead dataHead,DataInputStream dataIn){
		this.clientSocket = socket;
		this.dataHead = dataHead;
		this.dataIn = dataIn;
	}
	
	@Override
	public void run() {
			try {
			fromServer = clientSocket.getInputStream();
			toServer = clientSocket.getOutputStream();
			byte [] b = new byte[1024];
			int len = -1;
			//��д������ͷ
			toServer.write(DataHeadUtil.dataHead2Byte(this.dataHead));
			while((len = dataIn.read(b))!=-1){
				toServer.write(b, 0, len);
			}
			dataIn.close();
			toServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}