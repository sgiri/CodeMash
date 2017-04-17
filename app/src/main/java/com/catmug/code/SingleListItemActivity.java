package com.catmug.code;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

public class SingleListItemActivity  extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.session_list_item);

			// getting intent data
			Intent in = getIntent();

			// Get JSON values from previous intent
			String title = in.getStringExtra(SessionProperties.TAG_Title);
			String speaker = in.getStringExtra(SessionProperties.TAG_SpeakerName);				
			String technology = in.getStringExtra(SessionProperties.TAG_Technology);		
			String difficulty = in.getStringExtra(SessionProperties.TAG_Difficulty);
			String start = in.getStringExtra(SessionProperties.TAG_StartShow);
			String abstractt = in.getStringExtra(SessionProperties.TAG_Abstract);				
			String room = in.getStringExtra(SessionProperties.TAG_Room);

			// Displaying all values on the screen
			TextView lblTitle = (TextView) findViewById(R.id.title_label);
			TextView lblSpeaker = (TextView) findViewById(R.id.speakername_label);
			TextView lblTechnology = (TextView) findViewById(R.id.technology_label);
			TextView lblDifficulty = (TextView) findViewById(R.id.difficulty_label);
			TextView lblStart = (TextView) findViewById(R.id.start_label);
			TextView lblAbstract = (TextView) findViewById(R.id.abstract_label);        
			TextView lblRoom = (TextView) findViewById(R.id.room_label);

			lblTitle.setText(title);               
			lblSpeaker.setText(Html.fromHtml("<font color='#86c3f2'>Speaker:&emsp;</font>" + speaker), TextView.BufferType.SPANNABLE);
			lblTechnology.setText(Html.fromHtml("<font color='#86c3f2'>Technology:&emsp;</font>" + technology), TextView.BufferType.SPANNABLE);        
			lblDifficulty.setText(Html.fromHtml("<font color='#86c3f2'>Difficulty:&emsp;</font>" + difficulty), TextView.BufferType.SPANNABLE);  
			lblStart.setText(Html.fromHtml("<font color='#86c3f2'>Date/Time:&emsp;</font>" + start), TextView.BufferType.SPANNABLE);
			lblRoom.setText(Html.fromHtml("<font color='#86c3f2'>Room:&emsp;</font>" + room), TextView.BufferType.SPANNABLE);
			lblAbstract.setText(abstractt);                

			CheckBox favBox = (CheckBox)findViewById(R.id.check);
			String checked = in.getStringExtra(SessionProperties.TAG_Favorites);
			
			if( checked.equals("true") ){
				favBox.setChecked(true);
			}
			else
			{
				favBox.setChecked(false);
			}
		}
		catch (Exception ex) {						
			Log.e("log_tag", "SingleListItemActivity_onCreate: "+ ex.toString());	
		}	
	}
}
