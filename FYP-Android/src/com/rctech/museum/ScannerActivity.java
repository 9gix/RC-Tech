package com.rctech.museum;

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
	private void startBarcodeScanner(){
    	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 4);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (resultCode == RESULT_OK){
	        if (requestCode == 4) {
	            
	                String qr = intent.getStringExtra("SCAN_RESULT");
	                startActivityForResult(new Intent(getApplicationContext(),MuseumRetriever.class).putExtra("qr", qr), 2);
	            
	        }else if(requestCode == 2){
	        	
	        		setResult(RESULT_OK, (new Intent()).setAction(intent.getAction()));
	        		finish();
	        	
	        }
    	}else if(resultCode == RESULT_CANCELED){
    		Log.d("HELLO","RESULT CANCEL");
    		setResult(RESULT_CANCELED);
    		finish();
    	}
    }
}
