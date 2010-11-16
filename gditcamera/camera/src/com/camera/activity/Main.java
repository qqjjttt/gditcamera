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

import com.camera.util.Constant;
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
	private EditText etSubStation,etCommand,etSurveyStation,
			etStationCode,etHost1Ip,etHost2Ip,etHost1Port,etHost2Port;
	/**���б༭�򣬰�ť�ĸ���������*/
	private RelativeLayout mLayoutSubStation,mLayoutCommand,mLayoutSurveyStation,
			mLayoutStationCode,mLayoutBtnSave,mLayoutBtnSaveTest1,mLayoutBtnSaveTest2;

	/**Ĭ�ϵ�ͼƬĿ¼*/
	private String defaultImgDir;
	/**�־�����(16byte)*/
	private String subStation;
	/**����(16byte)*/
	private String command;
	/**��վ����(16byte)*/
	private String surveyStation;
	/**վ��(8byte)*/
	private String stationCode;
	private String path;
	/**����1��2 IP*/
	private String host1Ip,host2Ip;
	/**����1��2 Port*/
	private String port1,port2;
	
	/**�������ò���*/
	private PreferencesDAO dao;
	/**��ʶ�ϴ����ñ༭��ǰ�Ƿ�Ϊ���޸�״̬*/
	private boolean mCanChange1 = true;
	/**��ʶ���������ñ༭��ǰ�Ƿ�Ϊ���޸�״̬*/
	private boolean mCanChange2 = true;
	
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
		mLayoutCommand = (RelativeLayout) findViewById(R.id.layoutCommand);
		mLayoutSurveyStation = (RelativeLayout) findViewById(R.id.layoutSurveyStation);
		mLayoutStationCode = (RelativeLayout) findViewById(R.id.layoutStationCode);
		mLayoutBtnSave = (RelativeLayout) findViewById(R.id.layoutBtnSave);
		mLayoutBtnSaveTest1 = (RelativeLayout) findViewById(R.id.layoutBtnSaveTest1);
		mLayoutBtnSaveTest2 = (RelativeLayout) findViewById(R.id.layoutBtnSaveTest2);
		
		createTab();
		setListener();
		
		dao = new PreferencesDAO(this);
		//���ѱ�������ò������������
		Preferences savedPref = dao.getPreferences();
		if(savedPref!=null){
			btnBrowse.getEditText().setText(savedPref.getDefaultImgDir());
			etSubStation.setText(savedPref.getSubStation());
			etSurveyStation.setText(savedPref.getSurveyStation());
			etStationCode.setText(savedPref.getStationCode());
			String ip1 = savedPref.getHost1IP();
			String ip2 = savedPref.getHost1IP();
			int port1 = savedPref.getHost1Port();
			int port2 = savedPref.getHost2Port();
			etHost1Ip.setText(savedPref.getHost1IP());
			etHost2Ip.setText(savedPref.getHost2IP());
			etHost1Ip.setText(ip1==null?"":ip1);
			etHost2Ip.setText(ip2==null?"":ip2);
			etHost1Port.setText(port1<=0?"":port1+"");
			etHost2Port.setText(port2<=0?"":port2+"");
		}
	}
	
	public void setListener() {
		btnBrowse = (CEditTextButton) this.findViewById(R.id.btnBrowse);
		mBtnExit = (Button)this.findViewById(R.id.btnExit);
		mBtnUpdateManager = (Button)this.findViewById(R.id.btnUpdateManager);
//		mBtnTestService = (Button)this.findViewById(R.id.btnTestService);
		mBtnSave = (Button)this.findViewById(R.id.btnSave);
		mBtnSave1 = (Button)this.findViewById(R.id.btnSave1);
		mBtnSave2 = (Button)this.findViewById(R.id.btnSave2);
		mBtnTest1 = (Button)this.findViewById(R.id.btnTest1);
		mBtnTest2 = (Button)this.findViewById(R.id.btnTest2);
		etSubStation = (EditText) this.findViewById(R.id.etSubStation);
		etCommand = (EditText) this.findViewById(R.id.etCommand);
		etSurveyStation = (EditText) this.findViewById(R.id.etSurveyStation);
		etStationCode = (EditText) this.findViewById(R.id.etStationCode);
		etHost1Ip = (EditText) this.findViewById(R.id.etHost1Ip);
		etHost2Ip = (EditText) this.findViewById(R.id.etHost2Ip);
		etHost1Port = (EditText) this.findViewById(R.id.etHost1Port);
		etHost2Port = (EditText) this.findViewById(R.id.etHost2Port);

		btnBrowse.setOnClickListener(this);
		mBtnSave1.setOnClickListener(this);
		mBtnSave2.setOnClickListener(this);
		mBtnTest1.setOnClickListener(this);
		mBtnTest2.setOnClickListener(this);
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
			if(mCanChange1){
				if(checkUploadConfig()){
					Preferences p = new Preferences();
//					Map<String,Integer> hosts = new HashMap<String,Integer>();
//					hosts.put("http://192.168.1.1:8080",8080);
//					hosts.put("http://192.168.1.2:8080",8080);
//					hosts.put("http://192.168.1.3:8080",8080);
					p.setDefaultImgDir(defaultImgDir);
					p.setSubStation(subStation);
					p.setCommand(command);
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
		case R.id.btnSave1:
			if(checkServerConfig(1)){
				dao.saveByKey(Constant.HOST_1, "http://"+this.host1Ip+":"+this.port1);
				Toast.makeText(this, "������1����ɹ�", 300);
				setModifyEnable(false,1);
			}
			break;
		case R.id.btnSave2:
			if(checkServerConfig(2)){
				dao.saveByKey(Constant.HOST_2, "http://"+this.host2Ip+":"+this.port2);
				Toast.makeText(this, "������2����ɹ�", 300);
				setModifyEnable(false,2);
			}
			break;
		case R.id.btnTest1:
			break;
		case R.id.btnTest2:
			break;
		}
	}
	
	/**
	 * �����������Ƿ���Ч
	 */
	private boolean checkUploadConfig() {
		this.defaultImgDir = btnBrowse.getEditText().getText().toString();
		this.subStation = etSubStation.getText().toString();
		this.command = etCommand.getText().toString();
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
		if((errMsg=StringUtil.isCorrectCommand(command))!=null){
			isVaild = false;
			this.etCommand.setError(errMsg);
			mLayoutCommand.removeView(etCommand);
			mLayoutCommand.addView(etCommand);
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

	/**
	 * ��������IP �˿���û����д��ȷ
	 * @param which 1�����������1��2�����������2
	 * @return
	 */
	public boolean checkServerConfig(int which){
		boolean isVaild = true;
		switch(which){
		case 1:
			this.host1Ip = etHost1Ip.getText().toString();
			this.port1 = etHost1Port.getText().toString();
			String errMsg = null;
			Log.d("host1Ip", host1Ip+"");
			if((errMsg=StringUtil.isCorrectHostAdd(host1Ip))!=null){
				isVaild = false;
				this.etHost1Ip.setError(errMsg);
				
			}
			if((errMsg=StringUtil.isCorrectPort(port1))!=null){
				isVaild = false;
				this.etHost1Port.setError(errMsg);
				
			}
			return isVaild;
		case 2:
			this.host2Ip = etHost2Ip.getText().toString();
			this.port2 = etHost2Port.getText().toString();
			if((errMsg=StringUtil.isCorrectHostAdd(host2Ip))!=null){
			isVaild = false;
			this.etHost2Ip.setError(errMsg);
			
			}
			if((errMsg=StringUtil.isCorrectPort(port2))!=null){
				isVaild = false;
				this.etHost2Port.setError(errMsg);
				
			}
			return isVaild;
			default :
				return false;
		}
		
	}
	
	/**
	 * ���ð�ť�ϵ������Ǳ��滹���޸�
	 * @param enabled 
	 */
	private void setModifyEnable(boolean enabled){
		mCanChange1 = enabled;
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
	 * ���ð�ť�ϵ������Ǳ��滹���޸�
	 * @param enabled
	 * @param which 1�����������1��2�����������2
	 */
	private void setModifyEnable(boolean enabled,int which){
		mCanChange2 = enabled;
		switch(which){
		case 1:
			if(enabled){
				mBtnSave1.setText("����");
			}else{
				mBtnSave1.setText("�޸�");
			}
			etHost1Ip.setEnabled(enabled);
			etHost1Port.setEnabled(enabled);
			break;
			case 2:
			if(enabled){
				mBtnSave2.setText("����");
			}else{
				mBtnSave2.setText("�޸�");
			}
			etHost2Ip.setEnabled(enabled);
			etHost2Port.setEnabled(enabled);
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