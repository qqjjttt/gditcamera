package com.camera.vo;

/**
 * ��һ����--������Ϣ
 * @author tian
 *
 */
public class DataDesc {

	/**��ʼ��*/
	private String startId;
	/**�豸���*/
	private int deviceCode;
	/**����*/
	private int command;
	/**ʱ���ʽ����-��-��xʱ:��:��*/
	private String time;
	/**��λ��*/
	private String unitName;
	/**��վ��*/
	private String surveyStation;
	/**����*/
	private String desc;
	/**����*/
	private String endId;
	
	public String getStartId() {
		return startId;
	}
	public String setStartId(String startId) {
		this.startId = startId;
		return "error";
	}
	public int getDeviceCode() {
		return deviceCode;
	}
	public String setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
		return "error";
	}
	public int getCommand() {
		return command;
	}
	public String setCommand(int command) {
		this.command = command;
		return "error";
	}
	public String getTime() {
		return time;
	}
	public String setTime(String time) {
		this.time = time;
		return "error";
	}
	public String getUnitName() {
		return unitName;
	}
	public String setUnitName(String unitName) {
		this.unitName = unitName;
		return "error";
	}
	public String getSurveyStation() {
		return surveyStation;
	}
	public String setSurveyStation(String surveyStation) {
		this.surveyStation = surveyStation;
		return "error";
	}
	public String getDesc() {
		return desc;
	}
	public String setDesc(String desc) {
		this.desc = desc;
		return "error";
	}
	public String getEndId() {
		return endId;
	}
	public String setEndId(String endId) {
		this.endId = endId;
		return "error";
	}
	
	@Override
	public String toString(){
		return "";
	}
	
	/**
	 * �Ѹö���ת�����ֽ����鷵��
	 * @return
	 */
	public byte [] toBytes(){
		return new byte[0];
	}
}
