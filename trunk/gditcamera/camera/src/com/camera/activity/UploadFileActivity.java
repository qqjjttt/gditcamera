package com.camera.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.camera.adapter.ImageAdapter;
import com.camera.net.UploadFile;
import com.camera.picture.CutFileUtil;
import com.camera.picture.ImageCompress;
import com.camera.picture.PictureUtil;
import com.camera.util.IniControl;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;
import com.camera.vo.Constant;
import com.camera.vo.UploadFileList;
import com.camera.vo.VersionVo;

/**
 * ͼƬ�ϴ�����ģ��
 * @author ֣���
 */
public class UploadFileActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "UploadFileActivity";
	
	public static final int ACTIVITY_REQUEST_CODE = 1;
	private static int NOTIFICATION_ID = 1;
	
	private static String PICTURE_FOLDER = Constant.DEFAULT_IMAGE_FOLDER;
	/** ˢ��Ŀ¼ʧ��*/
	private static final int REFRESH_FOLDER_ERR = 11;
	/** �������*/
	private static final int CLEAR_BUFFER = 17;
	/** ��Ƭ�ɹ�*/
	private static final int FINISH_CUT_FILE = 12;
	/** �л����ȶԻ������*/
	public static final int PROGRESS_DIALOG = 13;
	/** �л����ȶԻ������*/
	public static final int FILE_NOT_FIND = 14;
	/** ��ʼ�ϴ�*/
	public static final int START_UPLOAD = 15;
	/** ѹ��ͼƬ*/
	public static final int COMPRESS_PICTURE = 16;
	/** ˢ��Ŀ¼�ɹ�*/
	private static final int REFRESH_FOLDER_SUCCESS = 20;
	/** �ϴ�����ͼƬ�ļ��ʱ��*/
	public static final int UPLOAD_INTERVAL = 4000;
	/** �ϴ�ͼƬʧ�ܵ������ϴ��ӳ�ʱ��*/
	public static final int REUPLOAD_INTERVAL = 4000;
	
	
	/** �ϴ�*/
	private Button mBtnUpload;
	/** �ϴ�����*/
	private Button mBtnUploadAll;
	/** ��Ƭ����*/
	private EditText mTxtMessage;
	/** ͼƬ���Gallery*/
	private Gallery mGallery;
	/** ͼƬ������*/
	private ImageAdapter adapter;
	/** �ļ�·��*/
	private TextView mTxtFilePath;
	/** �Ի���*/
	private ProgressDialog dialog;
	/** ��ǰѡ��ͼƬ���ļ���*/
	private String mCurrentImg;
	private String mCurrentFileName;
	private UploadFile uploadFile;
	private CutFileUtil cutFileUtil;
	
	/** ��ʶ�Ƿ������ϴ�*/
	private boolean isUploading = false;
	
	private NotificationManager mNotificationManager;
	
	/** �ϴ��ļ��б�*/
	private UploadFileList mUploadFileList = new UploadFileList();
	
	private String mImagePath;
	private String mImageName;
	
	
	Thread mUploadOnePicThread = new UploadThread();
	
	public class UploadThread extends Thread {
		
		private int mInterval = 0;
		
		public UploadThread() {
			super();
		}
		
		public UploadThread(int interval) {
			super();
			this.mInterval = interval;
		}
		
		@Override
		public void run() {
			synchronized (this) {
				try {
					isUploading = true;
					this.sleep(mInterval);
					mHandler.sendEmptyMessage(START_UPLOAD);
					String description = mTxtMessage.getText().toString();
					String imagePath = ImageCompress.compressJPG(mImagePath);
					mHandler.sendEmptyMessage(COMPRESS_PICTURE);
					cutFileUtil = new CutFileUtil(UploadFileActivity.this, imagePath, mHandler, description);
					mHandler.sendEmptyMessage(FINISH_CUT_FILE);
					uploadFile = new UploadFile(UploadFileActivity.this, mHandler, this);
					uploadFile.upload(cutFileUtil);
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(FILE_NOT_FIND);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					Log.e(TAG, "throw a exception while upload a file!!");
					e.printStackTrace();
//					mHandler.sendEmptyMessage(UploadFile.THROW_EXCEPTION);
				}
			}
		}	
	};
	
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
				if(dialog.isShowing())
					((ProgressDialog)dialog).setProgress((Integer)msg.obj);
				break;
			case CLEAR_BUFFER:
				/** ˢ��Ŀ¼ʧ��*/
				dialog.setMessage("�������ɹ�������ˢ��Ŀ¼......");
				break;
			
			case FILE_NOT_FIND:
				dialog.dismiss();
				Toast.makeText(UploadFileActivity.this, "δ�ҵ��ļ� " + mImageName + " ���볢��ˢ��һ�£�", Toast.LENGTH_SHORT).show();
				break;
			case COMPRESS_PICTURE:
				dialog.setMessage("���ڴ���ͼƬ(��Ƭ)....");
				dialog.setProgress(0);
				break;
			case FINISH_CUT_FILE:
				dialog.setProgress(0);
				dialog.setMessage("������" + UploadFile.CURRENT_FILE_INDEX + ":�������ӷ�����....");
				break;
				
			case START_UPLOAD:
				if(!dialog.isShowing()) {
					dialog.show();
				}
				break;
				
			case UploadFile.FINISH_SEND_FIRST_SERVER:
				if(!dialog.isShowing()) {
					dialog.show();
					dialog.setMessage("������" + UploadFile.CURRENT_FILE_INDEX + ": ��ʼ�ϴ�ͼƬ  " + mImageName + " ");
				}
				break;
				
			case UploadFile.DISMISS_DIALOG:
				if(dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
				
			case CutFileUtil.MSG_EXIST_PIECE:
				Builder builder = new Builder(UploadFileActivity.this);
				builder.setTitle("��ʾ").setMessage("ϵͳ��⵽���ļ��ϴ�δ�ϴ��꣬�Ƿ�����ϴ��ϴ�δ��ɵ�����");
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CutFileUtil.IS_SEND_LAST_PIECE = false;
						mUploadOnePicThread.interrupt();
					}
				});
				builder.setNeutralButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CutFileUtil.IS_SEND_LAST_PIECE = true;
						mUploadOnePicThread.interrupt();
					}
				});
				builder.show();
				break;
				
			case UploadFile.TIME_OUT:
				Toast.makeText(UploadFileActivity.this, "������" + UploadFile.CURRENT_FILE_INDEX + ": ���ӷ�������ʱ���ϴ�ͼƬ " + mImageName + " ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				sendNotification("�ϴ�ʧ��", "������" + UploadFile.CURRENT_FILE_INDEX + ": ���ӷ�������ʱ���ϴ�ͼƬ " + mImageName + " ʧ�ܣ�");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, false, REUPLOAD_INTERVAL);
				}
				break;
			
			case UploadFile.CONNECTION_SUCCESS:
//				Toast.makeText(UploadFileActivity.this, "���ӷ������ɹ���", Toast.LENGTH_SHORT).show();
				dialog.setMessage("������" + UploadFile.CURRENT_FILE_INDEX + ": �����ϴ�ͼƬ��������....");
				dialog.setProgress(0);
				break;
			case UploadFile.CONNECTION_FAILSE:
				Toast.makeText(UploadFileActivity.this, "������" + UploadFile.CURRENT_FILE_INDEX + ":���ӷ����� ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				sendNotification("�ϴ�ʧ��", "������" + UploadFile.CURRENT_FILE_INDEX + ": ���ӷ�����ʧ�ܣ�");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, false, REUPLOAD_INTERVAL);
				}
				break;
				
			case UploadFile.FINISH_SEND:
				if(dialog.isShowing()) {
					dialog.dismiss();
				}
				uploadNextFile(false, false, REUPLOAD_INTERVAL);
				break;
				
			case UploadFile.CONNECT_TIME_OUT:
				Toast.makeText(UploadFileActivity.this, "������" + UploadFile.CURRENT_FILE_INDEX + ": ���շ������ظ���ʱ���ϴ�ͼƬ " + mImageName + " ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				sendNotification("�ϴ�ʧ��", "������" + UploadFile.CURRENT_FILE_INDEX + ": ���շ������ظ���ʱ���ϴ�ͼƬ " + mImageName + " ʧ�ܣ�");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, true, REUPLOAD_INTERVAL);
				}
				break;
			
			case UploadFile.FINISH_UPLOAD_FILE:
				Toast.makeText(UploadFileActivity.this, "������" + UploadFile.CURRENT_FILE_INDEX + ": �ɹ��ϴ�ͼƬ  " + mImageName + " ��", Toast.LENGTH_SHORT).show();
				sendNotification("�ϴ��ɹ�", "������" + UploadFile.CURRENT_FILE_INDEX + ": �ɹ��ϴ�ͼƬ  " + mImageName + "����������");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(true, false, UPLOAD_INTERVAL);
				}
				break;
			case UploadFile.THROW_EXCEPTION:
				Toast.makeText(UploadFileActivity.this, "������" + UploadFile.CURRENT_FILE_INDEX + ": �ϴ�ͼƬ " + mImageName + " ��������ʱ�����쳣���ϴ�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				sendNotification("�ϴ�ʧ��", "������" + UploadFile.CURRENT_FILE_INDEX + ": �ϴ�ͼƬ " + mImageName + " ��������ʱ�����쳣���ϴ�ʧ�ܣ�");
				if(UploadFile.CURRENT_FILE_INDEX == UploadFile.SECOND_FILE) {
					dialog.dismiss();
					uploadNextFile(false, false, REUPLOAD_INTERVAL);
				}
				break;
			//����ˢ��Ŀ¼
			case REFRESH_FOLDER_SUCCESS:
				adapter = new ImageAdapter(UploadFileActivity.this, PICTURE_FOLDER);
				mGallery.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				//ѡ�е�һ��
				PictureUtil pictureUtil = new PictureUtil();
				if(adapter.getCount() > 0) {
					Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(0) + ".big");
			        mCurrentImg = adapter.getImagePath(0);
			        mCurrentFileName = StringUtil.convertBackFolderPath(mCurrentImg);
			        mCurrentFileName = StringUtil.getFileName(mCurrentFileName);
			        mTxtFilePath.setText("�ļ�����" + mCurrentFileName);
			        mImageView.setImageBitmap(bitmap);
			        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			        mGallery.setSelection(0);
				} else {
					mImageView.setImageResource(0);
				}
				break;
			case REFRESH_FOLDER_ERR:
				Toast.makeText(UploadFileActivity.this, "ˢ��Ŀ¼ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			}
		}
	};
	
	public void sendNotification(String title, String message) {
		
		if(mNotificationManager == null) {
			String ns = Context.NOTIFICATION_SERVICE;
			mNotificationManager = (NotificationManager) getSystemService(ns);
		}
				
		int icon = R.drawable.icon;        // icon from resources
		CharSequence tickerText = "camera";              // ticker-text
		long when = System.currentTimeMillis();         // notification time
		Context context = getApplicationContext();      // application Context

		Intent notificationIntent = new Intent(this, UploadFileActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, title, message, contentIntent);
		
		mNotificationManager.notify(NOTIFICATION_ID ++, notification);
	}
	
	/**
	 * �ϴ���һ���ļ�
	 * @param isLastSuccess �ļ������Ƿ�ɹ�
	 * @param isReSend �Ƿ����·���
	 * @param delay �ӳٷ�������
	 */
	public void uploadNextFile(boolean isLastSuccess, boolean isReSend, int delay) {
		if(mUploadFileList.size() < 1) {
			isUploading = false;
			return;
		}
		Object item = mUploadFileList.get(0);
		if(isLastSuccess) {
			mUploadFileList.addSuccess(item);
			mUploadFileList.remove(item);
		} else {
			mUploadFileList.addFailse(item);
			if(!isReSend) {
				mUploadFileList.remove(item);
			}
		}
		if(mUploadFileList.size() < 1) {
			isUploading = false;
			return;
		}
		mImagePath = (String)mUploadFileList.get(0);
		mImageName = StringUtil.getFileName(mImagePath);
//		System.out.println("---------------------" + mImagePath + "----------------------------");
		showDialog();
		Log.i(TAG, "--------------uploadNextFile:Send next picture-----------------");
		dialog.dismiss();
		mUploadOnePicThread = new UploadThread(delay);
		Log.w(TAG, "Send next picture; mUploadFileList size : " + mUploadFileList.size());
		mUploadOnePicThread.start();
	}
	
	/** ͼƬԤ���ؼ�*/
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//��ʼ��Ӧ�ó���
		try {
			IniControl.initConfiguration(this);
			Toast.makeText(this, "��ʼ�����ֳɹ���", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			Toast.makeText(this, "��ʼ�������쳣��", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		//���������ļ��Ƿ���ڣ�������ڣ����������ý���
//		File file = new File(Constant.PERFERENCES_FILE_PATH);
//		if(!file.exists()) {
//			Intent intent = new Intent();
//			intent.setClass(this, ConfigurationActivity.class);
//			this.startActivity(intent);
//			return;
//		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_filed);
		//��ȡͼƬ·��
		PreferencesDAO preferencesDao = new PreferencesDAO(this);
		PICTURE_FOLDER = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
		getComponents();
		startRefreshFolder();
		loadPicture();
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * ��ȡ�ؼ�
	 */
	public void getComponents() {
		mTxtFilePath = (TextView)this.findViewById(R.id.txtFilePath);
		mTxtMessage = (EditText)this.findViewById(R.id.txtMessage);
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
		mGallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
//	            Toast.makeText(UploadFileActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            PictureUtil pictureUtil = new PictureUtil();
	            Bitmap bitmap = pictureUtil.getBitmap(adapter.getImagePath(position) + ".big");
	            mCurrentImg = adapter.getImagePath(position);
	            mCurrentFileName = StringUtil.convertBackFolderPath(mCurrentImg);
	            mCurrentFileName = StringUtil.getFileName(mCurrentFileName);
	            mImageView.setImageBitmap(bitmap);
	            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	            mTxtFilePath.setText("�ļ�����" + mCurrentFileName);
	            mTxtFilePath.setSelected(true);
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
			if(isUploading) {
				Toast.makeText(this, "��ǰ���ϴ���������ִ�У���ȴ�������ɺ����ϴ���", 4000).show();
				return;
			}
			mImagePath = StringUtil.convertBackFolderPath(mCurrentImg);
			mImageName = StringUtil.getFileName(mImagePath);
			mUploadFileList = new UploadFileList();
			mUploadFileList.add(mImagePath);
			showDialog();
			mUploadOnePicThread = new UploadThread();
			mUploadOnePicThread.start();
			break;
		case R.id.btnUploadAll:
			if(isUploading) {
				Toast.makeText(this, "��ǰ���ϴ���������ִ�У���ȴ�������ɺ����ϴ���", 4000).show();
				return;
			}
			int size = adapter.getCount();
			if(size > 0) {
				mImagePath = StringUtil.convertBackFolderPath(adapter.getImagePath(0));
				mImageName = StringUtil.getFileName(mImagePath);
			}
			mUploadFileList = new UploadFileList();
			for(int i = 0; i < size; i ++) {
				mUploadFileList.add(StringUtil.convertBackFolderPath(adapter.getImagePath(i)));
			}
			showDialog();
			mUploadOnePicThread = new UploadThread();
			mUploadOnePicThread.start();
			break;
		}
	}
	
	public void showDialog() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		CharSequence title = "�����ϴ�ͼƬ " + mImageName;
		// CharSequence message = getString(R.string.xxx);
		CharSequence message = "��ǰ�������";
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setMessage("����ѹ��ͼƬ....");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setButton("��̨����", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				i.addCategory(Intent.CATEGORY_HOME);
				startActivity(i);
			}
		});
		progressDialog.setButton2("���ضԻ���", new Dialog.OnClickListener() {
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
//		case R.id.menuNotification:
//			
		//ˢ��Ŀ¼ͼƬ
		case R.id.menuRefresh:
			startRefreshFolder();
			break;
		//�������ý���
		case R.id.menuConfig:
			Intent intent = new Intent();
			if(Integer.parseInt(android.os.Build.VERSION.SDK) > 3) {
				intent.setClass(this, ConfigurationActivity.class);
			} else {
				intent.setClass(this, ConfigurationActivity2.class);
			}
			this.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
			break;
		case R.id.menuClear:
			dialog = ProgressDialog.show(this, "���Ժ�",
	                "������������ļ�......", true);
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						PictureUtil pictureUtil = new PictureUtil();
						pictureUtil.clearImagePieces();
						pictureUtil.clearThumbnail(PICTURE_FOLDER);
						mHandler.sendEmptyMessage(CLEAR_BUFFER);
						pictureUtil.createThumbnails(UploadFileActivity.this, PICTURE_FOLDER);
						refreshFolder();
						mHandler.sendEmptyMessage(REFRESH_FOLDER_SUCCESS);
					} catch(Exception e ){
						e.printStackTrace();
						mHandler.sendEmptyMessage(REFRESH_FOLDER_ERR);
					}
					
				}	
			};
			thread.start();
			break;
		case R.id.menuAbout:
			Builder builder = new Builder(UploadFileActivity.this);
			builder.setTitle("����");
			String msg = "";
			msg += "��ǰ�汾�� " + VersionVo.VERSION_NAME + "\n";
			msg += "�汾���ƣ� " + VersionVo.VERSION_DESC + "\n";

			msg += "�������ڣ� " + VersionVo.PUBLIC_DATE + "\n";
			msg += "�°汾�޸ģ� \n";
			msg += VersionVo.UPLOAD_INFO;
			builder.setMessage(msg);
			builder.setNegativeButton("ȷ��", null);
			builder.show();
			break;
		//�˳�ϵͳ
		case R.id.menuExit:
			exit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * �˳�����
	 */
	public void exit() {
		Builder builder = new Builder(this);
		builder.setTitle("��ʾ").setMessage("���Ƿ�Ҫ�˳�ϵͳ��");
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(isUploading) {
					Builder builder = new Builder(UploadFileActivity.this);
					builder.setTitle("��ʾ").setMessage("��ǰ���ϴ������������У��Ƿ�ֹͣ�ϴ������˳�ϵͳ��");
					builder.setNegativeButton("��̨����", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(Intent.ACTION_MAIN);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
							i.addCategory(Intent.CATEGORY_HOME);
							startActivity(i);
						}
					});
					
					builder.setNeutralButton("�˳�", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					});
					builder.show();
					return;
				} else {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}
		});
		builder.show();
	}
	
	/**
	 * �����첽ˢ��Ŀ¼
	 */
	public void startRefreshFolder() {
		dialog = ProgressDialog.show(this, "���Ժ�", 
                "����ˢ��ͼƬĿ¼......", true);
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					refreshFolder();
					mHandler.sendEmptyMessage(REFRESH_FOLDER_SUCCESS);
				} catch(Exception e ){
					e.printStackTrace();
					mHandler.sendEmptyMessage(REFRESH_FOLDER_ERR);
				}
				
			}	
		};
		thread.start();
	}
	
	/**
	 * ˢ��Ŀ¼ͼƬ��������������ͼ
	 */
	public void refreshFolder() throws Exception {
		try {
			//��ȡͼƬ·��
			PreferencesDAO preferencesDao = new PreferencesDAO(this);
			PICTURE_FOLDER = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
			PictureUtil pictureUtil = new PictureUtil();
//			pictureUtil.clearThumbnail(PICTURE_FOLDER);
			pictureUtil.createThumbnails(this, PICTURE_FOLDER);
//			pictureUtil.clearImagePieces();
		} catch (Exception e) {
			Log.e(TAG, "throw a exception while refresh the picture folder!");
			e.printStackTrace();
			throw new Exception("throw a exception while refresh the picture folder!");
		}
	}
	
	/**
	 * ��ȡ���ý��洫�صĲ���
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case ACTIVITY_REQUEST_CODE:
			if(resultCode == Activity.RESULT_OK){
				PreferencesDAO preferencesDao = new PreferencesDAO(this);
				String folderPath = preferencesDao.getPreferencesByKey(Constant.IMAGE_DIR);
				if(!PICTURE_FOLDER.equals(folderPath)) {
					startRefreshFolder();
				}
			}
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			exit();
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}