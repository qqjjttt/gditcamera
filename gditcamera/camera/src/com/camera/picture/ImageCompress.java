package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.camera.util.StringUtil;
import com.camera.vo.Constant;

/**
 * ͼƬѹ����
 * @author ֣���
 */
public class ImageCompress {
	
	public static final String TAG = "ImageCompress";

	/**ѹ��Ŀ��ͼ���ȴ�С*/
	public static int mMaxWidth = 700;
	/**ѹ��Ŀ��ͼ��߶ȴ�С*/
	public static int mMaxHeight = 700;
	/** �ļ���С����ʱѹ�� */
	public static int mImageCompressSize = Constant.IMAGE_COMPRESS_SIZE;
	
	/** ͼ������*/
	private static final int quality = 80;
	
	public ImageCompress() {
		
	}
	
	public static String compressJPG(String filePath) throws Exception {
		if(!checkIsJPEG(filePath)) {
			return filePath;
		}
		File file = new File(filePath);
		long length = file.length();
		boolean flag = true;
		while(length >= 10) {
			filePath = compress(filePath);
			file = new File(filePath);
			length = file.length();
		}
		Log.i(TAG, "The final image size is:" + length + "; file path is : " + filePath);
		return filePath;
	}
	
	private static boolean checkIsJPEG(String filePath) {
		int index = filePath.lastIndexOf(".");
		final String suffix = filePath.substring(index + 1).toLowerCase();
		if(suffix.equals("jpeg") || suffix.equals("jpg")) {
			Log.d(TAG, "The picture is JPEG suffix, suffix is : " + suffix);
			return true;
		}
		Log.d(TAG, "The picture is not JPEG suffix, suffix is : " + suffix);
		return false;
	}
	
	private static String compress_backup(String filePath) throws Exception {
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
//		newOpts.inSampleSize = (int) ratio + 1;
		//inJustDecodeBounds��Ϊfalse��ʾ��ͼƬ�����ڴ���
		newOpts.inJustDecodeBounds = false;
		//���ô�С�����һ���ǲ�׼ȷ�ģ�����inSampleSize��Ϊ׼���������������ȴ��������
		newOpts.outHeight = destHeight;
		newOpts.outWidth = destWidth;
		Log.e(TAG, "destHeight:" + destHeight + "; destWidth:" + destWidth);
		newOpts.inSampleSize = calculateInSampleSize2(filePath);
		destBitmap = BitmapFactory.decodeFile(filePath, newOpts);
		if(destBitmap == null) {
			throw new Exception("Can not compress the image file!!");
		}
		destFilePath = Constant.PIECE_COMPRESS_FOLDER + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, quality, os);
		os.close();
		if(!destBitmap.isRecycled()) {
			destBitmap.recycle();
		}
		File file = new File(filePath);
		file.delete();
		return destFilePath;
	}
	

	private static String compress(String filePath) throws Exception {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = 1;
		Bitmap destBitmap = BitmapFactory.decodeFile(filePath, newOpts);
		if(destBitmap == null) {
			throw new Exception("Can not compress the image file!!");
		}
		String destFilePath = Constant.PIECE_COMPRESS_FOLDER + UUID.randomUUID().toString() + ".jpg";
		File destFile = new File(destFilePath);
		OutputStream os = new FileOutputStream(destFile);
		destBitmap.compress(CompressFormat.JPEG, 1, os);
		os.close();
		if(!destBitmap.isRecycled()) {
			destBitmap.recycle();
		}
		Log.e(TAG, "File size:" + destFile.length());
		return destFilePath;
	}
	
	/**
	 * ��������ͼ
	 * @param filePath ͼƬ·��
	 * @param width ͼƬ���
	 * @param height ͼƬ�߶�
	 * @param quality ͼƬ����
	 * @param size ��ͼ����Сͼ��TRUEΪ��ͼ
	 */
	public static String extractThumbnail(String filePath, int width, 
			int height, int quality, boolean size) throws Exception {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		String thumbnailPath = null;
		int inSampleSize = 0;
		if(size) {
			inSampleSize = calculateInSampleSize(filePath);
			newOpts.inSampleSize = inSampleSize;
			thumbnailPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath) + ".big";
		} else {
			inSampleSize = calculateInSampleSize(filePath) * 2;
			newOpts.inSampleSize = inSampleSize;
			thumbnailPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath);
		}
		newOpts.inJustDecodeBounds = false;
		//���ô�С�����һ���ǲ�׼ȷ�ģ�����inSampleSize��Ϊ׼���������������ȴ��������
		newOpts.outHeight = width;
		newOpts.outWidth = height;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, newOpts);
		Log.i(TAG, "InSampleSize is :" + inSampleSize);
		File bitmapFile = new File(thumbnailPath);
		if (bitmapFile.exists()) {
			bitmapFile.delete();
		}
		FileOutputStream bitmapWtriter = new FileOutputStream(bitmapFile);
		if (!bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		return thumbnailPath;
	}
	
	/**
	 * ��������ͼʱ�����ȡͼƬ�ļ�Ҫ��С�ı���
	 */
	private static int calculateInSampleSize(String filePath) {
		File file = new File(filePath);
		long fileSize = file.length();
		Log.i(TAG, "File size is :" + fileSize);
		if(fileSize > 4 * 1024 * 1024) {
			return 30;
		} else if(fileSize > 2 * 1024 * 1024) {
			return 22;
		} else if(fileSize > 1024 * 1024) {
			return 15;
		} else if(fileSize > 1024 * 512) { 
			return 8;
		} else if(fileSize > 300 * 1024) {
			return 5;
		} else if(fileSize > 200 * 1024) {
			return 3;
		} else if(fileSize > 100 * 1024) {
			return 2;
		}
		return 1;
	}
	
	/**
	 * �ϴ�ͼƬѹ��ʱ�����ȡͼƬ�ļ�Ҫ��С�ı���
	 */
	private static int calculateInSampleSize2(String filePath) {
		File file = new File(filePath);
		long fileSize = file.length();
		Log.i(TAG, "File size is :" + fileSize);
		if(fileSize > 4 * 1024 * 1024) {
			return 8;
		} else if(fileSize > 2 * 1024 * 1024) {
			return 6;
		} else if(fileSize > 1024 * 1024) {
			return 4;
		} else if(fileSize > 1024 * 728) { 
			return 2;
		}
		return 1;
	}
}
