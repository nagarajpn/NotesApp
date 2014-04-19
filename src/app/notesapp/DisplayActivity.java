package app.notesapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class DisplayActivity extends ActionBarActivity {
	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.display_activity);
		
		TextView title = (TextView) findViewById(R.id.textView1);
		TextView note = (TextView) findViewById(R.id.textView2);
		
		BufferedReader reader = null;
		String eol = System.getProperty("line.separator");
		
		Intent i = getIntent();
		title.setText(i.getStringExtra("TITLE"));
		
		try{
			reader = new BufferedReader(new InputStreamReader(openFileInput(i.getStringExtra("FILENAME"))));
			StringBuffer acc_note = new StringBuffer();
			String temp = null;
			while((temp = reader.readLine()) != null){
				acc_note.append(temp);	
			}
		    note.setText(acc_note);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if (reader!=null){
				try{
					reader.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
