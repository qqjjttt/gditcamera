package com.camera.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Environment;
import android.util.Log;

import com.camera.vo.DataHead;
/**
 * �����߳�: �������пͻ���
 * @author tian
 *
 */
public class ServiceThread implements Runnable { 

	private static final String tag = "ClientThread";
	
	private Socket serverSocket;
	private ReturnActionListener returnActionListener;

	public ServiceThread(Socket socket){
		this.serverSocket = socket;
	}
	
	@Override
	public void run() {
		int finishBytes = 0;
		int uploadSize = -1;
		String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/99.png";
		DataHead dataHead ;
		
		try {
			final InputStream fromClient = this.serverSocket.getInputStream();
			final OutputStream toClient = this.serverSocket.getOutputStream();
			FileOutputStream toSdCard = new FileOutputStream(new File(savePath));

			boolean finished = false;
			boolean readHead = false;
			byte [] b = new byte[122];
			int len = -1;
//			finishBytes+=fromClient.read(dataHeadBytes);

			//�ȴ��ͻ����ϴ����ݣ���Ϻ��˳�ѭ��
//			while(true){
//				
//				while((len=fromClient.read(b))!=-1){
//					Log.d("fromClient:", new String(b,0,len,"GB2312"));
//				}
//				
//			}
			
			//��ͻ��˷�����Ϣ
			new SenderThread(){

				@Override
				public void action() {
					try {
						toClient.write("Msg:[Server=>Client]".getBytes("GB2312"));
//						Log.d("server:", "sending data to Client...");
						Thread.sleep(10000);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}.start();

			//��ȡ�ͻ��˴�������Ϣ
			new RecipientThread(){
				byte [] b = new byte[138];
				int len = -1;
				
				@Override
				public void action() {
					try {
						len=fromClient.read(b);
						Log.e("=======Received the data:=====","");
						System.out.println(DataHeadUtil.byte2DataHead(b));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}.start();

//			fromClient.read(b);
//			DataHeadUtil.byte2DataHead(b);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ReturnActionListener getReturnActionListener() {
		return returnActionListener;
	}

	/**
	 * �����ϴ���ɼ�����
	 * @author tian
	 *
	 */
	public void setReturnActionListener(ReturnActionListener returnActionListener) {
		this.returnActionListener = returnActionListener;
	}

	/**
	 * �ϴ���ɼ�����
	 * @author tian
	 *
	 */
	public interface ReturnActionListener{
		/**
		 * �ϴ���ɺ�ص�����
		 * @author tian
		 *
		 */
		public void OnReturnAction(String savedPath,DataHead dataHead,boolean success);
	}
}
