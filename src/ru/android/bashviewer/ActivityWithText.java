package ru.android.bashviewer;

import android.app.Activity;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityWithText extends Activity {
	final int SIZE_10 = 0;
	final int SIZE_12 = 1;
	final int SIZE_14 = 2;
	final int SIZE_16 = 3;	
	final int SIZE_18 = 4;	
	
	private TextView txtView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withtext);
		
		txtView = (TextView)findViewById(R.id.txtView);
		registerForContextMenu(txtView);
	}	
	@Override
	protected void onStart() {
		super.onStart();
		//load previus information
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//saved current state
	}
	
	@Override 
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.txtView) {
			menu.add(0, SIZE_10, 0, "10");
			menu.add(0, SIZE_12, 0, "12");
			menu.add(0, SIZE_14, 0, "14");
			menu.add(0, SIZE_16, 0, "16");
			menu.add(0, SIZE_18, 0, "18");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int newSize = 20;
		switch(item.getItemId()) {
		case SIZE_10:
			newSize = 10;
			break;
		case SIZE_12:
			newSize = 12;
			break;
		case SIZE_14:
			newSize = 14;
			break;
		case SIZE_16:
			newSize = 16;
			break;	
		case SIZE_18:
			newSize = 18;
			break;	
		}
		
		txtView.setTextSize(newSize);

		return super.onMenuItemSelected(featureId, item);
	}

}
