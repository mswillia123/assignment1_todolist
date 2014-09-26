package com.example.mswillia_notes002;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SummaryActivity extends Activity {
	
/*
Summarize
total number of TODO items checked
total number of TODO items left unchecked
total number of archived TODO items
	total number of checked archived TODO items
	total number of unchecked archived TODO items(non-Javadoc)
*/
	private TodoList todolist;
	private TodoList todoarchive;
	
	TextView textview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		Intent intent = getIntent();
		todolist = (TodoList) intent.getSerializableExtra("todolist");
		todoarchive = (TodoList) intent.getSerializableExtra("todoarchive");
		
	}
	
	protected void onStart() {
		super.onStart();
		
		TextView newtext1 = (TextView) findViewById(R.id.checkedtext);
		TextView newtext2 = (TextView) findViewById(R.id.uncheckedtext);
		TextView newtext3 = (TextView) findViewById(R.id.archivedtext);
		TextView newtext4 = (TextView) findViewById(R.id.archivedcheckedtext);
		TextView newtext5 = (TextView) findViewById(R.id.archiveduncheckedtext);
		
		ArrayList<Todo> list = todolist.getTodoList();
		int size = todolist.listSize();
		int checked = 0;
        for (int i = 0; i < size; i++) {
            if (list.get(i).getTodoChecked()) {
            	checked += 1;
            }
        }
        newtext1.setText(String.valueOf(checked));
        newtext2.setText(String.valueOf(size - checked));
        
        checked = 0;
        list = todoarchive.getTodoList();
        size = todoarchive.listSize();
        
        for (int i = 0; i < size ; i++) {
            if (list.get(i).getTodoChecked()) {
            	checked += 1;
            }
        }
        newtext3.setText(String.valueOf(size));
        newtext4.setText(String.valueOf(checked));
        newtext5.setText(String.valueOf(size - checked));
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*
		if (id == R.id.action_settings) {
			return true;
		}
		*/
		return super.onOptionsItemSelected(item);
	}
	
	public void returnToList(MenuItem menu) {
		this.finish();
	}
}
