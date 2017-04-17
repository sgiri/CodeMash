package com.catmug.code;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SpeakerListActivity extends ListActivity {

	// contacts JSONArray
	JSONArray speakers = null;

	// Search EditText
	EditText inputSearch;

	ListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);				

		try {			
			new SpeakerFetch().execute();	 
			ListView lv = getListView();

			// Launching new screen on Selecting Single ListItem
			lv.setOnItemClickListener(new OnItemClickListener() { 
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// getting values from selected ListItem
					String speakeruri= ((TextView) view.findViewById(R.id.SpeakerURI)).getText().toString();
					String speaker = ((TextView) view.findViewById(R.id.Name)).getText().toString();
					String biography = ((TextView) view.findViewById(R.id.Biography)).getText().toString();
					String twitterhandle = ((TextView) view.findViewById(R.id.TwitterHandle)).getText().toString();
					String blogurl = ((TextView) view.findViewById(R.id.BlogURL)).getText().toString();
					String sessions = ((TextView) view.findViewById(R.id.Sessions)).getText().toString();                      

					// Starting new intent
					Intent in = new Intent(getApplicationContext(), SpeakerListItemActivity.class);

					in.putExtra(SpeakerProperties.TAG_NAME , speaker);			
					in.putExtra(SpeakerProperties.TAG_BIOGRAPHY , biography);
					in.putExtra(SpeakerProperties.TAG_TWITTERHANDLE , twitterhandle);
					in.putExtra(SpeakerProperties.TAG_SPEAKERURI , speakeruri);
					in.putExtra(SpeakerProperties.TAG_BLOGURL , blogurl);			
					in.putExtra(SpeakerProperties.TAG_SESSIONS , sessions);	

					startActivity(in);
				}			
			});
		}
		catch (Exception e) 
		{e.printStackTrace();}
	}
	private class SpeakerFetch extends AsyncTask<String, Integer, JSONArray> {
		@Override
		protected JSONArray doInBackground(String... params) {

			JSONArray speakersList = new JSONArray();
			FileInputStream fIn = null;
			try{
				fIn = openFileInput("speakers.txt");
				BufferedReader buf = new BufferedReader(new InputStreamReader(fIn));
				String readString = buf.readLine();
				fIn.close();
				try{
					speakersList = new JSONArray(readString);
				}
				catch (Exception ex){
					ex.printStackTrace();}
			}
			catch (Exception e) {
				try{
					SessionService service = new SessionService(getApplicationContext());
					speakersList= service.fetchData(SpeakerProperties.speaker_url);
					try {

						final String TESTSTRING  = speakersList.toString();
						FileOutputStream fOut = openFileOutput("speakers.txt", MODE_WORLD_READABLE);
						OutputStreamWriter osw = new OutputStreamWriter(fOut);
						osw.write(TESTSTRING);
						osw.flush();
						osw.close();
					}
					catch (IOException ioe){
						ioe.printStackTrace();}
				}
				catch (Exception ex) {
					Log.e("log_tag", "ListView data: "+ ex.toString());
				}
			}
			return speakersList;

		}

		@Override
		protected void onPostExecute(JSONArray speakers) {
			// Hashmap for ListView				
			ArrayList<HashMap<String, String>> speakerList = new ArrayList<HashMap<String, String>>();
			int speakers_count = speakers.length();			
			try {		
				// looping through All Sessions
				for(int i = 0; i < speakers_count; i++){				
					JSONObject c = speakers.getJSONObject(i);

					String spkr = "";
					String lname = c.optString(SpeakerProperties.TAG_NAME, "n/a");
					String fname = c.optString(SpeakerProperties.TAG_FIRSTNAME, "n/a");
					String fname2 = c.getString(SpeakerProperties.TAG_FIRSTNAME);
					if(fname2.equals("null"))
					{
						continue;
					}
					spkr =  lname + ", " + fname;

				//	String name = c.optString(SpeakerProperties.TAG_NAME, "n/a");
					String biography = c.optString(SpeakerProperties.TAG_BIOGRAPHY);
					String twitterHandle = c.optString(SpeakerProperties.TAG_TWITTERHANDLE);
					String speakeruri = c.optString(SpeakerProperties.TAG_SPEAKERURI, "n/a");					
					String blogurl = c.optString(SpeakerProperties.TAG_BLOGURL);
					if(blogurl.equals("null"))
					{
						blogurl = "";
					}
					String sessions = c.optString(SpeakerProperties.TAG_SESSIONS, "n/a");
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value				
					map.put(SpeakerProperties.TAG_NAME, spkr);
					map.put(SpeakerProperties.TAG_BIOGRAPHY, biography);
					map.put(SpeakerProperties.TAG_TWITTERHANDLE, twitterHandle);			
					map.put(SpeakerProperties.TAG_SPEAKERURI, speakeruri);				
					map.put(SpeakerProperties.TAG_BLOGURL, blogurl);
					map.put(SpeakerProperties.TAG_SESSIONS, sessions);								

					Collections.sort(speakerList, new MapComparator(SpeakerProperties.TAG_NAME));

					speakerList.add(map);
				}	
			} catch (JSONException e) {	
				Log.e("log_tag", "Error looping data "+e.toString());			
			} 	

			/**
			 * Updating parsed JSON data into ListView
			 * */
			adapter = new SimpleAdapter(getApplicationContext(), speakerList,
					R.layout.speaker_list,
					new String[] { SpeakerProperties.TAG_NAME, SpeakerProperties.TAG_BIOGRAPHY, SpeakerProperties.TAG_TWITTERHANDLE,
				SpeakerProperties.TAG_SPEAKERURI,SpeakerProperties.TAG_BLOGURL,SpeakerProperties.TAG_SESSIONS}, new int[] {  
				R.id.Name, R.id.Biography, R.id.TwitterHandle, R.id.SpeakerURI, R.id.BlogURL, R.id.Sessions });			 

			setListAdapter(adapter);		
		} 
	} 


}