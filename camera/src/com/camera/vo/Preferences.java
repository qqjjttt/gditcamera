package com.camera.vo;

import java.util.List;

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
	/**��������ַ�б���ʽ:http://192.168.1.1:8080��http://www.baidu.com:8080,Ĭ��ʹ��80�˿�*/
	private List<String> hostList;
	
}
