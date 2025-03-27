package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.UUID;

import databasePart1.Answer;
import databasePart1.AnswersList;
import databasePart1.Question;
import databasePart1.QuestionsList;
import databasePart1.User;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmtd
 
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(50), "
				+ "isAdmin BOOLEAN, "
				+ "isUser BOOLEAN, "
				+ "isStaff BOOLEAN, "
				+ "isInstructor BOOLEAN, "
				+ "isReviewer BOOLEAN, "
				+ "isStudent BOOLEAN, "
                + "name VARCHAR(20), "
                + "email VARCHAR(50))";
		statement.execute(userTable);
		
		// Create the invitation codes table, Added columns to include: Deadline, and all of the is*Role* BOOLEANS
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE, "
                + "deadline DOUBLE, " 
                + "isAdmin BOOLEAN, "
				+ "isUser BOOLEAN, "
				+ "isStaff BOOLEAN, "
				+ "isInstructor BOOLEAN, "
				+ "isReviewer BOOLEAN, "
				+ "isStudent BOOLEAN)";
	    statement.execute(invitationCodesTable); 

		// Create the One Time Passwords Table
	    String OneTimePasswordsTable = "CREATE TABLE IF NOT EXISTS OneTimePasswords ("
	            + "password VARCHAR(20) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(OneTimePasswordsTable); 
	    
	    //================================================================================
	    //Storing the Current Unique ID Table.
	    String CurrentIdTable = "CREATE TABLE IF NOT EXISTS CurrentIdTable ("
	    		+ "id INT PRIMARY KEY, "
	    		+ "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(CurrentIdTable); 
	    
	    //Create the QUESTIONS and Answers Table
	    String QuestionsTable = "CREATE TABLE IF NOT EXISTS Questions ("
	            + "id INT PRIMARY KEY, "
	            + "title VARCHAR(50), "
	            + "questionText VARCHAR(2000),"
	            + "userName VARCHAR(17), "
	            + "resolved BOOLEAN, "
	            + "isSubQuestion BOOLEAN, "
	            + "parentQuestionLinkId INT, "
	            + "hideUserName BOOLEAN, "
	            + "preferredAnswerId INT, "
				+ "homeworkTag BOOLEAN DEFAULT FALSE, "					// additions
				+ "conceptTag BOOLEAN DEFAULT FALSE, "
				+ "examTag BOOLEAN DEFAULT FALSE, "
				+ "generalTag BOOLEAN DEFAULT FALSE, "
				+ "socialTag BOOLEAN DEFAULT FALSE, "
				+ "tag VARCHAR(15))";
	    statement.execute(QuestionsTable);
	    
	  //Create the ANSWERS and Answers Table
	    String AnswersTable = "CREATE TABLE IF NOT EXISTS Answers ("
	            + "id INT PRIMARY KEY, "
	            + "userName VARCHAR(17), "
	            + "answerText VARCHAR(3000), "
	            + "questionLinkID INT, "
	            + "hideUserName BOOLEAN, "
	            + "answerLinkID INT, "
	            + "level INT, "
				+ "highestRole VARCHAR(12))";
	    statement.execute(AnswersTable);
	    ///=======================================================================
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role, isAdmin, isUser, isStaff, isInstructor, isReviewer, isStudent, name, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3,  user.getRole());
			pstmt.setBoolean(4, user.getIsAdmin());
			pstmt.setBoolean(5, user.getIsUser());
			pstmt.setBoolean(6, user.getIsStaff());
			pstmt.setBoolean(7, user.getIsInstructor());
			pstmt.setBoolean(8, user.getIsReviewer());
			pstmt.setBoolean(9, user.getIsStudent());
            pstmt.setString(10, user.getName());
            pstmt.setString(11, user.getEmail());
			
			pstmt.executeUpdate();
		}
	}

    // Deletes a user from the database
    public void deleteUser(String userName) throws SQLException {
        String delete = "DELETE FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(delete)){
            pstmt.setString(1, userName); //gets the username from the user object.

            pstmt.executeUpdate();
        } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Validates a user's existance
	public boolean userExists(String userName) throws SQLException {
			String query = "SELECT * FROM cse360users WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, userName);
				
				try (ResultSet rs = pstmt.executeQuery()) {
					boolean result = rs.next();
					System.out.print(result);
					return result; //this would be true if it made it this far
					
				} catch (SQLException e) {
			        e.printStackTrace();
			    }
			} catch (SQLException e) {
		        e.printStackTrace();
		    }
			return false;
		}

	// Validates a user's login credentials.
	public boolean oneTimeLogin(String password) throws SQLException {
		String query = "SELECT * FROM OneTimePasswords WHERE password = ? AND isUsed = FALSE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, password);
			try (ResultSet rs = pstmt.executeQuery()) {
				markOneTimePasswordAsUsed(password);
				return rs.next();
			}
		}
	}

	// Marks the invitation code as used in the database.
	private void markOneTimePasswordAsUsed(String password) {
	    String query = "UPDATE OneTimePasswords SET isUsed = TRUE WHERE password = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, password);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Updates Specific Password of a User
    public void setPassword(String userName, String password) throws SQLException {
		String query = "UPDATE cse360users SET password = ? WHERE username = ?"; //Sets all necessary columns to the passed values @ specified username
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, password);
            pstmt.setString(2, userName);

	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }

	//this little guy will insert a new password that the admin gives.
	public void createOneTimePassword(String password) {
	    String query = "INSERT INTO OneTimePasswords (password) VALUES ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, password);
            
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}

    // Updates the specified Usernames Roles on the database, with these Booleans
    public void updateUserRoles(String userName, Boolean isAdmin, Boolean isUser, Boolean isStaff, Boolean isInstructor, Boolean isReviewer, Boolean isStudent) throws SQLException {
		String query = "UPDATE cse360users SET isAdmin = ?, isUser = ?, isStaff = ?, isInstructor = ?, isReviewer = ?, isStudent = ? WHERE username = ?"; //Sets all necessary columns to the passed values @ specified username
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, isAdmin);
			pstmt.setBoolean(2, isUser);
			pstmt.setBoolean(3, isStaff);
			pstmt.setBoolean(4, isInstructor);
			pstmt.setBoolean(5, isReviewer);
			pstmt.setBoolean(6, isStudent);
            pstmt.setString(7, userName);

	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }

	// Updates Specific Usernames Roles on the database, with fed role name and boolean
    public void setUserRoles(String userName, String role, Boolean isRole) throws SQLException {
		String query = "UPDATE cse360users SET " + role + " = ? WHERE username = ?"; //Sets all necessary columns to the passed values @ specified username
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, isRole);
            pstmt.setString(2, userName);

	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
	
	// Retrieves the role of a user from the database using their UserName.
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}

    // Retrives NAME of User using their username
	public String getName(String userName) {
		String query = "SELECT name FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("name"); // Return NAME if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}

    // Retrives EMAIL of user using their username
	public String getEmail(String userName) {
		String query = "SELECT email FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("email"); // Return EMAIL if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Retrives isADMIN var from a particular database using their username
	public Boolean getIsAdmin(String tableName, String userName, String code) {
		
		if(userName == "") {
			String query = "SELECT isAdmin FROM " + tableName + " WHERE code = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, code);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isAdmin"); // Return the isAdmin if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		else {
			String query = "SELECT isAdmin FROM " + tableName + " WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, userName);
				ResultSet rs = pstmt.executeQuery();
	        
				if (rs.next()) {
					return rs.getBoolean("isAdmin"); // Return the isAdmin if user exists
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null; // If no user exists or an error occurs
		}
	}
	
	// Retrives is USER var from a particular database using their username
	public Boolean getIsUser(String tableName, String userName, String code) {
		
		if(userName == "") {
			String query = "SELECT isUser FROM " + tableName + " WHERE code = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, code);
				ResultSet rs = pstmt.executeQuery();
	        
				if (rs.next()) {
					return rs.getBoolean("isUser"); // Return the isUser if user exists
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null; // If no user exists or an error occurs
		}
			
			else {
				String query = "SELECT isUser FROM " + tableName + " WHERE userName = ?";
				try (PreparedStatement pstmt = connection.prepareStatement(query)) {
					pstmt.setString(1, userName);
					ResultSet rs = pstmt.executeQuery();
		        
					if (rs.next()) {
						return rs.getBoolean("isUser"); // Return the isUser if user exists
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null; // If no user exists or an error occurs
				
			}
	}
	
	// Retrives isSTAFF from var from a particular database using their username
	public Boolean getIsStaff(String tableName, String userName, String code) {
		
		if(userName == "") {
			String query = "SELECT isStaff FROM " + tableName + " WHERE code = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, code);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isStaff"); // Return the isStaff if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		else {
			String query = "SELECT isStaff FROM " + tableName + " WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, userName);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isStaff"); // Return the isStaff if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
	}
	
	// Retrives isINSTRUCTOR var from a particular database using their username
	public Boolean getIsInstructor(String tableName, String userName, String code) {
		
		if(userName =="") {
			String query = "SELECT isInstructor FROM " + tableName + " WHERE code = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, code);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isInstructor"); // Return the isInstructor if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		else {
			String query = "SELECT isInstructor FROM " + tableName + " WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, userName);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isInstructor"); // Return the isInstructor if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		
	}
	
	// Retrives isREVIEWER var from a particular database using their username
	public Boolean getIsReviewer(String tableName, String userName, String code) {
		if(userName == "") {
			String query = "SELECT isReviewer FROM " + tableName + " WHERE code = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, code);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isReviewer"); // Return the isReviewer if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		else {
			String query = "SELECT isReviewer FROM " + tableName + " WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, userName);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isReviewer"); // Return the isReviewer if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		
	}
	
	// Retrives isSTUDENT var from a particular database using their username
	public Boolean getIsStudent(String tableName, String userName, String code) {
		if(userName =="") {
			String query = "SELECT isStudent FROM " + tableName + " WHERE code = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, code);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isStudent"); // Return the isStudent if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		else {
			String query = "SELECT isStudent FROM " + tableName + " WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, userName);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getBoolean("isStudent"); // Return the isStudent if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null; // If no user exists or an error occurs
		}
		
	}
	
	
	// Generates a new invitation code and inserts it into the database.
    /*Now Takes in arguments to specify what kind of Roles this invitation code will give a 
    user upon creating an account using the Invitation code before the deadline */
	public String generateInvitationCode(double deadline, Boolean isAdmin, Boolean isUser, Boolean isStaff, Boolean isInstructor, Boolean isReviewer, Boolean isStudent) { //Added Arguments to passthrough, 
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code, deadline, isAdmin, isUser, isStaff, isInstructor, isReviewer, isStudent) VALUES (?,?,?,?,?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
            pstmt.setDouble(2, deadline);
            pstmt.setBoolean(3, isAdmin);
            pstmt.setBoolean(4, isUser);
            pstmt.setBoolean(5, isStaff);
            pstmt.setBoolean(6, isInstructor);
            pstmt.setBoolean(7, isReviewer);
            pstmt.setBoolean(8, isStudent);
            
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code, double timeStamp) {
	    String query = "SELECT deadline FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	        	if(rs.getDouble("deadline") >= timeStamp){
					markInvitationCodeAsUsed(code);
					return true;
				}
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	//Return All Users in the system
		public LinkedList<User> getAllUsers(){
			String query = "SELECT name, userName, email, isAdmin, isUser, isInstructor, isStaff, isReviewer, isStudent FROM cse360users";
			//Create a 'blank' string to store everything
			LinkedList<User> usersList = new LinkedList<User>();
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					User user = new User(rs.getString("name"), rs.getString("userName"),rs.getString("email"),rs.getBoolean("isAdmin"),rs.getBoolean("isUser"),rs.getBoolean("isInstructor"),rs.getBoolean("isStaff"),rs.getBoolean("isReviewer"),rs.getBoolean("isStudent"));
					usersList.add(user);
		        }
				
			} catch (SQLException e){
				e.printStackTrace();
			}
			return usersList;
		}

	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

	
	//Stores a New Question on the Database
	public boolean addNewQuestion(Question newQuestion) {
		String insertQuestion = "INSERT INTO Questions (id, title, questionText, userName, resolved, isSubQuestion, parentQuestionLinkId, hideUserName, preferredAnswerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(insertQuestion)) {
				pstmt.setInt(1, getNewId());
				pstmt.setString(2, newQuestion.getTitle());
				pstmt.setString(3,  newQuestion.getQuestionText());
				pstmt.setString(4,  newQuestion.getUserName());
				pstmt.setBoolean(5, newQuestion.getResolved());
				pstmt.setBoolean(6, newQuestion.getIsSubQuestion());
				pstmt.setInt(7, newQuestion.getParentQuestionLinkId());
				pstmt.setBoolean(8, newQuestion.getHideUserName());
				pstmt.setInt(9, newQuestion.getPreferredAnswerId());
				
				pstmt.executeUpdate();
				
				System.out.println("A new Question was Added");
				return true;
			
			} catch (SQLException e1) {
				System.out.println("The Add Question Did not work");

				e1.printStackTrace();
				return false;
			}
	}
	
	
	
	
	
		
	
	public int countOfQuestions() {
		int count = 0;
		String query = "SELECT COUNT(id) FROM Questions";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				count++;
			}
			
			
		} catch (SQLException e){
			System.out.println("Count Questions Did not work");
			e.printStackTrace();
		}
		if(count <= 0) {
			return 0;
		}
		return count;

	}
	
	

	public QuestionsList loadQuestionsList() {
		QuestionsList questionsList = new QuestionsList();
		String query = "SELECT * FROM Questions";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				//Create New Question Object
				Question question = new Question(rs.getInt("id"), rs.getString("title"), rs.getString("questionText"), rs.getString("userName"), rs.getBoolean("resolved"), rs.getBoolean("isSubQuestion"), rs.getInt("parentQuestionLinkId"), rs.getBoolean("hideUserName"), rs.getInt("preferredAnswerId"));
				//add QUestion Object to the QuestionsList to return
				questionsList.loadQuestion(question);
				System.out.println("Getting Question with id: " + rs.getInt("id"));
	        }
			
		} catch (SQLException e){
			System.out.println("The Load Quesitons Did not work");

			e.printStackTrace();
		}
		return questionsList;
	}
	
	//New ID number generator for Questions and Answers
	public int getNewId() {
		return (int) (System.currentTimeMillis() % 1000000000);
	}
	
	
	//Stores a New ANSWER on the Database
	public boolean addNewAnswer(Answer answer) {
		String insertQuestion = "INSERT INTO Answers (id, userName, answerText, questionLinkID, hideUserName, answerLinkID, level, highestRole) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(insertQuestion)) {
				pstmt.setInt(1, getNewId());
				pstmt.setString(2, answer.getUserName());
				pstmt.setString(3,  answer.getAnswerText());
				pstmt.setInt(4, answer.getQuestionLinkID());
				pstmt.setBoolean(5,  answer.getHideUserName());
				pstmt.setInt(6, answer.getAnswerLinkID());
				pstmt.setInt(7, answer.getLevel());
				pstmt.setString(8,  answer.getHighestRole());
					
				pstmt.executeUpdate();
					
				System.out.println("Answer Added");
				return true;
				
			} catch (SQLException e1) {
				System.out.println("The Add Answer Did not work");
				e1.printStackTrace();
				return false;
			}
		}
	
	//Returns an AnswersList object will all answers from the database
	public AnswersList loadAnswersList() {
		AnswersList answersList = new AnswersList();
		String query = "SELECT * FROM Answers";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				//Create New Question Object
				Answer answer = new Answer(rs.getInt("id"), rs.getString("userName"), rs.getString("answerText"), rs.getInt("questionLinkID"), rs.getBoolean("hideUserName"), rs.getInt("answerLinkID"), rs.getInt("level"), rs.getString("highestRole"));
				//add QUestion Object to the QuestionsList to return
				answersList.loadAnswer(answer);
				System.out.println("Getting Answer with id: " + rs.getInt("id"));
	        }
			
		} catch (SQLException e){
			System.out.println("The Load Answers Did not work");

			e.printStackTrace();
		}
		return answersList;
	}
	
	
	// Deletes a Question from the database
    public boolean deleteQuesiton(int id) throws SQLException {
        String delete = "DELETE FROM Questions WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(delete)){
            pstmt.setInt(1, id); 
            
            deleteAnswersLinked(id);// Delete any AnswersLinked before deleting the question
            
            pstmt.executeUpdate();//deletes Question from DB
            System.out.println("Question " + id +" has been Deleted");
            return true;
        } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Question " + id +" has not been Deleted");
	        return false;
	    }
    }
    
 // Deletes a Answer from the database
    public boolean deleteAnswer(int id) throws SQLException {
    	//delete all replies as well
    	deleteRepliesLinked(id);
    	
    	
        String delete = "DELETE FROM Answers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(delete)){
            pstmt.setInt(1, id); //deletes Answer from DB
            pstmt.executeUpdate();
            System.out.println("Deleting Answer " + id);
            return true;
        } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Answer " + id +" has not been Deleted");
	        return false;
	    }
    }
    
    // Deletes Answers linked to a specific Question ID from the database
    public void deleteAnswersLinked(int questionId) throws SQLException {
        String delete = "DELETE FROM Answers WHERE questionLinkID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(delete)){
            pstmt.setInt(1, questionId); //deletes Question from DB

            pstmt.executeUpdate();
            System.out.println("Deleting Answer linked to Question " + questionId);
        } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error Deleting Answer linked to Question " + questionId);
	    }
    }
    
 // Deletes Replies linked to a specific Answer ID from the database
    public void deleteRepliesLinked(int answerId) throws SQLException {
    	
    	AnswersList repliesList = loadRepliesList(answerId);
    	if(repliesList.getSize() > 0) {
    		for(int i=0; i < repliesList.getSize(); i++) {
    			deleteRepliesLinked(repliesList.getAnswerFromIndex(i).getId());
    		}
    	}
        
        String delete = "DELETE FROM Answers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(delete)){
            pstmt.setInt(1, answerId); //deletes Reply from DB

            pstmt.executeUpdate();
            System.out.println("Deleted Reply Answer Id: " + answerId);
        } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Reply Answer " + answerId +" has not been Deleted");
	    }
        
        
        
    }
    
  //Returns an AnswersList object will all Replies from a given answerID from the database
  	public AnswersList loadRepliesList(int answerId) {
  		AnswersList answersList = new AnswersList();
  		String query = "SELECT * FROM Answers WHERE answerLinkID = ?";
  		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
  			pstmt.setInt(1, answerId);
  			
  			ResultSet rs = pstmt.executeQuery();
  			while (rs.next()) {
  				//Create New Question Object
  				Answer answer = new Answer(rs.getInt("id"), rs.getString("userName"), rs.getString("answerText"), rs.getInt("questionLinkID"), rs.getBoolean("hideUserName"), rs.getInt("answerLinkID"), rs.getInt("level"), rs.getString("highestRole"));
  				//add QUestion Object to the QuestionsList to return
  				answersList.loadAnswer(answer);
  				System.out.println("Getting Reply with id: " + rs.getInt("id"));
  	        }
  			
  		} catch (SQLException e){
  			System.out.println("The Load Replies Did not work");

  			e.printStackTrace();
  		}
  		return answersList;
  	}
    
    
    // Updates a Specific Quesiton in the database
    public boolean updateQuestion(Question question) throws SQLException {
		String query = "UPDATE Questions SET title = ?, questionText = ?, resolved = ?, hideUserName = ?, preferredAnswerId = ? WHERE id = ?"; 
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    
			pstmt.setString(1, question.getTitle());
			pstmt.setString(2,  question.getQuestionText());
			pstmt.setBoolean(3, question.getResolved());
			pstmt.setBoolean(4, question.getHideUserName());
			pstmt.setInt(5, question.getPreferredAnswerId());
			pstmt.setInt(6, question.getId());


	        pstmt.executeUpdate();
	        System.out.println("Question " + question.getId() +" has been updated");
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Question " + question.getId() +" has not been updated");
	        return false;
	    }
    }
    
    // Updates  a Specific Answer
    public boolean updateAnswer(Answer answer) throws SQLException {
		String query = "UPDATE Answers SET userName = ?, answerText = ?, hideUserName = ? WHERE id = ?"; 
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	pstmt.setString(1, answer.getUserName());
			pstmt.setString(2,  answer.getAnswerText());
			pstmt.setBoolean(3,  answer.getHideUserName());
	        pstmt.setInt(4, answer.getId());

	        pstmt.executeUpdate();
	        System.out.println("Answer " + answer.getId() +" has been updated");
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Answer " + answer.getId() +" has not been updated");
	        return false;
	    }
    }
    
 // Updates  a Specific Answer
    public Answer getAnswerById(int id) throws SQLException {
		String query = "SELECT * FROM Answers WHERE id = ?"; 
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	pstmt.setInt(1, id);
	    	ResultSet rs = pstmt.executeQuery();
	    	Answer answer;
	    	while (rs.next()) {
	    		answer = new Answer(rs.getInt("id"), rs.getString("userName"), rs.getString("answerText"), rs.getInt("questionLinkID"), rs.getBoolean("hideUserName"), rs.getInt("answerLinkID"), rs.getInt("level"), rs.getString("highestRole"));
	    		return answer;
	    	}
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
		return null;
    }
    

	// Updates the specified tags in the QuestionsTable database with these Booleans
    public void updateTags(int id, Boolean homeworkTag, Boolean conceptTag, Boolean examTag, Boolean generalTag, Boolean socialTag) throws SQLException {
		String query = "UPDATE Questions SET homeworkTag = ?, conceptTag = ?, examTag = ?, generalTag = ?, socialTag = ?, tag = ? WHERE id = ?"; //Sets all necessary columns to the passed values @ specified username
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, homeworkTag);
			pstmt.setBoolean(2, conceptTag);
			pstmt.setBoolean(3, examTag);
			pstmt.setBoolean(4, generalTag);
			pstmt.setBoolean(5, socialTag);
			
			System.out.println("Creating new question in database: ");
            
            if(homeworkTag) {
            	pstmt.setString(6, "Homework");
            	System.out.println("Set tag: homework");
            }
            else if(conceptTag) {
            	pstmt.setString(6, "Concept");
            	System.out.println("Set tag: concept");
            }
            else if(examTag) {
            	pstmt.setString(6, "Exam");
            	System.out.println("Set tag: exam");
            }
            else if(generalTag) {
            	pstmt.setString(6, "General");
            	System.out.println("Set tag: general");
            }
            else {
            	pstmt.setString(6, "Social");
            	System.out.println("Set tag: social");
            }
            pstmt.setInt(7, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tags updated successfully for question ID: " + id);
            } else {
                System.out.println("No question found with ID: " + id);
            }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }

	// Sets the specified tag in the QuestionTable database given a Boolean 
	public void setQuestionTag(int id, String tag, Boolean isTag) throws SQLException {
		String query = "UPDATE Questions SET " + tag + " = ? WHERE id = ?"; //Sets all necessary columns to the passed values @ specified username
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, isTag);
            pstmt.setInt(2, id);

	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }

	// This little guy doesn't  work. 
	/*
	public QuestionsList loadQuestionsTag(String tag) {
		QuestionsList questionsList = new QuestionsList();
		System.out.println("GOT HERE RAGH ");
		System.out.println("TAG IS THIS: " + tag);
		String query = "SELECT * FROM Questions WHERE tag = ?"; //maybe ==? 
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, tag);
			ResultSet rs = pstmt.executeQuery();  //This  ain't working. 
			while (rs.next()) {
				//Create New Question Object
				System.out.println("Here ya go: " + rs.getString("title"));
				Question question = new Question(rs.getInt("id"), rs.getString("title"), rs.getString("questionText"), rs.getString("userName"), rs.getBoolean("resolved"), rs.getBoolean("isSubQuestion"), rs.getInt("parentQuestionLinkId"), rs.getBoolean("hideUserName"), rs.getInt("preferredAnswerId"));
				//add QUestion Object to the QuestionsList to return
				questionsList.loadQuestion(question);
				System.out.println("Getting Question with id: " + rs.getInt("id"));
	        }
			
		} catch (SQLException e){
			System.out.println("The Load Quesitons Did not work");

			e.printStackTrace();
		}
		return questionsList;
	}
	*/
	
	public QuestionsList loadQuestionsListTags(String tag) {
		QuestionsList questionsList = new QuestionsList();
		QuestionsList returnList = new QuestionsList();
		String query = "SELECT * FROM Questions";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				//Create New Question Object
				Question question = new Question(rs.getInt("id"), rs.getString("title"), rs.getString("questionText"), rs.getString("userName"), rs.getBoolean("resolved"), rs.getBoolean("isSubQuestion"), rs.getInt("parentQuestionLinkId"), rs.getBoolean("hideUserName"), rs.getInt("preferredAnswerId"));
				//add QUestion Object to the QuestionsList to return
				questionsList.loadQuestion(question);
				System.out.println("Getting Question with id: " + rs.getInt("id"));
	        } 
			for(int i = 0; i < questionsList.getSize(); i++) {
				System.out.println();
				System.out.println(getTag(questionsList.getQuestionFromIndex(i).getId()));
				
				if(tag.contains(getTag(questionsList.getQuestionFromIndex(i).getId()))) {
					returnList.loadQuestion(questionsList.getQuestionFromIndex(i));
					System.out.println("Added new: " + i);
				}
			}
			
		} catch (SQLException e){
			System.out.println("The Load Quesitons Did not work");

			e.printStackTrace();
		}
		return returnList;
	}
	
	public String getTag(int id) {
		String query = "SELECT tag FROM Questions WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	System.out.println(rs.getString("tag") + " is the tag.");
	            return rs.getString("tag"); // Return tag if the question Exists
	        }
	        else {
	        	System.out.println("Fuck you.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no question exists or an error occurs
	}
	//get a ID based on the text 
	public int getQuestionID(String text) {
		String query = "SELECT id FROM Questions WHERE questionText = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, text);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt("id"); // Return id if question exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // If no user exists or an error occurs
	}
	public Question getQuestion(String text) {
        String query = "SELECT * FROM Questions WHERE questionText = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, text);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Question question = new Question(rs.getInt("id"), rs.getString("title"), rs.getString("questionText"), rs.getString("userName"), rs.getBoolean("resolved"), rs.getBoolean("isSubQuestion"), rs.getInt("parentQuestionLinkId"), rs.getBoolean("hideUserName"), rs.getInt("preferredAnswerId"));
                return question; // Return id if question exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // If no user exists or an error occurs
    }
	
	
}

	
	