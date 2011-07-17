package com.rctech.museum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

public class TabExplorer extends TabActivity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

	private static final int DIALOG_PLAYLIST_ID = 0;
	MediaPlayer mp = new MediaPlayer();
	MediaController mc;
	Handler handler = new Handler();
	boolean mediaPrepared = false;
	AlertDialog.Builder builder;
	private String link;
	private List audio_list;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explorer);
        String json = getIntent().getStringExtra("json");
        
        final TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("info")
        		.setIndicator("Info")
        		.setContent(new Intent(this, InfoActivity.class).putExtra("json", json)));
        tabHost.addTab(tabHost.newTabSpec("video")
        		.setIndicator("Video")
        		.setContent(new Intent(this, VideoActivity.class).putExtra("json", json)));
        for (int i = 0; i < 2; i++){
        	tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 30;
        }
        setupMedia();
        audio_list = getData(getJSONArrayfromIntent("audio"));
        try{
        	Map m = (Map) audio_list.get(0);
        	link = m.get("link").toString();
        }catch (IndexOutOfBoundsException e){
        	link = null;
        }
    	prepareData();
    }
    
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		try{
			mp.start();
		}catch (IllegalStateException e){
			prepareData();
		}
	}

    
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
			mp.pause();
		}catch(IllegalStateException e){
			mp.reset();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.release();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id){
		case DIALOG_PLAYLIST_ID:
			final List data = audio_list;
			final CharSequence[] items = new CharSequence[data.size()];
			for (int i = 0; i < data.size(); i++){
				Map m = (Map) data.get(i);
				items[i] = (String) m.get("title");
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Sound Track");
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	Map m = (Map) data.get(item);
			    	link = m.get("link").toString();
			        Toast.makeText(getApplicationContext(),"Playing " + items[item], Toast.LENGTH_SHORT).show();
			        prepareData();
			    }
			});
			AlertDialog alert = builder.create();
			return alert;
		}
		return super.onCreateDialog(id);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.explorer, menu);
    	return true;
    }    
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.findItem(R.id.control_opt).setEnabled(mediaPrepared);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()){
		case R.id.scan_opt:
			startActivity(new Intent(getApplicationContext(),ScannerActivity.class));
			return true;
		case R.id.mark_opt:
			return true;
		case R.id.playlist_opt:
			showDialog(DIALOG_PLAYLIST_ID);
			return true;
		case R.id.control_opt:
			mc.show();
			break;
		case R.id.setting_opt:
			break;
		case R.id.about_opt:
			break;
		default:
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return mp.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return mp.getDuration();
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return mp.isPlaying();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		mp.pause();
	}

	@Override
	public void seekTo(int pos) {
		// TODO Auto-generated method stub
		mp.seekTo(pos);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		mp.start();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
		handler.post(new Runnable() {
			public void run() {
				mc.setEnabled(true);
				mc.show();
				mediaPrepared = true;
				Log.d("MP3", "SHOWN");
			}
		});
	}

	
	/* Private Method */

    private void setupMedia() {
		// TODO Auto-generated method stub
    	mc = new MediaController(this);
    	mp.setOnPreparedListener(this);
    	mc.setMediaPlayer(this);
    	mc.setAnchorView(findViewById(R.id.explorer_view));
    	prepareData();
	}

	private void prepareData() {
		if (link == null){
			return;
		}
		if (mp.isPlaying() || mediaPrepared == false){
			mp.reset();
		}
		try {
			mp.setDataSource(link);
			mp.prepareAsync();
			mediaPrepared = false;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    private void buildPlaylist(JSONArray arr){
    	SimpleAdapter adapter = new SimpleAdapter(this, getData(arr),
				android.R.layout.select_dialog_item, new String[] { "title" },
				new int[] { android.R.id.text1 });
    	builder = new Builder(getApplicationContext());
    	builder.setAdapter(adapter, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
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
}
