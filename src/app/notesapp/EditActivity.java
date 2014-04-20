package app.notesapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends ActionBarActivity {
	boolean fromDisplayActivity = false;
	private String title;
	private String id;
	
	public void onCreate(Bundle savedBundleInstance){
		super.onCreate(savedBundleInstance);
		setContentView(R.layout.edit_activity);
		
		EditText titleView = (EditText) findViewById(R.id.editText1);
		EditText contentsView = (EditText) findViewById(R.id.editText2);
		
		Intent i = getIntent();
		if(i.getIntExtra("CALLING_ACTIVITY", 0) == ActivityConstants.DISPLAY_ACTIVITY){
			fromDisplayActivity = true;
			setTitle(i.getStringExtra("TITLE"));
			titleView.setText(title);
			
			setId(i.getStringExtra("FILENAME"));
		
			BufferedReader reader = null;
			String eol = System.getProperty("line.separator");
			
			try{
				reader = new BufferedReader(new InputStreamReader(openFileInput(id)));
				StringBuffer acc_note = new StringBuffer();
				String temp = null;
				while((temp = reader.readLine()) != null){
					acc_note.append(temp+eol);	
				}
			    contentsView.setText(acc_note);
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
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.edit_activity_actionbar, menu);
	    return true;   
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.save_note:
	        	if(fromDisplayActivity == true){
	        		saveNote();
	        		fromDisplayActivity = false;
	        	}
	        	else{
	        		saveNewNote();
	        	}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void saveNewNote(){
		EditText title = (EditText) findViewById(R.id.editText1);
		EditText contents = (EditText) findViewById(R.id.editText2);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String date = dateFormat.format(c.getTime());
		SharedPreferences sp = getSharedPreferences("MAIN_LIST",0);
		SharedPreferences.Editor editor = sp.edit();
		String old_date = sp.getString("CURRENT_DATE","0");
		int index = sp.getInt("FILE_INDEX",0);

		if(old_date.equals(date)){
			index++;
		}
		else{
			index=1;
		}
		
		String FILENAME = date + "-" + Integer.toString(index);
		BufferedWriter writer=null;
		String eol = System.getProperty("line.separator");
		
		try{
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("NOTES_LIST",Context.MODE_APPEND)));
		    
		}catch(IOException e){

			e.printStackTrace();
		}finally{
			if (writer!=null){
				try{
				    writer.write(FILENAME + eol);
				    writer.write(title.getText().toString() + eol);
					writer.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		try{
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILENAME,Context.MODE_PRIVATE)));
		    writer.write(contents.getText().toString() + eol);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if (writer!=null){
				try{
					writer.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		editor.putString("CURRENT_DATE", date);
		editor.putInt("FILE_INDEX", index);
		editor.putInt("TOTAL_ITEMS",sp.getInt("TOTAL_ITEMS", 0)+1);
		editor.commit();
		//Toast.makeText(getApplicationContext(),Integer.toString(sp.getInt("TOTAL_ITEMS", 0)), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	private void saveNote(){
		EditText title = (EditText) findViewById(R.id.editText1);
		EditText contents = (EditText) findViewById(R.id.editText2);
		
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
		String temp1 = acc_note.substring(0,acc_note.indexOf(id)+id.length()+1);
		String temp2 = acc_note.substring(acc_note.indexOf(id)+id.length()+1);
		temp2 = temp2.substring(temp2.indexOf(eol));
		
		BufferedWriter writer=null;
		
		try{
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("NOTES_LIST",Context.MODE_PRIVATE)));
		    
		}catch(IOException e){

			e.printStackTrace();
		}finally{
			if (writer!=null){
				try{
				    writer.write(temp1 + title.getText().toString() + temp2);
					writer.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		try{
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(id,Context.MODE_PRIVATE)));
		    writer.write(contents.getText().toString() + eol);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if (writer!=null){
				try{
					writer.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	private void setTitle(String title){
		this.title = title;
	}
	
	private void setId(String id){
		this.id = id;
	}
}