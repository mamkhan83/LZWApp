package com.example.lzwapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
{

	private EditText etEncodeStr;
	private EditText etDecodeStr;
	private Button btnEndcode;
	private Button btnDecode;

	private TextView tv1;

	List<Integer> integers;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etEncodeStr = (EditText) findViewById(R.id.editText1);
		etDecodeStr = (EditText) findViewById(R.id.editText2);

		btnEndcode = (Button) findViewById(R.id.button1);
		btnDecode = (Button) findViewById(R.id.button2);

		tv1 = (TextView) findViewById(R.id.tv1);

		btnEndcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				integers = compress(etEncodeStr.getText().toString());
				String str = "";
				for (int i = 0; i < integers.size(); i++)
				{
					str += integers.get(i);
				}

				tv1.setText(str);

			}
		});

		btnDecode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etDecodeStr.setText(decompress(integers));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/** Compress a string to a list of output symbols. */
	public static List<Integer> compress(String uncompressed) {
		// Build the dictionary.
		int dictSize = 256;
		Map<String, Integer> dictionary = new HashMap<String, Integer>();
		for (int i = 0; i < 256; i++)
			dictionary.put("" + (char) i, i);

		String w = "";
		List<Integer> result = new ArrayList<Integer>();
		for (char c : uncompressed.toCharArray()) {
			String wc = w + c;
			if (dictionary.containsKey(wc))
				w = wc;
			else {
				result.add(dictionary.get(w));
				// Add wc to the dictionary.
				dictionary.put(wc, dictSize++);
				w = "" + c;
			}
		}

		// Output the code for w.
		if (!w.equals(""))
			result.add(dictionary.get(w));
		return result;
	}

	/** Decompress a list of output ks to a string. */
	public static String decompress(List<Integer> compressed) {
		// Build the dictionary.
		int dictSize = 256;
		Map<Integer, String> dictionary = new HashMap<Integer, String>();
		for (int i = 0; i < 256; i++)
			dictionary.put(i, "" + (char) i);

		String w = "" + (char) (int) compressed.remove(0);
		StringBuffer result = new StringBuffer(w);
		for (int k : compressed) {
			String entry;
			if (dictionary.containsKey(k))
				entry = dictionary.get(k);
			else if (k == dictSize)
				entry = w + w.charAt(0);
			else
				throw new IllegalArgumentException("Bad compressed k: " + k);

			result.append(entry);

			// Add w+entry[0] to the dictionary.
			dictionary.put(dictSize++, w + entry.charAt(0));

			w = entry;
		}
		return result.toString();
	}

}
