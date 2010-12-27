package com.camera.head;

/**
 * ����ת��������
 * @author ֣���
 */
public class DataFormat {

	/**
	 * ����(max=65535(Ĭ��))ת���ɳ���Ϊ2(Ĭ��)��byte����
	 * @param num
	 * @return
	 */
	public static byte[] int2bytes(int num) {
		int byteLen = 2;//������ݳ���ֵΪ4�������ת�����������ֵInteger.MAX_VALUE
		int offset = (byteLen-1)*8;
		
		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i<<3)));
		}
		return b;
	}
	
	/**
	 * ����ת���ɳ���Ϊ4��byte����
	 * @param num
	 * @return
	 */
	public static byte[] int2bytesMax(int num) {
		int byteLen = 4;
		int offset = (byteLen-1)*8;
		
		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i<<3)));
		}
		return b;
	}
}
