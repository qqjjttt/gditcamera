package com.camera.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.activity.R;
import com.camera.util.UnitUtil;

/**
 * �����������ؼ�
 * @author ֣���
 */
public class CTitleBar extends RelativeLayout {

	//��߰�ť
	private View mLeftView;
	//�ұ߰�ť
	private View mRightView;
	//�м�TextView����
	private View mTitleView;
	//Context����
	private Context context;
	
	private int mLeftViewId, mRightViewId, mTitleViewId;
	
	public CTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CTitleBar);
		System.out.println("left_view: " + a.getString(R.styleable.CTitleBar_left_view));
		System.out.println("right_view: " + a.getString(R.styleable.CTitleBar_right_view));
		mLeftViewId = a.getResourceId(R.styleable.CTitleBar_left_view, 0);
		mRightViewId = a.getResourceId(R.styleable.CTitleBar_right_view, 0);
		mTitleViewId = a.getResourceId(R.styleable.CTitleBar_title_view, 0);
		a.recycle();
	}
	
	public CTitleBar(Context context) {
		super(context);
		this.context = context;
	}
	
	/**
	 * �������Ĺ��캯��
	 * @param txtTitle ����ؼ�
	 * @param leftView ��߿ؼ�
	 * @param rightView �ұ߿ؼ�
	 */
	public CTitleBar(Context context, TextView txtTitle, 
			TextView leftView, TextView rightView) {
		super(context);
		this.context = context;
		this.addLeftView(leftView);
		this.addRightView(rightView);
		this.addTitleView(txtTitle);
	}
	
	/**
	 * ����Ĭ�ϵı����������ؼ����ֱ�����߿ؼ����м�TextView���⣬�ұ߿ؼ�,
	 * ���������һ����ΪNULL�����ʾ�����ظÿؼ�
	 * @param title ����
	 * @param txtBtnLeft ��߰�ťText
	 * @param txtBtnRight �ұ߰�ťText
	 */
	public void loadDefaulComponents(String title, String txtBtnLeft, String txtBtnRight) {
		LayoutParams lp;
		//�������TextView�ؼ�
		if(title != null) {
			lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			int i = UnitUtil.formatDipToPx(context, 100);
			lp.setMargins(i, 0, i, 0);
			TextView txtTitle = new TextView(this.getContext());
			txtTitle.setGravity(Gravity.CENTER);
			txtTitle.setText(title);
			txtTitle.setTextSize(20);
			txtTitle.setTextColor(Color.WHITE);
			this.mTitleView = txtTitle;
			this.addView(mTitleView, lp);
		}
		//������ߵ�Button�ؼ�
		if(txtBtnLeft != null) {
			lp = new LayoutParams(UnitUtil.formatDipToPx(context, 50), LayoutParams.FILL_PARENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.alignWithParent = true;
			int i = UnitUtil.formatDipToPx(context, 5);
			lp.setMargins(i, i, i, i);
			mLeftView = new Button(this.getContext());
			((TextView)mLeftView).setText(txtBtnLeft);
			this.addView(mLeftView, lp);
		}
		//�����ұߵ�Button�ؼ�
		if(txtBtnRight != null) {
			lp = new LayoutParams(UnitUtil.formatDipToPx(context, 50), LayoutParams.FILL_PARENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.alignWithParent = true;
			int i = UnitUtil.formatDipToPx(context, 5);
			lp.setMargins(i, i, i, i);
			mRightView = new Button(this.getContext());
			((TextView)mRightView).setText(txtBtnLeft);
			this.addView(mRightView, lp);
		}
	}

	/**
	 * ��ȡ��������߿ؼ�
	 */
	public View getLeftView() {
		return mLeftView;
	}

	/**
	 * ���ñ���������߿ؼ�
	 */
	public void addLeftView(View mLeftView) {
		addLeftView(mLeftView, null);
	}
	
	/**
	 * ���ñ���������߿ؼ�
	 */
	public void addLeftView(View mLeftView, LayoutParams lp) {
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		}
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.alignWithParent = true;
		int i = UnitUtil.formatDipToPx(context, 5);
		lp.setMargins(i, i, i, i);
		mLeftView.setPadding(i + 2, mLeftView.getPaddingTop(), i + 2, mLeftView.getPaddingBottom());
		super.addView(mLeftView, -1, lp);
		this.mLeftView = mLeftView;
	}

	/**
	 * ��ȡ�������ұ߿ؼ�
	 */
	public View getRightView() {
		return mRightView;
	}

	/**
	 * ���ñ��������ұ߿ؼ�
	 */
	public void addRightView(View mRightView, LayoutParams lp) {
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		}
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.alignWithParent = true;
		int i = UnitUtil.formatDipToPx(context, 5);
		lp.setMargins(i, i, i, i);
		mRightView.setPadding(i + 2, mRightView.getPaddingTop(), i + 2, mRightView.getPaddingBottom());
		super.addView(mRightView, -1, lp);
		this.mRightView = mRightView;
	}
	
	/**
	 * ���ñ��������ұ߿ؼ�
	 */
	public void addRightView(View mRightView) {
		addRightView(mRightView, null);
	}

	/**
	 * ��ȡ�������м�ؼ�
	 */
	public View getTitleView() {
		return mTitleView;
	}

	/**
	 * ���ñ��������м�ؼ�
	 * @param mTxtTitle
	 */
	public void addTitleView(View mTxtTitle) {
		addTitleView(mTxtTitle, null);
	}
	
	/**
	 * ���ñ��������м�ؼ�
	 * @param mTxtTitle
	 */
	public void addTitleView(View mTxtTitle, LayoutParams lp) {

		
		if(lp == null) {
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		}
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.alignWithParent = true;
		int i = UnitUtil.formatDipToPx(context, 5);
		lp.setMargins(0, i, 0, i);
		mTxtTitle.setPadding(i + 2, mTxtTitle.getPaddingTop(), i + 2, mTxtTitle.getPaddingBottom());
		super.addView(mTxtTitle, -1, lp);
		this.mTitleView = mTxtTitle;
	}

	/**
	 * ��дaddViewInLayout�����������ӵĿؼ�Ϊ���⣬��ؼ����ҿؼ�ʱ�����µ������Զ���ĺ�����
	 */
	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		int id = child.getId();
		if (id == mLeftViewId) {
			this.addLeftView(child);
			return;
		} else  if (id == mRightViewId) {
			this.addRightView(child);
			return;
		} else if (id == mTitleViewId) {
			this.addTitleView(child);
			return;
		}
		super.addView(child, index, params);
	}
	
	
	
	
}
