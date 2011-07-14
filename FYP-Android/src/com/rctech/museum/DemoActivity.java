package com.rctech.museum;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DemoActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String qr = "SG_WW2_1";
		startActivityForResult(new Intent(getApplicationContext(),MuseumRetriever.class).putExtra("qr", qr), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
			setResult(RESULT_OK, (new Intent()).setAction(data.getAction()));
		else 
			setResult(RESULT_CANCELED);
        finish();
	}
	
	private JSONObject getData() {
		String qr = "SG_WW2_1";
		JSONObject response = null;
		HttpClient httpClient = new DefaultHttpClient();
		// HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(
				"http://rc-tech.appspot.com/museum/json_view/" + qr);
		Log.d("URL", httpGet.toString());
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody;

		try {
			responseBody = httpClient.execute(httpGet, responseHandler);
			response = new JSONObject(responseBody);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}
}