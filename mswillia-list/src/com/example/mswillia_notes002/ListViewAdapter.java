package com.example.mswillia_notes002;


import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
 
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
 
public class ListViewAdapter extends ArrayAdapter<Todo> {
	
	/*
	 * ListViewAdapter class manages the todo list data and adapts the data to the MainActivity list view
	 * A secondary inner ViewHolder class contains a text view and checkbox image view. Multiple selection
	 * of list items is modeled for interaction with Main Activity listeners.
	 *	
	 * Source code attribution: ListViewAdapter section (and associated setMultiChoiceModeListener() in MainActivity)
	 * partially based on tutorial code:
	 * 		http://www.androidbegin.com/tutorial/android-delete-multiple-selected-items-listview-tutorial/
	 *
	 */
	
    Context context;
    LayoutInflater inflater;
    //List<Todo> todoList;
    List<Todo> todoList;
    
    private SparseBooleanArray selectedItems;
 
    public ListViewAdapter(Context context, int resourceId, List<Todo> todoList) {
        super(context, resourceId, todoList);
        selectedItems = new SparseBooleanArray();
        this.context = context;
        this.todoList = todoList;
        inflater = LayoutInflater.from(context);
    }
 
    private class ViewHolder {
        TextView todoText;
        ImageView todoChecked;
    }
 
    public View getView(int position, View view, ViewGroup parent) {

    	final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();                      
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextView and Checkbox in listview_item.xml
            holder.todoText = (TextView) view.findViewById(R.id.todotext);
            holder.todoChecked = (ImageView) view.findViewById(R.id.todochecked);
            view.setTag(holder);            
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.todoText.setText(todoList.get(position).getTodoText());
        // Capture position and set to the ImageView
        if (todoList.get(position).getTodoChecked()) {
        	holder.todoChecked.setImageResource(android.R.drawable.checkbox_on_background);
        } else {
        	holder.todoChecked.setImageResource(android.R.drawable.checkbox_off_background);
        }
        
        return view;
    }
    
    protected Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	//used
    @Override
    public void remove(Todo object) {
    	todoList.remove(object);
        notifyDataSetChanged();
    }
 
 
    public List<Todo> getTodoList() {
        return todoList;
    }
    
    //Toggle selected items
    public void toggleSelection(int position) {
        selectView(position, !selectedItems.get(position));
        
    }
 
    public void removeSelection() {
        selectedItems = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedItems.put(position, value);
        else
            selectedItems.delete(position);
        notifyDataSetChanged();
    }
 

    public SparseBooleanArray getSelectedIds() {
        return selectedItems;
    }
    
    

}