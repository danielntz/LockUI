package com.example.lock;

import com.example.lock.LockPatternView.OnpatternchangeListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity implements OnpatternchangeListener{
        
	   protected static final String TAG = null;
	   private    TextView     xianshi;
	   private    LockPatternView    hhh ;
	   private    judgeflag    judge;
	   private    String      chuandimima;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		judge = (judgeflag)getApplication();
		
		hhh = (LockPatternView)findViewById(R.id.huatu);    //初始化画布
		chuandimima = getIntent().getStringExtra("judge");  
		xianshi = (TextView)findViewById(R.id.name);
		hhh.setOnpatternLListener(this);      //给图案锁设置监听器
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onpatterchanged(String passwordstr) {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(passwordstr)){
			 // xianshi.setText(passwordstr);
			// judge.password = passwordstr;		     //设置密码,把密码写到sharedpreference
			 if(chuandimima.equals("1")){
		         SharedPreferences  myshare = getSharedPreferences("sp", 0);
		         SharedPreferences.Editor edit = myshare.edit();
		         edit.putString("mima", passwordstr);     //写入到sharedPreference
			     edit.commit();
		     //  Log.i(TAG, passwordstr);
			     xianshi.setText("密码设置成功");
			    
			 }
			 else{
				 if(passwordstr.equals(judge.getPassword()))
				 { 
				   //xianshi.setText("解锁成功");
				   Intent  intent  = new Intent(this,zhujiemian.class);
				   startActivity(intent);
				   finish();
				 }
				 else
				   xianshi.setText("手势错误");
				
			 }
		}
		else{
			xianshi.setText("至少五个密码");
		}
	}

	@Override
	public void onpatterstart(boolean isstart) {
		// TODO Auto-generated method stub
		    if(isstart){
		    	xianshi.setText("请输入图案锁");
		    }
	}
}
