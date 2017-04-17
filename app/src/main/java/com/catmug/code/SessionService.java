package com.catmug.code;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;












public class SessionService {
	private boolean cancelled;
	private boolean requestInProgress;
	private HttpGet httpRequest;

	public SessionService() {}

	public SessionService(Context context) {
		super();		
	}

	public JSONArray fetchData(String url) throws Exception
	{
		InputStream is = null;	
		String json = "";
		JSONArray jArray = null;

		// Making HTTP request
		try {

			CustomHttpClient httpClient2 = new CustomHttpClient();
			HttpGet httpGet = new HttpGet(url);
			Log.d("sessionservice", "Before httpResponse");
			HttpResponse httpResponse = httpClient2.execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.d("sessionservice", "after statusCode, code= " +statusCode);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON array
		try {			
			jArray = new JSONArray(json); 
		} catch (JSONException e) {			
			Log.e("JSON Parser", "Error parsing data here " + e.getMessage());
		}
		return jArray;
	}

	public void cancelFetch() {
		setCancelled(true);
		if (isRequestInProgress()) {
			httpRequest.abort();
		}
	}

	@SuppressWarnings("unused")
	private synchronized boolean isCancelled() {
		return cancelled;
	}
	private synchronized void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	private synchronized boolean isRequestInProgress() {
		return requestInProgress;
	}
	@SuppressWarnings("unused")
	private synchronized void setRequestInProgress(boolean requestInProgress) {
		this.requestInProgress = requestInProgress;
	}
}