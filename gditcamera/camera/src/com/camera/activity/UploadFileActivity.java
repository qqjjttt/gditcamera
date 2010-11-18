package com.camera.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.adapter.ImageAdapter;
import com.camera.net.UploadFile;
import com.camera.picture.CutFileUtil;
import com.camera.picture.PictureUtil;
import com.camera.util.Constant;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;

/**
 * ͼƬ�ϴ�����ģ��
 * @author ֣���
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	
	private static String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	/** ˢ��Ŀ¼�ɹ�*/
	private static final int REFRESH_FOLDER_SUCCESS = 10;
	/** ˢ��Ŀ¼ʧ��*/
	private static final int REFRESH_FOLDER_ERR = 11;
	/** ��Ƭ�ɹ�*/
	private static final int FINISH_CUT_FILE = 12;
	/** �л����ȶԻ������*/
	public static final int PROGRESS_DIALOG = 13;
	
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
	private UploadFile uploadFile;
	private CutFileUtil cutFileUtil;
	/**
	 * �����첽�߳���Ϣ
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//ͼƬĿ¼ˢ����
			super.handleMessage(msg);
			switch(msg.what) {
			//����ͼƬ����
			case PROGRESS_DIALOG:
				System.out.println((Integer)msg.obj);
				((ProgressDialog)dialog).setProgress((Integer)msg.obj);
				break;
				
			case FINISH_CUT_FILE:
				dialog.setMessage("�������ӷ�����....");
				break;
				
			case UploadFile.TIME_OUT:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "���ӷ�������ʱ���ϴ�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				break;
			
			case UploadFile.CONNECTION_SUCCESS:
				Toast.makeText(UploadFileActivity.this, "���ӷ������ɹ���", Toast.LENGTH_SHORT).show();
				dialog.setMessage("�����ϴ�ͼƬ....");
				break;
			case UploadFile.CONNECTION_FAILSE:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "���ӷ�����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				break;
			
			
			case UploadFile.FINISH_UPLOAD_FILE:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "�ϴ�ͼƬ�ɹ���", Toast.LENGTH_SHORT).show();
				break;
			case UploadFile.THROW_EXCEPTION:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "�ϴ�ʱ�����쳣���ϴ�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				break;
			//����ˢ��Ŀ¼
			case REFRESH_FOLDER_SUCCESS:
				adapter = new ImageAdapter(UploadFileActivity.this, PICTURE_FOLDER);
				mGallery.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				break;
			case REFRESH_FOLDER_ERR:
				Toast.makeText(UploadFileActivity.this, "ˢ��Ŀ¼ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
		//��ȡͼƬ·��
		PreferencesDAO preferencesDao = new PreferencesDAO(this);
		PICTURE_FOLDER = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
		
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
//	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
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
			showDialog();
			Thread uploadOnePicThread = new Thread() {
				@Override
				public void run() {
					try {
						String imagePath = StringUtil.convertBackFolderPath(mCurrentImg);
						System.out.println(imagePath);
						cutFileUtil = new CutFileUtil(UploadFileActivity.this, imagePath, mHandler);
						mHandler.sendEmptyMessage(FINISH_CUT_FILE);
						uploadFile = new UploadFile(UploadFileActivity.this, mHandler, this);
//						uploadFile.upload(cutFileUtil);
					} catch (Exception e) {
						Log.e(TAG, "throw a exception while upload a file!!");
						e.printStackTrace();
						mHandler.sendEmptyMessage(UploadFile.THROW_EXCEPTION);
					}
				}	
			};
			uploadOnePicThread.start();
			break;
		case R.id.btnUploadAll:
			break;
		}
	}
	
	public void showDialog() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		CharSequence title = "ProgressDialog�����̴߳������";
		// CharSequence message = getString(R.string.xxx);
		CharSequence message = "��ǰ�������";
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setMessage("������Ƭ....");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setButton("��̨����", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		progressDialog.setProgress(0);
		progressDialog.setMax(100);
//		progressDialog.setOnCancelListener(mThread);
//		progressDialog.setOnDismissListener(mThread);
		progressDialog.show();
		dialog = progressDialog;
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
					try {
						refreshFolder();
						mHandler.sendEmptyMessage(REFRESH_FOLDER_SUCCESS);
					} catch(Exception e ){
						mHandler.sendEmptyMessage(REFRESH_FOLDER_ERR);
					}
					
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
	public void refreshFolder() throws Exception {
		try {
			PictureUtil pictureUtil = new PictureUtil();
			pictureUtil.clearThumbnail(PICTURE_FOLDER);
			pictureUtil.createThumbnails(PICTURE_FOLDER);
		} catch (Exception e) {
			Log.e(TAG, "throw a exception while refresh the picture folder!");
			e.printStackTrace();
			throw new Exception("throw a exception while refresh the picture folder!");
		}
	}
}