package com.rctech.museum;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DashboardActivity extends Activity {

	// Define Request code to be used in startActivityForResult
	private static final int SCAN_CODE = 1;
	private static final int LOAD_CODE = 2;
	private static final int SEARCH_CODE = 3;

	JSONObject json;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		addButtonListener();
	}

	private void addButtonListener() {
		((Button) findViewById(R.id.btn_scan)).setOnClickListener(scanBtnListener);
		((Button) findViewById(R.id.btn_load)).setOnClickListener(loadBtnListener);
		((Button) findViewById(R.id.btn_search)).setOnClickListener(searchBtnListener);
		((Button) findViewById(R.id.btn_camera)).setOnClickListener(cameraBtnListener);
		((Button) findViewById(R.id.btn_share)).setOnClickListener(shareBtnListener);
		((Button) findViewById(R.id.btn_mark)).setOnClickListener(markBtnListener);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String json = "";
		if (resultCode == RESULT_OK) {
			json = data.getAction();
			
			// save in history
			Log.d("JSON",json);
		} else {

		}
		
	};

	
	/* List of Button Listener */
	
	private OnClickListener searchBtnListener = new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			startActivityForResult(new Intent(getApplicationContext(),
					SearchActivity.class), SEARCH_CODE);
		}
	};
	private OnClickListener markBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

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
			// TODO Auto-generated method stub
			
		}
	};
	private OnClickListener scanBtnListener = new OnClickListener() {
	
		@Override
		public void onClick(View v) {
	
		}
	};
	private OnClickListener cameraBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};
}