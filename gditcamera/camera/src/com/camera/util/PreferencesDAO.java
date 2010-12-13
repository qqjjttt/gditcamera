package com.camera.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.camera.vo.Constant;
import com.camera.vo.Preferences;

/**
 * ������Ϣʵ��������ݷ��ʶ���
 * @author yaotian
 *
 */
public class PreferencesDAO {

	private Context context;
	private SharedPreferences sp;
    private SharedPreferences.Editor spEdit;
    /**
     * Ĭ�ϵ����ò���ʵ��
     */
    static final Preferences defaultPref;
    static{
		defaultPref = new Preferences();
		Map<String,String> hosts = new HashMap<String,String>();
//		hosts.put(Constant.HOST_1,"http://112.125.33.161:10808");
//		hosts.put(Constant.HOST_2,"http://1.1.1.1:8080");
		defaultPref.setHostList(hosts);
		defaultPref.setDefaultImgDir(Constant.SDCARD_PATH + "/DCIM/100MEDIA");
		defaultPref.setSubStation("�й�ˮ��");
		defaultPref.setSurveyStation("Ӧ�����");
		defaultPref.setStationCode("1090999");
		defaultPref.setCommand("1090999");
    }

	public PreferencesDAO(Context c){
		context = c;
		sp = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
    	spEdit = sp.edit();
	}

	/**
	 * ��ȡ��ǰ���ò���ʵ��
	 * @return
	 */
	public Preferences getPreferences(){
		if(sp.getAll().size()<=0){
			return null;
		}else{
			Preferences p = new Preferences();
			p.setDefaultImgDir(sp.getString(Constant.IMAGE_DIR, ""));
			p.setSubStation(sp.getString(Constant.STATION_SUB, ""));
			p.setCommand(sp.getString(Constant.COMMAND, ""));
			p.setSurveyStation(sp.getString(Constant.STATION_SURVEY, ""));
			p.setStationCode(sp.getString(Constant.STATION_CODE, ""));
			//�����б�
			Map<String,String> m = new HashMap<String, String>();
			String host1Add = sp.getString(Constant.HOST_1,null);
			String host2Add = sp.getString(Constant.HOST_2,null);

			if(host1Add!=null){
				m.put(Constant.HOST_1,host1Add);
			}
			if(host2Add!=null){
				m.put(Constant.HOST_2,host2Add);
			}
			
			p.setHostList(m);
			return p;
		}
	}
	
	/**
	 * �����ֶ�����ȡĳ�����ò���
	 * @param key
	 * @return
	 */
	public String getPreferencesByKey(String key){
		return sp.getString(key, getDefaultPreferencesByKey(key));
	}
	
	/**
	 * ��ȡĬ�ϵ����ò���ʵ��
	 * @return
	 */
	public static final Preferences getDefaultPreferences(){
		return defaultPref;
	}
	
	/**
	 * �����ֶ�����ȡĬ�ϵ�ĳ�����ò���
	 * @param key
	 * @return
	 */
	public static final String getDefaultPreferencesByKey(String key){
		if(key.equals(Constant.IMAGE_DIR)){
			return defaultPref.getDefaultImgDir();
		}else if(key.equals(Constant.STATION_CODE)){
			return defaultPref.getStationCode();
		}else if(key.equals(Constant.STATION_SUB)){
			return defaultPref.getSubStation();
		}else if(key.equals(Constant.STATION_SURVEY)){
			return defaultPref.getSurveyStation();
		}else if(key.equals(Constant.HOST_1)){
			Map<String,String> m = defaultPref.getHostList();
			final Set<String> keySet = m.keySet();
			return m.get(keySet);
		}else if(key.equals(Constant.HOST_2)){
			Map<String,String> m = defaultPref.getHostList();
			final Set<String> keySet = m.keySet();
			return m.get(keySet);
		}
		return null;
	}
	
	/**
	 * �������ò���ʵ��
	 * @param p
	 * @return
	 */
	public boolean save(Preferences p){
		spEdit.putString(Constant.IMAGE_DIR, p.getDefaultImgDir());
		spEdit.putString(Constant.STATION_CODE,p.getStationCode());
		spEdit.putString(Constant.STATION_SUB, p.getSubStation());
		spEdit.putString(Constant.COMMAND, p.getCommand());
		spEdit.putString(Constant.STATION_SURVEY, p.getSurveyStation());
		Map<String,String> hostList = p.getHostList();
		if(null!=hostList){
			for(String key : hostList.keySet())
				spEdit.putString(key, hostList.get(key));
		}
		return spEdit.commit();
	}
	
	/**
	 * �����ֶ�������ĳ�����ò���
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveByKey(String key,String value){
		spEdit.putString(key, value);
		return spEdit.commit();
	}


	/**
	 * �����ֶ���ɾ��ĳ�����ò���
	 * @param key
	 * @return
	 */
	public boolean deleteByKey(String key){
		spEdit.remove(key);
		return spEdit.commit();
	}
	
	/**
	 * ɾ���������ò���
	 * @return
	 */
	public boolean deleteAll(){
		spEdit.clear();
		return spEdit.commit();
	}
	
	/**
	 * ������������ʵ�壬��ͬ��save(p)
	 * @param p
	 * @return
	 */
	public boolean update(Preferences p){
		return save(p);
	}
	
	/**
	 * �����ֶ�������ĳ�����ò�������ͬ��saveByKey(key,value)
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean updateByKey(String key,String value){
		return saveByKey(key, value);
	}
}
