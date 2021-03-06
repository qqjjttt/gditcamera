package com.camera.picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.camera.activity.UploadFileActivity;
import com.camera.head.HeadTool;
import com.camera.net.DataHeadUtil;
import com.camera.util.StringUtil;
import com.camera.vo.Constant;


public class CutFileUtil {
	
	public static final String TAG = "CutFileUtil";
	
	public static final int MSG_EXIST_PIECE = 30;
	
	/**
	 * 标识当前切片是发送给哪个服务器
	 */
	public int whichService = 0;
	
	/** 切片的大小*/
	public int pieceSize = 1000;
	/** 包头信息*/
	private static byte[] packageHead;
	
	/** 文件名*/
	private String filePath;
	/** 切片命名时根据的文件路径*/
	private String realPath;
	
	/** 文件路径*/
	private File file;
	
	/** 当前切片数量*/
	private int pieceNum = 1;
	
	/** 切片总数*/
	private int totalPieceNum = 0;
	
	/** 文件大小,单位为byte*/
	private int fileSize;
	
	/** 当前获取的文件切片坐标*/
	private int nCurrentPiece = 0;
	
	/** 文件切片名集合*/
	private List<String> pieceFiles;
	private List<String> secPieceFiles;
	
	/** 标识是否第一次组装包*/
	private boolean isFirst = true;
	
	
	private Handler handler;
	
	private int progress = 0;
	
	/** 照片描述*/
	private String description;
	
	/** Context对象*/
	private Context context;
	
	private HeadTool headTool;
	
	/** 当有切片存在的情况下，是否使用上面的切片继续发给服务器*/
	public static boolean IS_SEND_LAST_PIECE = true;
	
	public CutFileUtil(Context context, String filePath, String realPath, Handler handler, String description) throws FileNotFoundException {
		IS_SEND_LAST_PIECE = true;
		this.description = description;
		this.realPath = realPath;
		this.handler = handler;
		this.context = context;
		this.filePath = filePath;
		file = new File(filePath);
		if(!file.exists()) {
			throw new FileNotFoundException("File not exist!");
		}
		pieceSize = calculatePieceSize(file);
		//检测图片是否已经有切片
		if(isExistPieces()) {
			handler.sendEmptyMessage(MSG_EXIST_PIECE);
			try {
				Thread.sleep(1000000000);
			} catch (InterruptedException e) {
				if(IS_SEND_LAST_PIECE) {
					return;
				}
				//删除已存在的切片文件
				String pieceName = StringUtil.convertFolderPath(realPath) + "_";
				File folder = new File(Constant.PIECE_FOLDER);
				File[] files = folder.listFiles();
				for(File file : files) {
					if(file.getName().contains(pieceName) && !file.isDirectory()) {
						file.delete();
					}
				}
				whichService = 1;
				pieceFiles = null;
				secPieceFiles = null;
			}
		}
		cutFile();
		whichService = 1;
	}
	
	/**
	 * 查找是否已有切片存在 
	 * @return
	 */
	public boolean isExistPieces() {
		String pieceName = StringUtil.convertFolderPath(realPath) + "_";
		int count = 0;
		int count2 = 0;
		Log.i(TAG, "Conver to piece name :" + pieceName);
		File folder = new File(Constant.PIECE_FOLDER);
		File[] files = folder.listFiles();
		String fileName = null;
		int i, j, min1 = 10000000, min2 = 1000000;
		try {
			for(File file : files) {
				fileName = file.getName();
				int length = fileName.length();
				if(!fileName.contains(pieceName)) {
					continue;
				}
				i = Integer.parseInt(fileName.substring(length - 1));
				int k = fileName.indexOf("_", fileName.length() - 5) + 1;
				j = Integer.parseInt(fileName.substring(k, length - 2));
				Log.e(TAG, "i :" + i + "; j : " + j);
				if(i == 1) {
					if(j <= min1) {
						min1 = j;
					}
					count ++;
				} else if(i == 2) {
					if(j <= min2)
						min2 = j;
					count2 ++;
				}
			}
			Log.i(TAG, "min1 : " + min1 + "; min2:" + min2);
			Log.i(TAG, "count : " + count + "; count2:" + count2);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(count > 0) {
			pieceFiles = new ArrayList<String>();
			totalPieceNum = count;
			for(i = min1; i <= count + min1 - 1; i ++) {
				pieceFiles.add(Constant.PIECE_FOLDER + pieceName + i + "_1");
				Log.i(TAG, "pieceFiles[" + i + "] :" + Constant.PIECE_FOLDER + pieceName + i + "_1");
			}
		}
		if(count2 > 0) {
			secPieceFiles = new ArrayList<String>();
			for(i = min2; i <= count2 + min2 - 1; i ++) {
				secPieceFiles.add(Constant.PIECE_FOLDER + pieceName + i + "_2");
				Log.i(TAG, "pieceFiles[" + i + "] :" + Constant.PIECE_FOLDER + pieceName + i + "_2");
			}
		}
		if(count > 0 || count2 > 0) {
			if(count > 0) {
				this.totalPieceNum = count;
				whichService = 1;
			} else {
				this.totalPieceNum = count2;
				this.pieceFiles = this.secPieceFiles;
				this.secPieceFiles = null;
				whichService = 2;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 文件切片
	 */
	private void cutFile() {
		try {
			FileInputStream in = new FileInputStream(filePath);
			fileSize = in.available();
			//计算切片总数
			int count = fileSize / pieceSize;
			totalPieceNum = (fileSize % pieceSize == 0) ? count : count + 1;
			Log.d(TAG, "total piece count : " + totalPieceNum + "; fileSize = " + fileSize);
			
			byte[] buf = new byte[pieceSize];
			int pieceNum = 1;
			int dataSize = 0;
			pieceFiles = new ArrayList<String>();
			secPieceFiles = new ArrayList<String>(); 
			
			//生成描述包头文件
			headTool = new HeadTool(context, new Date(), description, totalPieceNum);
			packageHead = headTool.getDataDesc();
			String pieceName = Constant.PIECE_FOLDER + StringUtil.convertFolderPath(realPath) + "_" +  1;
			FileOutputStream out1 = new FileOutputStream(pieceName + "_1");
			FileOutputStream out2 = new FileOutputStream(pieceName + "_2");
			pieceFiles.add(pieceName + "_1");
			secPieceFiles.add(pieceName + "_2");
			out1.write(packageHead);
			out1.close();
			out2.write(packageHead);
			out2.close();
			
			while(true) {
				if((dataSize = in.read(buf, 0, pieceSize - 90)) > 0) {
					packagePiece(buf, pieceNum, dataSize);
					pieceNum ++ ;
					continue;
				} 
				break;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 组装切片流
	 * @param buf 文件切片流
	 * @param pieceNum 当前包数
	 * @param dataSize 数据长度
	 * @throws IOException
	 */
	private void packagePiece(byte[] buf, int pieceNum, int dataSize) throws IOException {
		String pieceName = Constant.PIECE_FOLDER + StringUtil.convertFolderPath(realPath) + "_" +  (pieceNum + 1);
		Log.v(TAG, "piece file name : " + pieceName);
		FileOutputStream out1 = new FileOutputStream(pieceName + "_1");
		FileOutputStream out2 = new FileOutputStream(pieceName + "_2");
		pieceFiles.add(pieceName + "_1");
		secPieceFiles.add(pieceName + "_2");
		Log.i(TAG, "totalPieceNum : " + totalPieceNum + "; pieceNum" + pieceNum + "; dataSize " + dataSize);
		try {
			packageHead = headTool.getDataImage(pieceNum, dataSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out1.write(packageHead);
		out1.write(buf, 0, dataSize);
		out1.close();
		out2.write(packageHead);
		out2.write(buf, 0, dataSize);
		out2.close();
		Message msg = new Message();
		msg.obj = (Integer)(pieceNum  * 100 / totalPieceNum);
		msg.what = UploadFileActivity.PROGRESS_DIALOG;
		handler.sendMessage(msg);
	}
	
	/**
	 * 获取切片文件字节流
	 * @param buf 为NULL既可，内部将为buf自动分配内存空间
	 * @return -1表示文件已经读取完,否则返回读取buf的大小
	 * @throws IOException 
	 */
	public int getNextPiece(byte[] buf) {
		//如果文件名不存在，说明已经读完，则返回-1
		if(nCurrentPiece >= pieceFiles.size())
			return -1;
		String fileName = pieceFiles.get(nCurrentPiece);
		
		FileInputStream in;
		int pieceSizeTmp = 0;
		try {
			in = new FileInputStream(fileName);
			pieceSizeTmp = in.available();
			in.read(buf);
		} catch (Exception e) {
//			Toast.makeText(context, "获取切片失败！", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		
		nCurrentPiece ++;
		return pieceSizeTmp;
	}
	
	public void minusCurrentPiece() {
		nCurrentPiece -- ;
	}
	
	/**
	 * 重新获取切片文件字节流
	 * @param buf 为NULL既可，内部将为buf自动分配内存空间
	 * @return -1表示文件已经读取完,否则返回读取buf的大小
	 * @throws IOException 
	 */
	public int getCurrentPiece(byte[] buf, int i) throws IOException {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//如果文件不存在，说明已经读完，则返回-1
		if(fileName == null)
			return -1;
		FileInputStream in = new FileInputStream(fileName);
		int pieceSize = in.available();
		in.read(buf);
		return pieceSize;
	}
	
	/**
	 * 删除当前已读取好的文件
	 * @return
	 */
	public boolean removeCurrentFile() {
		String fileName = pieceFiles.get(nCurrentPiece - 1);
		//如果文件不存在，说明已经读完，则返回-1
		if(fileName == null)
			return false;
		File file = new File(fileName);
		if(file.exists()) {
			Log.v(TAG, "Success to delete piece file, file name is:" + fileName);
			return file.delete();
		}
		Log.e(TAG, "Cant not find the piece file, remove is failure!! file name is:" + fileName);
		return false;
	}
	
//	public void removeAllPieceFile() {
//		for(String filePath : pieceFiles) {
//			File file = new File(filePath);
//			file.delete();
//		}
//	}

	public int getTotalPieceNum() {
		return totalPieceNum;
	}

	public void setTotalPieceNum(int totalPieceNum) {
		this.totalPieceNum = totalPieceNum;
	}
	
	/**
	 * 根据文件大小计算文件切片的大小
	 */
	private int calculatePieceSize2(File file) {
		long fileSize = file.length();
		if(fileSize > 4 * 1024 * 1024) {
			return 63000;
		} else if(fileSize > 2 * 1024 * 1024) {
			return 50000;
		} else if(fileSize > 1024 * 1024) {
			return 40000;
		} else if(fileSize > 1024 * 512) { 
			return 15000;
		} else if(fileSize > 1024 * 100) { 
			return 10000;
		} else if(fileSize > 1024 * 50) {
			return 8000;
		} else if(fileSize > 1024 * 30) {
			return 5000;
		} else if(fileSize > 1024 * 20) {
			return 3000;
		} else if(fileSize > 1024 * 10) {
			return 2000;
		} else if(fileSize > 1024 * 5) {
			return 1000;
		}
		return 1000;
	}
	
	private int calculatePieceSize(File file) {
		return 1000;
	}
	
	/**
	 * 开始发送切片到下一个服务器
	 */
	public boolean changeNext() {
		this.nCurrentPiece = 0;
		if(secPieceFiles != null && secPieceFiles.size() > 0) {
			this.totalPieceNum = secPieceFiles.size();
			this.pieceFiles = this.secPieceFiles;
			whichService = 2;
			return true;
		}
		return false;
	}
	
}
