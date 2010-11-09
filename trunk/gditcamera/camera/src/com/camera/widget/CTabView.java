package com.camera.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.activity.R;

/**
 * ѡ���ǩ�ؼ���֧�ִ�ֱ��ˮƽ���֣�����ѡ���ǩ�ؼ�������
 * ͨ������CTabView.Builder.Create(CTabView view)�������д���
 * ����������ʹ��
 * 
 * @author ֣���
 */
public class CTabView extends RelativeLayout {
	
	/** ͼ��ؼ�*/
	private View mImageView;
	/** ���ֿؼ�*/
	private TextView mTextView;
	/** ���ڲ����Ӷ�һ��������*/
	private OnClickListener mInnerOnClickListener = null;
	
	private CTabView(Context context) {
		super(context);
		mImageView = new View(context);
		mTextView = new TextView(context);
	}
	
	public static class CTabViewFactory implements OnClickListener {
		
		/** ͼ����ԴID*/
		private int mImageResource = android.R.drawable.stat_sys_phone_call;
		/** ͼ��ĳߴ��С,Ĭ��Ϊ30*30dip */
		private int[] mImageSize = {30, 30};
		/** �Ƿ���ʾͼ��*/
		private boolean mIsShowImage = true;
		/** ͼ��ռ�õĿռ��С,��ֱ����ʱΪռ�õĸ߶ȣ�ˮƽ����ʱΪռ�õĿ�ȣ�Ĭ��Ϊ30dip*/
		private int mImageSpace = 60;
		
		/** ��ʾ�ı�����*/
		private String mText;
		/** ���ִ�С,Ĭ�������СΪ14*/
		private int mTextSize = 15;
		
		private Context mContext;
		/** ����ͼƬ*/
		private int mBackgroundResource = R.drawable.tab_title_bg_normal;
		/** ������л�������һ����ʽ*/
		private int mClickedBackground = R.drawable.tab_title_bg_press;
		/** ������Ƿ��л�������һ����ʽ*/
		private boolean mIsShowClickedBackground = true;
		/** ������Ĳ���,Ĭ��Ϊˮƽ����*/
		private boolean mOrientation = true;
		/** �ؼ��ĸ߶�*/
		private int mHeight = 40;
		
		/** ѡ��ؼ�����*/
		private List<CTabView> mTabViewList;
		private CTabView mTabView;
		
		public CTabViewFactory(Context context) {
			this.mContext = context;
			mTabViewList = new ArrayList<CTabView>();
		}
		
		/**
		 * ����һ��CTabView��������CTabView���󶼱���ͨ���ú������д�����������ʹ��
		 * @param tabView
		 * @return
		 */
		public CTabView create() {
			if(mTabView == null) {
				mTabView = new CTabView(mContext);
				mTabView.setBackgroundResource(mClickedBackground);
			} else {
				mTabView = new CTabView(mContext);
				mTabView.setBackgroundResource(mBackgroundResource);
			}
			mTabViewList.add(mTabView);
			initConext();
			//�����¼�������ʵ��ѡ��һ��������Ȼ���ʽ���ٰ������İ�ť����Ĭ����ʽ
			mTabView.setInnerOnClickListener(this);
			return mTabView;
		}
		
		@Override
		public void onClick(View v) {
			if(mTabView.mInnerOnClickListener == null)
				return;
			if(!mIsShowClickedBackground)
				return;
			for(CTabView tv : mTabViewList) {
				if(!tv.equals(v))
					tv.setBackgroundResource(CTabViewFactory.this.mBackgroundResource);
				else {
					v.setBackgroundResource(CTabViewFactory.this.mClickedBackground);
					((CTabView)v).mInnerOnClickListener.onClick(v);
				}
			}
		}
		
		private void initConext() {
			LayoutParams lp = null;
			//��������
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, mHeight, 1);
			mTabView.setLayoutParams(lp2);
			
			//����ͼ������
			View imgView = mTabView.mImageView;
			imgView.setBackgroundResource(mImageResource);
			
			//�����ı��ؼ�����
			TextView textView = mTabView.mTextView;
			textView.setText(mText);
			textView.setTextSize(mTextSize);
			textView.setTextColor(Color.WHITE);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
			
			//���ı��ؼ���ͼ��ؼ��ӵ�������
			if(mOrientation) {
				//ˮƽ״̬��
				if(mIsShowImage) {
					int[] i = mImageSize;
					lp =  new LayoutParams(i[0], i[1]);
					int k = (mImageSpace - mImageSize[0]) / 2;
					lp.setMargins(k, 0, 0, 0);
					lp.addRule(RelativeLayout.CENTER_VERTICAL);
					
					mTabView.addView(imgView, lp);
				}
				lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				lp.setMargins(mImageSpace, 0, 0, 0);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				mTabView.addView(textView, lp);
				
			} else {
				//��ֱ״̬��
				if(mIsShowImage) {
					int[] i = mImageSize;
					lp =  new LayoutParams(i[0], i[1]);
					int k = (mImageSpace - mImageSize[1]) / 2;
					lp.setMargins(0, k, 0, 0);
					lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					mTabView.addView(imgView, lp);
				}
				textView.setGravity(Gravity.CENTER);
				lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				lp.setMargins(0, mImageSpace, 0, 0);
				mTabView.addView(textView, lp);
			}
		}
		
		/**
		 * ѡ�е�ǰĳһ��
		 * @param position
		 */
		public void setClicked(int position) {
			if(mTabViewList == null || mTabViewList.size() <= position || position < 0) {
				mTabViewList.get(position).setBackgroundResource(this.mClickedBackground);
			}
		}
		
		public void setBackgroundResource(int resid) {
			this.mBackgroundResource = resid;
		}

		/**
		 * ����ͼ��ĳߴ磬��λΪdip
		 * @param width ���
		 * @param height ����
		 */
		public void setImageSize(int width, int height) {
			mImageSize[0] = width;
			mImageSize[1] = height;
		}

		/**
		 * ��ȡͼ��ID
		 */
		public int getImageResource() {
			return mImageResource;
		}

		/**
		 * ����ͼ��
		 */
		public void setImageResource(int mImageId) {
			this.mImageResource = mImageId;
		}
		
		/**
		 * ����ͼ��
		 * @param mImageId ͼ��ID
		 * @param width ͼ��Ŀ�
		 * @param height ͼ��ĸ�
		 */
		public void setImage(int mImageId, int width, int height) {
			mImageSize[0] = width;
			mImageSize[1] = height;
			setImageResource(mImageId);
		}

		
		/**
		 * �Ƿ���ʾͼ��
		 */
		public boolean isShowImage() {
			return mIsShowImage;
		}

		/**
		 * �����Ƿ���ʾͼ��
		 * @param mIsShowImage
		 */
		public void setShowImage(boolean mIsShowImage) {
			this.mIsShowImage = mIsShowImage;
		}

		/**
		 * ��ȡ�ı���С
		 */
		public int getTextSize() {
			return mTextSize;
		}

		/**
		 * �����ı���С
		 */
		public void setTextSize(int textSize) {
			this.mTextSize = textSize;
		}

		/**
		 * ���ú�������
		 */
		public void setOrientation(boolean orientation) {
			this.mOrientation = orientation;
		}

		/**
		 * ����ͼ��ռ�õĿռ��С,��ֱ����ʱΪռ�õĸ߶ȣ�ˮƽ����ʱΪռ�õĿ�ȣ�Ĭ��Ϊ30dip
		 */
		public int getImageSpace() {
			return mImageSpace;
		}

		/**
		 * ����ͼ��ռ�õĿռ��С,��ֱ����ʱΪռ�õĸ߶ȣ�ˮƽ����ʱΪռ�õĿ�ȣ�Ĭ��Ϊ30dip
		 */
		public void setImageSpace(int mImageSpace) {
			this.mImageSpace = mImageSpace;
		}

		/**
		 * ��ȡ�ؼ��������ʾ����ʽ
		 */
		public int getClickedBackground() {
			return mClickedBackground;
		}
		
		/**
		 * ���ÿؼ��ı�����
		 */
		public CTabViewFactory setText(String text) {
			this.mText = text;
			return this;
		}

		/**
		 * ���ÿؼ��������ʾ����ʽ
		 */
		public void setClickedBackground(int mSelectedBackground) {
			this.mClickedBackground = mSelectedBackground;
		}

		/**
		 * ��ȡ�Ƿ���ʾ�ؼ�������л�����ͬ�ı�����ʽ
		 */
		public boolean isShowSelectedBackground() {
			return mIsShowClickedBackground;
		}

		/**
		 * ���ÿؼ�������Ƿ��л�����ͬ����ʽ
		 */
		public void setShowSelectedBackground(boolean mIsShowSelectedBackground) {
			this.mIsShowClickedBackground = mIsShowSelectedBackground;
		}

		public void setHeight(int mHeight) {
			this.mHeight = mHeight;
		}

	}
	
	/**
	 * ��д�û����¼��������¼����ڲ����������ڲ������ã����ڲ�������ִ�����ٴ����û��ļ�����
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mInnerOnClickListener = l;
	}
	
	public void setInnerOnClickListener(OnClickListener listener) {
		super.setOnClickListener(listener);
	}
	
}
