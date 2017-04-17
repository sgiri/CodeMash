package com.catmug.code;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CodeMashApp extends Application{

	public JSONArray ReadFileIntoJSONArray( String filename)
	{
		FileInputStream fIn = null;
		JSONArray jsonarray = null;
		try{
			fIn = openFileInput(filename);
			BufferedReader buf = new BufferedReader(new InputStreamReader(fIn));
			String readString = buf.readLine();
			fIn.close();
			jsonarray = new JSONArray(readString);
		}
		catch (Exception ex){
			Log.e("log_tag", "ReadFileIntoJSONArray: "+ ex.toString());
		}
		return jsonarray;
	}



	public void WriteFileFromJSONArray( String filename, JSONArray array)
	{
		try{
			final String TESTSTRING  = array.toString();
			FileOutputStream fOut = openFileOutput(filename, MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(TESTSTRING);
			osw.flush();
			osw.close();
		}
		catch (Exception ex){
			Log.e("log_tag", "WriteFileFromJSONArray: "+ ex.toString());
		}
	}

	public JSONArray ReadFromURL(String url)
	{
		JSONArray sessionsList = null;
		try{
			SessionService service = new SessionService(getApplicationContext());
			sessionsList= service.fetchData(url);
		}
		catch (Exception ex) {
			Log.e("log_tag", "ReadFromURL: "+ ex.toString());
		}
		return sessionsList;
	}
}
