package com.camera.head;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;



/**
 * 第一部分--描述信息(超过1字节的数都是：低字节在前，高字节在后)
 * @author 
 *
 */
public class DataDesc {
	
	public static final String TAG = "DataDesc";

	/**起始段*/
	private byte[] startId;
	/**设备编号*/
	private byte[] deviceCode;
	/**口令*/
	private byte[] command;
	/**时间格式：年-月-日x时:分:秒*/
	private byte[] time = new byte[12];
	/**单位名*/
	private byte[] unitName;
	/**测站名*/
	private byte[] surveyStation;
	/** 摄像头ID*/
	private byte cameraId;
	/**描述*/
	private byte[] desc;
	/**结束*/
	private byte[] endId;
	
	public DataDesc() {
		startId = new byte[]{'$', 'C', 'A', 'A'};
		endId = new byte[]{'$', 'E', 'N', 'D'};
	}
	
	public String getStartId() {
		return CodeUtil.bytes2String(startId);
	}
	
	public long getDeviceCode() {
		return CodeUtil.bytes2int(deviceCode);
	}
	
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = CodeUtil.getGB2312ByteArray(deviceCode);
	}
	
	public long getCommand() {
		return CodeUtil.bytes2int(command);
	}
	
	public void setCommand(String command) {
		this.command = CodeUtil.getGB2312ByteArray(command);;
	}
	
	public String getTime() {
		return null;
	}
	
	public void setTime(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = dateFormat.format(date);
		this.time = CodeUtil.getGB2312ByteArray(strDate);
	}
	
	public String getUnitName() {
		try {
			return new String(unitName, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setUnitName(String unitName) {
		this.unitName = CodeUtil.getGB2312ByteArray(unitName);
	}
	
	public String getSurveyStation() {
		try {
			return new String(surveyStation, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setSurveyStation(String surveyStation) {
		this.surveyStation = CodeUtil.getGB2312ByteArray(surveyStation);
	}
	
	public int getCameraId() {
		return cameraId;
	}
	
	public void setCameraId(int cameraId) {
		if(cameraId < 1 || cameraId > 255) {
			Log.e(TAG, "CameraId is out of rang! CameraId will be set default value 0");
			cameraId = 1;
		}
		this.cameraId = CodeUtil.int2bytes(cameraId)[1];
	}
	
	public String getDesc() {
		try {
			return new String(desc, "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setDesc(String desc) {
		this.desc = CodeUtil.getGB2312ByteArray(desc);
	}
	
	public String getEndId() {
		return CodeUtil.bytes2String(startId);
	}
	
	/**
	 * 把该对象转换成字节数组返回
	 * @return
	 */
	public byte [] getBytes(){
		int n = startId.length + deviceCode.length + command.length 
			+ time.length + unitName.length + surveyStation.length
			+ 1 + desc.length + endId.length + 8;
		byte[] headBytes = new byte[n];
		int offset = 0;
		
		offset = addBytes(headBytes, startId, offset, false);
		offset = addBytes(headBytes, deviceCode, offset, false);
		offset = addBytes(headBytes, command, offset, false);
		offset = addBytes(headBytes, time, offset, false);
		offset = addBytes(headBytes, unitName, offset, false);
		offset = addBytes(headBytes, surveyStation, offset, false);
		headBytes[offset ++] = cameraId;
		headBytes[offset ++] = 0x3B;
		offset = addBytes(headBytes, desc, offset, false);
		offset = addBytes(headBytes, endId, offset, true);
		
		printf(this);
		
		return headBytes;
	}
	
	private int addBytes(byte[] headBytes, byte[] bytes, int offset, boolean isEnd) {
		for(int i = 0; i < bytes.length; i ++) {
			headBytes[offset ++] = bytes[i];
		}
		if(!isEnd)
			headBytes[offset ++] = 0x3B;
		return offset;
	}
	
	private static void printf(DataDesc data) {
//		data.setDeviceCode(1111111111L);
//		data.setCommand(2222222222L);
//		data.setDesc("我是郑澍璋");
//		data.setSurveyStation("测站");
//		data.setCameraId(10);
//		data.setTime(new Date());
//		data.setUnitName("单位名");
		
		String strPrint = "StartId:" + data.getStartId() + "\n";
		strPrint += "DeviceCide:" + data.getDeviceCode() + "\n";
		strPrint += "Command:" + data.getCommand() + "\n";
		strPrint += "Desc:" + data.getDesc() + "\n";
		strPrint += "SurveyStation:" + data.getSurveyStation() + "\n";
		strPrint += "CameraId:" + data.getCameraId() + "\n";
		strPrint += "UnitName:" + data.getUnitName() + "\n";
		strPrint += "End:" + data.getEndId() + "\n";
		
		Log.i(TAG, strPrint);
	}
	
}
