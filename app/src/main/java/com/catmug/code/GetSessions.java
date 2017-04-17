package com.catmug.code;

import android.net.ParseException;
import android.util.Log;

import com.catmug.code.SessionProperties.mSortOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class GetSessions {
	String picksort ="";
	String picksortFavorites ="";
	ArrayList<HashMap<String, String>> sessionList = new ArrayList<HashMap<String, String>>();
	MainActivity activity = new MainActivity();
	
	// constructor
	public GetSessions() {
	}	

	public ArrayList<HashMap<String, String>> getSessionsList(JSONArray sessions, String sessiontype, mSortOrder sortkey, int sessionorder) {
		try {		
			// looping through All Sessions
			for(int i = 0; i < sessions.length(); i++){				
				JSONObject c = sessions.getJSONObject(i);
				String speakers = "";
				try {
					JSONArray speakersArr = c.getJSONArray("Speakers");

					for (int j = 0; j < speakersArr.length(); j++) {
						JSONObject param1 = speakersArr.getJSONObject(j);

						//now get required values by key
						if (j != 0) {
							speakers = speakers + ", ";
						}
						String fname = param1.getString("FirstName");
						String lname = param1.getString("LastName");

						if (fname.equals("null")) {
							fname = "";
						}
						if (lname.equals("null")) {
							lname = "";
						}

						speakers = speakers + fname + " " + lname;
					}
				}
				catch(Exception exc) {
					Log.e("log_tag", "Error looping GetSessions "+exc.toString());
				}
				String rooms = "";
				try {
					JSONArray roomsArr = c.getJSONArray("Rooms");
					for( int y = 0; y < roomsArr.length(); y ++){
						if( y != 0 ) {
							rooms = rooms + ", ";
						}
						rooms = rooms + roomsArr.getString(y);
					}
				}
				catch(Exception ex){
					Log.e("log_tag", "Error looping GetSessions "+ex.toString());
				}


				String title = c.optString(SessionProperties.TAG_Title,"n/a");


				title = title.replaceAll("\"","\\\"");
				title = title.trim();

				String speaker = c.optString(SessionProperties.TAG_SpeakerName, "n/a");
				String technology = c.optString(SessionProperties.TAG_Technology,"n/a");
				String difficulty = c.optString(SessionProperties.TAG_Difficulty, "n/a");
				String start = c.optString(SessionProperties.TAG_StartShow, "TBD");
				String abstractt = c.optString(SessionProperties.TAG_Abstract, "n/a");
				String eventtype = c.optString(SessionProperties.TAG_EventType, "Session");
				String room = c.optString(SessionProperties.TAG_Room, "TBD");

				try {  

					if (start != "TBD")
					{					
						Date startIn = SessionProperties.inputformat.parse(start);
						//Long CurrentTime = startIn.getTime();
						//Long ESTtime = CurrentTime;
						//Date ESTdatetime = new Date(ESTtime);

						SessionProperties.startDateOutShow =  SessionProperties.outputformatshow.format(startIn);
						SessionProperties.startDateOutSort =  start;
					}
					else
					{
						Calendar altdatetime = Calendar.getInstance();
						SessionProperties.startDateOutShow = "TBD";
						SessionProperties.startDateOutSort =  SessionProperties.outputformatsort.format(altdatetime);
					}									

				} catch (ParseException e) {  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (eventtype.contains(sessiontype))
				{
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value				
					map.put(SessionProperties.TAG_Title, title);			
					map.put(SessionProperties.TAG_SpeakerName, speakers);
					map.put(SessionProperties.TAG_Technology, technology);
					map.put(SessionProperties.TAG_Difficulty, difficulty);
					map.put(SessionProperties.TAG_Start, SessionProperties.startDateOutSort);					
					map.put(SessionProperties.TAG_StartShow, SessionProperties.startDateOutShow);
					map.put(SessionProperties.TAG_Abstract, abstractt);
					map.put(SessionProperties.TAG_Room, rooms);

					switch (sortkey) {
					case SPEAKER_NAME:
						picksort = SessionProperties.TAG_SpeakerName;			
						break;
					case TECHNOLOGY_NAME: 
						picksort = SessionProperties.TAG_Technology;			
						break;
					case TITLE_NAME:
						picksort = SessionProperties.TAG_Title;			
						break;
					case SESSION_DATE:
						picksort = SessionProperties.TAG_Start;
						break;
					default:
						break;							
					}

					sessionList.add(map);								
				}																
				Collections.sort(sessionList, new MapComparator(picksort));
			}
		} catch (JSONException e) {
			//e.printStackTrace();			
			Log.e("log_tag", "Error looping GetSessions "+e.toString());			
		}
		return sessionList;		

	}

	public ArrayList<HashMap<String, String>> getFavoriteList(JSONArray sessions) {
		try {		
			MainActivity.mCheckedTitles.clear();
			// looping through All Sessions
			for(int i = 0; i < sessions.length(); i++){				
				JSONObject c = sessions.getJSONObject(i);

				String title = c.getString(SessionProperties.TAG_Title);
				title = title.replaceAll("\"","\\\"");
				String speaker = c.getString(SessionProperties.TAG_SpeakerName);												
				String technology = c.getString(SessionProperties.TAG_Technology);
				String difficulty = c.getString(SessionProperties.TAG_Difficulty);
				String start = c.optString(SessionProperties.TAG_Start, "TBD");
				String abstractt = c.getString(SessionProperties.TAG_Abstract);
				String room = c.optString(SessionProperties.TAG_Room, "TBD");

				try {  

				} catch (ParseException e) {  
					Log.e("log_tag", "GetSession.getFavoriteList1: "+ e.toString());	
				}


				try {  
					//getFavoriteList
					Date startIn = SessionProperties.inputformat.parse(start);
					SessionProperties.startDateOutShow =  SessionProperties.outputformatshow.format(startIn);
					SessionProperties.startDateOutSort = start;


				} catch (ParseException e) {  
					Log.e("log_tag", "GetSession.getFavoriteList1: "+ e.toString());	
				} catch (java.text.ParseException e) {
					Log.e("log_tag", "GetSession.getFavoriteList2: "+ e.toString());	
				}


				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key => value				
				map.put(SessionProperties.TAG_Title, title);			
				map.put(SessionProperties.TAG_SpeakerName, speaker);
				map.put(SessionProperties.TAG_Technology, technology);
				map.put(SessionProperties.TAG_Difficulty, difficulty);
				map.put(SessionProperties.TAG_Start, start);
				map.put(SessionProperties.TAG_StartShow, SessionProperties.startDateOutShow);
				map.put(SessionProperties.TAG_Abstract, abstractt);								
				map.put(SessionProperties.TAG_Room, room);		

				picksortFavorites = SessionProperties.TAG_Start;												
				
				sessionList.add(map);
				activity.UpdateCheckedTitles(title, true);
			}

			Collections.sort(sessionList, new MapComparatorForDates(picksortFavorites));

		} catch (JSONException e) {		
			Log.e("log_tag", "Error looping Favorites GetSessions"+e.toString());			
		}
		return sessionList;		
	}
}

