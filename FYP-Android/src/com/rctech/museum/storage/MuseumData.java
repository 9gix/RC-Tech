package com.rctech.museum.storage;

import static com.rctech.museum.storage.Constants.*;
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
		db.execSQL("CREATE TABLE " + VISITED_TABLE + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIME + " INTEGER, "
				+ QR + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + MARKED_TABLE + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIME + " INTEGER, "
				+ QR + " TEXT NOT NULL, "+ JSON +" TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + VISITED_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + MARKED_TABLE);
		onCreate(db);
	}

	public void clearVisited(SQLiteDatabase db) {
		db.execSQL("DELETE FROM " + VISITED_TABLE);
	}

	public void clearMarked(SQLiteDatabase db){
		db.execSQL("DELETE FROM " + MARKED_TABLE);
	}
}
