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
		return true;
	}
	

	
}
