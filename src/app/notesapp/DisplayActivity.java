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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayActivity extends ActionBarActivity {
	private String title;
	private String id;
	
	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.display_activity);
		
		TextView titleView = (TextView) findViewById(R.id.textView1);
		TextView noteView = (TextView) findViewById(R.id.textView2);
		
		BufferedReader reader = null;
		String eol = System.getProperty("line.separator");
		
		Intent i = getIntent();
		
		setTitle(i.getStringExtra("TITLE"));
		setId(i.getStringExtra("FILENAME"));
		
		titleView.setText(title);
		
		try{
			reader = new BufferedReader(new InputStreamReader(openFileInput(id)));
			StringBuffer acc_note = new StringBuffer();
			String temp = null;
			while((temp = reader.readLine()) != null){
				acc_note.append(temp+eol);	
			}
		    noteView.setText(acc_note);
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
	
	private void setTitle(String title){
		this.title = title;
	}
	
	private void setId(String id){
		this.id = id;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.display_activity_actionbar, menu);
	    return true;   
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.edit_note:
	            openEditActivity();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void openEditActivity(){
		Intent i = new Intent(this, EditActivity.class);
		i.putExtra("TITLE", title);
		i.putExtra("FILENAME", id);
		i.putExtra("CALLING_ACTIVITY",ActivityConstants.DISPLAY_ACTIVITY);
		startActivity(i);
	}
}
