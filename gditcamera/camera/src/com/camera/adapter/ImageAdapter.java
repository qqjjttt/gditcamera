package com.camera.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.activity.R;
import com.camera.picture.PictureUtil;

/**
 * GallaryͼƬ������
 * @author ֣���
 */
public class ImageAdapter extends BaseAdapter {  
	
	public static final String TAG = "ImageAdapter";
	
	/** ͼƬĿ¼*/
	private String mFolderPath;
    /** ͼƬ��Դ·��*/
    private List<String> mPaths;
    /** ͼƬ��������*/
    private PictureUtil pictureUtil;
    
    private Context mContext;
    
    /** Gallery����*/
    private int mGalleryItemBackground;

    public ImageAdapter(Context context, String folderPath) {
    	this.mFolderPath = folderPath;
        mContext = context;
        
        //��ȡͼƬ����ͼ��Դ��·��
        pictureUtil = new PictureUtil();
        try {
        	mPaths = pictureUtil.getThumbnailPathsByFolder(folderPath);
		} catch (Exception e) {
			Toast.makeText(mContext, "��������ͼ�ļ�ʱ����", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        a.recycle();
    }
    
    public int getCount() {
        return mPaths.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    /**
     * ͨ��λ�û�ȡ������ȡͼƬ
     * @param position λ������
     * @return ͼƬ��Դ
     */
    public String getImagePath(int position) {
    	return mPaths.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView = null;
		imageView = new ImageView(mContext);
        imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(mGalleryItemBackground);
        
        //��ȡͼƬ��Դ
        Bitmap bitmap = pictureUtil.getBitmap(mPaths.get(position));
        if(bitmap == null)
        	return null;
        imageView.setImageBitmap(bitmap);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}