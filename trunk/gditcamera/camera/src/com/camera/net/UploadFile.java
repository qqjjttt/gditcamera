package com.camera.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.activity.UploadFileActivity;
import com.camera.picture.CutFileUtil;
import com.camera.util.PreferencesDAO;
import com.camera.vo.Preferences;

public class UploadFile {
	
	public static final int FIRST_FILE = 1;
	public static final int SECOND_FILE = 2;
	
	public static final String TAG = "UploadFile";
	
	//��������Ϣ
	public static final int PACKAGE_SEND_FAIL = -1;
	public static final int PACKAGE_SEND_SUCCESS = 1;
	public static final int FINISH_UPLOAD_FILE = 2;
	public static final int THROW_EXCEPTION = 3;
	public static final int CONNECTION_FAILSE = 4;
	public static final int CONNECTION_SUCCESS = 5;
	public static final int FINISH_SEND_FIRST_SERVER = 7;
	public static final int DISMISS_DIALOG = 8;
	public static final int FINISH_SEND = 10;
	
	/** ���ӷ�������ʱ*/
	public static final int TIME_OUT = 6;
	
	
	/** ���������ӳ�ʱʱ��*/
	public static final int CONNECT_TIME_OUT = 5000;
	/** �����ļ���ʱ*/
	public static final int SEND_TIME_OUT = 20000;
	
	/** �����������߳�˯��ʱ��*/
	private static final int RECEIVE_THREAD_SLEEP_TIME = 500;
	
	private static final String tag = "ServerActivity";
	private static String HOST = "112.125.33.161";
	private static int PORT = 10808;
	
	private ServiceRecThread receiveThread;
	
	public static int CURRENT_FILE_INDEX;
	
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
	private int isFinish = 0;
	/** ��ʶҪ���͵����ͣ�0Ϊ���Է�������1Ϊ�����ļ���2Ϊ���Ͷ��ļ�*/
	private int sendType = -1;
	
	/** �ļ���Ƭ����*/
	private CutFileUtil mCutFileUtil;
	/** UI���������߳�*/
	private Thread currentThread;
	/** ��ʶ�������Ƿ����ӳɹ�*/

	
	private TimeOutThread timeOutThread;
	
	/** �ϴ��������*/
	private int errorCode;
	
	private Context context;
	

	public UploadFile(Context context, Handler handler, Thread thread){
		CURRENT_FILE_INDEX = FIRST_FILE;
		this.currentThread = thread;
		this.context = context;
		this.handler = handler;
	}
	
	/** ��������������Ƿ�ʱ*/
	private class TimeOutThread extends Thread {
		
		public boolean isConnect = false;
		
		@Override 
		public void run() {
			Log.d(TAG, "Start timeOutThread");
			try {
				Thread.sleep(CONNECT_TIME_OUT);
				if(isConnect == false) {
					handler.sendEmptyMessage(TIME_OUT);
					currentThread.interrupt();
				}
				Log.d(TAG, "stop timeOutThread");
				this.interrupt();
			} catch (InterruptedException e) {
				Log.w(TAG, "Send CONNECTION_FAILSE to the handler");
				handler.sendEmptyMessage(CONNECTION_FAILSE);
				e.printStackTrace();
			}
		}
	};

	public void uploadFile() throws SocketException {
		try {
			openSocketThread();
			receiveThread = new ServiceRecThread();
			receiveThread.start();
			//�������ݸ�������
			synchronized (this) {
				byte[] dataBuf = new byte[mCutFileUtil.pieceSize];
				int length = 0;
				//����Ƭ������һƬƬ��ȡ�ļ������ϴ���������
				int i = 0;
				int total = mCutFileUtil.getTotalPieceNum();
				while((length = mCutFileUtil.getNextPiece(dataBuf)) != -1) {
					Log.d(TAG, "Start send file of " + ++i + " piece");
					//��ʶδ���յ�
					out.write(dataBuf, 0, length);
					Log.e(TAG, "HAS WRITEING------------------------");
					//�����������δȷ�ϰ����ͳɹ������ڵȴ�״̬
					//������ȷ�Ϸ��ͳɹ�
					int time = 0;
					while(isFinish == 0) {
						Thread.sleep(5);
						time += 5;
						//�ж��Ƿ�ʱ
						if(time >= SEND_TIME_OUT) {
							//������ǰ����
							errorCode = TIME_OUT;
							currentThread.interrupt();
						}
						continue;
					}
					//������ȷ�Ϸ��ʹ���
					if(isFinish == 2) {
						errorCode = THROW_EXCEPTION;
						currentThread.interrupt();
					} 
					if(isFinish == 1) {
						//ɾ����ǰ��Ƭ
						mCutFileUtil.removeCurrentFile();
						isFinish = 0;
//						mCutFileUtil.removeCurrentFile();
						Log.i(TAG, "hased send the piece file of " + i + " piece!");
						Message msg = new Message();
						msg.obj = (Integer)(i  * 100 / total);
						msg.what = UploadFileActivity.PROGRESS_DIALOG;
						handler.sendMessage(msg);
					}
				}

				//�ļ��ϴ���,֪ͨ�����Ѿ��ϴ�����һ���ļ�
				handler.sendEmptyMessage(FINISH_UPLOAD_FILE);
				Log.d(TAG, "Finish send a file to the server!");
			}
		} catch (SocketException e) {
			throw new SocketException();
		} catch(InterruptedException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(errorCode);
		} catch (Exception e) {
			Log.e(TAG, "failse to send the data to the server!");
			e.printStackTrace();
			handler.sendEmptyMessage(THROW_EXCEPTION);
			currentThread.interrupt();
		} finally {
			//�ر��߳�
			System.out.println("stop thread.....");
			try {
				receiveThread.interrupt();
				in = null;
				out = null;
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sendType = -1;
		}
	}
	
	/**
	 * �׽��ֽ����߳�
	 */
	private class ServiceRecThread extends Thread {
		@Override
		public void run(){
			try {
				synchronized (this) {
					while(true) {
						//���������
						byte[] recDataBuf = new byte[14];
						int length = 0;
						if(in == null)
							break;
						if((length = in.read(recDataBuf)) == -1) {
							Thread.sleep(RECEIVE_THREAD_SLEEP_TIME);
							continue;
						}
						Message msg = new Message();
						
						Log.e(TAG, "-----------------------------------------");
						for(int i = 0; i < recDataBuf.length; i ++) {
							System.out.printf("0x%x", recDataBuf[i]);
							System.out.println();
						}
						Log.e(TAG, "-----------------------------------------");
						
						//������ȷ�������ͳɹ�
						if(recDataBuf[1] == 0x4F && recDataBuf[2] == 0x4B) {
							msg.what = PACKAGE_SEND_SUCCESS;
							handler.sendMessage(msg);
							//��ʶ�����ͳɹ�
							isFinish = 1;
							Log.d(TAG, "Receive service replay, answer is ok!");
							
						//������ȷ��������ʧ��
						} else if(recDataBuf[1] == 0X45 && recDataBuf[2] == 0X52) {
							Log.w(TAG, "Receive service replay, answer is ERROR!");
							msg.what = PACKAGE_SEND_FAIL;
							handler.sendMessage(msg);
							//��ʶ������ʧ��
							isFinish = 2;
						}
					}
				}
			} catch (InterruptedException e) {
				//�����������ͽ��̴�ϣ���������
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Throw exception while receive data from server");
			} 
		}
		
	};
	
	/**
	 * �ϴ��ļ�
	 * @param cutFileUtil �ļ���Ƭ����
	 */
	public void upload(CutFileUtil cutFileUtil) {
		this.mCutFileUtil = cutFileUtil;
		CURRENT_FILE_INDEX = cutFileUtil.whichService;
		try {
			uploadFile();
		} catch(SocketException e) {}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		handler.sendEmptyMessage(FINISH_SEND_FIRST_SERVER);
		//�����Ƭ�Ѿ������꣬�򷵻أ������û�����꣬�򷢸��ڶ���������
		if(!cutFileUtil.changeNext()) {
			handler.sendEmptyMessage(FINISH_SEND);
			return;
		}
		CURRENT_FILE_INDEX = cutFileUtil.whichService;
		try {
			uploadFile();
		} catch(SocketException e) {}
		
	}
	
	
	/**
	 * ���Է������Ƿ����ӳɹ�
	 * @param packageHead ���ֽ���
	 * @param handler
	 */
	public void testServer(String ip, int port) {
		try {
			timeOutThread = new TimeOutThread();
			timeOutThread.start();
			Socket socket = new Socket(ip, port);
			socket.close();
			timeOutThread.isConnect = true;
			handler.sendEmptyMessage(CONNECTION_SUCCESS);
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "testServer=> failse to connect the service");
			handler.sendEmptyMessage(CONNECTION_FAILSE);
		}
	}
	
	/**
	 * ��SOCKET�׽���
	 */
	private void openSocketThread() throws  SocketException {
		//��ȡ��������ַ���˿�
		PreferencesDAO preferencesDao = new PreferencesDAO(context);
		Preferences p = preferencesDao.getPreferences();
		if(CURRENT_FILE_INDEX == FIRST_FILE) {
			HOST = p.getHost1IP();
			PORT = p.getHost1Port();
			Log.e(TAG, "HOST1:" + HOST + "; PORT2:" + PORT);
		} else if(CURRENT_FILE_INDEX == SECOND_FILE){
			HOST = p.getHost2IP();
			PORT = p.getHost2Port();
			Log.e(TAG, "HOST2:" + HOST + "; PORT2:" + PORT);
		}
		
		if(HOST == null || HOST.equals("")) {
			if(CURRENT_FILE_INDEX == SECOND_FILE) {
				handler.sendEmptyMessage(FINISH_SEND);
			}
			throw new SocketException("socket host is null exception");
		}
		
		try {
			timeOutThread = new TimeOutThread();
			timeOutThread.start();
			Log.d(TAG, "openSocketThread=> start to connect the server!!");
			socket = new Socket(HOST,PORT);
			timeOutThread.isConnect = true;
			in = socket.getInputStream();
			out = socket.getOutputStream();
			handler.sendEmptyMessage(CONNECTION_SUCCESS);
		} catch (SocketException e) {
			e.printStackTrace();
			Log.e(TAG, "SocketException because of time out!");
			throw new SocketException("socket timeout exception");
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "openSocketThread : failse to connect the server");
			handler.sendEmptyMessage(CONNECTION_FAILSE);
		}
	}

}
