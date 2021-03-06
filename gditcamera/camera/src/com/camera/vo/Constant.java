package com.camera.vo;

import android.os.Environment;

/**
 * 存放常量值
 *
 */
public class Constant {

	/** 版本标识*/
	public static final boolean VERSION = false;
	
	/** SDCARD路径*/
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	
	/**每个包头大小*/
	public static final int DATA_HEAD_SIZE = 138;
	/**每个数据包大小*/
	public static final int PACKAGE_SIZE = 1024;
	/** 配置文件路径 */
	public static final String  PERFERENCES_FILE_PATH = "/shared_prefs/perferences.xml";
	/** 文件大小多少时压缩 */
	public static final int IMAGE_COMPRESS_SIZE = 100000;
	public static final int THUMBNAIL_BIG_SIZE = 300;
	public static final int THUMBNAIL_SMALL_SIZE = 80;
	
	/** 应用程序目录*/
	public static final String APP_FOLDER = SDCARD_PATH + "/hband/";
	/** 应用程序图片切片目录*/
	public static final String PIECE_FOLDER = SDCARD_PATH + "/hband/wUpload/";
	/** 应用程序图片压缩目录*/
	public static final String PIECE_COMPRESS_FOLDER = SDCARD_PATH + "/hband/compress/";
	/** 图片的格式*/
	public static final String IMAGE_SUFFIX = "BMP;PCX;TIFF;GIF;JPEG;JPG;TGA;EXIF;FPX;SVG;PSD;CDR;PCD;DXF;UFO;EPS;PNG;";
	
	
	/** 缩略图保存目录*/
	public static final String THUMBNAIL_FOLDER = SDCARD_PATH + "/hband/thumbnail/";
	/** 目录默认目录*/
	public static final String DEFAULT_IMAGE_FOLDER = SDCARD_PATH + "/DCIM/100MEDIA/";
	/** 文件名*/
	public static final String DEFAULT_FILE_JPG = "file.png";
	
	/**配置文件里面的属性名：图片目录*/
	public static final String IMAGE_DIR = "ImgDir";
	/**配置文件里面的属性名：服务器主机地址(如：主机1，配置文件里的属性名为host_1)*/
	public static final String HOST_1 = "host_1";
	public static final String HOST_2 = "host_2";
	/**配置文件里面的属性名：站码*/
	public static final String STATION_CODE = "stationCode";
	/**配置文件里面的属性名：分站*/
	public static final String STATION_SUB = "subStation";
	/**配置文件里面的属性名：口令*/
	public static final String COMMAND = "command";
	/**配置文件里面的属性名：测站*/
	public static final String STATION_SURVEY = "surveyStation";
	
}
