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
     * ��ӭ����
     */
	  private  judgeflag  collect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//��ӭ�����ӳ�����
		new  Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 //�����ǰ��û�����ù����룬��sharePreerenced
				  SharedPreferences sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
			       String password = sp.getString("mima", "");    //��sharepreference����keypassword����Ӧ��ֵ
			       
			       if(TextUtils.isEmpty(password)){
			    	   //�趨����
			    	   Intent  intent  = new Intent(WelcomeActivity.this,MainActivity.class);
			    	   intent.putExtra("judge", "1");   //�����趨����
			    	   startActivity(intent);
			           finish();
			       }
			       //���ù����룬�����������
			       else{
			    	   collect = (judgeflag)getApplication(); 
			    	   collect.setPassword(password); 
			    	   startActivity(new Intent(WelcomeActivity.this,MainActivity.class).putExtra("judge", "2"));  //��������
			           finish();
			       }
			}
		},1000);
		
		
	}
    
	  
}
