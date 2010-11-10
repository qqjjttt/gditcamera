package com.camera.util;

/**
 * 字符串工具
 * @author yaotian
 *
 */
public class StringUtil {

	/**
	 * 检查分局名称是否合法
	 * @param subStation
	 * @return 错误信息
	 */
	public static final String isCorrectSubStation(String subStation){
		if(subStation == null || subStation.length()<=0){
			return "不允许为空";
		}
		if(subStation.length()>16){
			return "不能超过16个字符";
		}
		char [] cArray = subStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "请输入数字或英文字母";
			}
		}
		return null;
	}
	
	/**
	 * 检查站码名称是否合法
	 * @param stationCode
	 * @return
	 */
	public static final String isCorrectStation(String stationCode){
		if(stationCode == null || stationCode.length()<=0){
			return "不允许为空";
		}
		if(stationCode.length()>8){
			return "不能超过8个字符";
		}
		char [] cArray = stationCode.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "请输入数字或英文字母";
			}
		}
		return null;
	}
	
	/**
	 * 检查分站名称是否合法
	 * @param surveyStation
	 * @return
	 */
	public static final String isCorrectSurveyStation(String surveyStation){
		if(surveyStation == null || surveyStation.length()<=0){
			return "不允许为空";
		}
		if(surveyStation.length()>16){
			return "不能超过16个字符";
		}
		char [] cArray = surveyStation.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "请输入数字或英文字母";
			}
		}
		return null;
	}
	
	/**
	 * 通过ASCII码判断 char 是否是大小写字母、或者数字中的某一个
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
