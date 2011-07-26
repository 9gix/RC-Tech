package com.rctech.museum.helper;

import com.rctech.museum.R;
import com.rctech.museum.R.id;
import com.rctech.museum.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView aboutUs;
        aboutUs = (TextView)findViewById(R.id.aboutUs);
        aboutUs.setText("This application is created by " +
        		"Eugene, Felicia and Tran from Republic Polytechnic. " +
        		"\n\nMuseum Partner is used as a guide, helping " +
        		"to  enhance the user experience during your museum visits."
        );
        
        aboutUs.setTextSize(18);
	}
}
