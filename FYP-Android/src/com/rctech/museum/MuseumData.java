package com.rctech.museum;

import static com.rctech.museum.Constants.QR;
import static com.rctech.museum.Constants.TABLE_NAME;
import static com.rctech.museum.Constants.TIME;
import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MuseumData extends SQLiteOpenHelper {

	private static final String DB_NAME = "museum.db";
	private static final int DB_VERSION = 1;

	public MuseumData(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIME + " INTEGER, "
				+ QR + " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	public void clearData(SQLiteDatabase db){
		db.execSQL("DELETE FROM "+TABLE_NAME);
	}

}
