package databasePart1;

//Class created by Bracken Peterson Feb 2025

public class Question {
		
	//Id will be set from Server side, when it is inserted into the table
    private int id; //THis will help with Creation, Reading, Updateing, BUT especially Deleting
    private String title;
    private String questionText;
    private String userName;
    private Boolean resolved;
    private Boolean isSubQuestion;
    private int parentQuestionLinkId;
    private Boolean hideUserName; //make question anonomous
    private int preferredAnswerId = 0; //Someone like an admin can select an answer to put here
    
    //Create Question from the NewQuestion Page
    public Question(String title, String question, String userName, Boolean resolved, Boolean isSubQuestion, int parentQuestionLinkId, Boolean hideUserName, int preferredAnswerId) {
        this.userName = userName;
        this.title = title;
        this.questionText = question;
        this.hideUserName = hideUserName;
        this.preferredAnswerId = 0;
        this.resolved = resolved;
        this.isSubQuestion = isSubQuestion;
        this.parentQuestionLinkId = parentQuestionLinkId;
        this.preferredAnswerId = preferredAnswerId;
    } 
   
  //Create Question from the Database Page
    public Question(int id, String title, String question, String userName, Boolean resolved, Boolean isSubQuestion, int parentQuestionLinkId, Boolean hideUserName, int preferredAnswerId) {
        this.id = id;
    	this.userName = userName;
        this.title = title;
        this.questionText = question;
        this.hideUserName = hideUserName;
        this.preferredAnswerId = 0;
        this.resolved = resolved;
        this.isSubQuestion = isSubQuestion;
        this.parentQuestionLinkId = parentQuestionLinkId;
        this.preferredAnswerId = preferredAnswerId;
    } 
    
    
    
    //Get info returned
    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getQuestionText() { return this.questionText; }  
    public String getUserName() { return this.userName; }
    public Boolean getResolved() { return this.resolved; }    
    public Boolean getIsSubQuestion() { 
    	if(this.isSubQuestion == null) {
    		return false;
    	} else { 
    		return this.isSubQuestion;
    	}
    }
    public int getParentQuestionLinkId() {return this.parentQuestionLinkId; }
    public Boolean getHideUserName() { return this.hideUserName; }
    public int getPreferredAnswerId() { 
    	return this.preferredAnswerId;
    }

    
    //Setters
    public void setHideUserName(Boolean bool) { this.hideUserName = bool; }
    public void setPreferredAnswerId(int answerId) { this.preferredAnswerId = answerId;}
    public void setParentQuestionLinkId(int parentQuestionID) { this.parentQuestionLinkId = parentQuestionID;}
    public void setResolved(Boolean bool) { this.resolved = bool; }
    public void setIsSubQuestion(Boolean bool) { this.isSubQuestion = bool; }
    public void setQuestionText(String text) { this.questionText = text; }
    public void setTitleText(String text) { this.title = text; }
    
    
}
