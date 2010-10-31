package com.camera.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.camera.widget.IListener.IOnItemClickListener;

/**
 * �������ؼ���ʵ�ְѼ���Button�ؼ����ϵ���������Ч��
 * @author ֣���
 */
public class CToolbar extends LinearLayout {
	
	/**---------------�������Ĳ���-------------**/
	//public static LayoutParams lp = new LayoutParams
	
	//�������пؼ���Ĭ����ʽ
	private int mItemStyle;
	//�������пؼ�����������ʽ
	private int mSelectedStyle;
	//�������пؼ���������Ƿ���ʾ��ͬ����ʽ
	private boolean mIsShowSelectedStyle = true;
	//��������ť�����¼�
	private IOnItemClickListener mOnItemClickListener;
	//�������Ŀؼ�����
	private List<View> mListView = new ArrayList<View>();

	public CToolbar(Context context) {
		super(context);
	}
	
	/**
	 * ���캯��
	 * @param itemStyle �������ؼ���Ĭ����ʽ
	 * @param selectedStyle �������ؼ�����������ʽ
	 * @param isShowSelectedStyle �������пؼ���������Ƿ���ʾ��ͬ����ʽ
	 */
	public CToolbar(Context context, int itemStyle, 
			int selectedStyle, boolean isShowSelectedStyle) {
		super(context);
		this.mItemStyle = itemStyle;
		this.mSelectedStyle = selectedStyle;
		this.mIsShowSelectedStyle = isShowSelectedStyle;
	}
	
	public CToolbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * �򹤾������һ���ؼ������Զ�Ϊ�¿ؼ�������ʽ
	 */
	public void addItem(View child) {
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1.0f;
		child.setBackgroundResource(mItemStyle);
		child.setLayoutParams(lp);
		super.addView(child);
		mListView.add(child);
		dispatchItemClickEvent(child);
	}
	
	/**
	 * �򹤾������һ���ؼ����������¿ؼ�����ʽ���ú��������ӿؼ����Լ��������ʽ����Ը������Ĳ���
	 * @param child �ӿؼ�
	 * @param lp ����ڸ������Ĳ��֣����ΪNULLʱ��Ӧ��Ĭ�ϲ���
	 * @param styleId �ؼ���ʽ �ؼ�����ʽID�����Ϊ0ʱ��Ӧ��Ĭ�ϵ���ʽ
	 */
	public void addItem(View child, LayoutParams lp, int styleId) {
		//Ӧ����ʽ
		if(styleId == 0) {
			styleId = mItemStyle;
		}
		child.setBackgroundResource(styleId);
		
		//Ӧ�ò���
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1.0f;
		}
		child.setLayoutParams(lp);
		
		super.addView(child);
		mListView.add(child);
		dispatchItemClickEvent(child);
	}
	
	/**
	 * ��ȡ�����еĿؼ������û�иÿؼ����򷵻�NULL
	 * @param index �ؼ������
	 * @return ��ȡ���Ŀؼ�
	 */
	public View getItem(int index) {
		return mListView.get(index);
	}
	
	/**
	 * �ӹ��������Ƴ��ؼ�
	 * @param child Ҫ�Ƴ����ӿؼ�
	 */
	public void removeItem(View child) {
		this.removeView(child);
		mListView.remove(child);
	}
	
	/**
	 * �ӹ��������Ƴ��ؼ�
	 * @param child Ҫ�Ƴ����ӿؼ����
	 */
	public void removeItem(int index) {
		this.removeView(this.getChildAt(index));
		mListView.remove(index);
	}
	
	/**
	 * �Ƴ����й������ϵĿؼ�
	 */
	public void removeAllItems() {
		this.removeAllViews();
		mListView.clear();
	}

	/**
	 * Ϊÿһ���¼ӽ����Ŀؼ���Ӽ����¼�������mOnItemClickListener.onItemClickͳһ����
	 * @param view �����Ŀؼ�
	 */
	protected void dispatchItemClickEvent(View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				
				//��ʾ�ڹ�������ť�����ť����ʽ
				if(mIsShowSelectedStyle) {
					if(mSelectedStyle != 0) {
						view.setBackgroundResource(mSelectedStyle);
						for(View v : mListView) {
							if(!v.equals(view)) {
								v.setBackgroundResource(mItemStyle);
							}
						}
					}
				}
				
				if(mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(mListView, view, mListView.indexOf(view), view.getId());
				}
			}
		});
	}
	
	/**
	 * ˢ�¹�������Ӧ��
	 */
	public void refreshChange() {
		
	}

	
	/**
	 * ��ȡ�ӿؼ���ͳһ��ʽ
	 */
	public int getItemStyle() {
		return mItemStyle;
	}

	/**
	 * �����ӿؼ���ͳһ��ʽ
	 */
	public void setItemStyle(int mItemStyle) {
		this.mItemStyle = mItemStyle;
	}

	/**
	 * ��ȡ����ӿؼ���ؼ�����ʽ
	 */
	public int getSelectedStyle() {
		return mSelectedStyle;
	}

	/**
	 * ���õ���ӿؼ���ؼ�����ʽ
	 */
	public void setSelectedStyle(int mSelectedStyle) {
		this.mSelectedStyle = mSelectedStyle;
	}

	/**
	 * �������пؼ���������Ƿ���ʾ��ͬ����ʽ
	 */
	public boolean isShowSelectedStyle() {
		return mIsShowSelectedStyle;
	}

	/**
	 * �������пؼ���������Ƿ���ʾ��ͬ����ʽ
	 */
	public void setShowSelectedStyle(boolean isShowSelectedStyle) {
		this.mIsShowSelectedStyle = isShowSelectedStyle;
	}

	/**
	 * �����ӿؼ��ļ����¼�
	 */
	public void setOnItemClickListener(IOnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

}
