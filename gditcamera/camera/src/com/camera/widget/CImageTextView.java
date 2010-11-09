package com.camera.widget;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.activity.R;

/**
 * ͼ���ı��ؼ���֧�ִ�ֱ��ˮƽ����
 * @author ֣���
 */
public class CImageTextView extends RelativeLayout implements OnClickListener {
	
	/** Ĭ��padding��С*/
	private static final int[] PADDING = {5, 10, 0, 10};
	/** Ĭ�ϱ�����ʽ*/
	private static final int BACKGROUND_RESOURCE = R.drawable.button_bg;

	
	/** ͼ����ԴID*/
	private int mImageResource;
	/** ��ʾ�ı�����*/
	private String mText;
	/** ͼ��ĳߴ��С,Ĭ��Ϊ30*30dip */
	private int[] mImageSize = {30, 30};
	/** ������Ĳ���,Ĭ��Ϊˮƽ����*/
	private boolean mOrientation = true;
	/** ���ֿؼ�*/
	private TextView mTextView;
	/** ͼ��ؼ�*/
	private View mImageView;
	/** �Ƿ���ʾͼ��*/
	private boolean mIsShowImage = true;
	private Context mContext;
	/** ���ִ�С,Ĭ�������СΪ14*/
	private int mTextSize = 15;
	/** ͼ��ռ�õĿռ��С,��ֱ����ʱΪռ�õĸ߶ȣ�ˮƽ����ʱΪռ�õĿ�ȣ�Ĭ��Ϊ30dip*/
	private int mImageSpace = 40;
	/** ������л�������һ����ʽ*/
	private int mClickedBackground;
	/** ������Ƿ��л�������һ����ʽ*/
	private boolean mIsShowClickedBackground = false;
	/** ����ÿؼ���������tab���л��������ø�List��������ѡ�������*/
	private List<View> mListSpecView;
	/** �л���Ĭ�ϱ����������ÿؼ���������TAB�л����Ż�����*/
	private int mBackgroundResource;
	
	private OnClickListener mOnClickListener2 = null;
	
	public CImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		//����Ĭ�ϵ�����
		setDefaultPropertie(mOrientation);
		//��XML�ļ��ж�ȡ�Զ�������
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CImageTextView);
		mImageResource = a.getResourceId(R.styleable.CImageTextView_image, R.drawable.tab_title_bg_normal);
		mText = a.getString(R.styleable.CImageTextView_text);
		mTextSize = a.getDimensionPixelSize(R.styleable.CImageTextView_textSize, mTextSize);
		mImageSize[0] = a.getDimensionPixelSize(R.styleable.CImageTextView_image_width, mImageSize[0]);
		mImageSize[1] = a.getDimensionPixelSize(R.styleable.CImageTextView_image_height, mImageSize[1]);
		mOrientation = a.getBoolean(R.styleable.CImageTextView_orientation, mOrientation);
		mImageSpace = a.getDimensionPixelSize(R.styleable.CImageTextView_imageSpace, mImageSpace);
		a.recycle();
		//����ͼ��
		this.setImageResource(mImageResource);
		//��������
		this.setText(mText);
	}
	
	/**
	 * ���캯��
	 * @param imageId ͼ��ID
	 * @param text �ı�����
	 * @param orientation ��ֱ(false)��ˮƽ(true)����
	 */
	public CImageTextView(Context context, int imageId, String text, boolean orientation) {
		super(context);
		this.mContext = context;
		this.mOrientation = orientation;
		this.mImageResource = imageId;
		this.mText = text;
		this.mOrientation = orientation;
		//����Ĭ�ϵ�����
		setDefaultPropertie(orientation);
		//����ͼ��
		this.setImageResource(imageId);
		//��������
		this.setText(text);
		
	}
	
	/**
	 * ���캯��
	 * @param imageView ͼ��ؼ�
	 * @param textView �ı��ؼ�
	 * @param orientation ��ֱ(false)��ˮƽ(true)����
	 */
	public CImageTextView(Context context, View imageView, TextView textView, boolean orientation) {
		super(context);
		setDefaultPropertie(orientation);
		this.addView(imageView);
		this.addView(textView);
	}
	
	/**
	 * ���캯��,Ĭ��Ϊˮƽ����
	 * @param imageId ͼ��ID
	 * @param text �ı�����
	 */
	public CImageTextView(Context context, int imageId, String text) {
		this(context, imageId, text, true);
	}
	
	/**
	 * ���캯��
	 * @param imageView ͼ��ؼ�
	 * @param textView �ı��ؼ�
	 */
	public CImageTextView(Context context, View imageView, TextView textView) {
		this(context, imageView, textView, true);
	}
	
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public CImageTextView(Context context) {
		super(context);
		this.mContext = context;
		setDefaultPropertie(true);
	}
	
	/**
	 * ���øÿؼ�Ĭ�ϵ�����
	 */
	public void setDefaultPropertie(boolean orientation) {
		this.setFocusable(true);
		int i[] = PADDING;
		this.setPadding(i[0], i[1], i[2], i[3]);
		super.setOnClickListener(this);
		this.setBackgroundResource(BACKGROUND_RESOURCE);
	}
	
	/**
	 * ����ͼ��ĳߴ磬��λΪdip
	 * @param width ���
	 * @param height ����
	 */
	public void setImageSize(int width, int height) {
		mImageSize[0] = width;
		mImageSize[1] = height;
		if(mImageView != null) {
			this.removeView(mImageView);
			this.setImageView(mImageView);
		}
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
		if(mImageView == null) {
			mImageView = new View(mContext);
			this.setImageView(mImageView);
		}
		mImageView.setBackgroundResource(mImageId);
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
	 * ��ȡ�ؼ����ı�
	 */
	public String getText() {
		if(mTextView != null) {
			return mTextView.getText().toString();
		}
		return null;
	}

	/**
	 * �����ı��Ŀؼ�
	 */
	public void setText(String mText) {
		if (mText == null) {
			return;
		}
		this.mText = mText;
		if (mTextView == null) {
			mTextView = new TextView(mContext);
			mTextView.setTextSize(mTextSize);
			mTextView.setTextColor(Color.WHITE);
			this.setTextView(mTextView);
		}
		mTextView.setText(mText);
	}

	/**
	 * ��ȡ�ı��ؼ�
	 */
	public TextView getTextView() {
		return mTextView;
	}

	/**
	 * �����ı��ؼ�
	 * @param mTextView
	 */
	public void setTextView(TextView mTextView) {
		this.mTextView = mTextView;
		
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lp.alignWithParent = true;
		//�л���ˮƽ����
		if(mOrientation) {
			lp.addRule(RelativeLayout.ALIGN_RIGHT);
			lp.setMargins(mImageSpace, 0, 0, 0);
			mTextView.setGravity(Gravity.CENTER_VERTICAL);
		} else {
			lp.addRule(RelativeLayout.ALIGN_BOTTOM);
			lp.alignWithParent = true;
			lp.setMargins(0, mImageSpace, 0, 0);
			mTextView.setGravity(Gravity.CENTER);
		}
		this.addView(mTextView, lp);
	}

	/**
	 * ��ȡͼ��ؼ�
	 */
	public View getImageView() {
		return mImageView;
	}

	/**
	 * ����ͼ��ؼ�
	 * @param mImageView
	 */
	public void setImageView(View mImageView) {

		LayoutParams lp = new LayoutParams(
				mImageSize[0], 
				mImageSize[1]);
		lp.alignWithParent = true;
		//�л���ˮƽ����
		if(mOrientation) {
			lp.setMargins(this.getPaddingLeft(), 0, 0, 0);
			lp.addRule(RelativeLayout.ALIGN_LEFT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
		} else {
			lp.setMargins(0, this.getPaddingTop(), 0, 0);
			lp.addRule(RelativeLayout.ALIGN_TOP);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		this.addView(mImageView, lp);
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
		if(mImageView != null) {
			if(mIsShowImage) {
				this.setImageView(mImageView);
			} else {
				this.removeView(mImageView);
			}
		}
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
		if(mTextView != null) {
			mTextView.setTextSize(textSize);
		}
	}

	/**
	 * ���ú�������
	 */
	public void setOrientation(boolean orientation) {
		if(mImageView == null || mTextView == null) {
			return;
		}
		this.mOrientation = orientation;
		fleshLayout();
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
		fleshLayout();
	}
	
	/**
	 * �������������ˢ�²���
	 */
	private void fleshLayout() {
		if(mImageView != null) {
			this.removeView(mImageView);
			this.setImageView(mImageView);
		}
		if(mTextView != null) {
			this.removeView(mTextView);
			this.setTextView(mTextView);
		}
	}

	/**
	 * ��ȡ�ؼ��������ʾ����ʽ
	 */
	public int getClickedBackground() {
		return mClickedBackground;
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

	/**
	 * ��д�û����¼��������¼����ڲ����������ڲ������ã����ڲ�������ִ�����ٴ����û��ļ�����
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mOnClickListener2 = l;
	}

	@Override
	public void onClick(View v) {
		//��ִ���ڲ��ĵ����߼�����ִ���û��ĵ����߼�
		if(mClickedBackground > 0 && mIsShowClickedBackground) {
			this.setBackgroundResource(mClickedBackground);
		}
		if(mListSpecView != null) {
			for(View view : mListSpecView) {
				if(!view.equals(this))
					view.setBackgroundResource(this.mBackgroundResource);
			}
		}
		if(mOnClickListener2 != null)
			mOnClickListener2.onClick(v);
	}
	
	/**
	 * ����ÿؼ�������tabѡ��������õ��÷���
	 * @param specViewList
	 */
	public void setSpecList(List<View> specViewList, int backgroundResource) {
		this.mListSpecView = specViewList;
		this.mBackgroundResource = backgroundResource;
	}
	
	/**
	 * ���ÿؼ��ı�����ʽΪ���������ʽ
	 */
	public void setClickedResource() {
		this.setBackgroundResource(this.mClickedBackground);
	}
}
