package com.camera.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.camera.activity.UploadFileActivity;
import com.camera.picture.CutFileUtil;
import com.camera.util.PreferencesDAO;
import com.camera.vo.Preferences;

public class UploadFile {
	
	public static final String TAG = "SocketManager";
	
	//��������Ϣ
	public static final int PACKAGE_SEND_FAIL = -1;
	public static final int PACKAGE_SEND_SUCCESS = 1;
	public static final int TIME_OUT = 0;
	public static final int FINISH_UPLOAD_FILE = 2;
	public static final int THROW_EXCEPTION = 3;
	public static final int CONNECTION_FAILSE = 4;
	public static final int CONNECTION_SUCCESS = 5;
	
	/** �����������߳�˯��ʱ��*/
	private static final int RECEIVE_THREAD_SLEEP_TIME = 500;
	
	private static final String tag = "ServerActivity";
	private static String HOST = "112.125.33.161";
	private static int PORT = 10808;
	
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
	/** ��ʶҪ���͵����ͣ�0Ϊ���Է�������1Ϊ�����ļ���2Ϊ���Ͷ��ļ�*/
	private int sendType = -1;
	
	/** �ļ���Ƭ����*/
	private CutFileUtil mCutFileUtil;
	
	private Context context;
	

	public UploadFile(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	/**
	 * �׽��ַ����߳�
	 */
	private Thread sendThread = new Thread() {
		
		/**
		 * ��SOCKET�׽���
		 */
		private void openSocketThread() {
			//��ȡ��������ַ���˿�
			PreferencesDAO preferencesDao = new PreferencesDAO(context);
			Preferences p = preferencesDao.getPreferences();
			HOST = p.getHost1IP();
			PORT = p.getHost1Port();
			

			try {
				System.out.println("start to connect the server!!");
				socket = new Socket(HOST,PORT);
				in = socket.getInputStream();
				out = socket.getOutputStream();
				handler.sendEmptyMessage(CONNECTION_SUCCESS);
			} catch(Exception e) {
				e.printStackTrace();
				Log.e(TAG, "file to connect the server");
				handler.sendEmptyMessage(CONNECTION_FAILSE);
			}
		}
		
		@Override
		public void run(){
			try {
				openSocketThread();
				receiveThread.start();
				//�������ݸ�������
				switch(sendType) {
				//���Է������Ƿ����ӵ�ͨ
				case 0:
					System.out.println("test server thread run.....");
					break;
				//���͵��ļ���������
				case 1:
					System.out.println("send file thread run.....");
					sendFile();
					break;
				//���Ͷ��ļ���������
				case 2:
					System.out.println("send some file thread run.....");
					break;
				default:
					break;
				}
			} catch (Exception e) {
				Log.e(TAG, "failse to send the data to the server!");
				e.printStackTrace();
				handler.sendEmptyMessage(THROW_EXCEPTION);
			} finally {
				//�ر��߳�
				System.out.println("stop thread.....");
				this.stop();
				receiveThread.stop();
				sendType = -1;
			}
		}
		
		/**
		 * ���Է�����
		 */
		public void testServer() throws Exception {
			handler.sendEmptyMessage(FINISH_UPLOAD_FILE);
		}
		
		/**
		 * ���͵��ļ�
		 * @throws Exception
		 */
		private void sendFile() throws Exception {
			synchronized (this) {
				byte[] dataBuf = new byte[CutFileUtil.pieceSize];
				int length = 0;
				//����Ƭ������һƬƬ��ȡ�ļ������ϴ���������
				int i = 0;
				while((length = mCutFileUtil.getNextPiece(dataBuf)) != -1) {
					//�����������δȷ�ϰ����ͳɹ������ڵȴ�״̬
					while(isFinish == 0) {
						this.sleep(5);
						continue;
					//������ȷ�Ϸ��ʹ���
					}
					Log.i(TAG, "send " + ++i + " piece");
					//��ʶδ���յ�
					isFinish = 0;
					out.write(dataBuf, 0, length);
					Log.e(TAG, "hased send the paskage!");
				}

				//�ļ��ϴ���,֪ͨ�����Ѿ��ϴ�����һ���ļ�
				handler.sendEmptyMessage(FINISH_UPLOAD_FILE);
				System.out.println("finish send a file to the server!");
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
				synchronized (this) {
					while(true) {
						//���������
						byte[] recDataBuf = new byte[14];
						int length = 0;
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
	 * �ϴ��ļ�
	 * @param cutFileUtil �ļ���Ƭ����
	 */
	public void upload(CutFileUtil cutFileUtil) {
		this.mCutFileUtil = cutFileUtil;
		sendType = 1;
		handler.post(sendThread);
	}
	
	
	/**
	 * ���Է������Ƿ����ӳɹ�
	 * @param packageHead ���ֽ���
	 * @param handler
	 */
	public static boolean testServer(String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "file to connect the server");
			return false;
		}
		return true;
	}

}
