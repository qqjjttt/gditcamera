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
	 * ����վ�����Ƿ�Ϸ�
	 * @param command
	 * @return
	 */
	public static final String isCorrectCommand(String command){
		if(command == null || command.length()<=0){
			return "������Ϊ��";
		}
		if(command.length()>16){
			return "���ܳ���16���ַ�";
		}
		char [] cArray = command.toCharArray();
		for(char c : cArray){
			if(!isValidateChar(c)){
				return "���������ֻ�Ӣ����ĸ";
			}
		}
		return null;
	}

	/**
	 * ����������Ƿ�Ϸ�
	 * @param hostAdd
	 * @return
	 */
	public static final String isCorrectHostAdd(String ip){
		if(ip==null)return "����Ϊ��";
		if(ip.lastIndexOf(".")==ip.length()-1 || ip.indexOf(".")==0){
			return "ip��ַ��ʽ����";
		}
		String [] strIp = ip.split("\\.");
		if(strIp.length!=4){
			return "ip��ַ��ʽ����";
		}
		for(int i=0;i<4;i++){
			System.out.println(strIp[i]);
			int value = 0;
			try{
				value = Integer.parseInt(strIp[i]);
			}catch(NumberFormatException e){
				return "��������";
			}
			if(i==0 || i==3){
				if(value<=0 || value>=255){
					return "ip���ַ�Χ(0~255)";
				}
			}else{
				if(value<0 || value>=255){
					return "ip���ַ�Χ(0~255)";
				}
			}
		}
		return null;
	}
	
	/**
	 * ������ȷ��������ַ��ȡip
	 * @param hostAdd
	 * @return
	 */
	public static final String getIpByHostAdd(String hostAdd){
		int indexProtocol = hostAdd.indexOf("//");
		int indexPort = hostAdd.lastIndexOf(":");
		Log.d("indexProtocol", indexProtocol+"");
		return hostAdd.substring(indexProtocol+2, indexPort);
	}
	
	/**
	 * ������ȷ��������ַ��ȡ�˿�
	 * @param hostAdd
	 * @return
	 */
	public static final int getPortByHostAdd(String hostAdd){
		int indexPort = hostAdd.lastIndexOf(":");
		String portStr = hostAdd.substring(indexPort+1);
		return Integer.parseInt(portStr);
	}
	
	/**
	 * ���˿ں��Ƿ�Ϸ�
	 * @param hostAdd
	 * @return
	 */
	public static final String isCorrectPort(String port){
		int p = 0;
		try{
			p = Integer.parseInt(port);
		}catch(NumberFormatException e){
			return "��������ȷ������";
		}
		if(p<=0||p>65535){
			return "��Χ(0~65535)";
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
