package com.camera.picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.camera.activity.UploadFileActivity;
import com.camera.net.DataHeadUtil;
import com.camera.util.StringUtil;
import com.camera.vo.Constant;


public class CutFileUtil {
	
	public static final String TAG = "CutFileUtil";
	
	public static final int MSG_EXIST_PIECE = 30;
	
	/** ��Ƭ�Ĵ�С*/
	public int pieceSize = 1000;
	/** ��ͷ��Ϣ*/
	private static byte[] packageHead;
	
	/** �ļ���*/
	private String filePath;
	
	/** �ļ�·��*/
	private File file;
	
	/** ��ǰ��Ƭ����*/
	private int pieceNum = 1;
	
	/** ��Ƭ����*/
	private int totalPieceNum = 0;
	private int secondTotalPieceNum = 0;
	
	/** �ļ���С,��λΪbyte*/
	private int fileSize;
	
	/** ��ǰ��ȡ���ļ���Ƭ����*/
	private int nCurrentPiece = 0;
	private int nSecCurrentPiece = 0;
	
	/** �ļ���Ƭ������*/
	private List<String> pieceFiles;
	
	/** ��ʶ�Ƿ��һ����װ��*/
	private boolean isFirst = true;
	
	private Handler handler;
	
	private int progress = 0;
	
	/** ��Ƭ����*/
	private String description;
	
	/** Context����*/
	private Context context;
	
	/** ������Ƭ���ڵ�����£��Ƿ�ʹ���������Ƭ��������������*/
	public static boolean IS_SEND_LAST_PIECE = true;
	
	public CutFileUtil(Context context, String filePath, Handler handler, String description) throws FileNotFoundException {
		IS_SEND_LAST_PIECE = true;
		this.description = description;
		this.handler = handler;
		this.context = context;
		this.filePath = filePath;
		file = new File(filePath);
		if(!file.exists()) {
			throw new FileNotFoundException("File not exist!");
		}
		pieceSize = calculatePieceSize(file);
		pieceFiles = new ArrayList<String>();
		//���ͼƬ�Ƿ��Ѿ�����Ƭ
		if(isExistPieces()) {
			handler.sendEmptyMessage(MSG_EXIST_PIECE);
			try {
				Thread.sleep(1000000000);
			} catch (InterruptedException e) {
				if(IS_SEND_LAST_PIECE) {
					return;
				}
				pieceFiles = null;
			}
		}
		cutFile();
	}
	
	/**
	 * �����Ƿ�������Ƭ���� 
	 * @return
	 */
	public boolean isExistPieces() {
		String pieceName = StringUtil.convertFolderPath(filePath) + "_";
		int count = 0;
		int count2 = 0;
		Log.i(TAG, "Conver to piece name :" + pieceName);
		File folder = new File(Constant.PIECE_FOLDER);
		File[] files = folder.listFiles();
		String fileName = null;
		int i, j, min1 = 10000000, min2 = 1000000;
		try {
			for(File file : files) {
				fileName = file.getName();
				int length = fileName.length();
				if(!fileName.contains(pieceName)) {
					continue;
				}
				i = Integer.parseInt(fileName.substring(length - 1));
				j = Integer.parseInt(fileName.substring(length - 3, length - 2));
				if(i == 1) {
					if(j <= min1) {
						min1 = j;
					}
					count ++;
				} else if(i == 2) {
					if(j <= min2)
						min2 = j;
					count2 ++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(count > 0) {
			totalPieceNum = count;
			for(i = min1; i <= count; i ++) {
				pieceFiles.add(Constant.PIECE_FOLDER + pieceName);
				Log.i(TAG, "pieceFiles[" + i + "] :" + Constant.PIECE_FOLDER + pieceName + i);
			}
		}
		if(count2 > 0) {
			secondTotalPieceNum = count2;
			for(i = min2; i <= count2; i ++) {
				pieceFiles.add(Constant.PIECE_FOLDER + pieceName);
				Log.i(TAG, "pieceFiles[" + i + "] :" + Constant.PIECE_FOLDER + pieceName + i);
			}
		}
		if(count > 0 || count2 > 0)
			return true;
		return false;
	}
	
	/**
	 * �ļ���Ƭ
	 */
	private void cutFile() {
		try {
			FileInputStream in = new FileInputStream(filePath);
			fileSize = in.available();
			//������Ƭ����
			int count = fileSize / pieceSize;
			totalPieceNum = (fileSize % pieceSize == 0) ? count : count + 1;
			secondTotalPieceNum = totalPieceNum;
			Log.d(TAG, "total piece count : " + totalPieceNum + "; fileSize = " + fileSize);
			
			byte[] buf = new byte[pieceSize];
			int pieceNum = 1;
			int dataSize = 0;
			while(true) {
				if((dataSize = in.read(buf, 0, pieceSize - 90)) > 0) {
					packagePiece(buf, pieceNum, dataSize);
					pieceNum ++ ;
					continue;
				} 
				break;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��װ��Ƭ��
	 * @param buf �ļ���Ƭ��
	 * @param pieceNum ��ǰ����
	 * @param dataSize ���ݳ���
	 * @throws IOException
	 */
	private void packagePiece(byte[] buf, int pieceNum, int dataSize) throws IOException {
		String pieceName = Constant.PIECE_FOLDER + StringUtil.convertFolderPath(filePath) + "_" +  pieceNum;
		Log.v(TAG, "piece file name : " + pieceName);
		FileOutputStream out1 = new FileOutputStream(pieceName + "_1");
		FileOutputStream out2 = new FileOutputStream(pieceName + "_2");
		pieceFiles.add(pieceName);
		Log.i(TAG, "totalPieceNum : " + totalPieceNum + "; pieceNum" + pieceNum + "; dataSize " + dataSize);
		try {
			if(isFirst) {
				packageHead = DataHeadUtil.getBytesHeadData(context, description, pieceNum, totalPieceNum, dataSize, false);
				isFirst = false;
			} else {
				packageHead = DataHeadUtil.getBytesHeadData(context, "", pieceNum, totalPieceNum, dataSize, true);
			}
		} catch (Exception e) {
//			Toast.makeText(context, "ת����ͷ��Ϣ����", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		out1.write(packageHead);
		out1.write(buf, 0, dataSize);
		out1.close();
		out2.write(packageHead);
		out2.write(buf, 0, dataSize);
		out2.close();
		Message msg = new Message();
		msg.obj = (Integer)(pieceNum  * 100 / totalPieceNum);
		msg.what = UploadFileActivity.PROGRESS_DIALOG;
		handler.sendMessage(msg);
	}
	
	/**
	 * ��ȡ��Ƭ�ļ��ֽ���
	 * @param buf ΪNULL�ȿɣ��ڲ���Ϊbuf�Զ������ڴ�ռ�
	 * @return -1��ʾ�ļ��Ѿ���ȡ��,���򷵻ض�ȡbuf�Ĵ�С
	 * @throws IOException 
	 */
	public int getNextPiece(byte[] buf, int i) {
		//����ļ��������ڣ�˵���Ѿ����꣬�򷵻�-1
		if(nCurrentPiece >= pieceFiles.size())
			return -1;
		String fileName = pieceFiles.get(nCurrentPiece) + "_" + i;
		
		FileInputStream in;
		int pieceSizeTmp = 0;
		try {
			in = new FileInputStream(fileName);
			pieceSizeTmp = in.available();
			in.read(buf);
		} catch (Exception e) {
//			Toast.makeText(context, "��ȡ��Ƭʧ�ܣ�", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		
		nCurrentPiece ++;
		return pieceSizeTmp;
	}
	
	/**
	 * ���»�ȡ��Ƭ�ļ��ֽ���
	 * @param buf ΪNULL�ȿɣ��ڲ���Ϊbuf�Զ������ڴ�ռ�
	 * @return -1��ʾ�ļ��Ѿ���ȡ��,���򷵻ض�ȡbuf�Ĵ�С
	 * @throws IOException 
	 */
	public int getCurrentPiece(byte[] buf, int i) throws IOException {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//����ļ������ڣ�˵���Ѿ����꣬�򷵻�-1
		if(fileName == null)
			return -1;
		fileName = fileName  + "_" + i;
		FileInputStream in = new FileInputStream(fileName);
		int pieceSize = in.available();
		in.read(buf);
		return pieceSize;
	}
	
	/**
	 * ɾ����ǰ�Ѷ�ȡ�õ��ļ�
	 * @return
	 */
	public boolean removeCurrentFile(int i) {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//����ļ������ڣ�˵���Ѿ����꣬�򷵻�-1
		if(fileName == null)
			return false;
		fileName += fileName + "_" + i;
		File file = new File(fileName);
		return file.delete();
	}
	
//	public void removeAllPieceFile() {
//		for(String filePath : pieceFiles) {
//			File file = new File(filePath);
//			file.delete();
//		}
//	}

	public int getTotalPieceNum() {
		return totalPieceNum;
	}

	public void setTotalPieceNum(int totalPieceNum) {
		this.totalPieceNum = totalPieceNum;
	}
	
	/**
	 * �����ļ���С�����ļ���Ƭ�Ĵ�С
	 */
	private int calculatePieceSize(File file) {
		long fileSize = file.length();
		if(fileSize > 4 * 1024 * 1024) {
			return 80000;
		} else if(fileSize > 2 * 1024 * 1024) {
			return 50000;
		} else if(fileSize > 1024 * 1024) {
			return 40000;
		} else if(fileSize > 1024 * 512) { 
			return 15000;
		} else if(fileSize > 1024 * 100) { 
			return 10000;
		} else if(fileSize > 1024 * 50) {
			return 8000;
		} else if(fileSize > 1024 * 30) {
			return 5000;
		} else if(fileSize > 1024 * 20) {
			return 3000;
		} else if(fileSize > 1024 * 10) {
			return 2000;
		} else if(fileSize > 1024 * 5) {
			return 1000;
		}
		return 1000;
	}
	
	/**
	 * ��ʼ������Ƭ����һ��������
	 */
	public void changeNext() {
		this.nCurrentPiece = 0;
		this.totalPieceNum = this.secondTotalPieceNum;
	}
	
}
