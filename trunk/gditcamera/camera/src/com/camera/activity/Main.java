package com.camera.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.camera.util.IniControl;
import com.camera.util.PreferencesDAO;
import com.camera.util.StringUtil;
import com.camera.vo.Preferences;
import com.camera.widget.CEditTextButton;
import com.camera.widget.CTabView;
import com.camera.widget.CTabView.CTabViewFactory;

public class Main extends TabActivity implements OnClickListener {
	
	private final static int REQUESTCODE_FOLDER = 1;//����ת��ѡ���ļ����������
	
	private Button mBtnExit;
	private Button mBtnUpdateManager;
	private CEditTextButton btnBrowse;
	private Button mBtnSave;
	/**�������������õ��İ�ť*/
	private Button mBtnTest1,mBtnTest2,mBtnSave1,mBtnSave2;
	/**�����������õ��ı༭��*/
	private EditText etSubStation,etSurveyStation,
			etStationCode,etHost1Ip,etHost2Ip,etHost1Port,etHost2Port;
	/**���б༭�򣬰�ť�ĸ���������*/
	private RelativeLayout mLayoutSubStation,mLayoutSurveyStation,
			mLayoutStationCode,mLayoutBtnSave,layoutBtnSaveTest1,layoutBtnSaveTest2;

	/**Ĭ�ϵ�ͼƬĿ¼*/
	private String defaultImgDir;
	/**�־�����(16byte)*/
	private String subStation;
	/**��վ����(16byte)*/
	private String surveyStation;
	/**վ��(8byte)*/
	private String stationCode;
	private String path;
	
	/**�������ò���*/
	private PreferencesDAO dao;
	/**��ʶ�༭��ǰ�Ƿ�Ϊ���޸�״̬*/
	private boolean mIsodifyAble = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//��ʼ��Ӧ�ó���
		try {
			IniControl.initConfiguration(this);
			Toast.makeText(this, "��ʼ�����ֳɹ���", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			Toast.makeText(this, "��ʼ�������쳣��", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}

		mLayoutSubStation = (RelativeLayout) findViewById(R.id.layoutSubStation);
		mLayoutSurveyStation = (RelativeLayout) findViewById(R.id.layoutSurveyStation);
		mLayoutStationCode = (RelativeLayout) findViewById(R.id.layoutStationCode);
		mLayoutBtnSave = (RelativeLayout) findViewById(R.id.layoutBtnSave);
		
		createTab();
		setListener();
		
		dao = new PreferencesDAO(this);
	}
	
	public void setListener() {
		btnBrowse = (CEditTextButton) this.findViewById(R.id.btnBrowse);
		mBtnExit = (Button)this.findViewById(R.id.btnExit);
		mBtnUpdateManager = (Button)this.findViewById(R.id.btnUpdateManager);
//		mBtnTestService = (Button)this.findViewById(R.id.btnTestService);
		mBtnSave = (Button)this.findViewById(R.id.btnSave);
		etSubStation = (EditText) this.findViewById(R.id.etSubStation);
		etSurveyStation = (EditText) this.findViewById(R.id.etSurveyStation);
		etStationCode = (EditText) this.findViewById(R.id.etStationCode);
		
		btnBrowse.setOnClickListener(this);
		mBtnExit.setOnClickListener(this);
		mBtnUpdateManager.setOnClickListener(this);
//		mBtnTestService.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBrowse:
			Intent intent3 = new Intent();
			intent3.setClass(this, SelectFolderActivity.class);
			this.startActivityForResult(intent3, REQUESTCODE_FOLDER);
			break;
		case R.id.btnUpdateManager:
			Intent intent = new Intent();
			intent.setClass(this, UploadFileActivity.class);
			this.startActivity(intent);
			this.finish();
			break;
		case R.id.btnExit:
			this.finish();
			break;
//		case R.id.btnTestService:
//			Intent intent2 = new Intent();
//			intent2.setClass(this, SelectFolderActivity.class);
//			this.startActivity(intent2);
//			break;
		case R.id.btnSave:
			if(mIsodifyAble){
				if(checkInput()){
					Preferences p = new Preferences();
//					Map<String,Integer> hosts = new HashMap<String,Integer>();
//					hosts.put("http://192.168.1.1:8080",8080);
//					hosts.put("http://192.168.1.2:8080",8080);
//					hosts.put("http://192.168.1.3:8080",8080);
					p.setDefaultImgDir(defaultImgDir);
					p.setSubStation(subStation);
					p.setStationCode(stationCode);
					p.setSurveyStation(surveyStation);
					if(dao.save(p)){
						Log.d("save:","success");
						setModifyEnable(false);
						Toast.makeText(this, "����ɹ�", 300).show();
					}
				}
			}else{
				setModifyEnable(true);
			}
			break;
		}
	}
	
	/**
	 * �����������Ƿ���Ч
	 */
	private boolean checkInput() {
		this.defaultImgDir = btnBrowse.getEditText().getText().toString();
		this.subStation = etSubStation.getText().toString();
		this.surveyStation = etSurveyStation.getText().toString();
		this.stationCode = etStationCode.getText().toString();
		
		boolean isVaild = true;
		String errMsg = null;
		if((errMsg=StringUtil.isCorrectImgDir(defaultImgDir))!=null){
			isVaild = false;
			EditText et = this.btnBrowse.getEditText();
			et.setError(errMsg);
			this.btnBrowse.removeView(et);
			this.btnBrowse.addView(et);
		}
		if((errMsg=StringUtil.isCorrectSubStation(subStation))!=null){
			isVaild = false;
			this.etSubStation.setError(errMsg);
			mLayoutSubStation.removeView(etSubStation);
			mLayoutSubStation.addView(etSubStation);
		}
		if((errMsg=StringUtil.isCorrectSurveyStation(surveyStation))!=null){
			isVaild = false;
			this.etSurveyStation.setError(errMsg);
			mLayoutSurveyStation.removeView(etSurveyStation);
			mLayoutSurveyStation.addView(etSurveyStation);
		}
		if((errMsg=StringUtil.isCorrectStationCode(stationCode))!=null){
			isVaild = false;
			this.etStationCode.setError(errMsg);
			mLayoutStationCode.removeView(etStationCode);
			mLayoutStationCode.addView(etStationCode);
		}
		return isVaild;
	}

	private void setModifyEnable(boolean enabled){
		mIsodifyAble = enabled;
		if(enabled){
			mLayoutBtnSave.removeView(mBtnSave);
			mBtnSave.setText("����");
			mLayoutBtnSave.addView(mBtnSave);
		}else{
			mLayoutBtnSave.removeView(mBtnSave);
			mBtnSave.setText("�޸�");
			mLayoutBtnSave.addView(mBtnSave);
		}
		
		btnBrowse.getEditText().setEnabled(enabled);
		etSubStation.setEnabled(enabled);
		etSurveyStation.setEnabled(enabled);
		etStationCode.setEnabled(enabled);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case REQUESTCODE_FOLDER:
			if(resultCode==Activity.RESULT_OK){
				path=data.getStringExtra("path");
				btnBrowse.getEditText().setText(path);
			}
			break;
		}
	}
	
	

}