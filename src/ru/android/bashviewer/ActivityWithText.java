package ru.android.bashviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ActivityWithText extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withtext);
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
}
