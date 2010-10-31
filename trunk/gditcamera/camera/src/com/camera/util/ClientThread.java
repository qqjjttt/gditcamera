package com.camera.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
/**
 * �������пͻ����̣߳�����˵���
 * @author tian
 *
 */
public class ClientThread implements Runnable {

	private static final String tag = "ClientThread";
	
	private Socket socket;
	private BufferedReader in;
	private String msg = "";
	// ��ſͻ���socket
	private List<Socket> clientList = new ArrayList<Socket>();
	
	public ClientThread(Socket s){
		this.socket = s;
		this.clientList.add(socket);
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			msg = "��ӭ��¼�����IP��: "+s.getInetAddress();
			int count = 0;
			for(;;){
				count++;
				Thread.sleep(1000);
				msg = count+"��";
				sendMsg();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				if((msg=in.readLine()).equals("exit")){
					clientList.remove(this.socket);
					in.close();
					msg = "IPΪ: "+this.socket.getInetAddress()+"���û����˳�";
					socket.close();
					sendMsg();
				}else{
					msg = this.socket.getInetAddress()+":"+msg;
					sendMsg();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendMsg() throws IOException{
		PrintWriter pout;
		for(Socket cs : clientList){
			pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cs.getOutputStream())),true);
			Log.d(tag, "send: "+msg);
			pout.println(this.msg);
			this.msg = "";
		}
	}

}
