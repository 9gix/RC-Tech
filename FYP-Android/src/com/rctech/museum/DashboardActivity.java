package com.rctech.museum;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DashboardActivity extends Activity {

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
		((Button) findViewById(R.id.btn_search)).setOnClickListener(searchBtnListener);
		((Button) findViewById(R.id.btn_camera)).setOnClickListener(cameraBtnListener);
		((Button) findViewById(R.id.btn_mark)).setOnClickListener(markBtnListener);
	}

	
	/* List of Button Listener */
	
	private OnClickListener visitedBtnListener = new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			 startActivity(new Intent(getApplicationContext(),VisitedActivity.class));
		}
	};
	private OnClickListener markBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(),BookmarkActivity.class));
		}
	};
	private OnClickListener searchBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			search();
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
			startActivity(new Intent(getApplicationContext(),ScannerActivity.class));
		}
	};
	private OnClickListener cameraBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(),CameraActivity.class));
		}
	};

	private void search() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Manual Input");
		alert.setMessage("QR Code");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String qr = input.getText().toString().toLowerCase();
		  startActivity(new Intent(getApplicationContext(),MuseumRetriever.class).putExtra("qr", qr));
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {

		  }
		});

		alert.show();
	}

	void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
