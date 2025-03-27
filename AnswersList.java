package databasePart1;

import java.util.LinkedList;

import databasePart1.DatabaseHelper;


public class AnswersList {

	public DatabaseHelper databaseHelper;
	
	public LinkedList<Answer> answersList;
	
	
	// Constructor to create an instance of AnswersList
	public AnswersList() {
		this.answersList = new LinkedList<Answer>();
	}
	
	
	// Return Answer Object given an Answer ID
    public Answer getAnswerFromId(int id) { 
    	// Loop through AnswerList
    	for(int i=0; i<answersList.size(); i++) {
    		
    		// Check if the current Answer Object ID matches the wanted Answer ID
    		if(answersList.get(i).getId() == id) {
    			
    			// Return Answer Object if it matches the given ID
    			return answersList.get(i);
    		}
    	}
    	// If nothing is found in loop, return NULL
		return null;
    }
	
    
    // Return the Size of the answers List
    public int getSize() {
    	// Check if list is empty
    	if (answersList == null) {
    	   return 0;
    	 } else {
    		 // If list is not empty, return size
    		 return this.answersList.size();
    	 }
    }
    
    
    /* This function allows an answer object to be added to the AnswersList object. 
    Primarily used For when the database loads Answers back in to a AnswersList and then sends that info to the ViewDiscussions page */
    public void loadAnswer(Answer answer) {
    	answersList.add(answer);
    }
    
    
    // Return an answer given an Index
    public Answer getAnswerFromIndex(int index) {
    	// Check if list is empty
    	if (answersList == null) {
    		return null;
    	} else {
    		// Return index if not empty
    		return this.answersList.get(index);
    	}
    }
    
    
    // Clear the entire list
    public boolean deleteList() {
    	// Check if list is empty
    	if(answersList.size() > 0) {
    		answersList.clear(); // Clear list
    		return true; // Return result
    	} else {
    		// Return false if list is empty or failed
    		return false;
    	}
	}
    
}
