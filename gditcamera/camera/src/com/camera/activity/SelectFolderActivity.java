package com.camera.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.camera.adapter.FileListAdapter;
import com.camera.vo.FileItem;

public class SelectFolderActivity extends Activity implements
		OnItemClickListener, Button.OnClickListener {

	/** ��ʾ�ļ����б� */
	private ListView mFolderListView;
	/** ѡ��ť */
	private Button mChoose;
	/** ȡ����ť */
	private Button mCancel;
	/** �洢�ļ��� */
	private List<FileItem> mFolders;
	/** sdcard·�� */
	private String mSdcardPath;
	/** Ĭ�ϴ�·�� */
	private File mDefaultFile;
	/** ��ǰ�ļ��� */
	private File curreentFile;
	FileListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosefolder);
		mSdcardPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		// ȡ�ý���Ŀؼ�
		mFolderListView = (ListView) findViewById(R.id.lvFolder);
		mFolderListView.setOnItemClickListener(this);
		mChoose = (Button) this.findViewById(R.id.btnChoose);
		mCancel = (Button) this.findViewById(R.id.btnCancel);
		mChoose.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		// ȡ��Ĭ���ļ���������ʱĬ��SD��
		mDefaultFile = new File(mSdcardPath);
		curreentFile = mDefaultFile;
		mFolders = new ArrayList<FileItem>();
		fileListView(mDefaultFile);// ����ļ���
	}

	/**
	 * ����ļ���
	 * 
	 * @param file
	 */
	private void fileListView(File file) {
		this.mFolders.clear();
		FileItem fileItem;
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {

				fileItem = new FileItem();
				fileItem.setTitle(files[i].getName());

				if (files[i].isDirectory()) {
					fileItem.setFlag(FileItem.FOLDER);
					fileItem.setImageResid(R.drawable.fileicon_dir);
					this.mFolders.add(fileItem);
				}
			}
		}
		Collections.sort(mFolders);
		if (file.getParentFile() != null
				&& !(file.getAbsolutePath().equals(mSdcardPath))) {
			fileItem = new FileItem();
			fileItem.setTitle("�����ϼ�");
			fileItem.setImageResid(R.drawable.fileicon_back);
			mFolders.add(0, fileItem);
		}
		mAdapter = new FileListAdapter(this, this.mFolders);
		mFolderListView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		String selectedFileName = this.mFolders.get(position).getTitle();
		File file;
		// �������ļ����¼�
		if (selectedFileName.equals("�����ϼ�")) {
			file = this.curreentFile.getParentFile();
		} else {
			file = new File(this.curreentFile.getAbsolutePath() + "/"
					+ selectedFileName);
		}
		this.curreentFile = file;
		fileListView(file);
	}

	@Override
	public void onClick(View v) {
		if (v == mChoose) {
			// �����ļ���
			Intent intent = new Intent();
			String path = this.curreentFile.getPath();
			intent.putExtra("path", path);
			setResult(Activity.RESULT_OK, intent);// ����Activity�Ż�ֵ
			this.finish();
		} else {
			Intent intent = new Intent();
			setResult(Activity.RESULT_CANCELED, intent);
			this.finish();
		}

	}
}
