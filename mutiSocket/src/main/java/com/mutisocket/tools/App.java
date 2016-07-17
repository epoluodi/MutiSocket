package com.mutisocket.tools;

import android.app.Application;

public class App extends Application {
	public static App app;

	public static String ServerUrl="http://services.kaiqiaole.com/com.kaiqiaole.service/";
	@Override
	public void onCreate() {
		app = this;
		super.onCreate();
	}
}
