package com.camera.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.camera.adapter.ImageAdapter;
import com.camera.net.UploadFile;
import com.camera.picture.CutFileUtil;
import com.camera.picture.PictureUtil;
import com.camera.util.Constant;
import com.camera.util.StringUtil;

/**
 * ͼƬ�ϴ�����ģ��
 * @author ֣���
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	private static final String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	
	private static final int IS_REFRESH_FOLDER = 10;
	
	private Button mBtnUpload;
	private Button mBtnUploadAll;
	private EditText mTxtMessage;
	/** ͼƬ���Gallery*/
	private Gallery mGallery;
	/** ͼƬ������*/
	private ImageAdapter adapter;
	/** �Ի���*/
	private ProgressDialog dialog;
	/** ��ǰѡ��ͼƬ���ļ���*/
	private String mCurrentImg;
	
	/**
	 * �����첽�߳���Ϣ
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//ͼƬĿ¼ˢ����
			super.handleMessage(msg);
			switch(msg.what) {
			case UploadFile.FINISH_UPLOAD_FILE:
				Log.i(TAG, "file send is finish");
				break;
			//����ˢ��Ŀ¼
			case IS_REFRESH_FOLDER:
				adapter = new ImageAdapter(UploadFileActivity.this, PICTURE_FOLDER);
				mGallery.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				break;
			}
		}
	};
	
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
		mBtnUpload = (Button)this.findViewById(R.id.btnUpload);
		mBtnUploadAll = (Button)this.findViewById(R.id.btnUploadAll);
		mBtnUpload.setOnClickListener(this);
		mBtnUploadAll.setOnClickListener(this);
		//mTxtMessage = (EditText)this.findViewById(R.id.txtMessage);
		mImageView = (ImageView)this.findViewById(R.id.img);
		mGallery = (Gallery) findViewById(R.id.gallery);
	}
	
	/**
	 * ����ͼƬ��Դ��Gallery
	 */
	public void loadPicture() {
		adapter = new ImageAdapter(this, PICTURE_FOLDER);
		mGallery.setAdapter(adapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            PictureUtil pictureUtil = new PictureUtil();
	            Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(position) + ".big");
	            mCurrentImg = adapter.getImagePath(position);
	            mImageView.setImageBitmap(bitmap);
	        }
	    });
	}
	
	/**
	 * �����¼�����
	 */
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		//�ϴ�һ��ͼƬ
		case R.id.btnUpload:
			String imagePath = StringUtil.convertBackFolderPath(mCurrentImg);
			System.out.println(imagePath);
			try {
				CutFileUtil cutFileUtil = new CutFileUtil(this, imagePath);
				UploadFile uploadFile = new UploadFile(this, mHandler);
				uploadFile.upload(cutFileUtil);
			} catch (Exception e) {
				Toast.makeText(this, "�ļ��ϴ����̳��ִ��󣬴���ԭ���ļ������ڻ���Ƭ���̳��ִ���", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			break;
		case R.id.btnUploadAll:
			break;
		}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		//ˢ��Ŀ¼ͼƬ
		case R.id.menuRefresh:
			dialog = ProgressDialog.show(this, "���Ժ�", 
                    "����ˢ��ͼƬĿ¼......", true);
			Thread thread = new Thread() {
				@Override
				public void run() {
					refreshFolder();
					mHandler.sendEmptyMessage(IS_REFRESH_FOLDER);
				}	
			};
			mHandler.post(thread);
			thread.start();
			mHandler.removeCallbacks(thread);
			break;
		//�������ý���
		case R.id.menuConfig:
			Intent intent = new Intent();
			intent.setClass(this, Main.class);
			this.startActivity(intent);
			this.finish();
			break;
		//�˳�ϵͳ
		case R.id.menuExit:
			Builder builder = new Builder(this);
			builder.setTitle("��ʾ").setMessage("���Ƿ�Ҫ�˳�ϵͳ��");
			builder.setNegativeButton("ȷ��", new Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNeutralButton("ȡ��", null);
			builder.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * ˢ��Ŀ¼ͼƬ��������������ͼ
	 */
	public void refreshFolder() {
		try {
			PictureUtil pictureUtil = new PictureUtil();
			pictureUtil.clearThumbnail(PICTURE_FOLDER);
			pictureUtil.createThumbnails(PICTURE_FOLDER);
		} catch (Exception e) {
			Toast.makeText(this, "ˢ��Ŀ¼�����ˣ�", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}