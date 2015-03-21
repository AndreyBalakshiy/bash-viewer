package ru.android.bashviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ExpandedListActivity extends Activity {
	static final String List_Value = "List_Value";
	
	ExpandableListView elvMain;
	AdapterExpandedListHelper ah;
	SimpleExpandableListAdapter adapter;
	TextView tvInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expanded_list);
        
		// ������� �������
		ah = new AdapterExpandedListHelper(this);
		adapter = ah.getAdapter();

		elvMain = (ExpandableListView) findViewById(R.id.elvMain);
		elvMain.setAdapter(adapter);

		// ������� �� �������
		elvMain.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent intent = new Intent();
				intent.putExtra(List_Value,
						ah.getGroupChildText(groupPosition, childPosition));
			    setResult(RESULT_OK, intent);
			    finish();
				return false;
			}
		});
	}
}