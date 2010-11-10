package com.camera.util;

/**
 * �ַ���������
 * @author ֣���
 */
public class StringUtil {

	/**
	 * ���ļ�·����"/"���滻��"_",��/mnt/abc/��ת����mnt_abc_
	 * @param filePath �ļ���
	 * @return ת������ַ���
	 */
	public static String convertFolderPath(String filePath) {
		if(filePath.charAt(0) == '/')
			filePath = filePath.substring(1);
		return filePath.replace("_", "/");
	}
	
	/**
	 * ���־������Ƿ�Ϸ�
	 * @param subStation
	 * @return ������Ϣ
	 */
	public static final String isCorrectSubStation(String subStation){
		if(subStation == null || subStation.length()<=0){
			return "������Ϊ��";
		}
		if(subStation.length()>16){
			return "���ܳ���16���ַ�";
		}
		char [] cArray = subStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "���������ֻ�Ӣ����ĸ";
			}
		}
		return null;
	}
	
	/**
	 * ���վ���Ƿ�Ϸ�
	 * @param stationCode
	 * @return
	 */
	public static final String isCorrectStationCode(String stationCode){
		if(stationCode == null || stationCode.length()<=0){
			return "������Ϊ��";
		}
		if(stationCode.length()>8){
			return "���ܳ���8���ַ�";
		}
		char [] cArray = stationCode.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "���������ֻ�Ӣ����ĸ";
			}
		}
		return null;
	}
	
	/**
	 * ����վ�����Ƿ�Ϸ�
	 * @param surveyStation
	 * @return
	 */
	public static final String isCorrectSurveyStation(String surveyStation){
		if(surveyStation == null || surveyStation.length()<=0){
			return "������Ϊ��";
		}
		if(surveyStation.length()>16){
			return "���ܳ���16���ַ�";
		}
		char [] cArray = surveyStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "���������ֻ�Ӣ����ĸ";
			}
		}
		return null;
	}
	
	/**
	 * ͨ��ASCII���ж� char �Ƿ��Ǵ�Сд��ĸ�����������е�ĳһ��
	 * @param char c
	 * @return boolean
	 */
	public static final boolean isValidateChar(char c){
		int asciiCode = (int)c;
		if( (asciiCode>=48&&asciiCode<=57) || (asciiCode>=65&&asciiCode<=90) || (asciiCode>=97&&asciiCode<=122)){
			return true;
		}else{
			return false;
		}
	}
}
