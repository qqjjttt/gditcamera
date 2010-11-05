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
			Log.d("uploadSize", uploadSize+"");
			FileOutputStream toSdCard = new FileOutputStream(new File(savePath));

			//�ȶ�ȡͷ������
			byte [] dataHeadBytes = new byte[122];
			finishBytes+=fromClient.read(dataHeadBytes);
			dataHead = DataHeadUtil.byte2DataHead(dataHeadBytes);
			uploadSize = dataHead.getDataLength();

			//�ٶ�ȡ�ϴ��ļ�������
			byte [] b = new byte[1024];
			int len = -1;
			while((len=fromClient.read(b))!=-1){
				toSdCard.write(b, 0, len);
				finishBytes+=len;
			}
			Log.d("Upload Success !!finishBytes", finishBytes+"");
			DataOutputStream toClient = new DataOutputStream(serverSocket.getOutputStream());
			toClient.write("�ϴ��ɹ�".getBytes("GB2312"));
			toClient.close();
			
			//�ж��Ƿ��ϴ��ɹ�
			if(returnActionListener!=null){
				returnActionListener.OnReturnAction(savePath, dataHead, (finishBytes==uploadSize ? true : false));
			}
			toSdCard.close();
			serverSocket.close();
			
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
