package com.example.lock;

import android.app.Application;
//�洢ҳ���ֵȫ�ֶ���ʹ��
public class judgeflag  extends Application{
      
	private int flag ;               //1  û�����룬�����������룬2  ��������
	private String password;         //�洢����
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password2) {
		// TODO Auto-generated method stub
		this.password = password2;
	}
	 
	 
}
