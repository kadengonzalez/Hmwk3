package databasePart1;

import java.util.LinkedList;

import databasePart1.DatabaseHelper;


public class QuestionsList {
	
	public DatabaseHelper databaseHelper;
	
    public LinkedList<Question> questionsList;
    
    
    // Constructor to create QuestionsList
    public QuestionsList() {
    	this.questionsList = new LinkedList<Question>();
    }
    
    
    // Return Question given an ID
    public Question getQuestionFromId(int id) { 
    	// Loop through QuestionsList
    	for(int i=0; i<questionsList.size(); i++) {
    		// Check each index if it matches ID
    		if(questionsList.get(i).getId() == id) {
    			// If Question has matching ID return it
    			return questionsList.get(i);
    		}
    	}
    	// Return null if Question Object was not found
		return null;
    }
    
    
    // Return the size of QuestionsList
    public int getSize() {
    	// Check if list is empty
    	if (questionsList == null) { 
    		// Return 0 if empty
    		return 0; 
    	} else { 
    		// If list not empty, return the size
    		return this.questionsList.size();
    	}
    }
    
    
    /* This function allows an question object to be added to the QuestionsList object. 
    Primarily used For when the database loads Questions back in to a QuestionsList and then sends that info to the ViewDiscussions page */
    public void loadQuestion(Question question) {
    	questionsList.add(question);
    }
    
    
    // Return Question Object given an index
    public Question getQuestionFromIndex(int index) {
    	// Loop through QuestionsList
    	if (questionsList == null) {
    		// If empty, return null
    		return null;
    	} else {
    		// If list not empty, return the index
    		return this.questionsList.get(index);
    	}
    }
    
    
    // Adding a new Question
    public void addQuestionBothPlaces(DatabaseHelper databaseHelper2, Question question) {
    	// Add Question to QuestionsList
    	questionsList.addLast(question);
    	
    	// Add Question to the Database
    	databaseHelper2.addNewQuestion(question);
    }
    
    
    // Add a Subset Question
    public void addSubQuestion(Question newQuestion) {
    	// Add Question to QuestionsList
    	questionsList.addLast(newQuestion);
    	
    	// Add Question to the Database
    	databaseHelper.addNewQuestion(newQuestion);
    }
    
    
    // Clear the entire list
    public boolean deleteList() {
    	// Ensure list is not empty
    	if(questionsList.size() > 0) {
    		// Clear if not empty
    		questionsList.clear();
    		// Return ersult to user
    		return true;
    	} else {
    		// Return false if it was empty or failed to clear list
    		return false;
    	}
	}
    
}
