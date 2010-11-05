package com.camera.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.vo.DataHead;

/**
 * �ͻ����̣߳��������߳��ϴ��ļ�
 * @author tian
 *
 */
public class ClientThread extends Thread {
	
	private Socket clientSocket;
	private Handler handler;
	private InputStream fromServer;
	private OutputStream toServer;
	private DataHead dataHead;
	private DataInputStream dataIn;
	private InputStreamReader dataFromServer;
	
	/**
	 * ���캯��
	 * @param socket ���������ӵ�socket
	 * @param dataHead ÿ�����ݰ��İ�ͷ
	 * @param dataIn ���ϴ����ļ���������
	 */
	public ClientThread(Socket socket,Handler handler,DataHead dataHead,DataInputStream dataIn){
		this.clientSocket = socket;
		this.handler = handler;
		this.dataHead = dataHead;
		this.dataIn = dataIn;
	}
	
	@Override
	public void run() {
			try {
			toServer = clientSocket.getOutputStream();
			fromServer = clientSocket.getInputStream();
			byte [] b = new byte[1024];
			int len = -1;
			
			//��д������ͷ
			toServer.write(DataHeadUtil.dataHead2Byte(this.dataHead));
			while((len = dataIn.read(b))!=-1){
				toServer.write(b, 0, len);
			}
			toServer.close();
			
			//�ٻ�ȡ��������Ӧ
			Log.d("waitting for server...", ".....");
			dataFromServer = new InputStreamReader(fromServer,"GB2312");
			BufferedReader buf = new BufferedReader(dataFromServer);
			String data = null;
			String result = "";
			while((data = buf.readLine()) != null){
				result += data;
			}
			Log.d("finish for server...", "OK");
			
			//��󷵻����ݵ�Ui����
			Bundle dataBundle = new Bundle();
			Message msg = handler.obtainMessage();
			dataBundle.putString("result", result);
			msg.setData(dataBundle);
			handler.sendMessage(msg);
			
			dataIn.close();
//			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}