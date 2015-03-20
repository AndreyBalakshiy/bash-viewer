package ru.android.bashviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {
	private final static String  Page_Num = "Page_Num";
	
	private int pageId = 1;
	
	static PageFragment newInstance(int id) {
		PageFragment pageFragment = new PageFragment();
		Bundle arg = new Bundle();
		arg.putInt(Page_Num, id);
		pageFragment.setArguments(arg);
		return pageFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageId = getArguments().getInt(Page_Num);
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment, null);
		TextView textView = (TextView)view.findViewById(R.id.tvPage);
		String text = Integer.toString(pageId);
		textView.setText(text);
		return view;
		
	}
}
