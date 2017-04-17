package com.catmug.code;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class EventMapActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.event_map);
			WebView  webView;
			webView =  (WebView) findViewById(R.id.webView1);
			webView.getSettings().setAllowFileAccess(true);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setBuiltInZoomControls(true);
			
		
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
			
			String data = "<body>" + "<img src=\"room_map.jpg\"/></body>";
			webView.loadDataWithBaseURL("file:///android_asset/",data , "text/html", "utf-8",null);
		}
		catch (Exception ex) {						
			Log.e("log_tag", "onCreate About: "+ ex.toString());	
		}      
	}
}


