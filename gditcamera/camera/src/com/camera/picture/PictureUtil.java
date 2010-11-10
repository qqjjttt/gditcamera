package com.camera.picture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * �ļ�������
 * @author ֣���
 */
public class PictureUtil {

	/** ͼƬ����ͼ�Ŀ��*/
	private int thumbnailWidth = 100;
	/** ͼƬ����ͼ�ĸ߶�*/
	private int thumbnailHeight = 100;
	
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
	public List<Bitmap> getPicturesByFolderPath(String folderPath) throws Exception {
		List<String> paths = getFilePathsFromFolder(folderPath);
		return getPictureThumbnail(paths);
	}
	
	/**
	 * ͨ��·����ȡͼƬ��Դ
	 * @param picturePaths ͼƬ·���б�
	 * @return �����ȡ�����κ���Դ���򷵻�NULL�����򷵻�ͼƬ��Դ�б�
	 */
	public List<Bitmap> getPictureThumbnail(List<String> picturePaths) {
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
	public Bitmap getPictureThumbnail(String path) {
		Bitmap bitmap = null;
		if(path == null)
			return bitmap;
		File file = new File(path);
		if(!file.exists())
			return bitmap;
		bitmap = BitmapFactory.decodeFile(path);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, thumbnailWidth, thumbnailHeight);
		return bitmap;
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
