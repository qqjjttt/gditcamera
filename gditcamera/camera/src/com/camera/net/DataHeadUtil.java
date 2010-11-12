package com.camera.net;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.camera.util.StringUtil;
import com.camera.vo.Data;
import com.camera.vo.DataHead;

/**
 * ������ͷ��Ϣת����byte����Ĺ���
 * @author tian
 *
 */
public class DataHeadUtil { 
	
	/**
	 * ��ӡ���飬������
	 * @param b
	 */
	public static void print(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i]);
			if (i < b.length - 1)
				System.out.print(",");
			else {
				System.out.println(" => " + bytes2int(b));
			}
		}
	}

	/**
	 * ����Ϊ2(Ĭ��)��byteת��������
	 * @param b
	 * @return
	 */
	public static int bytes2int(byte[] b) {
		int byteLen = (b.length>=4 ? 4 : 2);
		int mask = 0xff;//1111 1111
		int temp = 0;
		int res = 0;
		
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
		int byteLen = 2;//������ݳ���ֵΪ4�������ת�����������ֵInteger.MAX_VALUE
		int offset = (byteLen-1)*8;
		
		byte[] b = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			b[i] = (byte) (num >>> (offset - (i<<3)));
		}
		return b;
	}
	
	/**
	 * �����ݰ�ͷʵ����ת����byte����
	 * @param dataHead
	 * @return
	 * @throws Exception
	 */
	public static byte[] dataHead2Byte(DataHead dataHead)throws Exception{
		byte [] result = new byte[138];
		int offset1 = 0;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String [] arr = dateFormat.format(dataHead.getDataTime()).split("-");
		int year1 = Integer.parseInt(arr[0].substring(0,2));
		int year2 = Integer.parseInt(arr[0].substring(2,4));
		int month = Integer.parseInt(arr[1]);
		int day = Integer.parseInt(arr[2]);
		int hour = Integer.parseInt(arr[3]);
		int minute = Integer.parseInt(arr[4]);
		int second = Integer.parseInt(arr[5]);
		
		byte[] tem;

		/**$PHO(4byte)*/
		byte [] phoByte = dataHead.getPho();
		for(int i=0;i<4;i++){
			result[offset1++] = phoByte[i];
		}
		/**�־�����(16byte)*/
		String subStation = dataHead.getSubStation();
		byte [] subStationByte = StringUtil.getByteArrayByLength(subStation, "GB2312", 16);
		for(int i=0;i<16;i++){
			result[offset1++] = subStationByte[i];
		}
		/**��վ����(16byte)*/
		String surveyStation = dataHead.getSurveyStation();
		byte [] surveyStationByte = StringUtil.getByteArrayByLength(surveyStation, "GB2312", 16);
		for(int i=0;i<16;i++){
			result[offset1++] = surveyStationByte[i];
		}
		/**��Ƭ����,Unicode(32char=64byte)*/
		String phoDesc = dataHead.getPhoDesc();
//		Log.e("phoDesc", phoDesc.getBytes("GB2312").length+"");
		byte [] phoDescByte = StringUtil.getByteArrayByLength(phoDesc, "GB2312", 64);
		for(int i=0;i<64;i++){
			result[offset1++] = phoDescByte[i];
		}
		/**վ��(8byte)*/
		String stationCode = dataHead.getStationCode();
		byte [] stationCodeByte =StringUtil.getByteArrayByLength(stationCode, "GB2312", 8);
		for(int i=0;i<8;i++){
			result[offset1++] = stationCodeByte[i];
		}
		/**����(16byte)*/
		String command = dataHead.getCommand();
		byte [] commandByte =StringUtil.getByteArrayByLength(command, "GB2312",16);
		for(int i=0;i<16;i++){
			result[offset1++] = commandByte[i];
		}

		tem = int2bytes(year1);
		result[offset1++] = tem[1];
		
		tem = int2bytes(year2);
		result[offset1++] = tem[1];
		
		tem = int2bytes(month);
		result[offset1++] = tem[1];
		
		tem = int2bytes(day);
		result[offset1++] = tem[1];
		
		tem = int2bytes(hour);
		result[offset1++] = tem[1];
		
		tem = int2bytes(minute);
		result[offset1++] = tem[1];
		
		tem = int2bytes(second);
		result[offset1++] = tem[1];

		result[offset1++] = (byte)dataHead.getCameraId();

		tem = int2bytes(dataHead.getCurrentPackage());
		result[offset1++] = tem[0];
		result[offset1++] = tem[1];

		tem = int2bytes(dataHead.getTotalPackage());
		result[offset1++] = tem[0];
		result[offset1++] = tem[1];

		tem = int2bytes(dataHead.getDataLength());
		result[offset1++] = tem[0];
		result[offset1++] = tem[1];

		for(int i=0;i<result.length;i++){
			Log.e("result["+i+"]=",result[i]+"");
		}
		
		return result;
	}
	
	/**
	 * ��byte����ת�������ݰ�ͷʵ����DataHead
	 */
	public static DataHead byte2DataHead(byte [] b){
		DataHead d = new DataHead();
		int offset = 0;
		try {
			Log.e("##################PHO=","");
			print(subByteArray(b,offset,4));
			d.setPho(subByteArray(b,offset,4));
			offset+=4;
			d.setSubStation(new String(subByteArray(b, offset, 16)));
			Log.d(offset+"dataHead:subStaticon", d.getSubStation());
			offset+=16;
			String s = new String(subByteArray(b, offset, 16));
			d.setSurveyStation(s);
			Log.d(offset+"dataHead:SurveyStation", d.getSurveyStation());
			offset+=16;
			d.setPhoDesc(new String(subByteArray(b, offset, 64),"GB2312"));
			Log.d(offset+"dataHead:Desc", d.getPhoDesc());
			offset+=64;
			d.setStationCode(new String(subByteArray(b, offset,8)));
			Log.d(offset+"dataHead:StationCode", d.getStationCode());
			offset+=8;
			d.setCommand(new String(subByteArray(b, offset,16)));
			Log.d(offset+"dataHead:Command", d.getCommand());
			offset+=16;
			String dataString = "";
//			print(subByteArray(b, offset, 7));
			for(int i=0;i<7;i++){
				if(i==0)
					dataString+=(b[offset++]);
				else
					dataString+=(b[offset++]+"#");
			}
			d.setDataTime(dataTimeString2Date(dataString));
			Log.d(offset+"dataHead:Date", new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(d.getDataTime()));
			d.setCameraId(b[offset]);
			Log.d(offset+"dataHead:CameraId", d.getCameraId()+"");
			offset+=1;
			d.setCurrentPackage(bytes2int(subByteArray(b, offset, 2)));
			Log.d(offset+"dataHead:CurrentPackage", d.getCurrentPackage()+"");
			offset+=2;
			d.setTotalPackage(bytes2int(subByteArray(b, offset, 2)));
			Log.d(offset+"dataHead:TotalPackage", d.getTotalPackage()+"");
			offset+=2;
			d.setDataLength(bytes2int(subByteArray(b, offset, 2)));
			Log.d(offset+"dataHead:dataLength", d.getDataLength()+"");

			return d;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte [] subByteArray(byte []b,int start,int length){
		if(length>b.length||start<0||(start+length)>b.length)
			return null;
		byte [] result = new byte[length];
		for(int i=0;i<length;i++){
			result[i] = b[start+i];
		}
		return result;
	}
	
	/**
	 * ʱ���ַ���(20 10 11 03 20 37 05)ת�����ڶ���
	 * @param dataTimeString
	 * @return
	 */
	public static Date dataTimeString2Date(String dataTimeString){
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy#MM#dd#HH#mm#ss#");
		Date d;
		try {
			d = dataFormat.parse(dataTimeString);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ������
	 * @param args
	 * @throws Exception
	 */
//	public static void main(String args[])throws Exception{
//		DataHead data = new DataHead();
//		data.setPho("apho");
//		data.setSubStation("1234567890123456");
//		data.setSurveyStation("6543210987654321");
//		data.setPhoDesc("��ʮ�����ַ�����ʮ���ֽڡ���ʮ�����ַ�����ʮ���ֽڣ�һ����������");
//		data.setStationCode("12345678");
//		data.setDataTime(new Date());
//		data.setCameraId((byte)1);
//		data.setCurrentPackage(22);
//		data.setTotalPackage(2049);
//		data.setDataLength(10000);
//		
//		print(DataHeadUtil.dataHead2Byte(data));
//	}
	
//	static void print(byte [] b){
//		for(int i=0;i<b.length;i++){
//			System.out.println("b["+i+"]="+b[i]+" ");
//		}
//	}
}
