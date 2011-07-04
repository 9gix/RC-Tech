package com.rctech.museum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class InfoActivity extends Activity {
	String[] sMenuExampleNames;
	WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		setWebView();
		
		JSONArray jsonArr = getJSONArrayfromIntent("info");
		buildSpinner(jsonArr);
	}


	private void buildSpinner(JSONArray jsonArr) {
		SimpleAdapter adapter = new SimpleAdapter(this, getData(jsonArr),
				android.R.layout.simple_spinner_item, new String[] { "title" },
				new int[] { android.R.id.text1 });
		
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)findViewById(R.id.info_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
//                        showToast("Spinner1: position=" + position + " id=" + id);
                        Map map = (Map) parent.getItemAtPosition(position);
                        String link = (String)map.get("link");
                        showToast("Loading URL: " + link);
                        loadWeb(link);
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


	private void setWebView() {
		wv = (WebView)findViewById(R.id.webView);
		wv.getSettings().setJavaScriptEnabled(true);
		
		wv.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
	}

	
	private List getData(JSONArray videos) {
		List<Map> myData = new ArrayList<Map>();
		for (int i = 0; i < videos.length(); i++){
			JSONObject jo = null;
			String title = null;
			String link = null;
			try {
				 jo = videos.getJSONObject(i);
				 title = jo.getString("title");
				 link = jo.getString("link");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			addItem(myData, title, link);
		}
		return myData;
	}

	protected void addItem(List<Map> data, String name, String link) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("link", link);
		data.add(temp);
	}
	private void loadWeb(String url){
		wv.loadUrl(url);
	}
    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
