package com.camera.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SelectFolderActivity extends Activity implements OnItemClickListener,Button.OnClickListener{
	
	private ListView mFolderListView;
	private Button mChoose;
	private Button mCancel;
	private String reFolderName;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefolder);
        //ȡ�ý���Ŀؼ�
        mFolderListView=(ListView) this.findViewById(R.id.lvFolder);
        mChoose=(Button) this.findViewById(R.id.btnChoose);
        mCancel=(Button) this.findViewById(R.id.btnCancel);

    }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//�������ļ����¼�
		
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
