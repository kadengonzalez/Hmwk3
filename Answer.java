package databasePart1;


public class Answer {
	
    private int id; // The ID will help with Creation, Reading, Updating, and Deleting an Answer
	private int questionLinkID;
	private int answerLinkID; // This will be used to create subanswers and replies
	private int level = -1; // This will store if the answer is a reply to a question or an answer (or any subset of an answer)

    private String userName;
    private String answerText;
	private String highestRole; // This is for sorting the answers based on rank
	
	private Boolean hideUserName; // Make an Answer anonymous
	
    
    // Constructor to initialize a new Answer object
    public Answer( String userName, String answerText, int questionLinkID, Boolean hideUserName, int answerLinkID, int level) {
        this.userName = userName;
        this.answerText = answerText;
        this.questionLinkID = questionLinkID;
        this.hideUserName = hideUserName;
        this.answerLinkID = answerLinkID;
        this.level = level;
    }
    
    
    // Constructor for creating of an answer FOR AFTER it is already in the database, this is for taking it out of the database and creating the answer objects from the stored info
    public Answer(int id, String userName, String answerText, int questionLinkID, Boolean hideUserName, int answerLinkID, int level, String highestRole) {
    	this.id = id;
        this.userName = userName;
        this.answerText = answerText;
        this.questionLinkID = questionLinkID;
        this.hideUserName = hideUserName;
        this.answerLinkID = answerLinkID;
        this.level = level;
        this.highestRole = highestRole;
    }
    

    // Return info from Answer Object
    public int getId() { return this.id; }
    public int getQuestionLinkID() { return this.questionLinkID; }
    public int getAnswerLinkID() { return this.answerLinkID; }
    public int getLevel() { return this.level; }

    public String getUserName() { return this.userName; }
    public String getAnswerText() { return this.answerText; }  
    public String getHighestRole() { return this.highestRole; } 
    
    public Boolean getHideUserName() { return this.hideUserName; }

    
    // Setter info for Answer Object
    public void setQuestionLinkID(int id) { this.questionLinkID = id; }
    public void setAnswerLinkID(int id) { this.answerLinkID = id; }
    public void setLevel(int level) { this.level = level; }
    
    public void setAnswerText(String text) { this.answerText = text; }
    public void setHighestRole(String text) { this.highestRole = text; }
    
    public void setHideUserName(Boolean bool) { this.hideUserName = bool; }
}
