package ru.android.bashviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ExpandedListActivity extends Activity {
	static final String List_Value = "List_Value";
	static final String Sarcasm_Value = "Sarcasm_Value";
	
	ExpandableListView elvMain;
	AdapterExpandedListHelper ah;
	SimpleExpandableListAdapter adapter;
	TextView tvInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expanded_list);
        
		// создаем адаптер
		ah = new AdapterExpandedListHelper(this);
		adapter = ah.getAdapter();

		elvMain = (ExpandableListView) findViewById(R.id.elvMain);
		elvMain.setAdapter(adapter);

		// нажатие на элемент
		elvMain.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent intent = new Intent();
				intent.putExtra(List_Value,
						ah.getGroupChildText(groupPosition, childPosition));
				CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox1);
				intent.putExtra(Sarcasm_Value, checkBox.isChecked() ? 1 : 0);
				
			    setResult(RESULT_OK, intent);
			    finish();
				return false;
			}
		});
	}
}