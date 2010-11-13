package com.camera.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.camera.util.Constant;

/**
 * ͼƬ�ϴ�����ģ��
 * @author ֣���
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	private static final String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	
	private Button mBtnUpdate;
	private Button mBtnUpdateAll;
	private EditText mTxtMessage;
	private Gallery mGallery;
	private ImageAdapter adapter;
	
	/** ͼƬԤ���ؼ�*/
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_filed);
		getComponents();
		loadPicture();
		
	}
	
	/**
	 * ��ȡ�ؼ�
	 */
	public void getComponents() {
		mBtnUpdate = (Button)this.findViewById(R.id.btnUpdate);
		mBtnUpdateAll = (Button)this.findViewById(R.id.btnUpdateAll);
		//mTxtMessage = (EditText)this.findViewById(R.id.txtMessage);
		mImageView = (ImageView)this.findViewById(R.id.img);
		mGallery = (Gallery) findViewById(R.id.gallery);
	}
	
	/**
	 * ����ͼƬ��Դ��Gallery
	 */
	public void loadPicture() {
		//��������ͼ
		PictureUtil pictureUtil = new PictureUtil();
//		try {
//			pictureUtil.createThumbnails(PICTURE_FOLDER);
//		} catch (Exception e) {
//			Toast.makeText(this, "��������ͼ������", Toast.LENGTH_SHORT).show();
//			e.printStackTrace();
//		}
		adapter = new ImageAdapter(this, PICTURE_FOLDER);
		mGallery.setAdapter(adapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            PictureUtil pictureUtil = new PictureUtil();
	            Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(position) + ".big");
	            mImageView.setImageBitmap(bitmap);
	        }
	    });
	}

	@Override
	public void onClick(View arg0) {
		
	}
	
	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.menu, menu);
		  return true;
	}
}