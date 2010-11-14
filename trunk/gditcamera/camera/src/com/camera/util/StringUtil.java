package com.camera.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.util.Log;

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
		return filePath.replace("/", "_");
	}
	
	/**
	 * ������ͼƬ·��ת����ԭͼƬ·��
	 * @param filePath ����ͼ·��
	 * @return
	 */
	public static String convertBackFolderPath(String filePath) {
		int position = filePath.lastIndexOf("/");
		String strTmp = filePath.substring(position);
		return strTmp.replace("_", "/");
	}
	
	/**
	 * ���ͼƬ·���Ƿ�Ϸ�
	 * @param imgDir
	 * @return
	 */
	public static final String isCorrectImgDir(String imgDir){
		if(imgDir.indexOf("/")!=0){
			return "·��Ҫ��\"/\"��ͷ";
		}
		File f = new File(imgDir);
		if((!f.exists())){
			return "���ļ��в�����";
		}
		if(!f.isDirectory()){
			return "����Ŀ¼";
		}
		return null;
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
	
	/**
	 * ���ַ��������ض��ı���ת�����ض����ȵ��ֽ����飬�ַ������ֽ����鳬��ָ���ĳ���ʱ����null,����ָ�����Ȳ�0
	 * @param str Ҫת�����ַ���
	 * @param encoding ת������
	 * @param len ��Ҫת�ɵ�byte����ĳ���
	 * @return byte []
	 */
	public static final byte [] getByteArrayByLength(String str,String encoding,int len){
		byte [] b = new byte[len];
		byte [] tmp;
		int tmpLen = 0;
		try {
			tmp = str.getBytes(encoding);
			tmpLen = tmp.length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		if(tmpLen>len){ 
			Log.e("String", "the String:("+str+") bytes array length("+tmpLen+") is Longer then "+len);
			return null;
		}
		for(int i=0;i<len;i++){
			if(i>=tmpLen)
				b[i] = 0;
			else
				b[i] = tmp[i];
		}
		return b;
		
	}
}
