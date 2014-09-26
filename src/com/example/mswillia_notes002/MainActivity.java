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
 
    // Declare Variables
    private ListView list;
    private ListViewAdapter listviewadapter;
    //ListViewAdapter archiveadapter;
    private TodoList todoList;
    private TodoList todoArchive;
    private CombinedList cl;
    //TodoList inactiveList;
    //TodoList bothLists;
    
    boolean toggleViewMode; //false = Active todo list, true = Archived todo list

	private InterfaceFileManager todoFileManager;
    
    //TodoListFileManager todoFileManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.listview_main);
        cl = new CombinedList();
        todoFileManager = new TodoListFileManager(this);
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
       
        //XXX this portion from http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // holder.flag.setOnClickListener(new View.OnClickListener() {
                 public void onItemClick(AdapterView<?> parent, View view,
                         int position, long id) {
            	boolean flag = listviewadapter.getTodoList().get(position).getTodoChecked();
            	listviewadapter.getTodoList().get(position).setTodoChecked(!flag);
                //Object o = list.getItemAtPosition(position);
                //prestationEco str=(prestationEco)o;//As you are using Default String Adapter
                cl = new CombinedList();            
            	cl.setTodolist(todoList);
            	cl.setTodoarchive(todoArchive);
                todoFileManager.saveTodoList(cl);
                //listviewadapter.refresh(todoList.getTodoList());
                //listviewadapter.notifyDataSetChanged();
                
   
        		list.invalidateViews();
                //listviewadapter.refresh(todoList.getTodoList());

        		listviewadapter.notifyDataSetChanged();
                 }
                
             });
        //XXX end this portion from http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html
        
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
                listviewadapter.getTodoList().get(position).setTodoChecked(true);
                Button addButton = (Button) findViewById(R.id.addButtonArchive);
            	EditText mText = (EditText) findViewById(R.id.editTodoText);
                if (checkedCount != 1) {
                	                	
                		addButton.setText("Add");
                		mText.setText("");
                		                	                	
                }

               mode.invalidate();
               //XXX todoFileManager.saveTodoList(bothLists); //save todo list on change
               //todoFileManager.saveTodoList(todoList);
            }
 
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            	SparseBooleanArray selected = listviewadapter.getSelectedIds();
            	
            	
            	boolean email_all = false;
            	
                switch (item.getItemId()) {
                
                case R.id.edit:

                	Button mButton = (Button) findViewById(R.id.addButtonArchive);
                	EditText mText = (EditText) findViewById(R.id.editTodoText);
                	
                	if (selected.size() == 1 ){
	                	mButton.setText("Save");
	                	Todo selection = listviewadapter.getItem(selected.keyAt(0));
	                	mText.setText(selection.getTodoText().toString());
	                	return true;
                	}
                	
                	return false;
                	
                	case R.id.delete:
                	//XXX
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
                    cl = new CombinedList();
                	cl.setTodolist(todoList);
                	cl.setTodoarchive(todoArchive);
                    todoFileManager.saveTodoList(cl);
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

            		Intent intent = new Intent(Intent.ACTION_SEND);            		
            		//List<Todo> todolist = listviewadapter.getTodoList();            		
            		String fullstring = "Todo list (items: + checked, - unchecked)\n\n";

            		if (email_all) {
            			Toast.makeText(MainActivity.this, "Emailing All", Toast.LENGTH_SHORT).show();
            			for (Todo todo: todoList.getTodoList()){
            				if (todo.getTodoChecked()){
                				fullstring += "+ ";
                			} else { 
                				fullstring += "- ";
                			}
            				fullstring += todo.getTodoText() + "\n";
            				
            			}
            			fullstring += "\nArchived todos:\n";
            			for (Todo todo: todoArchive.getTodoList()){
            				if (todo.getTodoChecked()){
                				fullstring += "+ ";
                			} else { 
                				fullstring += "- ";
                			}
            				fullstring += todo.getTodoText() + "\n";
            				
            			}
            		} else {
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
    
   
	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "Before load", Toast.LENGTH_SHORT).show();
		//TodoList tl = new TodoList();
		//int n=1;
		CombinedList cl = new CombinedList();
		//todoList = cl.getTodolist();
		cl = todoFileManager.loadTodoList();
		//Toast.makeText(this, cl.getTodolist().getTodoList().get(0).toString(), Toast.LENGTH_SHORT).show();
		todoList = cl.getTodolist();
		//Toast.makeText(this, todoList.getTodoList().get(0).getTodoText(), Toast.LENGTH_SHORT).show();
		todoArchive = cl.getTodoarchive();
		//String td = todoList.getTodoList().get(0).getTodoText();
		//Toast.makeText(this, td, Toast.LENGTH_SHORT).show();
		listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoList.getTodoList());
		list.setAdapter(listviewadapter);
		//list.invalidateViews();
        //listviewadapter.refresh(todoList.getTodoList());

		//listviewadapter.notifyDataSetChanged();
		//list.invalidateViews();
		
		//Toast.makeText(this, todo, Toast.LENGTH_SHORT).show();
		
		/*
		String FILENAME = "file5.sav";
		
		try {
			
			FileOutputStream fileoutputstream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(fileoutputstream);
			objectoutputstream.writeObject(tl);
			//objectoutputstream.writeObject(todoarchive);
			//objectoutputstream.writeObject(todolist);
			//objectoutputstream.writeObject(todoarchive);
			fileoutputstream.close();
		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		tl = new TodoList();
		//todolist = null;
		//if (todolist ==  null) {
			try {
				FileInputStream fileinputstream = openFileInput(FILENAME);
				ObjectInputStream objectinputstream = new ObjectInputStream(fileinputstream);
				tl = (TodoList) objectinputstream.readObject(); //we assume the input file contains Todo items
				//todoarchive = (TodoList) objectinputstream.readObject();
				fileinputstream.close();
			} catch (Exception e) {
				//Log.i("TodoList", "Input stream is not proper Todo list");
				//e.printStackTrace();
			} 
			
			todoList = tl;
		*/
		
		//todoArchive = todoFileManager.loadTodoArchive();
		//tweetsViewAdapter = new ArrayAdapter<Tweet>(this,
		//		R.layout.list_item, tweets);
		//oldTweetsList.setAdapter(tweetsViewAdapter);
	}
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void toggleListView(MenuItem menu) {

		toggleViewMode =  !toggleViewMode;
		if (toggleViewMode) {
			listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoArchive.getTodoList());
			setTitle("Archived Todo");
			
		} else {
			listviewadapter = new ListViewAdapter(this, R.layout.listview_item, todoList.getTodoList());
			setTitle("Todo List");
		}
		
		list.setAdapter(listviewadapter);
    	

	}
	
	public void summary(MenuItem menu) {
		
		Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
		intent.putExtra("todolist", todoList);
		intent.putExtra("todoarchive", todoArchive);
		startActivity(intent);
		
	}
	
	
    public void addTodo(View v) {
    	
		EditText newtext = (EditText) findViewById(R.id.editTodoText);
		Button mButton = (Button) findViewById(R.id.addButtonArchive);
		
		if (mButton.getText().toString() == "Save" && list.getCheckedItemCount() == 1) {
			// if currently editing a todo do this
			SparseBooleanArray selected = listviewadapter.getSelectedIds();
			listviewadapter.getItem(selected.keyAt(0)).setTodoText(newtext.getText().toString());
			listviewadapter.notifyDataSetChanged();

		} else {
			//otherwise add a new todo item	
			if (newtext.length() > 0 ) {
				Todo newtodo = new Todo(false, newtext.getText().toString());
				listviewadapter.add(newtodo);
				newtext.setText("");
				listviewadapter.notifyDataSetChanged();
			}
		}
		cl = new CombinedList();
    	cl.setTodolist(todoList);
    	cl.setTodoarchive(todoArchive);
        todoFileManager.saveTodoList(cl);
    }
    
}