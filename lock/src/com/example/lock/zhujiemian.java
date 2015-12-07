package com.example.lock;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class zhujiemian  extends Activity implements android.view.View.OnClickListener{
     
	  private Button  change;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.zhujiemian_activity);
	    change = (Button)findViewById(R.id.changepassword);
	    change.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.changepassword:
			 SharedPreferences share = getSharedPreferences("sp", 0);
			 SharedPreferences.Editor edit = share.edit();
			 edit.remove("mima");
			 edit.commit();
			 break;

		default:
			break;
		}
	}
       
	
	 
}
