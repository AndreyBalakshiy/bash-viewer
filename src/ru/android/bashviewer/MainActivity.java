package ru.android.bashviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button btn = (Button)findViewById(R.id.btn);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getApplicationContext(), ActivityWithText.class);
		startActivity(intent);
	}	
}
