package com.catmug.code;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.catmug.code.SessionProperties.mSortOrder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionListActivity extends ListActivity {

	private ProgressDialog pDialog;

	int list_format;	
	int sessionorder;
	mSortOrder sortkey;
	public JSONArray sessions_public;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {		
			sessionorder = extras.getInt("Order");
		}		

		switch (sessionorder) {
		case 1:
			list_format = R.layout.session_list;
			sortkey = SessionProperties.mSortOrder.TITLE_NAME;
			break;
		case 2:
			list_format = R.layout.session_list_bydate;
			sortkey = SessionProperties.mSortOrder.SESSION_DATE;
			break;
		case 3:
			list_format = R.layout.session_list_byspeaker;
			sortkey = SessionProperties.mSortOrder.SPEAKER_NAME;
			break;
		case 4: 
			list_format = R.layout.session_list_bytechnology;
			sortkey = SessionProperties.mSortOrder.TECHNOLOGY_NAME;
			break;			
		case 5: 
			list_format = R.layout.favorite_list;
			sortkey = SessionProperties.mSortOrder.FAVORITES;
			break;										
		default:
			list_format = R.layout.session_list;
			sortkey = SessionProperties.mSortOrder.TITLE_NAME;
			break;	
		}			

		try {
			new SessionFetch().execute();
		}
		catch (Exception e) {	
			Log.e("log_tag", "SessionListActivity_onCreate: "+e.toString());			
		}
	}

	private class SessionFetch extends AsyncTask<String, Integer, JSONArray> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SessionListActivity.this);
			pDialog.setMessage("Loading Sessions ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
//changed for 2017
		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray sessionsList = new JSONArray();											
			try{
				CodeMashApp app = (CodeMashApp) getApplication();
				sessionsList = app.ReadFileIntoJSONArray("sessions.txt");
				if(sessionsList == null)
				{
					sessionsList = app.ReadFromURL("https://speakers.codemash.org/api/SessionsData?type=json");
					if(sessionsList != null){
						app.WriteFileFromJSONArray("sessions.txt",sessionsList);
					}
				}
			}
			catch (Exception e){ 
				Log.e("log_tag", "SessionListActivity_SessionFetch: "+ e.toString());	
			}								  			  				   						
			return sessionsList;		
		}

		protected void onPostExecute(JSONArray sessions) {
			sessions_public = sessions;
			pDialog.dismiss();
			try {									
				runOnUiThread(new Runnable() {
					public void run() {
						// Hashmap for ListView				
						ArrayList<HashMap<String, String>> sessionList = new ArrayList<HashMap<String, String>>();
						int layoutResourceId = R.layout.session_list;
						if( sortkey == SessionProperties.mSortOrder.FAVORITES){					
							layoutResourceId = R.layout.favorite_list;					
							MainActivity.FavoritesListadapter = new CustomSimpleArrayAdapter(getApplicationContext(),layoutResourceId,MainActivity.FavoriteList);
							MainActivity.FavoritesTabListView = getListView();			
							MainActivity.FavoritesTabListView.setAdapter(MainActivity.FavoritesListadapter);
							MainActivity.FavoritesTabListView.setOnItemClickListener((OnItemClickListener) MainActivity.FavoritesListadapter);
						}
						else{
							GetSessions sessionlisting = new GetSessions();
							sessionList = sessionlisting.getSessionsList(sessions_public, SessionProperties.SessionType, sortkey, sessionorder);			
							MainActivity.SessionArrayList = sessionList;
							if( sortkey == SessionProperties.mSortOrder.SESSION_DATE){
								layoutResourceId = R.layout.session_list_bydate;
								MainActivity.DateListadapter = new CustomSimpleArrayAdapter(getApplicationContext(),layoutResourceId,sessionList);
								MainActivity.DateListView = getListView();
								MainActivity.DateListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
								MainActivity.DateListView.setAdapter(MainActivity.DateListadapter);
								MainActivity.DateListView.setOnItemClickListener((OnItemClickListener) MainActivity.DateListadapter);				
							}
							else if( sortkey == SessionProperties.mSortOrder.SPEAKER_NAME){
								layoutResourceId = R.layout.session_list_byspeaker;
								MainActivity.SpeakerListadapter = new CustomSimpleArrayAdapter(getApplicationContext(),layoutResourceId,sessionList);
								MainActivity.SpeakerListView = getListView();
								MainActivity.SpeakerListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
								MainActivity.SpeakerListView.setAdapter(MainActivity.SpeakerListadapter);
								MainActivity.SpeakerListView.setOnItemClickListener((OnItemClickListener) MainActivity.SpeakerListadapter);
							}
							else if( sortkey == SessionProperties.mSortOrder.TECHNOLOGY_NAME){
								layoutResourceId = R.layout.session_list_bytechnology;
								MainActivity.TechnologyListadapter = new CustomSimpleArrayAdapter(getApplicationContext(),layoutResourceId,sessionList);
								MainActivity.TechnologyListView = getListView();
								MainActivity.TechnologyListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
								MainActivity.TechnologyListView.setAdapter(MainActivity.TechnologyListadapter);
								MainActivity.TechnologyListView.setOnItemClickListener((OnItemClickListener)MainActivity.TechnologyListadapter);				
							}				
							else if( sortkey == SessionProperties.mSortOrder.TITLE_NAME){
								layoutResourceId = R.layout.session_list;
								MainActivity.TitleListadapter = new CustomSimpleArrayAdapter(getApplicationContext(),layoutResourceId,sessionList);
								MainActivity.TitleListView = getListView();
								MainActivity.TitleListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
								MainActivity.TitleListView.setAdapter(MainActivity.TitleListadapter);
								MainActivity.TitleListView.setOnItemClickListener((OnItemClickListener) MainActivity.TitleListadapter);				
							}
						}
					}
				});
			}
			catch (Exception ex) {						
				Log.e("log_tag", "SessionListActivity_onPostExecute: "+ ex.toString());	
			}		
		} 
	}
}