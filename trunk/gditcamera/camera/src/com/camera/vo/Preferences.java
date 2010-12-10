package com.camera.vo;

import java.util.Map;

import com.camera.util.StringUtil;

/**
 * ������Ϣʵ��
 *
 */
public class Preferences {

	/**Э���(fixed)*/
	private static final byte [] pho = new byte[]{0x24,0x50,0x48,0x4F};
	/**Ĭ�ϵ�ͼƬĿ¼*/
	private String defaultImgDir;
	/**��վ����(16byte)*/
	private String subStation;
	/**����(16byte)*/
	private String command;
	/**��վ����(16byte)*/
	private String surveyStation;
	/**վ��(8byte)*/
	private String stationCode;
	/**
	 * ��������ַ�б�
	 * ��ʽ:Map<"192.168.1.1",8080> �� Map<"www.baidu.com",8080>
	 */
	private Map<String,String> hostList;
	
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
	public Map<String,String> getHostList() {
		return hostList;
	}
	public void setHostList(Map<String,String> hostList) {
		this.hostList = hostList;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * ��ȡЭ���
	 * @return
	 */
	public byte [] getPho(){
		return pho;
	}
	/**
	 * **********************************
	 * ����ػ�ȡ����1������2��ip��ַ�Ͷ˿�
	 * @return
	 * **********************************
	 */
	public String getHost1IP(){
		if(this.hostList==null){
			return null;
		}
		if(this.hostList.size()<=0){
			return null;
		}
		String address = this.hostList.get(Constant.HOST_1);
		if(null!=address){
			return StringUtil.getIpByHostAdd(address);
		}else{
			return null;
		}
	}
	
	public int getHost1Port(){
		if(this.hostList==null){
			return -1;
		}
		if(this.hostList.size()<=0){
			return -1;
		}
		
		String address = this.hostList.get(Constant.HOST_1);
		if(null!=address){
			return StringUtil.getPortByHostAdd(address);
		}else{
			return -1;
		}
	}
	
	public String getHost2IP(){
		if(this.hostList==null){
			return null;
		}
		if(this.hostList.size()<=0){
			return null;
		}
		String address = this.hostList.get(Constant.HOST_2);
		if(null!=address){
			return StringUtil.getIpByHostAdd(address);
		}else{
			return null;
		}
	}
	
	public int getHost2Port(){
		if(this.hostList==null){
			return -1;
		}
		if(this.hostList.size()<=0){
			return -1;
		}

		String address = this.hostList.get(Constant.HOST_2);
		if(null!=address){
			return StringUtil.getPortByHostAdd(address);
		}else{
			return -1;
		}
	}
	
}
