package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.camera.vo.Constant;

/**
 * ͼƬѹ����
 * @author ֣���
 */
public class ImageCompress {
	
	public static final String TAG = "ImageCompress";

	/**ѹ��Ŀ��ͼ���ȴ�С*/
	public static int mMaxWidth = 1024;
	/**ѹ��Ŀ��ͼ��߶ȴ�С*/
	public static int mMaxHeight = 1024;
	/** �ļ���С����ʱѹ�� */
	public static int mImageCompressSize = Constant.IMAGE_COMPRESS_SIZE;
	
	/** ͼ������*/
	private static int quality = 80;
	
	public ImageCompress() {
		
	}
	
	public static String compressJPG(String filePath) throws Exception {
		if(!checkIsJPEG(filePath)) {
			return filePath;
		}
		File file = new File(filePath);
		long length = file.length();
		while(length >= mImageCompressSize) {
			filePath = compress(filePath);
			file = new File(filePath);
			length = file.length();
		}
		return filePath;
	}
	
	private static boolean checkIsJPEG(String filePath) {
		int index = filePath.lastIndexOf("/");
		final String suffix = filePath.substring(index + 1).toLowerCase();
		if(suffix.equals("jpeg") || suffix.equals("jpg")) {
			Log.d(TAG, "The picture is JPEG suffix, suffix is : " + suffix);
			return true;
		}
		Log.d(TAG, "The picture is not JPEG suffix, suffix is : " + suffix);
		return false;
	}
	
	private static String compress(String filePath) throws Exception {
		String destFilePath = null;
		Bitmap destBitmap;
		int destWidth = 0;
		int destHeight = 0;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		//���ű���
		double ratio = 0.0;
		int maxWidth = mMaxWidth;
		int maxHeight = mMaxHeight;
		//�������ź��ͼƬ��С
		if(srcWidth <= mMaxWidth)
			maxWidth = srcWidth;
		if(srcHeight <= mMaxHeight)
			maxHeight = srcHeight;
		if (srcWidth > srcHeight) {
			ratio = srcWidth / mMaxWidth;
			destWidth = mMaxWidth;
			destHeight = (int)(srcHeight / ratio);
		} else {
			ratio = srcHeight / mMaxHeight;
			destHeight = mMaxHeight;
			destWidth = (int)(srcWidth / ratio);
		}
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ�Ŀǰ��ֻ����ֻ��ͨ��inSampleSize���������ţ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ
		newOpts.inSampleSize = (int) ratio + 1;
		//inJustDecodeBounds��Ϊfalse��ʾ��ͼƬ�����ڴ���
		newOpts.inJustDecodeBounds = false;
		//���ô�С�����һ���ǲ�׼ȷ�ģ�����inSampleSize��Ϊ׼���������������ȴ��������
		newOpts.outHeight = destHeight;
		newOpts.outWidth = destWidth;
		destBitmap = BitmapFactory.decodeFile(filePath, newOpts);
		if(destBitmap == null) {
			throw new Exception("Can not compress the image file!!");
		}
		destFilePath = Constant.PIECE_COMPRESS_FOLDER + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, quality, os);
		os.close();
		return destFilePath;
	}
}
