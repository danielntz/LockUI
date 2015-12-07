package com.example.lock;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

public class WelcomeActivity  extends Activity{
    /**
     * 欢迎界面
     */
	  private  judgeflag  collect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//欢迎界面延迟启动
		new  Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 //检测先前有没有设置过密码，用sharePreerenced
				  SharedPreferences sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
			       String password = sp.getString("mima", "");    //从sharepreference中找keypassword所对应的值
			       
			       if(TextUtils.isEmpty(password)){
			    	   //设定密码
			    	   Intent  intent  = new Intent(WelcomeActivity.this,MainActivity.class);
			    	   intent.putExtra("judge", "1");   //重新设定密码
			    	   startActivity(intent);
			           finish();
			       }
			       //设置过密码，则是密码解锁
			       else{
			    	   collect = (judgeflag)getApplication(); 
			    	   collect.setPassword(password); 
			    	   startActivity(new Intent(WelcomeActivity.this,MainActivity.class).putExtra("judge", "2"));  //解锁密码
			           finish();
			       }
			}
		},1000);
		
		
	}
    
	  
}
