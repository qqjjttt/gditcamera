package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

/**
 * ͼƬѹ����
 * @author ֣���
 */
public class ImageCompress {

	/**ѹ��Ŀ��ͼ���ȴ�С*/
	public static int maxWidth = 400;
	/**ѹ��Ŀ��ͼ��߶ȴ�С*/
	public static int maxHeight = 400;
	
	/** ͼ������*/
	private static int quality = 80;
	
	public ImageCompress() {
		
	}
	
	public static String compress(String filePath) throws Exception {
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
		//�������ź��ͼƬ��С
		if (srcWidth > srcHeight) {
			ratio = srcWidth / maxWidth;
			destWidth = maxWidth;
			destHeight = (int)(srcHeight / ratio);
		} else {
			ratio = srcHeight / maxHeight;
			destHeight = maxHeight;
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
		destFilePath = "/mnt/sdcard/" + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, quality, os);
		os.close();
		return destFilePath;
	}
}
