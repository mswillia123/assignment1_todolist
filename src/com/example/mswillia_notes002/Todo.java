package com.example.mswillia_notes002;

import java.io.Serializable;

public class Todo implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -1610812652116647634L;
	private String rank;
    //private String country;
    //private String population;
    private boolean flag;
 
    public Todo(boolean flag, String rank) { //String country,String population) {
        this.rank = rank;
        //this.country = country;
        //this.population = population;
        this.flag = flag;
    }
 
	public String getRank() {
        return rank;
    }
 
    public void setRank(String rank) {
        this.rank = rank;
    }
 
    public boolean getFlag() {
        return flag;
    }
    
    /*
    public boolean isSelected() {
        return flag;
  	}
  	*/
 
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
 
}

