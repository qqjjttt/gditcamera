package com.camera.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.camera.vo.DataHead;

import android.os.Environment;
import android.util.Log;
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
			InputStream fromClient = this.serverSocket.getInputStream();
			FileOutputStream toSdCard = new FileOutputStream(new File(savePath));

			boolean finished = false;
			boolean readHead = false;
			byte [] b = new byte[122];
			int len = -1;
//			finishBytes+=fromClient.read(dataHeadBytes);

			//�ȴ��ͻ����ϴ����ݣ���Ϻ��˳�ѭ��
			while(true){
				
				if(!finished){
					//�ٶ�ȡ�ϴ��ļ�������
					while((len=fromClient.read(b))!=-1){
						//�ȶ�ȡͷ������
						if(!readHead){
							dataHead = DataHeadUtil.byte2DataHead(b);
							uploadSize = dataHead.getDataLength();
							Log.d("uploadSize", uploadSize+"");
							readHead = true;
						}else{
							toSdCard.write(b, 0, len);
//							Log.d("Upload Success !!finishBytes", finishBytes+"");
							DataOutputStream toClient = new DataOutputStream(serverSocket.getOutputStream());
							toClient.write("�ϴ��ɹ�".getBytes("GB2312"));
							toClient.flush();
						}
						finishBytes+=len;
					}
				}else{
					//do other thing
					try {
						Thread.sleep(2000);
						Log.d("waitting", ".....");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			

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
