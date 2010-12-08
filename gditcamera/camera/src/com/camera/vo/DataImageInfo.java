package com.camera.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.camera.net.DataHeadUtil;
import com.camera.util.StringUtil;

/**
 * �ڶ�����--ͼ����Ϣ(����1�ֽڵ������ǣ����ֽ���ǰ�����ֽ��ں�)
 * @author tian
 *
 */
public class DataImageInfo {

	/**������ͷ�ܳ���*/
	private static final int LENGTH = 25;
	
	/**ͼ���ʶ���ַ���(ASCII���)*/
	private byte [] startId = new byte[4];
	/**վ��(4�ֽ�)*/
	private int code;
	/**����(4�ֽ�)*/
	private int command;
	/**ʱ��(7�ֽ�)*/
	private byte [] time = new byte[7];
	/**��ǰ�����(2�ֽ�)*/
	private short currentPackage;
	/**ͼ�������(2�ֽ�)*/
	private short totalPackage;
	/**ͼ��֡����(2�ֽ�)*/
	private short dataLength;
	
	public byte[] getStartId() {
		return startId;
	}
	public String setStartId(String s){
		char [] tmp = s.toCharArray();
		if(tmp.length>startId.length){
			return "���4���ַ�";
		}
		for(char c : tmp){
			if((int)c<0 || (int)c>255){
				return "������ASCII����ַ�";
			}
		}
		for(int i=0;i<startId.length;i++){
			startId[i] = (byte)tmp[i];
		}
		return null;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public byte[] getTime() {
		return time;
	}
	/**������������ʱ��*/
	public String setTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String dateStr = format.format(date);
		String [] dateNum = dateStr.split("-");
		
		int year = Integer.parseInt(dateNum[0]);
		int month = Integer.parseInt(dateNum[1]);
		int day = Integer.parseInt(dateNum[2]);
		int hour = Integer.parseInt(dateNum[3]);
		int minite = Integer.parseInt(dateNum[4]);
		int second = Integer.parseInt(dateNum[5]);
		
		if((year<2000 || year>65535)){
			return "��������";
		}
		int index = 0;
		
		byte [] tmp_year = DataHeadUtil.int2bytes(year);
		this.time[index++] = tmp_year[1];
		this.time[index++] = tmp_year[0];
		this.time[index++] = (byte)month;
		this.time[index++] = (byte)day;
		this.time[index++] = (byte)hour;
		this.time[index++] = (byte)minite;
		this.time[index++] = (byte)second;
		
		return null;
	}
	public short getCurrentPackage() {
		return currentPackage;
	}
	public void setCurrentPackage(short currentPackage) {
		this.currentPackage = currentPackage;
	}
	public short getTotalPackage() {
		return totalPackage;
	}
	public void setTotalPackage(short totalPackage) {
		this.totalPackage = totalPackage;
	}
	public short getDataLength() {
		return dataLength;
	}
	public void setDataLength(short dataLength) {
		this.dataLength = dataLength;
	}
	
	@Override
	public String toString(){
		StringBuffer strb = new StringBuffer();
		
		strb.append("0x"+Integer.toHexString(DataHeadUtil.bytes2int(startId))+"; ");
		strb.append("0x"+Integer.toHexString(code)+"; ");
		strb.append("0x"+Integer.toHexString(command)+"; ");
		strb.append("0x"+Integer.toHexString(currentPackage)+"; ");
		strb.append("0x"+Integer.toHexString(dataLength)+"; ");
		strb.append("time:");
		for(byte b : time){
			strb.append(b+"-");
		}
		strb.append(";");
		strb.append("0x"+Integer.toHexString(totalPackage)+"; ");
		
		return strb.toString();
	}
	
	/**
	 * �Ѹö���ת�����ֽ����鷵��
	 * @return
	 */
	public byte[] getBytes(){
		byte [] b = new byte[LENGTH];
		int index = 0;
		for(byte bb : startId){
			b[index++] = bb;
		}
		byte [] tmp = DataHeadUtil.int2bytesMax(code);
		for(int i=(tmp.length-1);i>=0;i--){
			b[index++] = tmp[i];
		}
		
		tmp = DataHeadUtil.int2bytesMax(command);
		for(int i=(tmp.length-1);i>=0;i--){
			b[index++] = tmp[i];
		}
		
		for(byte bb : time){
			b[index++] = bb;
		}
		
		tmp = DataHeadUtil.int2bytesMax(currentPackage);
		for(int i=(tmp.length-1);i>=0;i--){
			b[index++] = tmp[i];
		}
		
		tmp = DataHeadUtil.int2bytesMax(totalPackage);
		for(int i=(tmp.length-1);i>=0;i--){
			b[index++] = tmp[i];
		}
		
		tmp = DataHeadUtil.int2bytesMax(dataLength);
		for(int i=(tmp.length-1);i>=0;i--){
			b[index++] = tmp[i];
		}
		
		return b;
	}
}
