package ru.android.bashviewer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityWithText extends FragmentActivity implements OnClickListener {
	private ViewPager pager;
	private PagerAdapter pagerAdapter;
	
	final static String Key_001 = "TEXT_SIZE";
	private final String Key_002 = "FILE_NAME";
	final static String tag = "MyTags";
	
	final int SIZE_10 = 0;
	final int SIZE_12 = 1;
	final int SIZE_14 = 2;
	final int SIZE_16 = 3;	
	final int SIZE_18 = 4;	
	static float txtSize = 14;
	
	private int allCountFiles = 10;
	private int curFileId = 1;
	private String listValue = "Start";
	
	private String My_Table = "bash_table";
	private int VALUE_NOT_FOUND = -777;
	
	private DBHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withtext);
		
		((Button)findViewById(R.id.btn3)).setOnClickListener(this);
		
		pager = (ViewPager)findViewById(R.id.pager);
		pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {

		      @Override
		      public void onPageSelected(int position) {
		    	  Button btn3 = (Button) findViewById(R.id.btn3);
		    	  curFileId = position;		   
		    	  btn3.setText("Сарказм: " + isSarcasm(curFileId) + "; "
		    			  + getMark(getValueFromDb(curFileId)) + "; "
		    	  + Integer.toString(position) + "/" + "25386");
		      }

		      @Override
		      public void onPageScrolled(int position, float positionOffset,
		          int positionOffsetPixels) {
		      }

		      @Override
		      public void onPageScrollStateChanged(int state) {
		      }
		    });
		
		dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
	}	
	
	@Override
	protected void onStart() {
		Log.d(tag, "start");
		//узнать сколько всего файлов
		SharedPreferences sPref = getPreferences(MODE_PRIVATE);
	    txtSize = sPref.getFloat(Key_001, SIZE_14);		
		curFileId = sPref.getInt(Key_002, 1);
		
		pager.setCurrentItem(curFileId);
		if (listValue != "Start") {
			int mark = makeMark(listValue);
			addValueInDb(curFileId, mark);
			// adding result to databases
		}
		((Button)findViewById(R.id.btn3)).setText("Сарказм: " + isSarcasm(curFileId) + "; "
  			  + getMark(getValueFromDb(curFileId)) + "; "
  	   + Integer.toString(curFileId) + "/" + "25386");
		super.onStart();
	}
	
	@Override	
	protected void onStop() {
		SharedPreferences sPref = getPreferences(MODE_PRIVATE);
		Editor editor = sPref.edit();
		editor.putFloat(Key_001, txtSize);
		editor.putInt(Key_002, curFileId);
		editor.commit();
		super.onStop();
		//saved current state
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case SIZE_10:
			txtSize = 10;
			break;
		case SIZE_12:
			txtSize = 12;
			break;
		case SIZE_14:
			txtSize = 14;
			break;
		case SIZE_16:
			txtSize = 16;
			break;	
		case SIZE_18:
			txtSize = 18;
			break;	
		}
		
		((TextView) findViewById(R.id.txtView)).setTextSize(txtSize);

		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.txtView) {
			menu.add(0, SIZE_10, 0, "10");
			menu.add(0, SIZE_12, 1, "12");
			menu.add(0, SIZE_14, 2, "14");
			menu.add(0, SIZE_16, 3, "16");
			menu.add(0, SIZE_18, 4, "18");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		
		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return PageFragment.newInstance(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allCountFiles;
		}
	}

	private String getMark(int markInt) {
		String mark = "?";
		if (markInt != VALUE_NOT_FOUND) {
			if (markInt > 0) {
				if (markInt == 6)
					mark = "C";
				else
					mark = "+" + markInt;
			} else {
				if (markInt == -6)
					mark = "X";
				else
					mark = Integer.toString(markInt);
			}
		}
		return mark;
	}
	
	private int makeMark(String mark) {
		if (mark.equals("0"))
			return 0;
		int markInt = 0;
		if (mark.equals("Сарказм"))
			markInt = 6;
		else
			if (mark.equals("Удалить"))
				markInt = -6;
			else {
				markInt = (int)(mark.charAt(1)) - (int)'0';
				if (mark.charAt(0) == '-')
					markInt *= -1;
			}
		return markInt;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getApplicationContext(), ExpandedListActivity.class);
		startActivityForResult(intent, 1);
		//Уже обновленный listValue
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		listValue = data.getStringExtra(ExpandedListActivity.List_Value);	
	/*	if (pager.getCurrentItem() != allCountFiles) {
			pager.setCurrentItem(pager.getCurrentItem() + 1);
		}*/
	}
	
	private String isSarcasm(int id) {
		//return Да,  Нет, ?
		return "?";
	}
	
	private int getValueFromDb(int id) {
		String selection = "id = " + id;
		Cursor c = db.query(My_Table, null, selection, null, null, null, null);
		if (c.moveToFirst()) {
			int markColIndex = c.getColumnIndex("mark");
			return c.getInt(markColIndex);
		}
		return VALUE_NOT_FOUND;
	}
	private void addValueInDb(int id, int mark) {
		int c = getValueFromDb(id);
		if (c == mark)
			return;
		if (c != VALUE_NOT_FOUND)
			db.delete(My_Table, "id = " + id, null);
		ContentValues cv = new ContentValues();
		cv.put("id", id);
		cv.put("mark", mark);
		db.insert(My_Table, null, cv);
	}
}

class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context) {
		super(context, "myDB", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
	//	Log.d("MyTags", "--- OnCreate DB ---");
		db.execSQL("create table bash_table (" +
				"id integer primary key," +
				"mark tinyint" + ");");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}