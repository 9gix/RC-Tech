package com.rctech.museum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AudioActivity extends Activity implements
		MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

	private MediaPlayer mediaPlayer;
	private MediaController mediaController;
	private Handler handler = new Handler();
	private String link;
	Spinner audio_spinner;
	String[] sMenuExampleNames;

	// ----------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("LIFE","CREATED");
		setContentView(R.layout.audio);

		JSONArray jsonArr = getJSONArrayfromIntent("audio");
		buildSpinner(jsonArr);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("LIFE","STOP");
		mediaPlayer.stop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("LIFE","RESUME");	
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("LIFE","RESTART");
		
		// Reconstruct Spinner Listener to Play Audio on Restart
		JSONArray jsonArr = getJSONArrayfromIntent("audio");
		buildSpinner(jsonArr);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("LIFE","DESTROYED");
		mediaPlayer.release();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// the MediaController will hide after 3 seconds - tap the screen to
		// make it appear again
		mediaController.show();
		return false;
	}

	private void buildSpinner(JSONArray jsonArr) {
		SimpleAdapter adapter = new SimpleAdapter(this, getData(jsonArr),
				android.R.layout.simple_spinner_item, new String[] { "title" },
				new int[] { android.R.id.text1 });

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		audio_spinner = (Spinner) findViewById(R.id.audio_spinner);
		audio_spinner.setAdapter(adapter);


		audio_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				loadAudio();
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
		JSONArray jsonArr = null;
		try {
			jsonArr = json.getJSONArray(type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArr;
	}

	private List getData(JSONArray jsonArr) {
		List<Map> myData = new ArrayList<Map>();
		for (int i = 0; i < jsonArr.length(); i++) {
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

	private void loadAudio() {
		Map map = (Map) audio_spinner.getSelectedItem();
		link = (String) map.get("link");
		showToast("Loading URL: " + link);
		if (mediaPlayer != null)
			if (mediaPlayer.isPlaying())
				mediaPlayer.stop();
		mediaPlayer = new MediaPlayer();
		mediaController = new MediaController(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaController.setMediaPlayer(this); 
		mediaController.setAnchorView(findViewById(R.id.audio_view));
		try {
			mediaPlayer.setDataSource(link);
			mediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	// MediaPlayer methods
	public void onPrepared(MediaPlayer mediaPlayer) {
		Log.d("MP3", "onPrepared");
		mediaPlayer.start();
		handler.post(new Runnable() {
			public void run() {
				mediaController.setEnabled(true);
				mediaController.show();
				Log.d("MP3", "SHOWN");
			}
		});
	}

	// --MediaPlayerControl method
	// ----------------------------------------------
	public void start() {
		mediaPlayer.start();
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	public void seekTo(int i) {
		mediaPlayer.seekTo(i);
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public int getBufferPercentage() {
		return 0;
	}

	public boolean canPause() {
		return true;
	}

	public boolean canSeekBackward() {
		return true;
	}

	public boolean canSeekForward() {
		return true;
	}
}
