package com.example.lock;

import com.example.lock.LockPatternView.OnpatternchangeListener;

import android.app.Activity;
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
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hhh = (LockPatternView)findViewById(R.id.huatu);    //初始化画布
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
			  xianshi.setText(passwordstr);
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
