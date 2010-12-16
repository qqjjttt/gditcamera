package com.camera.head;

/**
 * �ڶ�����--ͼ����Ϣ(����1�ֽڵ������ǣ����ֽ���ǰ�����ֽ��ں�)
 * @author tian
 *
 */
public class DataImage {
	
	public static final String TAG = "DataImage";

	/**������ͷ�ܳ���*/
	private static final int LENGTH = 25;
	
	/**ͼ���ʶ���ַ���(ASCII���)*/
	private byte [] startId;
	/**վ��(4�ֽ�)*/
	private byte[] code;
	/**����(4�ֽ�)*/
	private byte[] command;
	/**ʱ��(7�ֽ�)*/
	private byte[] time = new byte[7];
	/**��ǰ�����(2�ֽ�)*/
	private byte[] currentPackage;
	/**ͼ�������(2�ֽ�)*/
	private byte[] totalPackage;
	/**ͼ��֡����(2�ֽ�)*/
	private byte[] dataLength;
	
	public DataImage() {
		startId = new byte[]{'$', 'D', 'A', 'A'};
	}
	
	public String getStartId() {
		return CodeUtil.bytes2String(startId);
	}
	
	public long getCode() {
		for(int i = 0; i < code.length; i ++) {
			System.out.printf("0x%x", code[i]);
			System.out.println();
		}
		return CodeUtil.bytes2int(code);
	}
	public void setCode(long code) {
		if(code < 0 || code > 4294967295L) {
			Log.e(TAG, "DeviceCode is out of rang! deviceCode will be set default value 0");
			code = 0;
		}
		this.code = CodeUtil.reversalBytes(CodeUtil.int2bytesMax(code));
	}
	public byte[] getCommand() {
		return command;
	}
	public void setCommand(byte[] command) {
		this.command = command;
	}
	public byte[] getTime() {
		return time;
	}
	public void setTime(byte[] time) {
		this.time = time;
	}
	public byte[] getCurrentPackage() {
		return currentPackage;
	}
	public void setCurrentPackage(byte[] currentPackage) {
		this.currentPackage = currentPackage;
	}
	public byte[] getTotalPackage() {
		return totalPackage;
	}
	public void setTotalPackage(byte[] totalPackage) {
		this.totalPackage = totalPackage;
	}
	public byte[] getDataLength() {
		return dataLength;
	}
	public void setDataLength(byte[] dataLength) {
		this.dataLength = dataLength;
	}
	
	public static void main(String[] args) {
		DataImage data = new DataImage();
		data.setCode(45001);
		
		String strPrint = "StartId:" + data.getStartId() + "\n";
		strPrint += "Code:" + data.getCode() + "\n";
//		strPrint += "Command:" + data.getCommand() + "\n";
//		strPrint += "Desc:" + data.getDesc() + "\n";
//		strPrint += "SurveyStation:" + data.getSurveyStation() + "\n";
//		strPrint += "CameraId:" + data.getCameraId() + "\n";
//		strPrint += "UnitName:" + data.getUnitName() + "\n";
//		strPrint += "End:" + data.getEndId() + "\n";
		
		System.out.println(strPrint);
	}
	
	public static class Log {
		public static void e(String tag, String info) {
			System.out.println(tag + ":" + info);
		}
	}
	
	
}
