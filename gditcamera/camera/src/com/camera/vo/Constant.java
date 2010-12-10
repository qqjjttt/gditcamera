package com.camera.vo;

import android.os.Environment;

/**
 * ��ų���ֵ
 *
 */
public class Constant {

	/** TRUE���ǵ�һ���汾��FALSE���ǵڶ����汾*/
	public static final boolean VERSION = false;
	
	/** SDCARD·��*/
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	
	/**ÿ����ͷ��С*/
	public static final int DATA_HEAD_SIZE = 138;
	/**ÿ�����ݰ���С*/
	public static final int PACKAGE_SIZE = 1024;
	/** �����ļ�·�� */
	public static final String  PERFERENCES_FILE_PATH = "/shared_prefs/perferences.xml";
	/** �ļ���С����ʱѹ�� */
	public static final int IMAGE_COMPRESS_SIZE = 100000;
	
	/** Ӧ�ó���Ŀ¼*/
	public static final String APP_FOLDER = SDCARD_PATH + "/ImageUploader/";
	/** Ӧ�ó���ͼƬ��ƬĿ¼*/
	public static final String PIECE_FOLDER = SDCARD_PATH + "/ImageUploader/wUpload/";
	/** Ӧ�ó���ͼƬѹ��Ŀ¼*/
	public static final String PIECE_COMPRESS_FOLDER = SDCARD_PATH + "/ImageUploader/compress/";
	/** ͼƬ�ĸ�ʽ*/
	public static final String IMAGE_SUFFIX = "BMP;PCX;TIFF;GIF;JPEG;JPG;TGA;EXIF;FPX;SVG;PSD;CDR;PCD;DXF;UFO;EPS;PNG;";
	
	
	/** ����ͼ����Ŀ¼*/
	public static final String THUMBNAIL_FOLDER = SDCARD_PATH + "/ImageUploader/thumbnail/";
	/** Ŀ¼Ĭ��Ŀ¼*/
	public static final String DEFAULT_IMAGE_FOLDER = SDCARD_PATH + "/DCIM/100MEDIA/";
	/** �ļ���*/
	public static final String DEFAULT_FILE_JPG = THUMBNAIL_FOLDER + "file.jpg";
	
	/**�����ļ��������������ͼƬĿ¼*/
	public static final String IMAGE_DIR = "ImgDir";
	/**�����ļ��������������������������ַ(�磺����1�������ļ����������Ϊhost_1)*/
	public static final String HOST_1 = "host_1";
	public static final String HOST_2 = "host_2";
	/**�����ļ��������������վ��*/
	public static final String STATION_CODE = "stationCode";
	/**�����ļ����������������վ*/
	public static final String STATION_SUB = "subStation";
	/**�����ļ������������������*/
	public static final String COMMAND = "command";
	/**�����ļ����������������վ*/
	public static final String STATION_SURVEY = "surveyStation";
	
}
