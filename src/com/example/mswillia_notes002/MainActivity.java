package com.example.mswillia_notes002;


import android.os.Bundle;
import android.app.Activity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.widget.AbsListView.MultiChoiceModeListener;
 
public class MainActivity extends Activity {
 
    // Declare Variables
    ListView list;
    ListViewAdapter listviewadapter;
    //ListViewAdapter archiveadapter;
    TodoList todoList;
    TodoList todoArchive;
    //TodoList inactiveList;
    //TodoList bothLists;
    
    boolean toggleViewMode; //false = Active todo list, true = Archived todo list

	InterfaceFileManager todoFileManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.listview_main);
 
 
        // Locate the ListView in listview_main.xml
        //ArrayList<Todo> todoList = new ArrayList<Todo>();
        todoList = new TodoList();
        todoArchive = new TodoList();
        //bothLists = new TodoList();
        toggleViewMode = false;
    	//bothLists = new TodoList();
			
    	//bothLists = todoFileManager.loadTodoList();

        //todoList.setTodoList(todoList) = bothLists.getTodoList();
        //todoArchive = bothLists.getTodoArchive();
        //inactiveList = todoArchive;
        list = (ListView) findViewById(R.id.listViewArchive);
 
        // Pass results to ListViewAdapter Class
        // Tie together array list and listview
        listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoList.getTodoList());

        // Binds the Adapter to the ListView
        list.setAdapter(listviewadapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // holder.flag.setOnClickListener(new View.OnClickListener() {
                 public void onItemClick(AdapterView<?> parent, View view,
                         int position, long id) {
            	boolean flag = listviewadapter.getTodoList().get(position).getFlag();
            	listviewadapter.getTodoList().get(position).setFlag(!flag);
                //Object o = list.getItemAtPosition(position);
                //prestationEco str=(prestationEco)o;//As you are using Default String Adapter
                Toast.makeText(MainActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                listviewadapter.notifyDataSetChanged();
                 }
                
             });

        
        // Capture ListView item click
        list.setMultiChoiceModeListener(new MultiChoiceModeListener() {   	
        	
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = list.getCheckedItemCount();
                // Set the CAB title according to total checked items
                //mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                
                listviewadapter.toggleSelection(position);
                listviewadapter.getTodoList().get(position).setFlag(true);
                Button addButton = (Button) findViewById(R.id.addButtonArchive);
            	EditText mText = (EditText) findViewById(R.id.editTodoText);
                if (checkedCount != 1) {
                	                	
                		addButton.setText("Add");
                		mText.setText("");
                		                	                	
                }

               mode.invalidate();
               //XXX todoFileManager.saveTodoList(bothLists); //save todo list on change
                
            }
 
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            	SparseBooleanArray selected = listviewadapter.getSelectedIds();
            	boolean email_all = false;
            	
                switch (item.getItemId()) {
                
                case R.id.edit:

                	Button mButton = (Button) findViewById(R.id.addButtonArchive);
                	EditText mText = (EditText) findViewById(R.id.editTodoText);
                	//Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                	if (selected.size() == 1 ){
	                	mButton.setText("Save");
	                	Todo selection = listviewadapter.getItem(selected.keyAt(0));
	                	mText.setText(selection.getRank().toString());
	                	return true;
                	}
                	
                	return false;
                	
                	case R.id.delete:
                    // Calls getSelectedIds method from ListViewAdapter Class
                    //SparseBooleanArray selected = listviewadapter
                    //        .getSelectedIds();
                    // Captures all selected ids with a loop
                    for (int i = (selected.size() - 1); i >= 0; i--) {
                        if (selected.valueAt(i)) {
                            Todo selecteditem = listviewadapter
                                    .getItem(selected.keyAt(i));
                            // Remove selected items following the ids
                            listviewadapter.remove(selecteditem);
                        }
                    }
                    // Close CAB
                    mode.finish();
                    return true;
                case R.id.archive:
                	// Archive/Unarchive: Move all selected items to currently inactive list

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
                	//listviewadapter.removeSelection();
                	//listviewadapter.notifyDataSetChanged();
                	
                	return true;
                case R.id.emailall:
        			email_all = true; //falls through to case:email to continue
                case R.id.email:
                	
            		if (selected.size() <= 0) { 
            			Toast.makeText(MainActivity.this, "No items selected", Toast.LENGTH_SHORT).show();
            			return false;
        			}
            		Toast.makeText(MainActivity.this, "Emailing", Toast.LENGTH_SHORT).show();
            		
            		//Bundle b = new Bundle();
            		//b.
            		
            		Intent intent = new Intent(Intent.ACTION_SEND);
            		
            		//intent.putExtra("todolist", todoList);
            		//intent.putExtra("todoarchive", todoArchive);
            		//startActivity(intent);
            		List<Todo> todolist = listviewadapter.getTodoList();
            		
            		String fullstring = "Todo list (items: + checked, - unchecked)\n\n";
            		//for (Todo todo : todolist)
            		if (email_all) {
            			Toast.makeText(MainActivity.this, "Emailing All", Toast.LENGTH_SHORT).show();
            			for (Todo todo: todoList.getTodoList()){
            				if (todo.getFlag()){
                				fullstring += "+ ";
                			} else { 
                				fullstring += "- ";
                			}
            				fullstring += todo.getRank() + "\n";
            				
            			}
            			fullstring += "\nArchived todos:\n";
            			for (Todo todo: todoArchive.getTodoList()){
            				if (todo.getFlag()){
                				fullstring += "+ ";
                			} else { 
                				fullstring += "- ";
                			}
            				fullstring += todo.getRank() + "\n";
            				
            			}
            		} else {
	            		for(int i = 0; i < selected.size(); i++) 
	            		{
	                    	
	            			Todo todo = listviewadapter.getItem(selected.keyAt(i));
	            			if (todo.getFlag()){
	            				fullstring += "+ ";
	            			} else { 
	            				fullstring += "- ";
	            			}
	            			fullstring += todo.getRank() + "\n";
	
	            		}
            		}
            		
            		intent.setType("message/rfc822");
            		intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
            		intent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            		intent.putExtra(Intent.EXTRA_TEXT   , fullstring);
            		try {
            		    startActivity(Intent.createChooser(intent, "Send mail..."));
            		} catch (android.content.ActivityNotFoundException ex) {
            		    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            		}
               	
                	
                	return true;

                default:
                    return false;
                }
            }
 
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                              
                return true;
            }
 
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                listviewadapter.removeSelection();
            }
 
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            	
            	//mode.getMenuInflater().inflate(R.menu.activity_main, menu);
        		final int checkedCount = list.getCheckedItemCount();
        		menu.clear();
        		mode.getMenuInflater().inflate(R.menu.activity_main, menu);
        	   //MenuInflater inflater = getMenuInflater();
        	   //inflater.inflate(R.menu.activity_main, menu);
        	    MenuItem item = menu.findItem(R.id.edit);
        	    //MenuItem item2 = menu.findItem(R.id.archive);
        	    //MenuItem item2 = menu.findItem(R.id.menu_save);
        	    if (checkedCount == 1) {
        	    item.setVisible(true);
        	    //item2.setVisible(true);
        	    } else {
        	    	item.setVisible(false);
        	    	//item2.setVisible(false);
        	    }
        	    
        	    return true;
            }
        });
       	
    }
    
    /*
	@Override
	protected void onStart() {
		super.onStart();

		//todoList = todoFileManager.loadTodoList();
		//todoArchive = todoFileManager.loadTodoArchive();
		//tweetsViewAdapter = new ArrayAdapter<Tweet>(this,
		//		R.layout.list_item, tweets);
		//oldTweetsList.setAdapter(tweetsViewAdapter);
	}
    */
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void toggleListView(MenuItem menu) {
		//Toast.makeText(this, "View Archive", Toast.LENGTH_SHORT).show();
		toggleViewMode =  !toggleViewMode;
		if (toggleViewMode) {
			listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoArchive.getTodoList());
			setTitle("Archived Todo");
			
		} else {
			listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoList.getTodoList());
			setTitle("Todo List");
		}
		
		list.setAdapter(listviewadapter);
		//listviewadapter.setToggleViewMode();
    	String str = String.valueOf(toggleViewMode);
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		//Intent intent = new Intent(MainActivity.this, ArchiveActivity.class);
		//startActivity(intent);
	}
	
	public void summary(MenuItem menu) {
		Toast.makeText(this, "Summarize", Toast.LENGTH_SHORT).show();
		
		//Bundle b = new Bundle();
		//b.
		
		Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
		intent.putExtra("todolist", todoList);
		intent.putExtra("todoarchive", todoArchive);
		startActivity(intent);
	};
	
	
    public void addTodo(View v) {		
		Toast.makeText(this, "Add a to-do", Toast.LENGTH_SHORT).show();
		EditText newtext = (EditText) findViewById(R.id.editTodoText);
		Button mButton = (Button) findViewById(R.id.addButtonArchive);
		
		if (mButton.getText().toString() == "Save" && list.getCheckedItemCount() == 1) {
			//change list item to reflect edit text
			//Toast.makeText(this, "Saving new entry", Toast.LENGTH_SHORT).show();
			SparseBooleanArray selected = listviewadapter.getSelectedIds();
			listviewadapter.getItem(selected.keyAt(0)).setRank(newtext.getText().toString());
			//Todo listitem = (Todo) listviewadapter.getItem(selected.keyAt(0));
			//listitem.setRank(newtext.getText().toString());
			//Toast.makeText(this, toString(listitem.getItem(selected.valueAt(0))), Toast.LENGTH_SHORT).show();
			listviewadapter.notifyDataSetChanged();

		} else {
			//otherwise add a new todo item	
			if (newtext.length() > 0 ) {
				Todo newtodo = new Todo(false, newtext.getText().toString());
				todoList.addTodo(newtodo);
				newtext.setText("");
			}
		
		}
    }
    
}