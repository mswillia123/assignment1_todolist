package com.example.mswillia_notes002;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;


public class TodoListFileManager {//implements IDataManager{
	
//XXX Class borrowed from Lonely Twitter
//XXX Have changed variable names and moved functions, but otherwise looks identical
/*
 * public interface IDataManager {
	
	public ArrayList<Todo> loadTodoList();
	
	public void saveTodos(List<Todo> todolist);

}
 * */

	private static final String FILENAME = "file2.sav";
	
	private Context context;
	
	
	public TodoListFileManager(Context context) {
		this.context = context;
	}
	
	/* working on this right now!! should implement this as separate class file
	public class bothLists {
		  private ArrayList<Todo> person = null;
		  private DataExporterPerson dataExporterPerson = null;

		  //getters and setters.
		}
	*/
	
	public void saveTodoList(TodoList todolist ) throws IOException {
		try {
			
			FileOutputStream fileoutputstream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(fileoutputstream);
			objectoutputstream.writeObject(todolist);
			//objectoutputstream.writeObject(todolist);
			//objectoutputstream.writeObject(todoarchive);
			fileoutputstream.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TodoList loadTodoList() throws ClassNotFoundException, IOException {

		TodoList todolist = new TodoList();
		todolist = null;
		
		try {
			FileInputStream fileinputstream = context.openFileInput(FILENAME);
			ObjectInputStream objectinputstream = new ObjectInputStream(fileinputstream);
			todolist = (TodoList) objectinputstream.readObject(); //we assume the input file contains Todo items
			//todoarchive = (ArrayList<Todo>) objectinputstream.readObject();
		} catch (Exception e) {
			Log.i("TodoList", "Input stream is not proper Todo list");
			e.printStackTrace();
		} 

		return todolist;
	}
	

}