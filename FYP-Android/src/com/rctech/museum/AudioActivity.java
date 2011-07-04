package com.rctech.museum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SimpleAdapter;

public class AudioActivity extends ListActivity implements OnPreparedListener, MediaController.MediaPlayerControl {
	
	private MediaPlayer mediaPlayer;	
	private MediaController mediaController;
	private Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.audio);
		JSONObject json = null;
		try {
			json = new JSONObject(getIntent().getExtras().getString("json"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray audios = null;
		try {
			audios = json.getJSONArray("audio");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setListAdapter(new SimpleAdapter(this, getData(audios),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
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
//			Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map map = (Map) l.getItemAtPosition(position);
		String link = (String)map.get("link");
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
	
	  public void onPrepared(MediaPlayer mediaPlayer) {
		    Log.d("MP3", "onPrepared");
		    mediaController.setMediaPlayer(this);
//		    mediaController.setAnchorView(findViewById(R.id.audio_view));
		    mediaController.setAnchorView(getListView());

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
	  
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	                                  ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_audio, menu);
	  }
	  
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	    case R.id.save:
//	      save(info.id);
	      return true;
	    case R.id.share:
//	      share(info.id);
	      return true;
	    case R.id.stop:
	    	mediaPlayer.stop();
	    	return true;
	    default:
	      return super.onContextItemSelected(item);
	    }
	  }
}
