package com.rctech.museum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rctech.museum.R;
import com.rctech.museum.R.id;
import com.rctech.museum.R.layout;

import android.app.Activity;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class InfoActivity extends Activity {
	private String[] sMenuExampleNames;
	private WebView wv;
	private String link;
	private String title;
	JSONArray jsonArr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		jsonArr = getJSONArrayfromIntent("info");
		buildSpinner(jsonArr);
		setWebView();
//		setLoadBtnListener();
		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		LinearLayout l1 = (LinearLayout)findViewById(R.id.linear1);
//		
//		l1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
	}

//	private void setLoadBtnListener() {
//		// TODO Auto-generated method stub
//		Button loadBtn = (Button)findViewById(R.id.Load);
//		loadBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				loadWeb(link);
//			}
//
//		});
//	}


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
                        link = (String)map.get("link");
                        
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

	
	private List getData(JSONArray jsonArr) {
		List<Map> myData = new ArrayList<Map>();
		addItem(myData,"Select Article","http://en.m.wikipedia.org/");
		for (int i = 0; i < jsonArr.length(); i++){
			JSONObject jo = null;
			title = null;
			link = null;
			try {
				 jo = jsonArr.getJSONObject(i);
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
		showToast("Loading URL: " + link);
	}
    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
