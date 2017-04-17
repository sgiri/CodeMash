package com.catmug.code;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class AboutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.about);
		}
		catch (Exception ex) {						
			Log.e("log_tag", "onCreate About: "+ ex.toString());	
		}               
	}
}
