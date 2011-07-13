package com.rctech.museum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class VideoActivity extends Activity {



	Intent waitingToPlay;


	String[] sMenuExampleNames;
	private OnClickListener playBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(waitingToPlay);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		
		JSONArray jsonArr = getJSONArrayfromIntent("video");
		buildSpinner(jsonArr);
		
		Button btnPlay = (Button)findViewById(R.id.btn_play);
		btnPlay.setOnClickListener(playBtnListener );
	}

	protected void addItem(List<Map> data, String name, Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}
	
	private void buildSpinner(JSONArray jsonArr) {
		SimpleAdapter adapter = new SimpleAdapter(this, getData(jsonArr),
				android.R.layout.simple_spinner_item, new String[] { "title" },
				new int[] { android.R.id.text1 });
		
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)findViewById(R.id.video_spinner);
        Log.d("HELLO",adapter.getItem(0).toString());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        Map map = (Map) parent.getItemAtPosition(position);
                        Intent intent = (Intent)map.get("intent");
                        launch(intent);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        showToast("Nothing to Load");
                    }
                });
	}


	private JSONArray getJSONArrayfromIntent(String type) {
		JSONObject json = null;
		try {
			json = new JSONObject(getIntent().getExtras().getString("json"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray info = null;
		try {
			info = json.getJSONArray(type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}
	private List getData(JSONArray jsonArr) {
		List<Map> myData = new ArrayList<Map>();
		for (int i = 0; i < jsonArr.length(); i++){
			JSONObject jo = null;
			String title = null;
			String link = null;
			try {
				 jo = jsonArr.getJSONObject(i);
				 title = jo.getString("title");
				 link = jo.getString("link");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			addItem(myData, title, intent);
		}
		return myData;
	}
	protected void addItem(List<Map> data, String name, String link) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("link", link);
		data.add(temp);
	}
	private void launch(Intent intent){
		waitingToPlay = intent;
	}
    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
