package com.camera.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.camera.util.Constant;
import com.camera.util.StringUtil;

/**
 * �ļ�������
 * @author ֣���
 */
public class PictureUtil {
	
	public static final String TAG = "PictureUtil";

	/** ͼƬ����ͼ�Ŀ��*/
	private int thumbnailWidth = 100;
	/** ͼƬ����ͼ�ĸ߶�*/
	private int thumbnailHeight = 100;
	/** ͼƬ����ͼ�Ŀ��*/
	private int thumbnailWidth2 = 300;
	/** ͼƬ����ͼ�ĸ߶�*/
	private int thumbnailHeight2 = 300;
	/** ����ҪӦ����ȥ������ͼ�߶�*/
	private int mTagetHeight;
	/** ����ҪӦ����ȥ������ͼ���*/
	private int mTagetWidth;
	
	
	/**
	 * ��ȡĿ¼������ļ�
	 * @param folderPath Ŀ¼·��
	 * @return �ļ��б�
	 */
	public List<String> getFilePathsFromFolder(String folderPath) throws Exception {
		List<String> filePaths = null;
		File folder = new File(folderPath);
		if(!folder.exists() || !folder.isDirectory()) {
			throw new Exception("Cant not find the folder");
		}
		File[] files = folder.listFiles();
		if(files.length > 0) 
			filePaths = new ArrayList<String>();
		for(File file : files) {
			if(file.isDirectory()) {
				continue;
			}
			filePaths.add(file.getAbsolutePath());
		}
		return filePaths;
	}
	
	/**
	 * ͨ��ͼƬ�ļ���·����ȡͼƬ��Դ
	 * @param folderPath ͼƬ�ļ���·��
	 * @return �����ȡ�����κ���Դ���򷵻�NULL�����򷵻�ͼƬ��Դ�б�
	 */
	public List<Bitmap> getBitmapByFolderPath(String folderPath) throws Exception {
		List<String> paths = getFilePathsFromFolder(folderPath);
		return getBitmapList(paths);
	}
	
	/**
	 * ͨ��·����ȡͼƬ��Դ
	 * @param picturePaths ͼƬ·���б�
	 * @return �����ȡ�����κ���Դ���򷵻�NULL�����򷵻�ͼƬ��Դ�б�
	 */
	public List<Bitmap> getBitmapList(List<String> picturePaths) {
		List<Bitmap> bitmaps = null;
		Bitmap bitmap = null;
		if(picturePaths == null || picturePaths.size() < 0) 
			return bitmaps;
		bitmaps = new ArrayList<Bitmap>();
		for(String path : picturePaths) {
			bitmap = BitmapFactory.decodeFile(path);
			if(bitmap == null)
				continue;
			bitmaps.add(bitmap);
		}
		return bitmaps;
	}
	
	
	/**
	 * ͨ��·����ȡͼƬ������ͼ
	 * @param path ͼƬ��Դ·��
	 * @return �����ȡ����ͼƬ���򷵻�NULL
	 */
	public Bitmap getBitmap(String path) {
		Bitmap bitmap = null;
		if(path == null)
			return bitmap;
		File file = new File(path);
		if(!file.exists())
			return bitmap;
		bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}
	
	/**
	 * ͨ���ļ���Ŀ¼·����������ͼĿ¼�л�ȡ��Ŀ¼�µ���������ͼ·��
	 * @param folderPath ͼƬĿ¼
	 * @return ��Ŀ¼�����е�����ͼ·��
	 * @throws Exception
	 */
	public List<String> getThumbnailPathsByFolder(String folderPath) throws Exception {
		List<String> filePaths = null;
		File[] files = getThumbnailFile();
		filePaths = new ArrayList<String>();
		String thumbnailFolderPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(folderPath);
		for(File file : files) {
			String path = file.getAbsolutePath();
			Log.d(TAG, "file path : " + path);
			if(path.contains(thumbnailFolderPath) && !path.contains(".big")) {
				filePaths.add(path);
			}
		}
		return filePaths;
	}
	
	public File[] getThumbnailFile() throws Exception {
		File folder = new File(Constant.THUMBNAIL_FOLDER);
		System.out.println("" + Constant.THUMBNAIL_FOLDER);
		if(!folder.exists() || !folder.isDirectory()) {
			throw new Exception("Cant not find the folder");
		}
		return folder.listFiles();
	}
	
	/**
	 * �����ļ��е���������ָ��������ͼĿ¼
	 * @param folderPath �ļ���Ŀ¼
	 * @return ����ͼ·���б�
	 * @throws Exception
	 */
	public List<String> createThumbnails(String folderPath) throws Exception {
		List<String> thumbnailPaths = null;
		List<String> paths = getFilePathsFromFolder(folderPath);
		if(paths == null) {
			return thumbnailPaths;
		}
		if(paths.size() > 0)
			thumbnailPaths = new ArrayList<String>();
		for(String filePath : paths) {
			String targetPath = createThumbnail(filePath);
			thumbnailPaths.add(targetPath);
		}
		return thumbnailPaths;
		
	}
	
	/**
	 * �����ļ�����ͼ��ָ��Ŀ¼
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private String createThumbnail(String filePath) throws Exception {
		String thumbnailPath = null;
		Bitmap bitmap = this.getBitmap(filePath);
		if (bitmap == null) 
			throw new Exception("Can't get the file!");
		//��������
		this.calculateThumbnailSize(bitmap.getWidth(), bitmap.getHeight(), thumbnailWidth, thumbnailHeight);
		Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, mTagetWidth, mTagetHeight);
		//����С������ͼ��ָ��Ŀ¼
		thumbnailPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath);
		File bitmapFile = new File(thumbnailPath);
		if (bitmapFile.exists()) {
			bitmapFile.delete();
		}
		FileOutputStream bitmapWtriter = new FileOutputStream(bitmapFile);
		if (!bitmap1.compress(Bitmap.CompressFormat.JPEG, 20, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		//����������ͼ��ָ��Ŀ¼
		this.calculateThumbnailSize(bitmap.getWidth(), bitmap.getHeight(), thumbnailWidth2, thumbnailHeight2);
		Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmap, mTagetWidth, mTagetHeight);
		String thumbnailPath2 = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(filePath) + ".big";
		File bitmapFile2 = new File(thumbnailPath2);
		if (bitmapFile2.exists()) {
			bitmapFile2.delete();
		}
		bitmapWtriter = new FileOutputStream(bitmapFile2);
		if (!bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, bitmapWtriter)) {
			throw new Exception("Can't save the thumbnail file!");
		}
		bitmapWtriter.close();
		return thumbnailPath;
			
	}
	
	/**
	 * ���ָ��Ŀ¼������ͼ
	 * @param folderPath
	 * @throws Exception 
	 */
	public void clearThumbnail(String folderPath) throws Exception {
		String thumbnailFolderPath = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(folderPath);
		File[] files = getThumbnailFile();
		for(File file : files) {
			String path = file.getAbsolutePath();
			if(path.contains(thumbnailFolderPath)) {
				file.delete();
			}
		}
	}
	
	/**
	 * ��������ͼ�ĸ߶ȺͿ��
	 * @param oriWidth ��ʵͼƬ�Ŀ��
	 * @param oriHeight ��ʵͼƬ�ĸ߶�
	 */
	public void calculateThumbnailSize(int oriWidth, int oriHeight, int width, int height) {
		if(oriWidth > oriHeight) {
			mTagetWidth = width;
			mTagetHeight = oriHeight * width / oriWidth;
		} else {
			mTagetWidth = width;
			mTagetWidth = oriWidth * height / oriHeight;
		}
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

}
