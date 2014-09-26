assignment1_todolist
====================

CMPUT301 Assignment 1 Todo List Android App
Michael Williams 
1309667
mswillia@ualberta.ca
github: https://github.com/mswillia123/assignment1_todolist.git

NOTE: github removed the README formatting on the front page, but it looks fine in RAW or Preview

***NOTE: a long press/click on a todo list item will bring up the selection menu ***

Usage:

The application has two modes:
1. Todo list mode - the application starts in this mode. 
	- The view displays a text edit field, where you may enter the text for a new todo. 
	- Clicking the "Add" button adds the todo to the list, with an empty checkbox. 
	- Click on any todo in the list to check or uncheck the todo.
	Todo list mode has a menu, found in the upper right corner, with two options:
		- View Archive/Todo: this menu option alternates the viewed list between your todo list and your archived todo list.
		- Summarize: this menu option displays a summary of your checked and unchecked todos in both the todo list and the archived list.
2. Selection mode - a long press/click on any of the todo items activates selection mode.
	- In selection mode you may click on any number of todos to select them. The action bar at the top of the screen indicates how many todos are selected.
	- There are several actions you may perform on the selected todos, which are found in the action bar menu:
		- Edit: brings the text of the selected todo into the text edit field, where you may change it. Click on the Save button to save your changes to the todo. Note: the edit option is only available when a single todo is selected.
		- Delete: removes the selected todos from your list.
		- Archive/Unarchive: move the selected todos from the todo list to the archive (or from the archive to the todo list), based upon which list you are currently viewing.
		- Email Selected Items: adds the selected todo items as text to an email body and intitiates your default email client application with the email.
		- Email All: similarly to the previous option, but this option adds all todos in the todo and archive list to the email.

Classes:

MainActivity class is the primary view.
 
Active todos and archived todos are stored in two TodoList objects.
Todo list mode - the application starts in this mode. List mode has a menu, with summary, and view archive/todo list options.
Selection mode - a long press/click on any of the todo items activates context action bar selection mode, and allows for edit, delete, archive/unarchive, and email functions on the selected items
	Primary functions:
	 * setOnItemClickListener():
	 * 	Monitors clicks on individual todos in list
	 *        
	 *setMultiChoiceModeListener():
	 *  Long press on a ListView item activates multiple selection listener, with context action bar menu.
	 * 	Implements all edit, delete, archive/unarchive, and email functions.
	 * 
	 * addTodo():
	 * 	Implements addition of Todo item to todo list, as well as saving edited todo items.
	 * 
	 * summary():
	 * 	Activates the summary activity via an intent
	 * 
	 * Design rationale: to prevent redundant code and an additional activity class, both lists are implemented
	 * 	within the same view, via swapping of todo list objects into the ListView adapter

ListViewAdapter class manages the todo list data and adapts the data to the MainActivity list view
A secondary inner ViewHolder class contains a text view and checkbox image view. Multiple selection
of list items is modeled for interaction with Main Activity listeners.

Todo class gets and sets todo text and checkbox state

TodoList class provides methods to set and get full todo lists, and also provides methods to add and remove todo's from the list and get the size of the list. 

CombinedList class combines todo list and archive list into a single object

InterfaceFileManager provides method signatures for TodoListFileManager and facilitates MainActivity access to file read and write.

TodoListFileManager class implements saving/loading of a single CombinedList object containing the todo list and archive list

Summary Activity summarizes todo item statistics:
	total number of todo items checked
	total number of todo items left unchecked
	total number of archived todo items
	total number of checked archived todo items
	total number of unchecked archived todo items
	 
External source code usage/attribution:	
	setMultiChoiceModeListener() section (and associated ListViewAdapter class) partially based on tutorial code:
		http://www.androidbegin.com/tutorial/android-delete-multiple-selected-items-listview-tutorial/
	setOnItemClickListener() section based on tutorial code:
		http://www.mysamplecode.com/2012/07/android-listview-checkbox-example.html
	email intent code based upon instructor recommended link:
		http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
	additional code ideas derived from A. Hindle Student Picker video tutorials (Creative Commons Attribution Licence):
		Student Picker for Android: http://youtu.be/5PPD0ncJU1g, http://youtu.be/VKVYUXNuDDg, http://youtu.be/k9ZNbsc0Qgo, http://youtu.be/fxjIA4HIruU, http://youtu.be/uLnoI7mbuEo
	
