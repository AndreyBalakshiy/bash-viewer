package ru.android.bashviewer;

import android.app.Activity;
import android.os.Bundle;

public class ActivityWithText extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withtext);
	}
}
