package ru.android.bashviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	private final String tag = "MyTags";
	
	final int SIZE_10 = 0;
	final int SIZE_12 = 1;
	final int SIZE_14 = 2;
	final int SIZE_16 = 3;	
	final int SIZE_18 = 4;	
	static float txtSize = 14;
	
	private int allCountFiles = 0;
	private int curFileId = 1;
	private String listValue = "Start";

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
		    	  String mark = "?";
		    	  //getting mark from DB 
		    	  curFileId = position;
		    	  btn3.setText(mark + "; ќценить;" + " " + Integer.toString(position) + "/" + "10");
		      }

		      @Override
		      public void onPageScrolled(int position, float positionOffset,
		          int positionOffsetPixels) {
		      }

		      @Override
		      public void onPageScrollStateChanged(int state) {
		      }
		    });
		
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
			// adding result to databases
		}
		String mark = "?";
		// getting mark
		((Button)findViewById(R.id.btn3)).setText(mark + "; ќценить;" + " " + Integer.toString(curFileId) + "/" + "10" + ";");
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
			return 6;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getApplicationContext(), ExpandedListActivity.class);
		startActivityForResult(intent, 1);
		((Button)findViewById(R.id.btn3)).setText("123");
		//”же обновленный listValue
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		listValue = data.getStringExtra(ExpandedListActivity.List_Value);	
	}
}
