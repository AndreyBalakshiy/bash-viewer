package ru.android.bashviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ExpandedListActivity extends Activity {
	
	ExpandableListView elvMain;
	AdapterExpandedListHelper ah;
	SimpleExpandableListAdapter adapter;
	TextView tvInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expanded_list);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
        
		// создаем адаптер
		ah = new AdapterExpandedListHelper(this);
		adapter = ah.getAdapter();

		elvMain = (ExpandableListView) findViewById(R.id.elvMain);
		elvMain.setAdapter(adapter);

		// нажатие на элемент
		elvMain.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				tvInfo.setText(ah.getGroupChildText(groupPosition,
						childPosition));
				return false;
			}
		});
	}
}