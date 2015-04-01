package ru.android.bashviewer;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {
	private final String tag = "MyTags";
	private final static String  Page_Num = "Page_Num";
	
	public static File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
	private JSONObject json;
	
	private int pageId = 0;
	
	static PageFragment newInstance(int id) {
		PageFragment pageFragment = new PageFragment();
		Bundle arg = new Bundle();
		arg.putInt(Page_Num, id + 1);
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
		
		TextView textView = (TextView)view.findViewById(R.id.txtView);
    	registerForContextMenu(textView);
    	textView.setTextSize(ActivityWithText.txtSize);
		
		String text = "ERROR";
		try {
		//	text = loadBashText(pageId);
			text = loadBashText(ActivityWithText.filesList[pageId - 1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textView.setText(text);
		return view;
		
	}
	
	private String loadBashText(int fileId) throws JSONException {
		File sdPath = Environment.getExternalStorageDirectory();
	    // добавляем свой каталог к пути
	    sdPath = new File(sdPath.getAbsolutePath() + File.separator + "Bashes" + File.separator);
	    // формируем объект File, который содержит путь к файлу
	    File sdFile = new File(sdPath, Integer.toString(fileId));
		try {
			json = JSONReader.read(sdFile);
		} catch (IOException e1) {
			Log.d(tag, "read IOE");
			e1.printStackTrace();
		} catch (JSONException e1) {
			Log.d(tag, "read JSONE");
			e1.printStackTrace();
		}
		return (String) json.get("text");
	}
	
}
