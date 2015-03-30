package ru.android.bashviewer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private String tag = "MyTag";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button btn = (Button)findViewById(R.id.btn);
		btn.setOnClickListener(this);
		((Button)findViewById(R.id.btn2)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
	//	Log.d(tag, "create");
		switch(v.getId()) {
		case R.id.btn:
			Intent intent = new Intent(getApplicationContext(), ActivityWithText.class);
			startActivity(intent);
			break;
		case R.id.btn2:
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				Log.d(ActivityWithText.tag, "SD-карта не доступна: " + Environment.getExternalStorageState());
				return;
			}
			
			File sdPath = Environment.getExternalStorageDirectory();
			sdPath = new File(sdPath.getAbsolutePath() + "/" + "BD");
			sdPath.mkdirs();
			File sdFile = new File(sdPath, "BashBD");
			try {
				// открываем поток для записи
				BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
				// пишем данные
				writeToDataBase(bw);
	//			bw.write("Содержимое файла на SD");
				// закрываем поток
				bw.close();
				Log.d(ActivityWithText.tag, "Файл записан на SD: " + sdFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	private String My_Table = "bash_table";
	private void writeToDataBase(BufferedWriter bw) {
		DBHelper dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor c = db.query(My_Table, null, null, null, null, null, null);
		if (c.moveToFirst()) {
			int idColIndex = c.getColumnIndex("id");
			int markColIndex = c.getColumnIndex("mark");
			int sarcColIndex = c.getColumnIndex("sarcasm");
			do {
				try {
					bw.write(Integer.toString(c.getInt(idColIndex)) + " " + Integer.toString(c.getInt(markColIndex)) + " " + 
													Integer.toString(c.getInt(sarcColIndex)) + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} while (c.moveToNext());
		}
		c.close();
	}
}
