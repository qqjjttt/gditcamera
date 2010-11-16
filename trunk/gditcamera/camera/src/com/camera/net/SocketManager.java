package com.camera.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.camera.picture.CutFileUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SocketManager {
	
	public static final String TAG = "SocketManager";
	
	public static final int PACKAGE_SEND_FAIL = -1;
	public static final int PACKAGE_SEND_SUCCESS = 1;
	public static final int TIME_OUT = 0;
	public static final int FINISH_UPLOAD_FILE = 2;
	
	/** �����������߳�˯��ʱ��*/
	private static final int RECEIVE_THREAD_SLEEP_TIME = 500;
	
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
	/** �жϰ��Ƿ��Ѿ����ͣ����ȷ���Ѿ����ͣ��ٷ�����һ����*/
	private int isFinish = 1;
	
	/** �ļ���Ƭ����*/
	private CutFileUtil mCutFileUtil;
	
	private Context context;
	

	public SocketManager(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
		openSocketThread();
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
				synchronized (this) {
					
					Log.i(TAG, "run....");
					byte[] dataBuf = new byte[CutFileUtil.pieceSize];
					int length = 0;
					//����Ƭ������һƬƬ��ȡ�ļ������ϴ���������
					int i = 0;
					while((length = mCutFileUtil.getNextPiece(dataBuf)) != -1) {
						System.out.println(++i);
						//�����������δȷ�ϰ����ͳɹ������ڵȴ�״̬
						while(isFinish == 0) {
							this.sleep(5);
							continue;
						//������ȷ�Ϸ��ʹ���
						}
						Log.i(TAG, "send " + i + "piece");
						//��ʶδ���յ�
						isFinish = 0;
						out.write(dataBuf, 0, length);
						handler.removeCallbacks(this);
						Log.e(TAG, "hased send the paskage!");
					}
					//�ļ��ϴ���,֪ͨ�����Ѿ��ϴ�����һ���ļ�
					Message msg = new Message();
					msg.what = FINISH_UPLOAD_FILE;
					handler.sendMessage(msg);
					
					//�ͻ��˷�������
//					this.stop();
				}
			} catch (Exception e) {
				Log.e(TAG, "failse to send the data to the server!");
//				Toast.makeText(context, "��������ʧ�ܣ�", Toast.LENGTH_SHORT);
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
				byte[] recDataBuf = new byte[14];
				int length = 0;
				synchronized (this) {
					while(true) {
						if(socket.isClosed()) {
							this.sleep(RECEIVE_THREAD_SLEEP_TIME);
							continue;
						}
						if((length = in.read(recDataBuf)) == -1) {
							this.sleep(RECEIVE_THREAD_SLEEP_TIME);
							Log.v(TAG, "length : " + length);
							continue;
						}
						Log.e(TAG, "length : " + length);
						Message msg = new Message();
						//������ȷ�������ͳɹ�
						if(recDataBuf[1] == 0x4F && recDataBuf[2] == 0x4B) {
							msg.what = PACKAGE_SEND_SUCCESS;
							handler.sendMessage(msg);
							//��ʶ�����ͳɹ�
							isFinish = 1;
							
						//������ȷ��������ʧ��
						} else if(recDataBuf[1] == 0X45 && recDataBuf[2] == 0X52) {
							msg.what = PACKAGE_SEND_FAIL;
							handler.sendMessage(msg);
							//��ʶ������ʧ��
							isFinish = 2;
						}
						//��ӡ���ص�����
						for(int i = 1; i < 3; i++){
							Log.e("recDataBuf["+i+"]=", Integer.toHexString((int)recDataBuf[i]));
						}
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "throw exception while receive data from server");
//				Toast.makeText(context, "���շ�������ݳ����쳣��", Toast.LENGTH_SHORT);
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
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "file to connect the server");
//			Toast.makeText(context, "���ӷ�����ʧ�ܣ�", Toast.LENGTH_SHORT);
		}
	}
	
	public void sendFile(CutFileUtil cutFileUtil) {
		//�����˴�������
		handler.post(sendThread);
		this.mCutFileUtil = cutFileUtil;
		sendThread.start();
	}

}
