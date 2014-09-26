package com.example.mswillia_notes002;

import java.util.ArrayList;

public interface InterfaceFileManager {
	
	//public void saveTodoList(TodoList todolist );
	//public CombinedList loadTodoList();
	
	public void saveTodoList(CombinedList combined);
	public CombinedList loadTodoList();


}
