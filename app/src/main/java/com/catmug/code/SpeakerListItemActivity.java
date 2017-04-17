package com.catmug.code;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class SpeakerListItemActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaker_list_item);

		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		String speaker = in.getStringExtra(SpeakerProperties.TAG_NAME);
		String biography = in.getStringExtra(SpeakerProperties.TAG_BIOGRAPHY);                         
		String twitterhandle = in.getStringExtra(SpeakerProperties.TAG_TWITTERHANDLE);           
		String blogurl = in.getStringExtra(SpeakerProperties.TAG_BLOGURL);                                            

		// Displaying all values on the screen
		TextView lblSpeakerName = (TextView) findViewById(R.id.Name);
		TextView lblBiography = (TextView) findViewById(R.id.Biography);
		TextView lblTwitterHandle = (TextView) findViewById(R.id.TwitterHandle);
		TextView lblBlogURL = (TextView) findViewById(R.id.BlogURL);                      

		lblSpeakerName.setText(speaker);               
		lblBiography.setText(biography);		

		if (twitterhandle.length() > 0)		
		{
			if (!twitterhandle.startsWith("@"))
			{								
				twitterhandle = "@" + twitterhandle;
			}
		}

		lblTwitterHandle.setText(Html.fromHtml("<font color='#86c3f2'>Twitter Handle:&emsp;</font>" + twitterhandle));
		Linkify.addLinks(lblTwitterHandle, Linkify.ALL);

		lblBlogURL.setText("Blog: " + Html.fromHtml("<a href=\"" + blogurl + "\">" + blogurl + "</a>"));  
		Linkify.addLinks(lblBlogURL, Linkify.ALL);
	}
}
