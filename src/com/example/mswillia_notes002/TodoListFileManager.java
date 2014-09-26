package com.example.mswillia_notes002;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;


public class TodoListFileManager implements InterfaceFileManager{	

// TodoListFileManager Class implements saving/loading of a single CombinedList object containing the todo list and archive list
//XXX Portions of class borrowed from CMPUT301 Lonely Twitter lab tutorial XXX

	private static final String FILENAME = "todolist.sav";
	private Context context;	
	
	public TodoListFileManager(Context context) {
		this.context = context;
	}
	
	public void saveTodoList(CombinedList cl) {
		try {
			
			FileOutputStream fileoutputstream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(fileoutputstream);
			objectoutputstream.writeObject(cl);
			objectoutputstream.close();
			fileoutputstream.close();
		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	//*/
	public CombinedList loadTodoList(){ 

		CombinedList cl = new CombinedList();

			try {
				FileInputStream fileinputstream = context.openFileInput(FILENAME);
				ObjectInputStream objectinputstream = new ObjectInputStream(fileinputstream);
				cl = (CombinedList) objectinputstream.readObject(); //we assume the input file contains a combined todo list
				objectinputstream.close();
				fileinputstream.close();
				
			} catch (Exception e) {
				Log.i("TodoList", "Input stream is not proper Todo list");
				e.printStackTrace();
			} 

		return  cl;
	}
	

}