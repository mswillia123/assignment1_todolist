package com.example.mswillia_notes002;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;
import java.util.List;

import android.widget.AbsListView.MultiChoiceModeListener;
 
public class MainActivity extends Activity {
 
	/*
	 * MainActivity is the primary view
	 * Active todos and archived todos are stored in two TodoList objects.
	 * Todo list mode - the application starts in this mode.
	 * List mode has a menu, with summary, and view archive/todo list options.
	 * Selection mode - a long press/click on any of the todo items activates context action bar selection mode, and allows
	 * 	for edit, delete, archive/unarchive, and email functions on the selected items
	 * 
	 * 	
	 * Primary functions:
	 * setOnItemClickListener():
	 * 	Monitors clicks on individual todos in list
	 *        
	 *setMultiChoiceModeListener():
	 *  Long press on a ListView item activates multiple selection listener, with context action bar menu.
	 * 	Implements all edit, delete, archive/unarchive, and email functions.
	 * 
	 * addTodo():
	 * 	Implements addition of Todo item to todo list, as well as saving edited todo items.
	 * 
	 * summary():
	 * 	Activates the summary activity via an intent
	 * 
	 * Design rationale: to prevent redundant code and an additional activity class, both lists are implemented
	 * 	within the same view, via swapping of todo list objects into the ListView adapter
	 * 
	 * Source code attribution:
	 * 	setMultiChoiceModeListener() section (and associated ListViewAdapter class) partially based on tutorial code:
	 * 		http://www.androidbegin.com/tutorial/android-delete-multiple-selected-items-listview-tutorial/
	 * 	setOnItemClickListener() section based on tutorial code:
	 *		http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html
	 * 	email intent code based upon instructor recommended link:
	 * 		http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
	 */
	

    private ListView list;					//UI view containing the todo list to be displayed
    private ListViewAdapter listviewadapter;//binds the UI list view to the currently selected list (todo or archive)
    private TodoList todoList;				//active todo list: TodoList object consisting of multiple Todo objects
    private TodoList todoArchive;			//archived todo list: TodoList object consisting of multiple Todo objects
    private CombinedList cl;				//CombinedList object consisting of both todo and archive lists, used 
    										//	primarily for file load and save operations    
    boolean toggleViewMode; 				//toggle alternates visible todo list based on user menu selection
    										//	false = Active todo list view, true = Archived todo list view

	private InterfaceFileManager todoFileManager; 	//interface for file manager object, instantiated via 
													//TodoListFileManager class
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
       
        setContentView(R.layout.listview_main); // main layout view from listview_main.xml
        
        //Variable initialization
        cl = new CombinedList();
        todoFileManager = new TodoListFileManager(this);
        todoList = new TodoList();
        todoArchive = new TodoList();
        toggleViewMode = false;        

        list = (ListView) findViewById(R.id.listViewArchive); //ListView that displays current todo list
 
        // Bind active todo list to list view
        listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoList.getTodoList());
        list.setAdapter(listviewadapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL); //enable multiple selections within list

        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	
        	// Listener observing clicks on list items
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		// determine whether clicked list item has checkbox checked, and toggle state
            	boolean flag = listviewadapter.getTodoList().get(position).getTodoChecked();
            	listviewadapter.getTodoList().get(position).setTodoChecked(!flag);
            	
            	//persist changed state to storage via TodoListFileManager class object
                cl = new CombinedList();            
            	cl.setTodolist(todoList);
            	cl.setTodoarchive(todoArchive);
                todoFileManager.saveTodoList(cl);           
                //update UI list view and bound data set to reflect checked item changed state
        		//list.invalidateViews();
        		listviewadapter.notifyDataSetChanged();
        	}
                
        });

        
        // Long press on a ListView item activates multiple selection listener, with context action bar menu
        list.setMultiChoiceModeListener(new MultiChoiceModeListener() {   	
        	
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Total number of selected list items
                final int checkedCount = list.getCheckedItemCount();
                // context action bar indicates number of items selected
                mode.setTitle(checkedCount + " Selected");
                
                listviewadapter.toggleSelection(position);
                listviewadapter.getTodoList().get(position).setTodoChecked(true); //retrieve checked state from list view
                
                // addButton performs dual function:
                //	1. when editing existing todo text, button text displays "Save" and saves edited text back to list item
                //	2. all other occasions button text displays "Add" and adds a new todo item to the list
                // editing of todo text is only available when a single list item is selected
                Button addButton = (Button) findViewById(R.id.addButtonArchive);
            	EditText editText = (EditText) findViewById(R.id.editTodoText);            	
                if (checkedCount != 1) {                	                	
                		addButton.setText("Add");
                		editText.setText("");                		                	                	
                }

               mode.invalidate(); // force call to onPrepareActionMode(), which updates list view in UI
            }
 
            // select action based on user selection from context action bar menu
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            	
            	SparseBooleanArray selected = listviewadapter.getSelectedIds(); // get list of all currently selected list view items       	          	
            	boolean email_all = false;  
            	
            	// context action bar menu selection
                switch (item.getItemId()) {
                

                case R.id.edit:                	
                    // menu selection: edit existing todo item text
                    // option only available when a single list item is selected
                	Button editButton = (Button) findViewById(R.id.addButtonArchive);
                	EditText editText = (EditText) findViewById(R.id.editTodoText);
                	
                	if (selected.size() == 1 ){ // single list item must be selected to have edit option
	                	editButton.setText("Save"); // change button text to "Save", to save edited text
	                	Todo selection = listviewadapter.getItem(selected.keyAt(0));
	                	editText.setText(selection.getTodoText().toString()); //copy todo text to TextEdit field for editing
	                	return true;
                	}
                	return false;
                	
                	case R.id.delete:
	                	// menu selection: remove selected items from list view and associated TodoList object
	                    // loop through selected items and remove
	                    for (int i = (selected.size() - 1); i >= 0; i--) {
	                        if (selected.valueAt(i)) {
	                            Todo selecteditem = listviewadapter.getItem(selected.keyAt(i));
	                            listviewadapter.remove(selecteditem);
	                        }
	                    }
	                    
	                    //persist changed state to storage via TodoListFileManager class object
	                    cl = new CombinedList();
	                	cl.setTodolist(todoList);
	                	cl.setTodoarchive(todoArchive);
	                    todoFileManager.saveTodoList(cl);
	                    mode.finish(); // exit context action bar mode
	                    return true;
	                    
                case R.id.archive:
                	// menu selection: Archive/Unarchive: Move all selected items from currently visible
                	//	todo list to inactive list (menu item text changes from "Archive" to "Unarchive"
                	//	based upon currently active list
                	//	loop through selected items and move from one list to the other
                	for (int i = (selected.size() - 1); i >= 0; i--) {
                        if (selected.valueAt(i)) {
                            Todo selecteditem = listviewadapter.getItem(selected.keyAt(i));
                            listviewadapter.remove(selecteditem);                           
                            if (toggleViewMode){
                            	todoList.addTodo(selecteditem);
                            } else {
                            	todoArchive.addTodo(selecteditem);
                            }
                        }                        
                    } 
                	//persist changed state to storage via TodoListFileManager class object
                    cl = new CombinedList();
                	cl.setTodolist(todoList);
                	cl.setTodoarchive(todoArchive);
                    todoFileManager.saveTodoList(cl);
                    mode.finish(); // exit context action bar mode
                    return true;
                    
                case R.id.emailall:
                	// menu selection: email all todo and archive list items
        			email_all = true; //flag set, case falls through to case:email for email intent handling
                case R.id.email:
                	// menu selection: email selected items
            		if (selected.size() <= 0) { 
            			Toast.makeText(MainActivity.this, "No items selected", Toast.LENGTH_SHORT).show();
            			return false;
        			}

            		// initialize email intent and string to store todo items
            		Intent intent = new Intent(Intent.ACTION_SEND);            		         		
            		String fullstring = "Todo list (items: + checked, - unchecked)\n\n";
            		// append todo items as strings
            		if (email_all) { // email all option - all items on both lists
            			Toast.makeText(MainActivity.this, "Emailing All", Toast.LENGTH_SHORT).show();
            			for (Todo todo: todoList.getTodoList()){
            				if (todo.getTodoChecked()) { fullstring += "+ ";
                			} else { fullstring += "- ";}
            				fullstring += todo.getTodoText() + "\n";            				
            			}
            			fullstring += "\nArchived todos:\n";
            			for (Todo todo: todoArchive.getTodoList()){
            				if (todo.getTodoChecked()){ fullstring += "+ ";
                			} else { fullstring += "- ";
                			}
            				fullstring += todo.getTodoText() + "\n";            				
            			}
            		} else { // email selected option - only selected items in currently visible list
	            		for(int i = 0; i < selected.size(); i++) 
	            		{	                    	
	            			Todo todo = listviewadapter.getItem(selected.keyAt(i));
	            			if (todo.getTodoChecked()){
	            				fullstring += "+ ";
	            			} else { 
	            				fullstring += "- ";
	            			}
	            			fullstring += todo.getTodoText() + "\n";
	            		}
            		}
            		
            		intent.setType("message/rfc822");
            		intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
            		intent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            		intent.putExtra(Intent.EXTRA_TEXT   , fullstring);
            		try {
            		    startActivity(Intent.createChooser(intent, "Send mail..."));
            		} catch (android.content.ActivityNotFoundException ex) {
            		    Toast.makeText(MainActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            		}          	
                	return true;

                default:
                    return false;
                }
            }
 
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            	//intialize context action mode menu
                mode.getMenuInflater().inflate(R.menu.activity_main, menu);                              
                return true;
            }
 
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                listviewadapter.removeSelection();
            }
 
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            	// called to refresh context action mode menu
        		final int checkedCount = list.getCheckedItemCount(); // list of selectes list view items
        		menu.clear();
        		mode.getMenuInflater().inflate(R.menu.activity_main, menu);
        		
        		// hide edit option if more than single list item selected
        		MenuItem item = menu.findItem(R.id.edit);        	    
        	    if (checkedCount == 1) { 
        	    	item.setVisible(true);
        	    } else {
        	    	item.setVisible(false);
        	    }
        	    // toggle archive/unarchive menu option based on visible todo list
        	    item = menu.findItem(R.id.archive);        	    
        	    if (toggleViewMode){
        	    	item.setTitle("Unarchive");
        	    } else{
        	    	item.setTitle("Archive");
        	    }     	    
        	    return true;
            }
        }); // end of setMultiChoiceListener()
       	
    } // end of onCreate()
    
   
	@Override
	protected void onStart() {
		super.onStart();
		
		// load persistent CombinedList (contains todo list and archive list)
		CombinedList cl = new CombinedList();
		cl = todoFileManager.loadTodoList(); 
		todoList = cl.getTodolist();
		todoArchive = cl.getTodoarchive();

		// set ListViewAdapter to display active todo list on startup
		listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoList.getTodoList());
		list.setAdapter(listviewadapter);
		
	}
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void toggleListView(MenuItem menu) {

		// when called, swap the active todo list and the archive todo list into the ListViewAdaper
		// for display in UI

		toggleViewMode =  !toggleViewMode;
		if (toggleViewMode) {
			listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoArchive.getTodoList());
			setTitle("Archived Todo");
			menu.setTitle("View Todo");
		} else {
			listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoList.getTodoList());
			setTitle("Todo List");
			menu.setTitle("View Archive");
		}		
		list.setAdapter(listviewadapter);	
	}
	
	public void summary(MenuItem menu) {
		//set intent to SummaryActivity and start summary activity, passing todo and archive lists
		Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
		intent.putExtra("todolist", todoList);
		intent.putExtra("todoarchive", todoArchive);
		startActivity(intent);
		
	}
	
	
    public void addTodo(View v) {
    	
    	// if edit mode is active (button indicates "Save") save EditText text to currently displayed
    	// todo list, otherwise add a new todo to the list
		EditText newtext = (EditText) findViewById(R.id.editTodoText);
		Button editButton = (Button) findViewById(R.id.addButtonArchive);
		
		if (editButton.getText().toString() == "Save" && list.getCheckedItemCount() == 1) {
			// save edit text field text to selected list item
			SparseBooleanArray selected = listviewadapter.getSelectedIds();
			listviewadapter.getItem(selected.keyAt(0)).setTodoText(newtext.getText().toString());
			listviewadapter.notifyDataSetChanged();

		} else {
			//otherwise add a new todo item	to the current list
			if (newtext.length() > 0 ) {
				Todo newtodo = new Todo(false, newtext.getText().toString());
				listviewadapter.add(newtodo);
				newtext.setText("");
				listviewadapter.notifyDataSetChanged();
			}
		}
		//persist changed state to storage via TodoListFileManager class object
		cl = new CombinedList();
    	cl.setTodolist(todoList);
    	cl.setTodoarchive(todoArchive);
        todoFileManager.saveTodoList(cl);
    }
    
}