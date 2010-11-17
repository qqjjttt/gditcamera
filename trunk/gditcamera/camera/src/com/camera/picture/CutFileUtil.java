package com.camera.picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.camera.net.DataHeadUtil;
import com.camera.util.Constant;
import com.camera.vo.DataHead;


public class CutFileUtil {
	
	public static final String TAG = "CutFileUtil";
	
	/** ��Ƭ�Ĵ�С*/
	public static final int pieceSize = 5000;
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
	
	/** �ļ���С,��λΪbyte*/
	private int fileSize;
	
	/** ��ǰ��ȡ���ļ���Ƭ����*/
	private int nCurrentPiece = 0;
	
	/** �ļ���Ƭ������*/
	private List<String> pieceFiles;
	
	/** ��ʶ�Ƿ��һ����װ��*/
	private boolean isFirst = true;
	
	/** Context����*/
	private Context context;
	
	public CutFileUtil(Context context, String filePath) throws Exception {
		this.context = context;
		this.filePath = filePath;
		file = new File(filePath);
		if(!file.exists()) {
			throw new Exception("File not exist!");
		}
		pieceFiles = new ArrayList<String>();
		cutFile();
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
			Log.d(TAG, "total piece count : " + totalPieceNum + "; fileSize = " + fileSize);
			
			byte[] buf = new byte[pieceSize];
			int pieceNum = 1;
			int dataSize = 0;
			while(true) {
				if((dataSize = in.read(buf, 0, pieceSize - 90)) > 0) {
					Log.d(TAG, "dataSize " + dataSize);
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
		String pieceName = Constant.PIECE_FOLDER + pieceNum;
		FileOutputStream out = new FileOutputStream(pieceName);
		pieceFiles.add(pieceName);
		System.out.println("dataSize:" + dataSize);
		Log.i(TAG, "totalPieceNum : " + totalPieceNum + "; pieceNum" + pieceNum + "; dataSize" + dataSize);
//		try {
//			if(isFirst) {
//				packageHead = DataHeadUtil.getBytesHeadData("��Ƭ����", pieceNum, totalPieceNum, dataSize, true);
//				isFirst = false;
//			}
//			else
//				packageHead = DataHeadUtil.getBytesHeadData("", pieceNum, totalPieceNum, dataSize, false);
//		} catch (Exception e) {
//			Toast.makeText(context, "ת����ͷ��Ϣ����", Toast.LENGTH_SHORT);
//			e.printStackTrace();
//		}
//		out.write(packageHead);
		out.write(buf, 0, dataSize);
		out.close();
	}
	
	/**
	 * ��ȡ��Ƭ�ļ��ֽ���
	 * @param buf ΪNULL�ȿɣ��ڲ���Ϊbuf�Զ������ڴ�ռ�
	 * @return -1��ʾ�ļ��Ѿ���ȡ��,���򷵻ض�ȡbuf�Ĵ�С
	 * @throws IOException 
	 */
	public int getNextPiece(byte[] buf) {
		//����ļ��������ڣ�˵���Ѿ����꣬�򷵻�-1
		if(nCurrentPiece >= pieceFiles.size())
			return -1;
		String fileName = pieceFiles.get(nCurrentPiece);
		
		FileInputStream in;
		int pieceSizeTmp = 0;
		try {
			in = new FileInputStream(fileName);
			pieceSizeTmp = in.available();
			in.read(buf);
		} catch (Exception e) {
			Toast.makeText(context, "��ȡ��Ƭʧ�ܣ�", Toast.LENGTH_SHORT);
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
	public int getCurrentPiece(byte[] buf) throws IOException {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//����ļ������ڣ�˵���Ѿ����꣬�򷵻�-1
		if(fileName == null)
			return -1;
		FileInputStream in = new FileInputStream(fileName);
		int pieceSize = in.available();
		in.read(buf);
		return pieceSize;
	}
	
	/**
	 * ɾ����ǰ�Ѷ�ȡ�õ��ļ�
	 * @return
	 */
	public boolean removeCurrentFile() {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//����ļ������ڣ�˵���Ѿ����꣬�򷵻�-1
		if(fileName == null)
			return false;
		File file = new File(fileName);
		return file.delete();
	}
	
}
