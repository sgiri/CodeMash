package com.catmug.code;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SessionTabLayoutActivity 
extends TabActivity {	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.session_main);
				
		this.setTitle(SessionProperties.SessionType + " Listing");
		TabHost tabHost = getTabHost();


		TabSpec TitleSpec = tabHost.newTabSpec(SessionProperties.TITLE_SPEC);				
		TitleSpec.setIndicator(SessionProperties.TITLE_SPEC, getResources().getDrawable(R.drawable.icon_title));
		Intent TitleIntent = new Intent(this, SessionListActivity.class);				
		TitleIntent.putExtra("Order", 1);
		TitleSpec.setContent(TitleIntent);

		// Date Tab
		TabSpec DateSpec=tabHost.newTabSpec(SessionProperties.DATE_SPEC);
		DateSpec.setIndicator(SessionProperties.DATE_SPEC, getResources().getDrawable(R.drawable.icon_date));
		Intent DateIntent = new Intent(this, SessionListActivity.class);
		DateIntent.putExtra("Order", 2);
		DateSpec.setContent(DateIntent);


		// Speaker Tab				
		TabSpec SpeakerSpec=tabHost.newTabSpec(SessionProperties.SPEAKER_SPEC);
		SpeakerSpec.setIndicator(SessionProperties.SPEAKER_SPEC, getResources().getDrawable(R.drawable.icon_profile));
		Intent SpeakerIntent = new Intent(this, SessionListActivity.class);
		SpeakerIntent.putExtra("Order", 3);
		SpeakerSpec.setContent(SpeakerIntent);

		// Technology Tab
		TabSpec TechnologySpec=tabHost.newTabSpec(SessionProperties.TECHNOLOGY_SPEC);
		TechnologySpec.setIndicator(SessionProperties.TECHNOLOGY_SPEC, getResources().getDrawable(R.drawable.icon_tech));
		Intent TechnologyIntent = new Intent(this, SessionListActivity.class);
		TechnologyIntent.putExtra("Order", 4);
		TechnologySpec.setContent(TechnologyIntent);

		
		// Favorites Tab
		TabSpec FavoritesSpec=tabHost.newTabSpec(SessionProperties.FAVORITES_SPEC);
		FavoritesSpec.setIndicator(SessionProperties.FAVORITES_SPEC, getResources().getDrawable(R.drawable.icon_star));
		Intent FavoritesIntent = new Intent(this, SessionListActivity.class);
		FavoritesIntent.putExtra("Order", 5);
		FavoritesSpec.setContent(FavoritesIntent);				


		tabHost.addTab(TitleSpec);
		tabHost.addTab(DateSpec);
		tabHost.addTab(SpeakerSpec);
		tabHost.addTab(TechnologySpec);
		tabHost.addTab(FavoritesSpec);

	}
}
