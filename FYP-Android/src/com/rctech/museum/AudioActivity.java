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
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AudioActivity extends Activity implements OnPreparedListener, MediaController.MediaPlayerControl {
	
	private MediaPlayer mediaPlayer;	
	private MediaController mediaController;
	private Handler handler = new Handler();
	
	  public void onPrepared(MediaPlayer mediaPlayer) {
		    Log.d("MP3", "onPrepared");
		    mediaController.setMediaPlayer(this);
		    mediaController.setAnchorView(findViewById(R.id.audio_view));
//		    mediaController.setAnchorView(getListView());

		    handler.post(new Runnable() {
		      public void run() {
		        mediaController.setEnabled(true);
		        mediaController.show();
		        Log.d("MP3","SHOWN");
		      }
		    });
		  }

	//--MediaPlayerControl methods----------------------------------------------------
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
	  //--------------------------------------------------------------------------------
	  @Override
	  protected void onStop() {
	    super.onStop();
	    mediaPlayer.stop();
	    mediaPlayer.release();
	  }

	  @Override
	  public boolean onTouchEvent(MotionEvent event) {
	    //the MediaController will hide after 3 seconds - tap the screen to make it appear again
	    mediaController.show();
	    return false;
	  }
	  
	  String[] sMenuExampleNames;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.audio);
			
			JSONArray jsonArr = getJSONArrayfromIntent("audio");
			buildSpinner(jsonArr);
		}


		private void buildSpinner(JSONArray jsonArr) {
			SimpleAdapter adapter = new SimpleAdapter(this, getData(jsonArr),
					android.R.layout.simple_spinner_item, new String[] { "title" },
					new int[] { android.R.id.text1 });
			
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        Spinner audio_spinner = (Spinner)findViewById(R.id.audio_spinner);
	        audio_spinner.setAdapter(adapter);
	        audio_spinner.setOnItemSelectedListener(
	                new OnItemSelectedListener() {
	                    public void onItemSelected(
	                            AdapterView<?> parent, View view, int position, long id) {
//	                        showToast("Spinner1: position=" + position + " id=" + id);
	                        Map map = (Map) parent.getItemAtPosition(position);
	                        String link = (String)map.get("link");
	                        showToast("Loading URL: " + link);
	                        loadAudio(link);
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
		private void loadAudio(String link){
		    if (mediaPlayer != null){
		    	mediaPlayer.stop();
		    }
		    mediaPlayer = new MediaPlayer();
		    
		    mediaPlayer.setOnPreparedListener(this);

		    mediaController = new MediaController(this);

		    try {
		      mediaPlayer.setDataSource(link);
		      mediaPlayer.prepare();
		      mediaPlayer.start();
		    } catch (IOException e) {
		      Log.e("MP3", "Could not open file " + link + " for playback.", e);
		    }
		}
	    void showToast(CharSequence msg) {
	        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	    }
}
