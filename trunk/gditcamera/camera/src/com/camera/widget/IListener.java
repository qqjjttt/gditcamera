package com.camera.widget;

import java.util.List;

import android.view.View;

/**
 * �����Զ���ļ����¼������ڸ�����
 * @author ֣���
 */
public interface IListener {

	/**
	 * ��һ�������е�ĳ���ؼ������ʱ����ʹ�øü�����
	 */
	public interface IOnItemClickListener {
		
		/**
		 * ��֣��谡����������¼�
		 * @param list ���ڵĿؼ��б�
		 * @param view �������ؼ�����
		 * @param index �ؼ��������е�λ��
		 * @param id �������ؼ���ID
		 */
		public void onItemClick(List<View> list, View view, int index, int id);
	}
}
