package com.camera.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.camera.activity.R;

/**
 * EditText�� Button�Ľ�Ͽؼ�
 * @author ֣���
 */
public class CEditTextButton extends RelativeLayout implements OnClickListener {

	/** Button�ؼ��Ŀ��,��λΪpx����*/
	private static final int BTN_WIDTH  = 58;
	/** Button�ؼ���padding��С*/
	private static final int[] BUTTON_PADDING = {0, 0, 0, 0};
	/** Ĭ��Button������ʽ*/
	private static final int BUTTON_BACKGROUND_RESOURCE = 0;
	
	/** EditText�ؼ���padding��С*/
	private static final int[] EDITTEXT_PADDING = {10, 4, 4, 4};
	/** Ĭ��EditText������ʽ*/
	private static final int EDITTEXT_BACKGROUND_RESOURCE = 0;
	/** Ĭ��EditText��margin��С*/
	private static final int[] EDITTEXT_MARGIN = {0, 0, BTN_WIDTH, 0};
	
	
	/** �ؼ�Ĭ�ϵĸ߶ȣ���λΪdip*/
	private static final int HEIGHT = 30;
	/** Ĭ�ϵı�����ʽ*/
	private static final int BACKGROUND_RESOURCE = R.drawable.edittext_button;
	/** Ĭ��padding��С*/
	private static final int[] PADDING = {0, 0, 0, 0};
	
	/** EditText�ؼ�*/
	private EditText mEditText;
	
	/** Button�ؼ�*/
	private Button mBtn;
	/** Button�ĵ����¼�*/
	private OnClickListener mOnClickListener;
	
	/** Context����*/
	private Context mContext;

	public CEditTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initLayout();
		initProperties();
	}

	public CEditTextButton(Context context) {
		super(context);
		this.mContext = context;
		initLayout();
		initProperties();
	}
	
	/**
	 * ��ʼ�����ָ�����
	 */
	private void initLayout() {
		int i[] = PADDING;
		this.setPadding(i[0], i[1], i[2], i[3]);
		LayoutParams lp = null;
		
		//����EditText
		mEditText = new EditText(mContext);
		lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.alignWithParent = true;
		lp.setMargins(EDITTEXT_MARGIN[0], EDITTEXT_MARGIN[1], 
				EDITTEXT_MARGIN[2], EDITTEXT_MARGIN[3]);
		mEditText.setBackgroundResource(EDITTEXT_BACKGROUND_RESOURCE);
		this.addView(mEditText, lp);
		
		//����Button
		mBtn = new Button(mContext);
		lp = new LayoutParams(BTN_WIDTH, LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.alignWithParent = true;
		mBtn.setBackgroundResource(BUTTON_BACKGROUND_RESOURCE);
		mBtn.setGravity(Gravity.CENTER);
		this.addView(mBtn, lp);
	}
	
	/**
	 * ��ʼ������
	 */
	private void initProperties() {
		
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setClickable(true);
		this.setBackgroundResource(BACKGROUND_RESOURCE);
		
		mEditText.setSingleLine();
		int i[] = EDITTEXT_PADDING;
		mEditText.setPadding(i[0], i[1], i[2], i[3]);
		mEditText.setClickable(false);
		mEditText.setFocusable(false);
		mEditText.setFocusableInTouchMode(false);
		
		i = BUTTON_PADDING;
		mBtn.setPadding(i[0], i[1], i[2], i[3]);
		mBtn.setClickable(false);
		mBtn.setFocusable(false);
		mBtn.setFocusableInTouchMode(false);
		mBtn.setText("���");
		
	}
	
	@Override
	public void onClick(View v) {
		if(mOnClickListener != null) {
			mOnClickListener.onClick(this);
		}
	}

	public EditText getEditText() {
		return mEditText;
	}

	public void setEditText(EditText mEditText) {
		this.mEditText = mEditText;
	}

	public Button getBtn() {
		return mBtn;
	}

	public void setBtn(Button mBtn) {
		this.mBtn = mBtn;
	}

	public OnClickListener getOnClickListener() {
		return mOnClickListener;
	}


	@Override
	public void setEnabled(boolean enabled) {
		this.setFocusable(enabled);
		mBtn.setEnabled(enabled);
		mEditText.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
}
