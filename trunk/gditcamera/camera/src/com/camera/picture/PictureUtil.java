package com.camera.picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.camera.util.StringUtil;
import com.camera.vo.Constant;

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
	private String[] imageSuffixs;
	
	
	
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
		//��ȡ�ļ���ʽ����
		String imageSuffix = Constant.IMAGE_SUFFIX;
		imageSuffixs = imageSuffix.split(";");
		for(File file : files) {
			if(file.isDirectory()) {
				continue;
			}
			filePaths.add(file.getAbsolutePath());
		}
		return filePaths;
	}
	
	/**
	 * ����ļ��Ƿ���ͼƬ��Դ
	 * @param file �ļ�
	 * @return �ǻ��
	 */
	public boolean isImage(File file) {
		String fileName = file.getName();
		int point = fileName.lastIndexOf(".");
		if(point < 0) {
			return false;
		}
		String suffix = fileName.substring(point + 1);
		System.out.println("suffix : " + suffix);
		for(String str : imageSuffixs) {
			if(suffix.toUpperCase().equals(str)) {
				return true;
			}
		}
		return false;
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
	public Options getImageOptions(String path) {
		if(path == null)
			return null;
		File file = new File(path);
		if(!file.exists())
			return null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		return opts;
	}
	
	public Bitmap getBitmap(String path) {
		if(path == null)
			return null;
		File file = new File(path);
		if(!file.exists())
			return null;
		return BitmapFactory.decodeFile(path);
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
//			Log.d(TAG, "file path : " + path);
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
	public List<String> createThumbnails(Context context, String folderPath) throws Exception {
		List<String> thumbnailPaths = null;
		List<String> paths = getFilePathsFromFolder(folderPath);
		if(paths == null) {
			return thumbnailPaths;
		}
		if(paths.size() > 0)
			thumbnailPaths = new ArrayList<String>();
		for(String filePath : paths) {
			String targetPath = createThumbnail(context, filePath);
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
	private String createThumbnail(Context context, String filePath) throws Exception {
		//����ļ��Ƿ���ͼƬ�ļ�
		File file = new File(filePath);
		if(!isImage(file)) {
			// TODO 
			String smallFileName = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(file.getPath());
			String bigFileName = Constant.THUMBNAIL_FOLDER + StringUtil.convertFolderPath(file.getPath()) + ".big";
			File smallFile = new File(smallFileName);
			File bigFile = new File(bigFileName);
			InputStream in = context.getAssets().open("file.jpg");
			OutputStream smallOut = new FileOutputStream(smallFile);
			OutputStream bigOut = new FileOutputStream(bigFile);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			smallOut.write(buffer);
			bigOut.write(buffer);
			bigOut.close();
			smallOut.close();
			in.close();
			return smallFileName;
		}
		String thumbnailPath = null;
		Options options = this.getImageOptions(filePath);
		if (options == null) 
			throw new Exception("Can't get the file!");
		//��������ͼ
		thumbnailPath = ImageCompress.extractThumbnail(filePath);
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
	 * �����ƬĿ¼
	 */
	public void clearImagePieces() {
		File folder = new File(Constant.PIECE_FOLDER);
		File[] files = folder.listFiles();
		for(File file : files) {
			file.delete();
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
			mTagetHeight = height;
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
