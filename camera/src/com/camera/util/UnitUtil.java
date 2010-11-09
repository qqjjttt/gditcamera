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
		 return  (int)Math.ceil( dip * dm.density);
	}
	
	/**
	 * ��dip��λת��px��λ
	 * @param context context����
	 * @param dip dip��ֵ
	 * @return
	 */
	public static double formatDipToPx(Context context, double dip) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
		 return  Math.ceil( dip * dm.density);
	}
	
	/**
	 * ��dip��λת��px��λ
	 * @param context context����
	 * @param dips dip����
	 * @return ����px�������
	 */
	public static int[] formatDipToPx(Context context, int[] dips) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
	     int[] result = new int[dips.length];
	     for(int i = 0; i < dips.length; i ++) {
	    	 result[i] =  (int)Math.ceil( dips[i] * dm.density);
	     }
		 return result;
	}
	
	/**
	 * ��dip��λת��px��λ
	 * @param context context����
	 * @param dips dip����
	 * @return ����px�������
	 */
	public static double[] formatDipToPx(Context context, double[] dips) {
		 DisplayMetrics dm = new DisplayMetrics();   
	     ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm); 
	     double[] result = new double[dips.length];
	     for(int i = 0; i < dips.length; i ++) {
	    	 result[i] =  Math.ceil( dips[i] * dm.density);
	     }
		 return result;
	}
}
