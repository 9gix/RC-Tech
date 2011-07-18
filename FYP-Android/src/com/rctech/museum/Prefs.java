package com.rctech.museum;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity{

	private static final String OPT_AUTOPLAY = "autoplay";
	private static final boolean OPT_AUTOPLAY_DEF = true; // DEFAULT VALUE
	private static final String OPT_NEXT = "next";
	private static final boolean OPT_NEXT_DEF = true;
	private static final String OPT_REPEAT = "repeat";
	private static final boolean OPT_REPEAT_DEF = false;
	private static final String OPT_SHUFFLE = "shuffle";
	private static final boolean OPT_SHUFFLE_DEF = false;
	private static final String OPT_STACK = "stack";
	private static final boolean OPT_STACK_DEF = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
	public static boolean getAutoplay(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_AUTOPLAY,OPT_AUTOPLAY_DEF);
	}
	public static boolean getPlaynext(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_NEXT,OPT_NEXT_DEF);
	}
	public static boolean getRepeatplay(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_REPEAT,OPT_REPEAT_DEF);
	}
	public static boolean getShuffleplay(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_SHUFFLE,OPT_SHUFFLE_DEF);
	}
	public static boolean getStack(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_STACK,OPT_STACK_DEF);
	}
}
