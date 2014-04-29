package app.notesapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	        case R.id.delete_note_from_display:
	        	deleteNote();
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
	
	private void deleteNote(){
		Context c = this;
		c.deleteFile(id);
		
		BufferedReader reader = null;
		String eol = System.getProperty("line.separator");
		StringBuffer acc_note = new StringBuffer();
		
		try{
			reader = new BufferedReader(new InputStreamReader(openFileInput("NOTES_LIST")));
			String temp = null;
			while((temp = reader.readLine()) != null){
				acc_note.append(temp+eol);	
			}
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
		String temp1 = acc_note.substring(0,acc_note.indexOf(id));
		String temp2 = acc_note.substring(acc_note.indexOf(id)+id.length()+1);
		temp2 = temp2.substring(temp2.indexOf(eol)+1);
		
		BufferedWriter writer=null;
		
		try{
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("NOTES_LIST",Context.MODE_PRIVATE)));
		    
		}catch(IOException e){

			e.printStackTrace();
		}finally{
			if (writer!=null){
				try{
				    writer.write(temp1 + temp2);
					writer.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		SharedPreferences sp = getSharedPreferences("MAIN_LIST",0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("TOTAL_ITEMS",sp.getInt("TOTAL_ITEMS", 0)-1);
		editor.commit();
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
	}
}
