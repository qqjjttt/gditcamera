package com.camera.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.camera.picture.CutFileUtil;

public class UploadFile extends Thread {
	
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
				break;
			//������ʧ�ܵĴ���
			case SocketManager.PACKAGE_SEND_FAIL:
				break;
			//�����ͳ�ʱ�Ĵ���
			case SocketManager.TIME_OUT:
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
		byte[] dataBuf = new byte[CutFileUtil.pieceSize];
		int length = 0;
		//����Ƭ������һƬƬ��ȡ�ļ������ϴ���������
		while((length = mCutFileUtil.getNextPiece(dataBuf)) != -1) {
			mSocketManger.send(dataBuf, length);
		}
		//�ļ��ϴ���,֪ͨ�����Ѿ��ϴ�����һ���ļ�
		Message msg = new Message();
		msg.what = 1;
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
