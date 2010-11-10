package com.camera.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.adapter.ImageAdapter;
import com.camera.picture.PictureUtil;

/**
 * 图片上传管理模块
 * @author 郑澍璋
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	private static final String PICTURE_FOLDER = "/mnt/sdcard/camera/";
	
	private Button mBtnUpdate;
	private Button mBtnUpdateAll;
	private EditText mTxtMessage;
	private Gallery mGallery;
	private ImageAdapter adapter;
	
	/** 图片预览控件*/
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_filed);
		getComponents();
		loadPicture();
		
	}
	
	/**
	 * 获取控件
	 */
	public void getComponents() {
		mBtnUpdate = (Button)this.findViewById(R.id.btnUpdate);
		mBtnUpdateAll = (Button)this.findViewById(R.id.btnUpdateAll);
		//mTxtMessage = (EditText)this.findViewById(R.id.txtMessage);
		mImageView = (ImageView)this.findViewById(R.id.img);
		mGallery = (Gallery) findViewById(R.id.gallery);
	}
	
	/**
	 * 加载图片资源到Gallery
	 */
	public void loadPicture() {
		//生成缩略图
		PictureUtil pictureUtil = new PictureUtil();
//		try {
//			pictureUtil.createThumbnails(PICTURE_FOLDER);
//		} catch (Exception e) {
//			Toast.makeText(this, "生成缩略图出错！！", Toast.LENGTH_SHORT);
//			e.printStackTrace();
//		}
		adapter = new ImageAdapter(this, PICTURE_FOLDER);
		mGallery.setAdapter(adapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            PictureUtil pictureUtil = new PictureUtil();
	            Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(position));
	            mImageView.setImageBitmap(bitmap);
	        }
	    });
	}

	@Override
	public void onClick(View arg0) {
		
	}
}