package com.catmug.code;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FavoriteSessionsActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {

			ListView lv = getListView();
			// Launching new screen on Selecting Single ListItem
			lv.setOnItemClickListener(new OnItemClickListener() { 
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// getting values from selected ListItem
					String title = ((TextView) view.findViewById(R.id.title)).getText().toString();			 
					String speakername = ((TextView) view.findViewById(R.id.speakername)).getText().toString();
					String technology = ((TextView) view.findViewById(R.id.technology)).getText().toString();
					String difficulty = ((TextView) view.findViewById(R.id.difficulty)).getText().toString();
					String start = ((TextView) view.findViewById(R.id.start)).getText().toString();
					String startshow = ((TextView) view.findViewById(R.id.startshow)).getText().toString();
					String abstractt = ((TextView) view.findViewById(R.id.abstractt)).getText().toString();
					String room = ((TextView) view.findViewById(R.id.room)).getText().toString();

					// Starting new intent
					Intent in = new Intent(getApplicationContext(), SingleListItemActivity.class);

					in.putExtra(SessionProperties.TAG_Title, title);			
					in.putExtra(SessionProperties.TAG_SpeakerName, speakername);
					in.putExtra(SessionProperties.TAG_Technology, technology);
					in.putExtra(SessionProperties.TAG_Difficulty, difficulty);							
					in.putExtra(SessionProperties.TAG_Start, start);
					in.putExtra(SessionProperties.TAG_StartShow, startshow);										
					in.putExtra(SessionProperties.TAG_Abstract, abstractt);	
					in.putExtra(SessionProperties.TAG_Room, room);
					in.putExtra(SessionProperties.TAG_Favorites, "true");	
					startActivity(in);
				}			
			});
			
						
			MainActivity.Favoritesadapter = new CustomSimpleArrayAdapter(getApplicationContext(),R.layout.favorite_list,MainActivity.FavoriteList);			
			MainActivity.FavoritesListView = getListView();
			MainActivity.FavoritesListView.setAdapter(MainActivity.Favoritesadapter );
			MainActivity.FavoritesListView.setOnItemClickListener((OnItemClickListener) MainActivity.Favoritesadapter);
			
		}
		catch (Exception e) {	
			Log.e("log_tag", "FavoriteSessionsActivity_onCreate: "+e.toString());			
		}

	}
}
