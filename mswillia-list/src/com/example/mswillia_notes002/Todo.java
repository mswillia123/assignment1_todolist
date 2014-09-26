package com.example.mswillia_notes002;

import java.io.Serializable;

public class Todo implements Serializable{
	
	
    /*
	Todo class gets and sets todo text and checkbox state
	*/
	
	private static final long serialVersionUID = -1610812652116647634L;
	private String todoText;
    private boolean todoChecked;
 
    public Todo(boolean todoChecked, String todoText) {
        this.todoText = todoText;
        this.todoChecked = todoChecked;
    }
 
	public String getTodoText() {
        return todoText;
    }
 
    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }
 
    public boolean getTodoChecked() {
        return todoChecked;
    }
    
    public void setTodoChecked(boolean todoChecked) {
        this.todoChecked = todoChecked;
    }
 
}

