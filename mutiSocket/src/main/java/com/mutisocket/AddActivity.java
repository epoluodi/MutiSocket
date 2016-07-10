package com.mutisocket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mutisocket.tools.Toast;

public class AddActivity extends Activity implements OnClickListener {

	ImageView imgbtn;
	Context mContext;
	Button addDevSet;
	EditText devip;
	EditText devport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		mContext = AddActivity.this;

		imgbtn = (ImageView) findViewById(R.id.undo);
		imgbtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				AddActivity.this.finish();
				return false;
			}
		});

		addDevSet = (Button) findViewById(R.id.addDevSet);
		addDevSet.setOnClickListener(this);
		devip = (EditText) findViewById(R.id.devip);
		devport = (EditText) findViewById(R.id.devport);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addDevSet:
			String dip = devip.getText().toString();
			String dport = devport.getText().toString();
			if (!dip.equals("") || !dport.equals("")) {
				Intent i = new Intent(mContext, AddActivity.class);
				i.putExtra("devip", dip);
				i.putExtra("devport", dport);
				setResult(100, i);
				finish();
			} else {
				Toast.show(mContext, "ip/port", 2000);
			}
			break;
		}
	}
}
