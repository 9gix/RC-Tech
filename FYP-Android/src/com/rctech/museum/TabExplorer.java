package com.rctech.museum;


import static com.rctech.museum.storage.Constants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rctech.museum.helper.AboutActivity;
import com.rctech.museum.retriever.ScannerActivity;
import com.rctech.museum.storage.MuseumData;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

public class TabExplorer extends TabActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaController.MediaPlayerControl {

	private static final int DIALOG_PLAYLIST_ID = 0;
	MediaPlayer mp = new MediaPlayer();
	MediaController mc;
	Handler handler = new Handler();
	boolean mediaPrepared = false;
	AlertDialog.Builder builder;
	private String link;
	private List audio_list;
	private int nowPlaying = 0;
	String json_str;
	String qr;
	MuseumData museumData;
	private int mCurrentBufferPercentage;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explorer);
//        json_str = getIntent().getAction();
        json_str = getIntent().getStringExtra("json");
        qr = getIntent().getStringExtra("qr");
        
        setTitle(qr2title(qr));
        
        final TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("info")
        		.setIndicator("Info")
        		.setContent(new Intent(this, InfoActivity.class).putExtra("json", json_str)));
        tabHost.addTab(tabHost.newTabSpec("video")
        		.setIndicator("Video")
        		.setContent(new Intent(this, VideoActivity.class).putExtra("json", json_str)));
        for (int i = 0; i < 2; i++){
        	tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 30;
        }
        setupMedia();
        audio_list = getData(getJSONArrayfromIntent("audio"));
        try{
        	Map m = (Map) audio_list.get(nowPlaying);
        	link = m.get("link").toString();
        }catch (IndexOutOfBoundsException e){
        	link = null;
        }
        if (Prefs.getAutoplay(getApplicationContext()))
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
			if (!Prefs.getStack(getApplicationContext())){
				finish();
			}
			return true;
		case R.id.mark_opt:
			bookmark();
			return true;
		case R.id.playlist_opt:
			showDialog(DIALOG_PLAYLIST_ID);
			return true;
		case R.id.control_opt:
			mc.show();
			return true;
		case R.id.setting_opt:
			startActivity(new Intent(getApplicationContext(),Prefs.class).putExtra("xml",R.xml.prefs));
			return true;
		case R.id.about_opt:
			startActivity(new Intent(getApplicationContext(),AboutActivity.class));
			break;
		default:
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	private void bookmark() {

		museumData = new MuseumData(this);
		try{
			addQR(qr);
			Log.d("HELLO","SAVED");
		}finally{
			showToast("Bookmark added");
			museumData.close();
		}
	}

	private void addQR(String qr){
		SQLiteDatabase db = museumData.getWritableDatabase();
		ContentValues values = new ContentValues();
		Long t = System.currentTimeMillis();
		Date date = new Date(t);
		values.put(TIME, date.toLocaleString());
		values.put(QR, qr2title(qr));
		values.put(JSON, json_str);
		db.insertOrThrow(MARKED_TABLE, null, values);
	}

	private String qr2title(String qr) {
		String title = qr.replace("_", " ");
		return title;
	}
	
	private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
			new MediaPlayer.OnBufferingUpdateListener() {
		    	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		    		mCurrentBufferPercentage = percent;
		        }
		    };


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
		if (mp != null) {
            return mCurrentBufferPercentage;
        }
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
    	mp.setOnBufferingUpdateListener(mBufferingUpdateListener);
    	mp.setOnPreparedListener(this);
    	mCurrentBufferPercentage = 0;
    	mc.setMediaPlayer(this);
    	mc.setAnchorView(findViewById(R.id.explorer_view));
    	mp.setOnCompletionListener(this);
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
    
    private JSONArray getJSONArrayfromIntent(String type) {
		JSONObject json = null;
		try {
			json = new JSONObject(this.json_str);
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

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.d("HELLO","COMPLETED");
		if (Prefs.getPlaynext(getApplicationContext())){
			Log.d("HELLO","PLAYED NEXT");
			if (Prefs.getShuffleplay(getApplicationContext())){
				Log.d("HELLO","SHUFFLED");
				int previous = nowPlaying;
				while (nowPlaying==previous){
					nowPlaying = new Random().nextInt(audio_list.size());
				}
			}else{
				nowPlaying++;
				if (nowPlaying > audio_list.size()){
					if (!Prefs.getRepeatplay(getApplicationContext())){
						return;
					}else{
						nowPlaying = 0;
					}
				}
			}
			try{
	        	Map m = (Map) audio_list.get(nowPlaying);
	        	link = m.get("link").toString();
	        }catch (IndexOutOfBoundsException e){
	        	link = null;
	        }
			mp.reset();
			prepareData();
		}
		
	}
	
   void showToast(CharSequence msg) {
       Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
   }


}
