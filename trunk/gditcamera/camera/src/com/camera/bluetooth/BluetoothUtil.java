package com.camera.bluetooth;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.Toast;

/**
 * �������͹�����
 * @author ֣���
 */
public class BluetoothUtil {

	/** ���������*/
	public static final int REQUEST_BLUETOOTH = 1;
	/** ����Ĭ��ɨ��ʱ��*/
	public static final int SCAN_SECONDS = 60;
	
	/** ����������*/
	private BluetoothAdapter mBluetoothAdapter;
	
	private Context mContext;
	
	public BluetoothUtil(Context context) {
		this.mContext = context;
	}
	
	/**
	 * Ҫͨ���������͵��ļ�·��
	 * @param filePath �ļ�·��
	 */
	public void sendFile(String filePath) {
		
	}
	
	/**
	 * ��ʼ���������û���
	 */
	public void initEnvironment() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
        	Toast.makeText(mContext, "�����豸��֧���������ܣ�", Toast.LENGTH_SHORT).show();
        	return;
        } else {
        	if (!mBluetoothAdapter.isEnabled()) {
        		Builder builder = new Builder(mContext);
            	builder.setTitle("��ʾ").setMessage("�����豸δ�����������ܣ������Ƿ����ã�");
            	builder.setNegativeButton("ȡ��", null);
            	//���������豸
            	builder.setNeutralButton("ȷ��", new OnClickListener()  {
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	        	    ((Activity)mContext).startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH);
    				}
            	});
            	builder.show();
        	}
        	
        }
	}
	
	/**
	 * ��������ɨ��
	 */
	public boolean startDiscovery() {
		return mBluetoothAdapter.startDiscovery();
	}
	
	/**
	 * ֹͣ����ɨ��
	 */
	public boolean cancelDiscovery() {
		return mBluetoothAdapter.cancelDiscovery();
	}
	/**
	 * ��ȡ��ƥ��������豸
	 * @return ��ƥ��������豸
	 */
	public Set<BluetoothDevice> getPairedDevices() {
		if(mBluetoothAdapter == null)
			return null;
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		return pairedDevices;
	}
}
