package com.rctech.museum;

import static com.rctech.museum.Constants.QR;
import static com.rctech.museum.Constants.VISITED_TABLE;
import static com.rctech.museum.Constants.TIME;

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MuseumRetriever extends Activity {
	
	MuseumData museumData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String qr = getIntent().getExtras().getString("qr").replace(" ", "_");
		String response;
		if (isOnline()){
			JSONObject jo = getData(qr);
			if (jo == null){
				showToast("QR Code Not Found");
				finish();
			}else{
				response = jo.toString();
				startActivity(new Intent(getApplicationContext(),TabExplorer.class).putExtra("qr", qr).putExtra("json", response));
			}
		}else{
			showToast("No Internet Connectivity");
			showToast("Stored in Visited History, Please load it when you have internet connectivity");
			setResult(RESULT_CANCELED);
		}
		museumData = new MuseumData(this);
		try{
			addQR(qr);
			Log.d("HELLO","SAVED");
		}finally{
			museumData.close();
		}
        finish();
	}

	private void addQR(String qr){
		SQLiteDatabase db = museumData.getWritableDatabase();
		ContentValues values = new ContentValues();
		Long t = System.currentTimeMillis();
		Date date = new Date(t);
		values.put(TIME, date.toLocaleString());
		values.put(QR, qr2title(qr));
		db.insertOrThrow(VISITED_TABLE, null, values);
	}

	private String qr2title(String qr) {
		String title = qr.replace("_", " ");
		return title;
	}

	private JSONObject getData(String qr) {
		JSONObject response = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://rc-tech.appspot.com/museum/json_view/" + qr);
//				"http://localhost/museum/json_view/" + qr);
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
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}

   void showToast(CharSequence msg) {
       Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
   }
}
