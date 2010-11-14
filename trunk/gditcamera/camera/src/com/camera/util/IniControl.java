package com.camera.util;

import java.io.File;
import java.io.IOException;

import com.camera.picture.CutFileUtil;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * Ӧ�ó���ĳ�ʼ������
 * @author Administrator
 */
public class IniControl {

	/**
	 * ��ʼ��Ӧ�ó������� 
	 * @param context
	 * @throws IOException
	 */
	public static boolean initConfiguration(Context context) throws IOException {
		File file = new File(Constant.APP_FOLDER);
		if(!file.exists()) {
			file.mkdir();
		}
		file = new File(Constant.PIECE_FOLDER);
		if(!file.exists()) {
			file.mkdir();
		}
		file = new File(Constant.THUMBNAIL_FOLDER);
		if(!file.exists()) {
			file.mkdir();
		}
		file = new File(Constant.DEFAULT_IMAGE_FOLDER);
		if(!file.exists()) {
			file.mkdir();
		}
		cutFile(context);
		return true;
	}
	
	public static void cutFile(Context context) {

		String filePath = Constant.SDCARD_PATH + "/camera/wUpload/a.txt";

		try {
			CutFileUtil cutFileUtil = new CutFileUtil(context, filePath);
			byte[] buf = new byte[CutFileUtil.pieceSize];
			//System.out.println("--------------------��ʼ��ȡ��Ƭ--------------------------");
			while(cutFileUtil.getNextPiece(buf) != -1) {
//				System.out.println("--------------------��ȡ��һ����Ƭ--------------------------");
//				String str = new String(buf, "GB2312");
//				System.out.println(str);
			}
			//System.out.println("--------------------��ȡ��������Ƭ--------------------------");
		} catch (Exception e) {
			Toast.makeText(context, "�ļ��д��ˣ���", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
	}
	
}
