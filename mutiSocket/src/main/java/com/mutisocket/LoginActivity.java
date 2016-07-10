package com.mutisocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {

	EditText uname;
	EditText upwd;
	Button loginbtn;
	EditText addressPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// �������ı���Ľ�����Զ������ļ���
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		uname = (EditText) findViewById(R.id.uname);
		upwd = (EditText) findViewById(R.id.upwd);
		addressPort = (EditText) findViewById(R.id.addressPort);
		loginbtn = (Button) findViewById(R.id.loginbtn);
		loginbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.loginbtn:
			Intent i = new Intent(this, MainActivity.class);
			String s = uname.getText().toString();
			i.putExtra("lname", s);
			i.putExtra("addressPort", addressPort.getText().toString());
			startActivity(i);
			break;
		}
	}

}
