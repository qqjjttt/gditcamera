package com.camera.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * ��Ƭ�ϴ����ݵ�����ͷ
 *
 */
public class DataHead implements Serializable{

	private static final long serialVersionUID = -5294458703266575872L;
	
	/**id:����*/
	private String id;
	/**$PHO ASCII��(4byte)*/
	private byte [] pho;
	/**�־�����(16byte)--���һ��*/
	private String subStation;
	/**��վ����(16byte)--���һ��*/
	private String surveyStation;
	/**��Ƭ����,Unicode(32char=64byte)--���һ��*/
	private String phoDesc;
	/**վ��(8byte)--4�ֽ�*/
	private String stationCode;
	/**����(16byte)--4�ֽ�*/
	private String command;
	/**ʱ��(7byte)*/
	private Date dataTime;
	/**����ͷID(1byte)--���һ��*/
	private byte cameraId;
	/**��ǰ����(2byte)*/
	private int currentPackage;
	/**�ܰ���,ʮ�����Ʊ�ʾ(2byte)*/
	private int totalPackage;
	/**����֡����:ÿ����Ƭ��С��������ͷ,ʮ�����Ʊ�ʾ(2byte)*/
	private int dataLength;
	
	public DataHead(){
		pho = new byte[4];
	}
	
	//=========Getter and Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte [] getPho() {
		return pho;
	}

	public void setPho(byte [] pho) {
		this.pho = pho;
	}

	public String getSubStation() {
		return subStation;
	}

	public void setSubStation(String subStation) {
		this.subStation = subStation;
	}

	public String getSurveyStation() {
		return surveyStation;
	}

	public void setSurveyStation(String surveyStation) {
		this.surveyStation = surveyStation;
	}

	public String getPhoDesc() {
		return phoDesc;
	}

	public void setPhoDesc(String phoDesc) {
		this.phoDesc = phoDesc;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public byte getCameraId() {
		return cameraId;
	}

	public void setCameraId(byte cameraId) {
		this.cameraId = cameraId;
	}

	public int getCurrentPackage() {
		return currentPackage;
	}

	public void setCurrentPackage(int currentPackage) {
		this.currentPackage = currentPackage;
	}

	public int getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(int totalPackage) {
		this.totalPackage = totalPackage;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public String toString() {
		StringBuffer strb = new StringBuffer();
		strb.append("$pho="+printByteArray(this.pho)+"\n");
		strb.append("subStation="+this.subStation+"\n");
		strb.append("surveyStation="+this.surveyStation+"\n");
		strb.append("phoDesc="+this.phoDesc+"\n");
		strb.append("stationCode="+this.stationCode+"\n");
		strb.append("command="+this.command+"\n");
		strb.append("dataTime="+this.dataTime+"\n");
		strb.append("cameraId="+this.cameraId+"\n");
		strb.append("currentPackage="+this.currentPackage+"\n");
		strb.append("totalPackage="+this.totalPackage+"\n");
		return strb.toString();
	}

	public String printByteArray(byte [] b){
		StringBuffer strb = new StringBuffer();
		for(byte by : b){
			strb.append(by+",");
		}
		return strb.toString();
	}
}
