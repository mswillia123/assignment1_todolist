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
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<Todo> todoList;
    //ArrayList<Todo> todoList;
    //Boolean toggleViewMode = true;
    private SparseBooleanArray mSelectedItemsIds;
 
    public ListViewAdapter(Context context, int resourceId,
            List<Todo> todoList) {
        super(context, resourceId, todoList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.todoList = todoList;
        inflater = LayoutInflater.from(context);
    }
 
    private class ViewHolder {
        TextView rank;
        //CheckBox flag;
        ImageView flag;
    }
 
    public View getView(int position, View view, ViewGroup parent) {
    	//final ViewHolder holder;
    	final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            
            //LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextView and Checkbox in listview_item.xml
            holder.rank = (TextView) view.findViewById(R.id.rank);
            holder.flag = (ImageView) view.findViewById(R.id.flag);
            view.setTag(holder);
            
            //XXX this portion from http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html
            
            /*
            holder.flag.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox checkbox = (CheckBox) v;
					Todo todo = (Todo) checkbox.getTag();
					Toast.makeText(getApplicationContext(),
						       "Clicked on Checkbox: " + checkbox.getText() +
						       " is " + checkbox.isChecked(), 
						       Toast.LENGTH_LONG).show();
					todo.setFlag(checkbox.isChecked());
				}
			});
			*/
            // XXX end portion
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.rank.setText(todoList.get(position).getRank());
        //holder.country.setText(todoList.get(position).getCountry());
        //holder.population.setText(todoList.get(position)
                //.getPopulation());
        // Capture position and set to the ImageView
        if (todoList.get(position).getFlag()) {
        	holder.flag.setImageResource(android.R.drawable.checkbox_on_background);
        } else {
        	holder.flag.setImageResource(android.R.drawable.checkbox_off_background);
        }
        
        //holder.flag.setChecked(todoList.get(position).getFlag());
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
 
    //not in  use
    public List<Todo> getTodoList() {
        return todoList;
    }
    
    //used
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
        
    }
 
    //used
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
 
    //not in  use
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }
 
    //not in  use
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }
    //used
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    
    /*
    public Boolean getToggleViewMode() {
    	return toggleViewMode;
    }
    
    public Boolean setToggleViewMode() {    	
    	return !toggleViewMode;
    }
	*/
}