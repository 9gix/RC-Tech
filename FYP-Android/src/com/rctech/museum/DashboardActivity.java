package com.rctech.museum;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends Activity {

	// Define Request code to be used in startActivityForResult
	private static final int SCAN_CODE = 1;
	private static final int LOAD_CODE = 2;
	private static final int SEARCH_CODE = 3;
	private static final int DEMO_CODE = 4;
	private static final int VISITED_CODE = 5;

	JSONObject json;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		addButtonListener();
	}

	private void addButtonListener() {
		((Button) findViewById(R.id.btn_scan)).setOnClickListener(scanBtnListener);
		((Button) findViewById(R.id.btn_load)).setOnClickListener(loadBtnListener);
		((Button) findViewById(R.id.btn_visited)).setOnClickListener(visitedBtnListener);
		((Button) findViewById(R.id.btn_sample)).setOnClickListener(sampleBtnListener);
		((Button) findViewById(R.id.btn_camera)).setOnClickListener(cameraBtnListener);
		((Button) findViewById(R.id.btn_share)).setOnClickListener(shareBtnListener);
		((Button) findViewById(R.id.btn_mark)).setOnClickListener(markBtnListener);
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String jsdata = "";
		if (resultCode == RESULT_OK) {
			jsdata = data.getAction();
			Log.d("JSON",jsdata);
			
//			json = new JSONObject(jsdata);
			
			// save in history
			
			
			// explore exhibit
			
			startActivity(new Intent(getApplicationContext(),TabExplorer.class).putExtra("json", jsdata));
		} else if (resultCode == RESULT_CANCELED) {
			
		}
		
	};

	
	/* List of Button Listener */
	
	private OnClickListener visitedBtnListener = new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			 startActivityForResult(new Intent(getApplicationContext(),VisitedActivity.class), VISITED_CODE);
		}
	};
	private OnClickListener markBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};
	private OnClickListener sampleBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivityForResult(new Intent(getApplicationContext(),
					DemoActivity.class), DEMO_CODE);
		}
	};
	private OnClickListener shareBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};
	private OnClickListener loadBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};
	private OnClickListener scanBtnListener = new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			startActivityForResult(new Intent(getApplicationContext(),
					ScannerActivity.class), SCAN_CODE);
		}
	};
	private OnClickListener cameraBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(),CameraActivity.class));
		}
	};

}
