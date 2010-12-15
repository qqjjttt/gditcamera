package com.camera.head;


public class CodeUtil {

	/**
	 * ��byte����ת��������
	 * @param b
	 * @return
	 */
	public static long bytes2int(byte[] b) {
		int byteLen = (b.length >= 4 ? 4 : 2);
		long mask = 0xff;
		long temp = 0;
		long res = 0;

		for (int i = 0; i < byteLen; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	/**
	 * ����(max=65535(Ĭ��))ת���ɳ���Ϊ2(Ĭ��)��byte����
	 * @param num
	 * @return
	 */
	public static byte[] int2bytes(int num) {
		int byteLen = 2;// ������ݳ���ֵΪ4�������ת�����������ֵInteger.MAX_VALUE
		int offset = (byteLen - 1) * 8;

		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i << 3)));
		}
		return b;
	}
	
	/**
	 * ����ת���ɳ���Ϊ4��byte����
	 * @param num
	 * @return
	 */
	public static byte[] int2bytesMax(long num) {
		int byteLen = 4;
		int offset = (byteLen - 1) * 8;

		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i << 3)));
		}
		return b;
	}
	
	/**
	 * byte����ת�����ַ���
	 * @param bytes byte����
	 * @return �ַ���
	 */
	public static String bytes2String(byte[] bytes) {
		String str = "";
		int n = bytes.length;
		for(int i = 0; i < n; i ++) {
			str += (char)(bytes[i]);
		}
		return str;
	}
	
}
