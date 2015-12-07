package com.example.lock;

import android.app.Application;
//存储页面的值全局对象使用
public class judgeflag  extends Application{
      
	private int flag ;               //1  没有密码，重新申请密码，2  解锁密码
	private String password;         //存储密码
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password2) {
		// TODO Auto-generated method stub
		this.password = password2;
	}
	 
	 
}
