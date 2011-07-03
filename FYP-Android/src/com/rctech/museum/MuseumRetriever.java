package com.rctech.museum;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MuseumRetriever extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String qr = getIntent().getExtras().getString("qr");
		String response = getData(qr).toString();
		setResult(RESULT_OK, (new Intent()).setAction(response));
        finish();
	}


	private JSONObject getData(String qr) {
		JSONObject response = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://rc-tech.appspot.com/museum/json_view/" + qr);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody;

		try {
			responseBody = httpClient.execute(httpGet, responseHandler);
			response = new JSONObject(responseBody);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return response;
	}
}
