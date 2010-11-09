package com.camera.activity;

import java.io.IOException;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.camera.util.IniControl;
import com.camera.widget.CEditTextButton;
import com.camera.widget.CTabView;
import com.camera.widget.CTabView.CTabViewFactory;

public class Main extends TabActivity implements OnClickListener {
	
	private Button mBtnExit;
	private Button mBtnUpdateManager;
	private Button mBtnTestService;
	private CEditTextButton btnBrowse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.findViewById(R.id.btnBrowse).setOnClickListener(this);
		//��ʼ��Ӧ�ó���
		try {
			IniControl.initConfiguration(this);
			Toast.makeText(this, "��ʼ�����ֳɹ���", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			Toast.makeText(this, "��ʼ�������쳣��", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		createTab();
		setListener();
	}
	
	public void setListener() {
		mBtnExit = (Button)this.findViewById(R.id.btnExit);
		mBtnUpdateManager = (Button)this.findViewById(R.id.btnUpdateManager);
		mBtnTestService = (Button)this.findViewById(R.id.btnTestService);
		mBtnExit.setOnClickListener(this);
		mBtnUpdateManager.setOnClickListener(this);
		mBtnTestService.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBrowse:
			Intent intent3 = new Intent();
			intent3.setClass(this, SelectFolderActivity.class);
			this.startActivity(intent3);
			break;
		case R.id.btnUpdateManager:
			Intent intent = new Intent();
			intent.setClass(this, UploadFileActivity.class);
			this.startActivity(intent);
			break;
		case R.id.btnExit:
			this.finish();
			break;
		case R.id.btnTestService:
			Intent intent2 = new Intent();
			intent2.setClass(this, SelectFolderActivity.class);
			this.startActivity(intent2);
			break;
		}
	}
	
	/**
	 * ����tabѡ�
	 */
	public void createTab() {
		Resources res = getResources();
	    TabHost tabHost = getTabHost();  
	    TabHost.TabSpec spec;  
	    tabHost.setBackgroundColor(Color.parseColor("#10386B"));
	    
	    CTabViewFactory factory = new CTabViewFactory(this);
	    CTabView tv1 = factory.setText("�ϴ�����").create();
	    CTabView tv2 = factory.setText("����������").create();
	    
	    spec = tabHost.newTabSpec("artists").setIndicator(tv1).setContent(R.id.tab_1);
	    tabHost.addTab(spec);
	    spec = tabHost.newTabSpec("albums").setIndicator(tv2).setContent(R.id.tab_2);
	    tabHost.addTab(spec);
	    tabHost.setCurrentTab(0);
	}

}