package com.camera.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;


public class CutFileUtil {
	
	/** ��Ƭ�Ĵ�С*/
	public static final int pieceSize = 2000;
	/** ��ͷ��Ϣ*/
	private static byte[] packageHead;
	
	/** �ļ���*/
	private String filePath;
	
	/** �ļ�·��*/
	private File file;
	
	/** ��ǰ��Ƭ����*/
	private int pieceNum = 1;
	
	/** �ļ���С,��λΪbyte*/
	private int fileSize;
	
	/** ��ǰ��ȡ���ļ���Ƭ����*/
	private int nCurrentPiece = 0;
	
	/** �ļ���Ƭ������*/
	private List<String> pieceFiles;
	
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
			InputStream in = context.openFileInput(filePath);
			fileSize = in.available();
			byte[] buf = new byte[pieceSize];
			while(true) {
				if(in.read(buf, 0, pieceSize) > 0) {
					packagePiece(buf);
					pieceNum ++ ;
				} else {
					packagePiece(buf);
					break;
				}
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
	 * @throws IOException 
	 */
	private void packagePiece(byte[] buf) throws IOException {
		String pieceName = filePath + pieceNum + ".txt";
		FileOutputStream out = context.openFileOutput(pieceName, Context.MODE_WORLD_WRITEABLE);
		pieceFiles.add(pieceName);
		//out.write(packageHead);
		out.write(buf);
		out.close();
	}
	
	/**
	 * ��ȡ��Ƭ�ļ��ֽ���
	 * @param buf ΪNULL�ȿɣ��ڲ���Ϊbuf�Զ������ڴ�ռ�
	 * @return -1��ʾ�ļ��Ѿ���ȡ��,���򷵻ض�ȡbuf�Ĵ�С
	 * @throws IOException 
	 */
	public int getNextPiece(byte[] buf) throws IOException {
		//����ļ��������ڣ�˵���Ѿ����꣬�򷵻�-1
		if(nCurrentPiece >= pieceFiles.size())
			return -1;
		String fileName = pieceFiles.get(nCurrentPiece);
		
		InputStream in = context.openFileInput(fileName);
		int pieceSize = in.available();
		in.read(buf);
		nCurrentPiece ++;
		return pieceSize;
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
		InputStream in = context.openFileInput(fileName);
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
