package com.rctech.museum.storage;

import static android.provider.BaseColumns._ID;
import static com.rctech.museum.storage.Constants.*;

import com.rctech.museum.TabExplorer;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class BookmarkActivity extends ListActivity{
	private static final String[] FROM = { _ID, TIME, TITLE, JSON };
	private static final String ORDER_BY = TIME + " DESC";
	private MuseumData museumData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		museumData = new MuseumData(this);
		try {
			Cursor cursor = getMarked();
			showMarked(cursor);
			Log.d("HELLO", "MARKED");
		} finally {
			museumData.close();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor c = (Cursor) l.getItemAtPosition(position);
		String json = c.getString(3);
		String title = c.getString(2);
		if (isOnline()) {
			startActivity(new Intent(getApplicationContext(),
					TabExplorer.class).putExtra("json", json).putExtra("title", title));
			finish();
		} else {
			showToast("Unable to connect to internet");
		}
	}

	private void showMarked(Cursor cursor) {
		ListAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
				android.R.layout.two_line_list_item, cursor, new String[] {
						TITLE, TIME }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		setListAdapter(adapter);
	}

	private Cursor getMarked() {
		SQLiteDatabase db = museumData.getReadableDatabase();
		Cursor c = db.query(MARKED_TABLE, FROM, null, null, null, null, ORDER_BY);
		startManagingCursor(c);
		return c;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.addSubMenu("clear");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		SQLiteDatabase db = museumData.getWritableDatabase();
		museumData.clearMarked(db);
		finish();
		return super.onMenuItemSelected(featureId, item);
	}
}
