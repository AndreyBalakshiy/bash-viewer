package ru.android.bashviewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils.StringSplitter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ActivityWithText extends Activity {
	private final String Key_001 = "TEXT_SIZE";
	private final String Key_002 = "FILE_NAME";
	private final String tag = "MyTags";
	
	final int SIZE_10 = 0;
	final int SIZE_12 = 1;
	final int SIZE_14 = 2;
	final int SIZE_16 = 3;	
	final int SIZE_18 = 4;	
	private float txtSize = 14;
	
	private int allCountFiles = 0;
	private String curFileName = "";
	
	private JSONObject json;
	private TextView txtView;
	
	public static File EXTERNAL_DIR = Environment.getExternalStorageDirectory();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withtext);
		
		txtView = (TextView)findViewById(R.id.txtView);
		registerForContextMenu(txtView);
	//	Log.d(tag, "create");
	}	
	@Override
	protected void onStart() {
		Log.d(tag, "start");
		
		//узнать сколько всего файлов
		
		SharedPreferences sPref = getPreferences(MODE_PRIVATE);
		txtSize = sPref.getFloat(Key_001, SIZE_14);
		txtView.setTextSize(txtSize);
		
		curFileName = sPref.getString(Key_002, "1");
		loadBashText(curFileName);
		
		super.onStart();
		//load previus information
	}
	
	@Override	
	protected void onStop() {
		SharedPreferences sPref = getPreferences(MODE_PRIVATE);
		Editor editor = sPref.edit();
		editor.putFloat(Key_001, txtSize);
		editor.putString(Key_002, curFileName);
		editor.commit();
		super.onStop();
		//saved current state
	}
	
	private void refreshStatusBar() {
		
	}
	
	@Override 
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.txtView) {
			menu.add(0, SIZE_10, 0, "10");
			menu.add(0, SIZE_12, 1, "12");
			menu.add(0, SIZE_14, 2, "14");
			menu.add(0, SIZE_16, 3, "16");
			menu.add(0, SIZE_18, 4, "18");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
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
		
		txtView.setTextSize(txtSize);

		return super.onMenuItemSelected(featureId, item);
	}

	private void loadBashText(String fileName) {
		File sdPath = Environment.getExternalStorageDirectory();
	    // добавляем свой каталог к пути
	    sdPath = new File(sdPath.getAbsolutePath() + File.separator + "Data" + File.separator);
	    // формируем объект File, который содержит путь к файлу
	    File sdFile = new File(sdPath, fileName);
		try {
			json = JSONReader.read(sdFile);
		} catch (IOException e1) {
			Log.d(tag, "read IOE");
			e1.printStackTrace();
		} catch (JSONException e1) {
			Log.d(tag, "read JSONE");
			e1.printStackTrace();
		}
		try {
			txtView.setText((CharSequence) json.get("text"));
		} catch (JSONException e) {
			Log.d(tag, "SetText JSONE");
			e.printStackTrace();
		}
	}
	
	private void setCurFileName(int id) {
		curFileName = Integer.toString(id);
	}
	
	private void nextBashText() {
		int nextId = Integer.getInteger(curFileName);
		nextId++;
		//Обновить статус бар
		setContentView(nextId);
		loadBashText(curFileName);
	}
	
	private void prevBashText() {
		int nextId = Integer.getInteger(curFileName);
		nextId = Math.max(nextId - 1, 1);
		//Обновить статус бар
		setContentView(nextId);
		loadBashText(curFileName);		
	}
}
