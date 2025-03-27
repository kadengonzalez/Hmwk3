package databasePart1;
//Original Code By Professor Lynn Robert Carter. Original has been Editied by Bracken Peterson Jan 2025
/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private String name;  //changed  
    private String email; //changed 
    private String role;  //Not Really used, but still needed to function.
    private Boolean isAdmin = false;
    private Boolean isUser = false;
    private Boolean isStaff = false;
    private Boolean isInstructor = false;
    private Boolean isReviewer = false;
    private Boolean isStudent = false;

    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, String name, String email, String role) {
        this.userName = userName;
        this.password = password;
        this.name = name; 
        this.email = email; 
        this.role = role;
    }
    
    //A constructor for building reConstructing users on the Database
    public User(String name, String userName, String email, Boolean isAdmin, Boolean isUser, Boolean isInstructor, Boolean isStaff, Boolean isReviewer, Boolean isStudent) {
    	this.name = name; 
    	this.userName = userName;
    	this.email = email; 
        this.isAdmin = isAdmin;
        this.isUser = isUser;
        this.isInstructor = isInstructor;
        this.isStaff = isStaff;
        this.isReviewer = isReviewer;
        this.isStudent = isStudent;
    }
    
    // Constructor to initialize a new Complete User object with userName, password, and roles.
    public User( String userName, String password, String name,  String role, Boolean isAdmin, Boolean isUser, Boolean isInstructor, Boolean isStaff, Boolean isReviewer, Boolean isStudent, String email) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.role = role;
    	this.email = email; 
        this.isAdmin = isAdmin;
        this.isUser = isUser;
        this.isInstructor = isInstructor;
        this.isStaff = isStaff;
        this.isReviewer = isReviewer;
        this.isStudent = isStudent;
    }
    
    // Sets the role of the user.
    public void setRole(String role) {
    	this.role=role;
    }
    
    //Get basic info
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getName() { return name; }   //changed 
    public String getEmail() {return email; }   //changed 
    public String getRole() { return role; }    
    
    
    //Getters for Role Types
    public Boolean getIsAdmin() { return isAdmin; }
    public Boolean getIsUser() { return isUser; }
    public Boolean getIsStaff() { return isStaff; }
    public Boolean getIsInstructor() { return isInstructor; }
    public Boolean getIsReviewer() { return isReviewer; }
    public Boolean getIsStudent() { return isStudent; }
    
    //Set all is Role Actions
    public void setIsAdmin(Boolean bool) { isAdmin = bool; }
    public void setIsUser(Boolean bool) { isUser = bool; }
    public void setIsStaff(Boolean bool) { isStaff = bool; }
    public void setIsInstructor(Boolean bool) { isInstructor = bool; }
    public void setIsReviewer(Boolean bool) { isReviewer = bool; }
    public void setIsStudent(Boolean bool) { isStudent = bool; }
    
    
    
    
}
