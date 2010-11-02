package com.camera.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.camera.activity.R;
import com.camera.util.UnitUtil;

/**
 * EditText�� Button�Ľ�Ͽؼ�
 * @author ֣���
 */
public class CEditTextButton extends RelativeLayout {

	/** Button�ؼ��Ŀ��,��λΪpx����*/
	private static final int BTN_WIDTH  = 60;
	/** Button�ؼ���padding��С*/
	private static final int[] BUTTON_PADDING = {2, 2, 2, 2};
	/** Ĭ��Button������ʽ*/
	private static final int BUTTON_BACKGROUND_RESOURCE = 0;
	
	/** EditText�ؼ���padding��С*/
	private static final int[] EDITTEXT_PADDING = {2, 2, 2, 2};
	/** Ĭ��EditText������ʽ*/
	private static final int EDITTEXT_BACKGROUND_RESOURCE = 0;
	/** Ĭ��EditText��margin��С*/
	private static final int[] EDITTEXT_MARGIN = {0, 0, BTN_WIDTH, 0};
	
	
	/** �ؼ�Ĭ�ϵĸ߶ȣ���λΪdip*/
	private static final int HEIGHT = 40;
	/** Ĭ�ϵı�����ʽ*/
	private static final int BACKGROUND_RESOURCE = R.drawable.edittext_button;
	/** Ĭ��padding��С*/
	private static final int[] PADDING = {0, 0, 0, 0};
	
	/** EditText�ؼ�*/
	private EditText mEditText;
	/** Button�ؼ�*/
	private Button mBtn;
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
		int i[] = UnitUtil.formatDipToPx(mContext, PADDING);
		this.setPadding(i[0], i[1], i[2], i[3]);
		LayoutParams lp = new LayoutParams(200, 
				UnitUtil.formatDipToPx(mContext, HEIGHT));
		this.setLayoutParams(lp);
		
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
		this.addView(mBtn, lp);
	}
	
	/**
	 * ��ʼ������
	 */
	private void initProperties() {
		
		this.setFocusable(true);
		this.setBackgroundColor(Color.RED);
		
		mEditText.setSingleLine();
		int i[] = UnitUtil.formatDipToPx(mContext, EDITTEXT_PADDING);
		mEditText.setPadding(i[0], i[1], i[2], i[3]);
		
		i = UnitUtil.formatDipToPx(mContext, BUTTON_PADDING);
		mBtn.setPadding(i[0], i[1], i[2], i[3]);
		
		mBtn.setText("��ť");
		mBtn.setBackgroundColor(Color.GRAY);
		mEditText.setBackgroundColor(Color.YELLOW);
		this.setBackgroundResource(BACKGROUND_RESOURCE);
	}

	
}
