package app.notesapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;import android.support.v7.app.ActionBarActivity;import android.support.v7.app.ActionBarActivity;import android.support.v7.app.ActionBarActivity;import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity{
	@Override
	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		
		//init();
		
		SharedPreferences sp = getSharedPreferences("MAIN_LIST",0);
		int LIST_LENGTH = sp.getInt("TOTAL_ITEMS", 0);
		
		if (LIST_LENGTH > 0){
			setContentView(R.layout.main_activity);
			final ListView listview = (ListView) findViewById(R.id.listview);
			final Note[] items = new Note[LIST_LENGTH];
		
			getList(items);
			final ArrayAdapter adapter = new ArrayAdapter(this,R.layout.list,items);
			listview.setAdapter(adapter);
		
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//final String title = (final) items[position-1].getTitle();
					//final String id = items[position-1].getId();
					//Toast.makeText(getApplicationContext(),((TextView) view).getText(), Toast.LENGTH_SHORT).show();
					Intent i = new Intent(MainActivity.this, DisplayActivity.class);
					i.putExtra("TITLE", items[position].getTitle());
					i.putExtra("FILENAME", items[position].getId());
					startActivity(i);
				}
			});
		}
		else{
			setContentView(R.layout.main_activity_empty);
			TextView empty = (TextView) findViewById(R.id.no_list);
			empty.setText("No notes to display");
			String a = getFilesDir().list().toString();
		}
	}
		
		
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actionbar, menu);
	    return true;   
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.new_note:
			openEditActivity();
			return true;
		default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	public void openEditActivity(){
		Intent intent = new Intent(this, EditActivity.class);
		intent.putExtra("CALLING_ACTIVITY",ActivityConstants.MAIN_ACTIVITY);
		startActivity(intent);
	}
	
	private void getList(Note[] items){
		BufferedReader reader =null;
		//String eol = System.getProperty("line.separator");
		//Toast.makeText(getApplicationContext(),Integer.toString(items.length), Toast.LENGTH_SHORT).show();

		try{
			reader = new BufferedReader(new InputStreamReader(openFileInput("NOTES_LIST")));
			
			for ( int i=0; i<items.length; i++) {
				//Toast.makeText(getApplicationContext(),"before note creation", Toast.LENGTH_SHORT).show();
				items[i]=new Note();
				//Toast.makeText(getApplicationContext(),"after note creation", Toast.LENGTH_SHORT).show();
				items[i].setId(reader.readLine());
				items[i].setTitle(reader.readLine());
				//Toast.makeText(getApplicationContext(),items[i].getTitle(), Toast.LENGTH_SHORT).show();
				}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if (reader != null) {
			    try {
			    reader.close();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			}
		}
		//Toast.makeText(getApplicationContext(),items[0].getTitle(), Toast.LENGTH_SHORT).show();
	}
	
	public class Note{
		String id;
		String title;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
		@Override
		public String toString(){
			return (this.title);
		}
	}
	
	public void sampleInputs(){
		SharedPreferences sp = getPreferences(0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("TOTAL_ITEMS", 1);
		editor.commit();
		
		storeItem("1","Item1");
		
	}
	
	public void storeItem(String id, String title){
		BufferedWriter writer=null;
		String eol = System.getProperty("line.separator");
		
		try{
			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("NOTES_LIST",Context.MODE_PRIVATE)));
			
		    writer.write(id + eol);
		    writer.write(title + eol);
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
	}
}