/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rctech.museum;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * An example of tab content that launches an activity via {@link android.widget.TabHost.TabSpec#setContent(android.content.Intent)}
 */
public class TabExplorer extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explorer);
        String json = getIntent().getStringExtra("json");
        
        final TabHost tabHost = getTabHost();
        
        tabHost.addTab(tabHost.newTabSpec("video")
                .setIndicator("Video")
                .setContent(new Intent(this, VideoList.class).putExtra("json", json)));
        tabHost.addTab(tabHost.newTabSpec("audio")
                .setIndicator("Audio")
                .setContent(new Intent(this, AudioActivity.class).putExtra("json", json)));
        tabHost.addTab(tabHost.newTabSpec("info")
                .setIndicator("Info")
                .setContent(new Intent(this, InfoActivity.class).putExtra("json", json)));
        for (int i = 0; i < 3; i++){
        	tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 30;
        }
    }
    


}
