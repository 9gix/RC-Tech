package com.rctech.museum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class VideoList extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		JSONObject json = null;
		try {
			json = new JSONObject(getIntent().getExtras().getString("json"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray videos = null;
		try {
			videos = json.getJSONArray("video");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setListAdapter(new SimpleAdapter(this, getData(videos),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
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
			Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			addItem(myData, title, videoIntent);
		}
		return myData;
	}

	protected void addItem(List<Map> data, String name, Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map map = (Map) l.getItemAtPosition(position);

		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}

}
