package com.camera.vo;

import java.util.Map;

/**
 * ������Ϣʵ��
 * @author yaotian
 *
 */
public class Preferences {

	/**Ĭ�ϵ�ͼƬĿ¼*/
	private String defaultImgDir;
	/**��վ����(16byte)*/
	private String subStation;
	/**��վ����(16byte)*/
	private String surveyStation;
	/**վ��(8byte)*/
	private String stationCode;
	/**
	 * ��������ַ�б�
	 * ��ʽ:http://192.168.1.1:8080 �� http://www.baidu.com:8080,Ĭ��ʹ��80�˿�
	 */
	private Map<Integer,String> hostList;
	
	public String getDefaultImgDir() {
		return defaultImgDir;
	}
	public void setDefaultImgDir(String defaultImgDir) {
		this.defaultImgDir = defaultImgDir;
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
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public Map<Integer, String> getHostList() {
		return hostList;
	}
	public void setHostList(Map<Integer, String> hostList) {
		this.hostList = hostList;
	}
	
}
