package com.example.mswillia_notes002;

import java.util.ArrayList;

public interface InterfaceFileManager {
	
	/* InterfaceFileManager provides method signatures for TodoListFileManager
	and facilitates MainActivity access to file read and write
	*/
	
	public void saveTodoList(CombinedList combined);
	public CombinedList loadTodoList();


}
