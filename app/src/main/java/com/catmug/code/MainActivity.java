package com.catmug.code;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

	private ProgressDialog pDialog;
	TextView btn1;
	TextView btn2;
	TextView btn3;	
	TextView btn4;	
	TextView btn5;
	TextView btn6;
	TextView btn7;
	TextView btn8;

	public static ArrayList<HashMap<String, String>> FavoriteList = new ArrayList<HashMap<String, String>>();		
	public static ArrayList<HashMap<String, String>> SessionArrayList = new ArrayList<HashMap<String, String>>();
	public static JSONArray SessionsList = new JSONArray();
	public static JSONArray SpeakersList = new JSONArray();
	public static List<String>  mCheckedTitles = new ArrayList<String>();
	public static String FavFileName = "favoritesessions.txt";
	public static ListView DateListView;
	public static ListView SpeakerListView;
	public static ListView TechnologyListView;
	public static ListView TitleListView;
	public static ListView FavoritesListView;
	public static ListView FavoritesTabListView;
	public static CustomSimpleArrayAdapter DateListadapter;
	public static CustomSimpleArrayAdapter SpeakerListadapter;
	public static CustomSimpleArrayAdapter TechnologyListadapter;
	public static CustomSimpleArrayAdapter TitleListadapter;	
	public static CustomSimpleArrayAdapter FavoritesListadapter;
	public static CustomSimpleArrayAdapter Favoritesadapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		AddToFavorites();

		btn1 = (TextView) findViewById(R.id.btnSessions);
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SessionProperties.SessionType = "Session";
				getSessionView();
			}
		}); 

		btn2 = (TextView) findViewById(R.id.btnPrecompilerSessions);		
		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SessionProperties.SessionType = "Pre-Compiler";
				getSessionView();
			}
		});		

		btn3 = (TextView) findViewById(R.id.btnSpeakers);		
		btn3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent a = new Intent();
				a.setClass(MainActivity.this, com.catmug.code.SpeakerListActivity.class);
				startActivity(a);
			}
		});

		btn4 = (TextView) findViewById(R.id.btnFavorites);		
		btn4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				Intent a = new Intent();
				a.setClass(MainActivity.this, com.catmug.code.FavoriteSessionsActivity.class);
				startActivity(a);
			}
		});		

		btn5 = (TextView) findViewById(R.id.btnRefreshAll);
		btn5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new RefreshData().execute();
			}
		}); 

		btn6 = (TextView) findViewById(R.id.btnTwitterFeed);
		btn6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				String url = "https://twitter.com/codemash";
				Intent a = new Intent(Intent.ACTION_VIEW);
				a.setData(Uri.parse(url));
				startActivity(a);
			}
		}); 

		btn7 = (TextView) findViewById(R.id.btnEventMap);
		btn7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent a = new Intent();
				a.setClass(MainActivity.this, com.catmug.code.EventMapActivity.class);
				startActivity(a);
			}
		}); 


		btn8 = (TextView) findViewById(R.id.btnAbout);		
		btn8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				Intent a = new Intent();
				a.setClass(MainActivity.this, com.catmug.code.AboutActivity.class);
				startActivity(a);
			}
		});			
	}


	private class RefreshData extends AsyncTask<String, Integer, JSONArray> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Refresh Sessions ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected JSONArray doInBackground(String... params) {
			@SuppressWarnings("unused")
			boolean deleted = false;
			JSONArray ja = new JSONArray();
			try{
				File dir = getFilesDir();
				File file = new File(dir, "sessions.txt");
				deleted = file.delete();

				File file2 = new File(dir, "speakers.txt");
				deleted = file2.delete();	

				CodeMashApp app = (CodeMashApp) getApplication();
				JSONArray sessionsList = app.ReadFromURL("https://speakers.codemash.org/api/SessionsData?type=json");
				if(sessionsList != null){
					MainActivity.SessionsList = sessionsList;
					app.WriteFileFromJSONArray("sessions.txt",sessionsList);					
				}				
				JSONArray speakersList = app.ReadFromURL("https://speakers.codemash.org/api/SpeakersData?type=json");
				if(speakersList != null){
					MainActivity.SpeakersList = speakersList;
					app.WriteFileFromJSONArray("speakers.txt",speakersList);					
				}
			}
			catch(Exception e){
				Log.e("log_tag", "RefreshData: "+ e.toString());	
			}
			return ja;
		}

		protected void onPostExecute(JSONArray session) {
			pDialog.dismiss();
		} 
	}

	public void getSessionView(){
		Intent a = new Intent();
		a.setClass(MainActivity.this, com.catmug.code.SessionTabLayoutActivity.class);
		startActivity(a);
	}

	//ReadFromFavorites
	public void AddToFavorites(){
		try{
			CodeMashApp app = (CodeMashApp) getApplication();
			JSONArray  ja = app.ReadFileIntoJSONArray( FavFileName);
			GetSessions sessionlisting = new GetSessions();
			FavoriteList = sessionlisting.getFavoriteList(ja);	     
		}
		catch (Exception ex){ 
			Log.e("log_tag", "AddToFavorites: "+ ex.toString());	
		}				
	}

	public  void UpdateCheckedTitles(String title, boolean add ){
		try
		{
			if(add){
				mCheckedTitles.add(title);			
			}
			else{
				mCheckedTitles.remove(title);
			}

			if(DateListadapter != null){
				DateListadapter.notifyDataSetChanged();
			}

			if(Favoritesadapter != null){
				Favoritesadapter.notifyDataSetChanged();		
			}

			if( FavoritesListadapter != null){
				FavoritesListadapter.notifyDataSetChanged();
			}

			if(SpeakerListadapter != null){
				SpeakerListadapter.notifyDataSetChanged();
			}

			if(TitleListadapter != null){	
				TitleListadapter.notifyDataSetChanged();
			}
			if(TechnologyListadapter != null){
				TechnologyListadapter.notifyDataSetChanged();			
			}
		}
		catch (Exception ex){ 
			Log.e("log_tag", "UpdateCheckedTitles: "+ ex.toString());	
		}	
	}

}
