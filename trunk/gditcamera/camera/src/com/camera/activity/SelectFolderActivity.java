package com.camera.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.camera.util.FileUtil;
import com.camera.vo.FileItem;

public class SelectFolderActivity extends Activity implements OnItemClickListener,Button.OnClickListener{
	
	private ListView mFolderListView;
	private Button mChoose;
	private Button mCancel;
	private String reFolderName;
	private List<FileItem> mFolders;
	private String mSdcardPath;
	private File mDefaultFile;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefolder);
        
        //ȡ�ý���Ŀؼ�
        mFolderListView=(ListView) this.findViewById(R.id.lvFolder);
        mChoose=(Button) this.findViewById(R.id.btnChoose);
        mCancel=(Button) this.findViewById(R.id.btnCancel);
        mDefaultFile=new File(mSdcardPath);
        mFolders=new ArrayList<FileItem>();

    }
	
	private void fileListView(File file){
		
		
		Collections.sort(this.mFolders);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		String selectedFileName = this.mFolders.get(position).getFileName();
		//�������ļ����¼�
		if(selectedFileName.equals("�����ϼ�")){
			
		}else{
			
		}
	}

	@Override
	public void onClick(View v) {
		if(v==mChoose){
			//�����ļ���
			
		}else{
			this.finish();
		}
		
	}
}
