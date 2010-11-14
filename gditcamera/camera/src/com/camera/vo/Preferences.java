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
	 * ��ʽ:Map<"192.168.1.1",8080> �� Map<"www.baidu.com",8080>
	 */
	private Map<String,Integer> hostList;
	
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
	public Map<String,Integer> getHostList() {
		return hostList;
	}
	public void setHostList(Map<String,Integer> hostList) {
		this.hostList = hostList;
	}
	
}
