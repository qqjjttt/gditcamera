package com.camera.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.picture.CutFileUtil;

public class UploadFile extends Thread {
	
	public static final String TAG = "UploadFile";
	
	public static final int FINISH_UPLOAD_FILE = 1;

	/** �ļ���Ƭ����*/
	private CutFileUtil mCutFileUtil;
	/** �׽����߳�*/
	private SocketManager mSocketManger;
	/** ����handler*/
	private Handler mUIHandler;
	private Context mContext;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			//�����ͳɹ��Ĵ���
			case SocketManager.PACKAGE_SEND_SUCCESS:
				Log.i(TAG, "PACKAGE_SEND_SUCCESS");
				break;
			//������ʧ�ܵĴ���
			case SocketManager.PACKAGE_SEND_FAIL:
				Log.i(TAG, "PACKAGE_SEND_FAIL");
				break;
			//�����ͳ�ʱ�Ĵ���
			case SocketManager.TIME_OUT:
				Log.i(TAG, "TIME_OUT");
				break;
			}
		}
		
	};
	
	public UploadFile(Context context, Handler handler) {
		this.mContext = context;
		this.mUIHandler = handler;
		mSocketManger = new SocketManager(mContext, handler);
	}
	
	@Override
	public void run() {
		Log.i(TAG, "run....");
		byte[] dataBuf = new byte[CutFileUtil.pieceSize];
		int length = 0;
		//����Ƭ������һƬƬ��ȡ�ļ������ϴ���������
		int i = 0;
		while((length = mCutFileUtil.getNextPiece(dataBuf)) != -1) {
			Log.i(TAG, "send " + i++ + "piece");
			mSocketManger.send(dataBuf, length);
		}
		//�ļ��ϴ���,֪ͨ�����Ѿ��ϴ�����һ���ļ�
		Message msg = new Message();
		msg.what = FINISH_UPLOAD_FILE;
		mUIHandler.sendMessage(msg);
	}

	/**
	 * �ϴ��ļ�
	 * @param cutFileUtil �ļ���Ƭ����
	 */
	public void upload(CutFileUtil cutFileUtil) {
		this.mCutFileUtil = cutFileUtil;
		mUIHandler.post(this);
		this.start();
	}
}
