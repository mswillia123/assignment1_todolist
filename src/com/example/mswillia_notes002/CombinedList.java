package com.example.mswillia_notes002;

import java.io.Serializable;

public class CombinedList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6332734647886032604L;
	private TodoList todolist;
	private TodoList todoarchive;
	
	public CombinedList(){
		this.todolist = new TodoList();
		this.todoarchive = new TodoList();
	}
	
	public TodoList getTodolist() {
		return todolist;
	}
	public void setTodolist(TodoList todolist) {
		this.todolist = todolist;
	}
	public TodoList getTodoarchive() {
		return todoarchive;
	}
	public void setTodoarchive(TodoList todoarchive) {
		this.todoarchive = todoarchive;
	}

}
