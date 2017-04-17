package com.catmug.code;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public class SessionProperties {

	public SessionProperties() {

	}	

	public static final String url = "https://speakers.codemash.org/api/SessionsData?type=json";
	// JSON Node names	
	public static final String TAG_Title = "Title";
	public static final String TAG_SpeakerName = "LastName";
	public static final String TAG_Technology = "Category";
	public static final String TAG_Difficulty = "SessionType";
	public static final String TAG_Start = "SessionStartTimeSort";
	public static final String TAG_StartShow = "SessionStartTime";
	public static String TAG_StartSort = null;
	public static final String TAG_Abstract = "Abstract";
	public static final String TAG_URI = "https://speakers.codemash.org/api/SessionsData/" + "Id";
	public static final String TAG_EventType = "SessionType";
	public static final String TAG_SpeakerURI = "https://speakers.codemash.org/api/SpeakersData/" + "Id";
			//"GravatarUrl";
	public static final String TAG_Room = "Rooms";
	public static final String TAG_Favorites = "Favorites";
	
	public static String SessionType = null;	
	public static Date startDateIn = null;
	public static String startDateOutSort = null;

	public static String startDateOutShow = null;

	public static SimpleDateFormat inputformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
	public static SimpleDateFormat outputformatsort = new SimpleDateFormat("yyyy-MM-dd HH:mm a", Locale.US);
	public static SimpleDateFormat outputformatshow = new SimpleDateFormat("M/d h:mm aa, EEEE", Locale.US);

	public static Date startDateSort;

	public static enum mSortOrder {
		TITLE_NAME, SESSION_DATE, SPEAKER_NAME, TECHNOLOGY_NAME, FAVORITES;
	}

	public static final String TITLE_SPEC = "by Title";
	public static final String DATE_SPEC = "by Date";
	public static final String SPEAKER_SPEC = "by Speaker";
	public static final String TECHNOLOGY_SPEC = "by Technology";	
	public static final String FAVORITES_SPEC = "Favorites";

}

class MapComparator implements Comparator<Map<String, String>>
{
	private final String key;

	public MapComparator(String key)
	{
		this.key = key;
	}
	public int compare(Map<String, String> first,
			Map<String, String> second)
	{
		// TODO: Null checking, both for maps and values
		if(key != SessionProperties.TAG_Start) {
			String firstValue = (String) first.get(key);
			String secondValue = (String) second.get(key);
			return (firstValue).compareTo(secondValue);
		}
		else{
			return  1;
		}

	}
}


	class MapComparatorForDates implements Comparator<Map<String, String>>
	{
		private final String key;

		public MapComparatorForDates(String key)
		{
			this.key = key;
		}
		public int compare(Map<String, String> first,
						   Map<String, String> second) {
			// TODO: Null checking, both for maps and values
			if (key != SessionProperties.TAG_Start) {
				String firstValue = (String) first.get(key);
				String secondValue = (String) second.get(key);
				return (firstValue).compareTo(secondValue);
			} else {
				int i = 1;
try {
	SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	String firstDate = (String) first.get(key);
	String secondDate = (String) second.get(key);
	Date o1 = sdfmt.parse(firstDate);
	Date o2 = sdfmt.parse(secondDate);
	i = o1.compareTo(o2);

}
catch(Exception e){
	Log.e("log_tag", "GetSession: "+ e.toString());

}


				return i;

			}

		}
}


