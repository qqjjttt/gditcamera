package com.camera.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CutFileUtil {
	
	/** �ļ���*/
	private String filePath;
	
	/** �ļ�·��*/
	private File file;
	
	/** ��Ƭ�Ĵ�С*/
	private int pieceSize = 20;
	
	/** �ļ���С,��λΪbyte*/
	private int fileSize;
	
	/** �ļ���Ƭ������*/
	private String[] pieceFiles;
	
	public CutFileUtil(String filePath) throws Exception {
		file = new File(filePath);
		if(file.exists()) {
			throw new Exception("File not exist!");
		}
		cutFile();
	}
	
	public void cutFile() {
		try {
			//ͳ���ļ���Ϣ
			calculatorInfo();
			FileOutputStream out = new FileOutputStream(file);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �����ļ���Ϣ
	 * @throws IOException
	 */
	public void calculatorInfo() throws IOException {
		FileInputStream in = new FileInputStream(file);
		fileSize = in.available();
		in.close();
	}
}
