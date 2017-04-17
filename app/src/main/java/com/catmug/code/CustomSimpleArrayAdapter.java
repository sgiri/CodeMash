package com.catmug.code;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.catmug.code.R.id.start;

public class CustomSimpleArrayAdapter extends ArrayAdapter<ArrayList<HashMap<String, String>>> 
implements OnItemClickListener
{
	private final Context context;
	private ArrayList<HashMap<String, String>> arrList;
	public int LayoutResourceId;

	public CustomSimpleArrayAdapter(Context context,int layoutResourceId,ArrayList<HashMap<String, String>> values) 
	{
		super(context,layoutResourceId);
		this.context = context;
		this.arrList = values;  
		this.LayoutResourceId = layoutResourceId;
	}
	public int getCount() {

		return arrList.size();
	}

	@Override
	public View getView(final int position, View rowView, ViewGroup parent) {
		try{
			if (rowView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(LayoutResourceId, parent, false);
			}

			final HashMap<String, String> item = arrList.get(position);

			final String title = item.get(SessionProperties.TAG_Title);
			TextView titleView = (TextView) rowView.findViewById(R.id.title);
			titleView.setText(title);

			TextView speakerView = (TextView) rowView.findViewById(R.id.speakername);
			speakerView.setText( item.get(SessionProperties.TAG_SpeakerName));

			TextView tView = (TextView) rowView.findViewById(R.id.technology);
			tView.setText( item.get(SessionProperties.TAG_Technology));

			TextView dView = (TextView) rowView.findViewById(R.id.difficulty);
			dView.setText( item.get(SessionProperties.TAG_Difficulty));

			TextView sView = (TextView) rowView.findViewById(start);
			sView.setText( item.get(SessionProperties.TAG_Start));

			TextView ssView = (TextView) rowView.findViewById(R.id.startshow);
			ssView.setText( item.get(SessionProperties.TAG_StartShow));

			TextView absView = (TextView) rowView.findViewById(R.id.abstractt);
			absView.setText( item.get(SessionProperties.TAG_Abstract));

			TextView rmView = (TextView) rowView.findViewById(R.id.room);
			rmView.setText( item.get(SessionProperties.TAG_Room));

			// Check Box in list
			CheckBox favBox = (CheckBox) rowView.findViewById(R.id.check);
			if( favBox != null){
				favBox.setFocusable(false);
				//listener for check box
				favBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
						try{        	
							boolean flag = isChecked;               
							if(flag){                                           		    		
								if(!MainActivity.mCheckedTitles.contains(title)) {
									MainActivity activity = new MainActivity();
									
									MainActivity.FavoriteList.add(item);  								
									Collections.sort(MainActivity.FavoriteList, new MapComparatorForDates(SessionProperties.TAG_Start));
									
									activity.UpdateCheckedTitles(title, true);
									SaveFavorites();
								}        	         		
							}
							else
							{											        		
									String rowtitle = item.get(SessionProperties.TAG_Title);
									if(MainActivity.mCheckedTitles.contains(rowtitle) ){        
									for (int i = 0; i < MainActivity.FavoriteList.size(); i++) {
										HashMap<String, String> map = MainActivity.FavoriteList.get(i);	     			
										String title = map.get(SessionProperties.TAG_Title);
										if (title.equals(rowtitle)){
											MainActivity.FavoriteList.remove(i);											
											Collections.sort(MainActivity.FavoriteList, new MapComparatorForDates(SessionProperties.TAG_Start));
											break;
										}   										
									}  
									MainActivity activity = new MainActivity();
									activity.UpdateCheckedTitles(rowtitle, false);
									SaveFavorites();  
								}       			
							}	       	                 
						}
						catch (Exception e) {	
							Log.e("log_tag", "MySimpleArrayAdapter: "+e.toString());			
						}
					}
				}); 
			}
			if( favBox != null){

				if( MainActivity.mCheckedTitles.contains(title))
				{
					favBox.setChecked(true);
				}
				else
				{
					favBox.setChecked(false);
				}
			}
			
		}
		catch (Exception e) {	
			Log.e("log_tag", "MySimpleArrayAdapter_getView: "+e.toString());			
		}
		return rowView;
	}


	private void SaveFavorites(){
		try{
			File dir = new File(context.getFilesDir() + "/");    			
			File file = new File(dir, MainActivity.FavFileName);    			
			@SuppressWarnings("unused")
			boolean deleted = file.delete();
			JSONArray ja = new JSONArray();

			for (int i = 0; i < MainActivity.FavoriteList.size(); i++) {
				HashMap<String, String> map = MainActivity.FavoriteList.get(i);	
				JSONObject obj = new JSONObject();

				String title = map.get(SessionProperties.TAG_Title);
				String speaker = map.get(SessionProperties.TAG_SpeakerName);
				String technology = map.get(SessionProperties.TAG_Technology);
				String difficulty = map.get(SessionProperties.TAG_Difficulty);
				String start = map.get(SessionProperties.TAG_Start);
				String abstractt = map.get(SessionProperties.TAG_Abstract);   
				String room = map.get(SessionProperties.TAG_Room);

				obj.put(SessionProperties.TAG_Title, title);
				obj.put(SessionProperties.TAG_SpeakerName, speaker);    			
				obj.put(SessionProperties.TAG_Technology, technology);
				obj.put(SessionProperties.TAG_Difficulty, difficulty);				
				obj.put(SessionProperties.TAG_Start, start);
				obj.put(SessionProperties.TAG_Abstract, abstractt);
				obj.put(SessionProperties.TAG_Room, room);				
				
				ja.put(obj);
			}

			final String TESTSTRING  = ja.toString();         
			FileOutputStream fOut = context.openFileOutput(MainActivity.FavFileName,Context.MODE_WORLD_READABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(TESTSTRING);
			osw.flush();
			osw.close();	
		}
		catch (Exception e) {	
			Log.e("log_tag", "MySimpleArrayAdapter_SaveFavorites: "+e.toString());			
		}
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id)  {	
		View rowView = v;
		try
		{
			if (rowView == null) {

				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(LayoutResourceId, null);   
			}
			// getting values from selected ListItem
			String title = ((TextView) rowView.findViewById(R.id.title)).getText().toString();			 
			String speakername = ((TextView) rowView.findViewById(R.id.speakername)).getText().toString();
			String technology = ((TextView) rowView.findViewById(R.id.technology)).getText().toString();
			String difficulty = ((TextView) rowView.findViewById(R.id.difficulty)).getText().toString();
			String start = ((TextView) rowView.findViewById(R.id.start)).getText().toString();
			String startshow = ((TextView) rowView.findViewById(R.id.startshow)).getText().toString();
			String abstractt = ((TextView) rowView.findViewById(R.id.abstractt)).getText().toString();	
			CheckBox favbox = (CheckBox) rowView.findViewById(R.id.check);
			String room = ((TextView) rowView.findViewById(R.id.room)).getText().toString();
			String checked = "false";
			if( favbox.isChecked())
			{
				checked = "true";
			}

			// Starting new intent
			Intent in = new Intent(context, SingleListItemActivity.class);			

			in.putExtra(SessionProperties.TAG_Title, title);			
			in.putExtra(SessionProperties.TAG_SpeakerName, speakername);
			in.putExtra(SessionProperties.TAG_Technology, technology);
			in.putExtra(SessionProperties.TAG_Difficulty, difficulty);
			in.putExtra(SessionProperties.TAG_Start, start);
			in.putExtra(SessionProperties.TAG_StartShow, startshow);
			in.putExtra(SessionProperties.TAG_Abstract, abstractt);	
			in.putExtra(SessionProperties.TAG_Room, room);
			in.putExtra(SessionProperties.TAG_Favorites, checked);				
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(in);
			
		}
		catch (Exception ex) {						
			Log.e("log_tag", "MySimpleArrayAdapter_onItemClick: "+ ex.toString());	
		}	
	}

	public void notifyDataSetChanged (){		
		super.notifyDataSetChanged();
	} 

	public void add(HashMap<String, String> item){
		arrList.add(item);
	}
}