package com.camera.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * ��λ��ת������
 * @author ֣���
 */
public class UnitUtil {

	/**
	 * ��dip��λת��px��λ
	 * @param context context����
	 * @param dip dip��ֵ
	 * @return
	 */
	public static int formatDipToPx(Context context, int dip) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		 return (int) Math.ceil( dip * dm.density);
	}
}
