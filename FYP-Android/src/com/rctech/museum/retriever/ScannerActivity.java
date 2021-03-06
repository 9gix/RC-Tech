package com.rctech.museum.retriever;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ScannerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		startBarcodeScanner();
	}

	private void startBarcodeScanner() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 4);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			String title = intent.getStringExtra("SCAN_RESULT");
			startActivity(new Intent(getApplicationContext(),
					MuseumRetriever.class).putExtra("title", title));
		}
		finish();
	}
}
