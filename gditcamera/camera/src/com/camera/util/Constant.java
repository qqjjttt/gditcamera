package com.camera.util;

import android.os.Environment;

/**
 * ��ų���ֵ
 * @author tian
 *
 */
public class Constant {

	/** SDCARD·��*/
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	/**ÿ����ͷ��С*/
	public static final int DATA_HEAD_SIZE = 138;
	/**ÿ�����ݰ���С*/
	public static final int PACKAGE_SIZE = 1024;
	
	/** Ӧ�ó���Ŀ¼*/
	public static final String APP_FOLDER = SDCARD_PATH + "/camera/";
	/** Ӧ�ó���ͼƬ��ƬĿ¼*/
	public static final String PIECE_FOLDER = SDCARD_PATH + "/camera/wUpload/";
	
	
	/** ����ͼ����Ŀ¼*/
	public static final String THUMBNAIL_FOLDER = SDCARD_PATH + "/thumbnail/";
	
	/**�����ļ��������������ͼƬĿ¼*/
	public static final String IMAGE_DIR = "ImgDir";
	/**�����ļ��������������������������ַ�б�(�磺����1�������ļ����������Ϊhost_1)*/
	public static final String HOST_LIST = "host_";
	/**�����ļ��������������վ��*/
	public static final String STATION_CODE = "stationCode";
	/**�����ļ����������������վ*/
	public static final String STATION_SUB = "subStation";
	/**�����ļ����������������վ*/
	public static final String STATION_SURVEY = "surveyStation";
	
}
