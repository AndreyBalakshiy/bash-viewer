package ru.android.bashviewer;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
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
	private final String Key_003 = "FILE_NAME_FULL";
	final static String tag = "MyTags";
	
	final int SIZE_10 = 0;
	final int SIZE_12 = 1;
	final int SIZE_14 = 2;
	final int SIZE_16 = 3;	
	final int SIZE_18 = 4;	
	static float txtSize = 14;

	static int filesList[];
	private int fullFirstName = -1;
	private int filesCount = 2000;
	private int curFileId = 1;
	private String listValue = "Start";
	private int sarcasmValue = 2;
	
	static String My_Table = "bash_table";
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
		    	  btn3.setText("Сарказм: " + isSarcasm(filesList[curFileId]) + "; "
		    			  + getMark(getValueFromDb(filesList[curFileId])) + "; "
		    	  + Integer.toString(curFileId) + "/" + Integer.toString(filesCount));
		    /*	  btn3.setText("Сарказм: " + isSarcasm(curFileId) + "; "
		    			  + getMark(getValueFromDb(curFileId)) + "; "
		    	  + Integer.toString(curFileId) + "/" + Integer.toString(filesCount));*/
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
	
	private void getFilesList() {
		File sdPath = Environment.getExternalStorageDirectory();
	    sdPath = new File(sdPath.getAbsolutePath() + File.separator + "Data" + File.separator);
  
	    String allFiles[] = sdPath.list(); 
		filesList = new int[allFiles.length + 1];
		for (int i = 0; i < allFiles.length; i++)
			filesList[i] = Integer.parseInt(allFiles[i]);
	}
	
	@Override
	protected void onStart() {
	//	Log.d(tag, "start");
		//узнать сколько всего файлов
		if (listValue == "Start") {
			getFilesList();

			SharedPreferences sPref = getPreferences(MODE_PRIVATE);
			txtSize = sPref.getFloat(Key_001, SIZE_14);

			curFileId = sPref.getInt(Key_002, 1);
			// filesCount = filesList.length - 2;
			fullFirstName = sPref.getInt(Key_003, -1);
			if (curFileId > filesCount || fullFirstName != filesList[0]) {
				curFileId = 0;
				fullFirstName = filesList[0];
			}
		}
	//	Log.d(tag, "pager begin");
		pager.setCurrentItem(curFileId);
	//	Log.d(tag, "pager end");

		if (listValue != "Start") {
			int mark = makeMark(listValue);
			addValueInDb(filesList[curFileId], mark, sarcasmValue);
		//	addValueInDb(curFileId, mark, sarcasmValue);
		}
		((Button)findViewById(R.id.btn3)).setText("Сарказм: " + isSarcasm(filesList[curFileId]) + "; "
	  			  + getMark(getValueFromDb(filesList[curFileId])) + "; "
	  	   + Integer.toString(curFileId) + "/" + Integer.toString(filesCount));
	/*	((Button)findViewById(R.id.btn3)).setText("Сарказм: " + isSarcasm(curFileId) + "; "
	  			  + getMark(getValueFromDb(curFileId)) + "; "
	  	   + Integer.toString(curFileId) + "/" + Integer.toString(filesCount));*/
		
		Log.d(tag, "superStart");
		super.onStart();
	}
	
	@Override	
	protected void onStop() {
		SharedPreferences sPref = getPreferences(MODE_PRIVATE);
		Editor editor = sPref.edit();
		editor.putFloat(Key_001, txtSize);
		editor.putInt(Key_002, curFileId);
		editor.putInt(Key_003, fullFirstName);
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
			return filesCount;
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
		if (mark.equals("Удалить"))
			markInt = -6;
		else {
			markInt = (int) (mark.charAt(1)) - (int)'0';
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
		sarcasmValue = data.getIntExtra(ExpandedListActivity.Sarcasm_Value, 2);
	/*	if (pager.getCurrentItem() != allCountFiles) {
			pager.setCurrentItem(pager.getCurrentItem() + 1);
		}*/
	}
	
	private String isSarcasm(int id) {
		String selection = "id = " + id;
		Cursor c = db.query(My_Table, null, selection, null, null, null, null);
		if (c.moveToFirst()) {
			int sarcasmColIndex = c.getColumnIndex("sarcasm");
			int sarcasmValue = c.getInt(sarcasmColIndex);
	 		if (sarcasmValue == 1)
				return "Да";
			else
				if (sarcasmValue == 0)
					return "Нет";
				else
					return "?";
		}		
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
	private void addValueInDb(int id, int mark, int sarcasm) {
		int c = getValueFromDb(id);
		String isSarc = isSarcasm(id);
		int sarc = isSarc.equals("Да") ? 1 : isSarc.equals("Нет") ? 0 : 2;
		if (c == mark && sarc == sarcasm)
			return;
		if (c != VALUE_NOT_FOUND)
			db.delete(My_Table, "id = " + id, null);
		ContentValues cv = new ContentValues();
		cv.put("id", id);
		cv.put("mark", mark);
		cv.put("sarcasm", sarcasm);
		db.insert(My_Table, null, cv);
	}
	
}

class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context) {
		super(context, "myDB", null, 5);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
	//	Log.d("MyTags", "--- OnCreate DB ---");
	//	db.execSQL("drop table " + ActivityWithText.My_Table);
		db.execSQL("create table " + ActivityWithText.My_Table + " (" +
				"id integer primary key," +
				"mark tinyint" +
				"sarcasm tinyint DEFAULT 2" + ");");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	/*	if (oldVersion < 3 && newVersion == 3) {
			db.execSQL("alter table bash_table add column sarcasm tinyint DEFAULT 2"); //0 - no, 1 - yes, 2 - ?
		}*/
	}
}