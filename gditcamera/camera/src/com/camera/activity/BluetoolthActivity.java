package com.camera.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.camera.adapter.DeviceListAdapter;
import com.camera.bluetooth.BluetoothUtil;

public class BluetoolthActivity extends Activity implements OnClickListener {
	
	public static final int STOP_SCAN = 1;
	
	public static final String START_SCAN_TEXT = "ɨ���豸";
	public static final String STOP_SCAN_TEXT = "ֹͣɨ��";
    
	/** ��������*/
	private BluetoothUtil mBtnTool;
	/** �����豸�б�*/
	private ListView mLstDeviceList;
	/** ������*/
	private DeviceListAdapter mAdapter;
	/** ɨ���豸��ť*/
	private Button mBtnScan;
	/** ɨ���б����*/
	private TextView mTxtScanTitle;
	/** �����豸�б�*/
	private List<BluetoothDevice> mDevicesList;
	/** ��ʱ�߳�*/
	private CancleScanThread mCancleThread;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case STOP_SCAN:
				mBtnScan.setText(START_SCAN_TEXT);
				mTxtScanTitle.setText("ɨ�赽�������豸");
				mBtnTool.cancelDiscovery();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * ������ͣ��������
	 */
	private class CancleScanThread extends Thread {
		@Override
		public void run() {
			//Ĭ��ɨ��12����
			try {
				Thread.sleep(60000);
				mBtnTool.cancelDiscovery();
				mHandler.sendEmptyMessage(STOP_SCAN);
			} catch (InterruptedException e) {}
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);
        initContent();
    }
    
    /**
     * ��ʼ�����漰�豸
     */
    private void initContent() {
    	mBtnScan = (Button)this.findViewById(R.id.btnScanDevice);
    	mLstDeviceList = (ListView)this.findViewById(R.id.lstDeviceList);
    	mTxtScanTitle = (TextView)this.findViewById(R.id.txtScanDevices);
    	
    	mBtnTool = new BluetoothUtil(this);
        mBtnTool.initEnvironment();
        mDevicesList = new ArrayList<BluetoothDevice>(mBtnTool.getPairedDevices());
        mAdapter = new DeviceListAdapter(this, mDevicesList);
        mLstDeviceList.setAdapter(mAdapter);
        
        mBtnScan.setOnClickListener(this);
        
        /** ע��㲥*/
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//��������
		switch(requestCode){
		case BluetoothUtil.REQUEST_BLUETOOTH:
			if (resultCode == Activity.RESULT_OK){
				Toast.makeText(this, "�Ѿ����������ܣ�", Toast.LENGTH_LONG).show();
			} else if(resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(this, "����������δ���������ܷ����ļ���", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
    
    /**
     * �������չ㲥
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevicesList.add(device);
                mAdapter.setData(mDevicesList);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnScanDevice:
			if(mBtnScan.getText().toString().equals(START_SCAN_TEXT)) {
		        if(!mBtnTool.startDiscovery()) {
		        	mBtnTool.initEnvironment();
		        } else {
		        	mDevicesList.clear();
		        	mDevicesList = new ArrayList<BluetoothDevice>(mBtnTool.getPairedDevices());
		        	mAdapter.notifyDataSetChanged();
			        mBtnScan.setText(STOP_SCAN_TEXT);
			        mTxtScanTitle.setText("����ɨ�������豸...");
			        mCancleThread = new CancleScanThread();
			        mCancleThread.start();
		        }
			} else {
				mBtnScan.setText(START_SCAN_TEXT);
				mTxtScanTitle.setText("ɨ�赽�������豸");
				mBtnTool.cancelDiscovery();
				mCancleThread.interrupt();
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	
	
}