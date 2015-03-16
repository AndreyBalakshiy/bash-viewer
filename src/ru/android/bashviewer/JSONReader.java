package ru.android.bashviewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class JSONReader {
	private static String readAll(File file) {
		final StringBuilder sb = new StringBuilder();

		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
		try {
			int c = 0;
			while ((c = reader.read()) != -1) {
				sb.append((char)c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static JSONObject read(File file) throws IOException, JSONException {
		final String stringsForJson = readAll(file);
		JSONObject json = new JSONObject(stringsForJson);
		return json;
	}
}
