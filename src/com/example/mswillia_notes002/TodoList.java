package com.example.mswillia_notes002;

import java.io.Serializable;
import java.util.ArrayList;


public class TodoList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2205661156686002749L;
	
	private ArrayList<Todo> todoList;
	//private ArrayList<Todo>	todoArchive;
	
	public TodoList() {
		todoList = new ArrayList<Todo>();		
		//todoArchive = new ArrayList<Todo>();
	}
	
	public ArrayList<Todo> getTodoList() {
		return todoList;
	}
	public void setTodoList(ArrayList<Todo> todoList) {
		this.todoList = todoList;
	}
	
	public void addTodo(Todo todo) {
		todoList.add(todo);
		//notifyListeners();
	}
	
	public void removeTodo(Todo todo) {
		todoList.remove(todo);
		//notifyListeners();
	}
	
	public int listSize() {
		return todoList.size();
	}
	
	
}
