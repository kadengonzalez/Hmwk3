package databasePart1;

import java.sql.SQLException;
//import java.util.LinkedList;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//import application.SearchDiscussions;

public class ViewDiscussions {
	
	private final DatabaseHelper databaseHelper;
	public QuestionsList questionsList;
	public Question currentQuestion;
	public Answer currentAnswer;
	public AnswersList answersList;
	public VBox allAnswers = new VBox(1);
	public Label preferredAnswerText = new Label();
	public Label resolvedText = new Label("");

    public ViewDiscussions(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

   
    
	//This cool little guy will recursively display each sub reply, and it will make sure the sub replies sub replies are taken care of etc.
	public void showReplies(Answer parentAnswer, AnswersList answersList, Stage primaryStage, User user){
		System.out.println("Creating Reply Chain");
		// Ensure answer list isn't blank
		int size = answersList.getSize();
		if(size > 0){
			// Loop through all the answers in answersList
			for (int j=0; j < size; j++){
				// if correct level (one greater than parent) && has parentID that equals the parentAnswer ID
				// then you display the sub answers
				if ((answersList.getAnswerFromIndex(j).getLevel() == (parentAnswer.getLevel() + 1)) && (answersList.getAnswerFromIndex(j).getAnswerLinkID() == parentAnswer.getId())){
					
					//New Label for Spacing
					Label spacingLabel = new Label("");
					
					//Add tab spacing to the label text for each level of reply it is
					for(int k = 0; k < answersList.getAnswerFromIndex(j).getLevel(); k++){
						spacingLabel.setText(spacingLabel.getText() + "\t");
						if(k > 5) {
							break;
						}
					}
					//this new hbox will hold the whole reply Answer
					HBox replyBox = new HBox();
					
					//Create a new Hbox for each answer, including a button and a label
					HBox newHBox = new HBox(5); 
					VBox newVBox = new VBox();
					replyBox.setId("answer-box");
					
					//Dynamic element creation
					Button setAsPreferredAnswer = new Button ("Preferred Answer");
					setAsPreferredAnswer.setId("answer-box-buttons");
					setAsPreferredAnswer.setStyle("-fx-font-size: 8px;");
					setAsPreferredAnswer.setPrefHeight(12);
					
					//Storing the answer obj, in each preferred ans. button, for future reference
					setAsPreferredAnswer.setUserData(answersList.getAnswerFromIndex(j));
				
					//Set Button Clicks
					setAsPreferredAnswer.setOnAction( b-> {
						//remake the Answer Object on Click
						Answer answerObj = (Answer) setAsPreferredAnswer.getUserData(); 
						currentAnswer = answerObj;
						currentQuestion.setPreferredAnswerId(currentAnswer.getId());
						currentQuestion.setResolved(true);
						updateResolvedLabelText(currentQuestion.getResolved());
						//update the preffered answer ID number in the database too
						try {
							databaseHelper.updateQuestion(currentQuestion);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						preferredAnswerText.setText(currentAnswer.getAnswerText());
					});
					
					//If you are not the creator of the question, then you don't get to See the preffered answer button
					if(!answersList.getAnswerFromIndex(j).getUserName().equals(user.getUserName())) {
						setAsPreferredAnswer.setVisible(false);
					}
					
					//edit and delete buttons set up
					Button editAnswer = new Button ("Edit");
					editAnswer.setId("answer-box-buttons");
					editAnswer.setStyle("-fx-font-size: 8px;");
					editAnswer.setPrefHeight(12);
					
					Button deleteAnswer = new Button ("Delete");
					deleteAnswer.setId("answer-box-buttons");
					deleteAnswer.setStyle("-fx-text-fill: red; -fx-font-size: 8px;");
					deleteAnswer.setPrefHeight(12);
					
					Button replyAnswer = new Button ("Reply");
					replyAnswer.setId("answer-box-buttons");
					replyAnswer.setStyle("-fx-text-fill: black; -fx-font-size: 8px;");
					replyAnswer.setPrefHeight(12);
					
					//give each button an answer obj for future reference
					editAnswer.setUserData(answersList.getAnswerFromIndex(j));
					deleteAnswer.setUserData(answersList.getAnswerFromIndex(j));
					replyAnswer.setUserData(answersList.getAnswerFromIndex(j));
					
					//Set all to Invisible by Default
					setAsPreferredAnswer.setVisible(false);
					editAnswer.setVisible(false);
					deleteAnswer.setVisible(false);
					replyAnswer.setVisible(false);
					
					//Edit Button set up
					editAnswer.setOnAction( b-> {
						Answer answerObj = (Answer) editAnswer.getUserData(); //remake the Answer Object on Click
						currentAnswer = answerObj;
						
						//Make sure only the answer creator is the only one able to edit their answer
						if(currentAnswer.getUserName().equals(user.getUserName())) {
							//currentQuestion.setPreferredAnswerId(currentAnswer.getId());
							AnswerQuestionPage answerQuestionPage = new AnswerQuestionPage(databaseHelper);
							answerQuestionPage.showUpdateAnswer(primaryStage, user, currentQuestion, currentAnswer);
						}
					});
					//Delete Button Set Up
					deleteAnswer.setOnAction( b-> {
						Answer answerObj = (Answer) deleteAnswer.getUserData(); //remake the Answer Object on Click
						currentAnswer = answerObj;
						
						if(currentAnswer.getUserName().equals(user.getUserName()) || user.getIsAdmin()) {
							try {
								databaseHelper.deleteAnswer(currentAnswer.getId());
							} catch (SQLException e) {
								e.printStackTrace();
							}
							//recalls the stage, to refresh 
							ViewDiscussions viewDiscussions = new ViewDiscussions(databaseHelper); 
							viewDiscussions.show(primaryStage, user, currentQuestion);
						}
					});
					//Reply Button Set up
					replyAnswer.setOnAction( b-> {
						Answer answerObj = (Answer) editAnswer.getUserData(); //remake the Answer Object on Click
						currentAnswer = answerObj;

						AnswerQuestionPage answerQuestionPage = new AnswerQuestionPage(databaseHelper);
						answerQuestionPage.showReply(primaryStage, user, currentQuestion, currentAnswer);
					});					
					
					
					//Make Buttons only appear when Mouse Enters AnswerVBox
					newVBox.setOnMouseEntered(event -> {
						setAsPreferredAnswer.setVisible(true);
						editAnswer.setVisible(true);
						deleteAnswer.setVisible(true);
						replyAnswer.setVisible(true);
					});
					newVBox.setOnMouseExited(event -> {
						setAsPreferredAnswer.setVisible(false);
						editAnswer.setVisible(false);
						deleteAnswer.setVisible(false);
						replyAnswer.setVisible(false);
					});
					
					
					//Set the Username, and make sure it can be anonymous
					Label usersname = new Label();
					usersname.setId("discussion-info");
					usersname.setStyle("-fx-font-weight: bold;");
					usersname.setUserData(answersList.getAnswerFromIndex(j));
					
					//Answer newAnswer = (Answer) usersname.getUserData();
					if(answersList.getAnswerFromIndex(j).getHideUserName()) { 
						usersname.setText("User: Anonymous");
					} 
					else {
						usersname.setText("User: " + answersList.getAnswerFromIndex(j).getUserName()); 
						
					}
					newHBox.getChildren().add(usersname);
					//add to hbox
					newHBox.getChildren().addAll(setAsPreferredAnswer, editAnswer, deleteAnswer, replyAnswer);
					
					//The actual Answer Text
					Label answer = new Label(answersList.getAnswerFromIndex(j).getAnswerText());
					answer.setId("discussion-info");
					
					setAsPreferredAnswer.setMinWidth(30);
					
					//make sure the answer doesnt go off the page, and stays in view
					answer.setWrapText(true);
					
					//add the remaining elements
					newVBox.getChildren().addAll(newHBox, answer);
					newHBox.setMinWidth(400);
					newVBox.setMaxWidth(newHBox.getWidth());
					newVBox.setStyle("-fx-border-color: grey");
					replyBox.getChildren().addAll(spacingLabel, newVBox);
					allAnswers.getChildren().add(replyBox);
					
					//Recursivley call the reply tree
					showReplies(answersList.getAnswerFromIndex(j), answersList, primaryStage, user);
				} // if answer has correct level (one greater than parent) && has parentID that equals the parentAnswer ID
			}//for the size of the answersList
		}//if the answersList != empty
	}//End showReplies method
	
	public void updateResolvedLabelText(boolean isResolved) {
		//Update the Resolved Text Label, YES OR NO in Green and RED
		if(isResolved) { 
			resolvedText.setText(" Yes");
			resolvedText.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
		}
		else { 
			resolvedText.setText(" No"); 
			resolvedText.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
		}
	}
	
	//for loading in a particular question
	public void show(Stage primaryStage, User user, Question loadThisQuestion) {
    	//Split up the page.
    	BorderPane layout = new BorderPane(); //Has Different Prebuilt Sections
    	layout.setId("layout");//CSS
    	
    	//The main 2 parts of the Page
    	VBox listOfAllQuestions = new VBox(5); //to store the List of Questions, LEFT SIDE
    	listOfAllQuestions.setId("discussion-control-vertical");//CSS
    	VBox questionContent = new VBox(5); //to store content of Currently Selected QUestion, RIGHT SIDE
    	questionContent.setId("content");//CSS
    	
    	//Text for an error label
    	Label errorLabel = new Label();
    	errorLabel.setWrapText(true);
    	errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
    	
    	//A button for the User to use to logout
    	Button logout = new Button("Logout"); 
    	logout.setId("discussion-control-horizontal");//CSS
		logout.setOnAction( a-> {
				UserLoginPage userLoginPage = new UserLoginPage(databaseHelper); 
				userLoginPage.show(primaryStage);
		});
		
		//Back to User Homepage
		Button userButton = new Button("Switch Roles");
		userButton.setId("discussion-control-horizontal");//CSS
		userButton.setOnAction(a -> {
	    	new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
	    });
		
		//A Button to Launch the New Question Page
		Button newQuestion = new Button ("New Question");
		newQuestion.setId("discussion-control-horizontal");//CSS
		newQuestion.setOnAction( a-> {
			NewQuestion newQuestionPage = new NewQuestion(databaseHelper);
			newQuestionPage.show(primaryStage, user);
		});
		
		//A refresh Button for Fun
		Button refresh = new Button ("Refresh");
		refresh.setId("discussion-control-horizontal");//CSS
		refresh.setOnAction( a-> {
			ViewDiscussions viewDiscussions = new ViewDiscussions(databaseHelper); 
			if(currentQuestion != null) {
				viewDiscussions.show(primaryStage, user, currentQuestion);
			}
			else {
				Question questionNull = new Question(0, "null", "null", "null", false, false, 0, false, 0);//a blank question to load through the ViewDiscussion Page, used to save 424 lines of code
    			viewDiscussions.show(primaryStage, user, questionNull);
			}
		});
				
		//this vbox is for the top section of the border pane
		VBox topSection = new VBox(5);
		//The Top Box to Store the Buttons
		HBox topHBox = new HBox(10);
		//sets the top vbox as the top element
    	layout.setTop(topSection); //Storing this hbox in the Page Layout (BorderPane)
    	
    	Label tagLabel = new Label("Tag:");
    	tagLabel.setStyle("-fx-font-weight: bold;");
    	tagLabel.setId("discussion-info");
    	Label questionTag = new Label("");
    	questionTag.setId("discussion-info");
    	
    	//Labels to Update when we click on a question, these are the defaults
    	Label idLabel = new Label("Id:");
    	idLabel.setStyle("-fx-font-weight: bold;");
    	idLabel.setId("discussion-info");
    	Label idNumber = new Label("");
    	idNumber.setId("discussion-info");
    	Label questionTitleLabel = new Label("");
    	questionTitleLabel.setStyle("-fx-font-weight: bold;");
    	questionTitleLabel.setId("discussion-title");//CSS id
    	
    	Label spaceLabel1 = new Label("\t"); //Tab Label for spacing use for titles/texts
    	Label spaceLabel2 = new Label("\t"); //^^
    	Label spaceLabel3 = new Label("\t"); //^^
    	Label spaceLabel4 = new Label("\t"); //^^
    	
    	Label userNameLabel = new Label("Username: ");
    	userNameLabel.setStyle("-fx-font-weight: bold;");
    	userNameLabel.setId("discussion-info");
    	
    	Label userNameText = new Label("");
    	userNameText.setId("discussion-info");
    	
    	Label resolvedLabel = new Label("Resolved:");
    	resolvedLabel.setId("discussion-info");
    	resolvedLabel.setStyle("-fx-font-weight: bold;");
    	//Set to Gloabl for recursive buttons Label resolvedText = new Label("");
    	resolvedText.setStyle("-fx-font-weight: bold;");
    	resolvedText.setId("discussion-info");
    	
    	Label questionText = new Label();
    	questionText.setStyle("-fx-font-weight: bold;");
    	questionText.setId("discussion-question");//CSS ID
    	
    	Label preferredAnswerLabelTitle = new Label("The Preferred Answer: ");
    	preferredAnswerLabelTitle.setId("discussion-info");
    	preferredAnswerLabelTitle.setMinWidth(130);
    	preferredAnswerLabelTitle.setStyle("-fx-font-weight: bold;");
    	// Turned this guy into a global Label preferredAnswerText = new Label();
    	preferredAnswerText.setWrapText(true); // so the text will wrap around the side to a new line if the page size or answer text demands it.
    	preferredAnswerText.setId("discussion-info");
    	Label answersLabelTitle = new Label("Answers:");
    	answersLabelTitle.setStyle("-fx-font-weight: bold;");
    	answersLabelTitle.setId("discussion-info");
    	
    	//To Store the Basic Info about a particular Question
    	VBox questionContentTitle = new VBox();
    	HBox info1 = new HBox();
    	HBox info2 = new HBox();
    	HBox info3 = new HBox();
    	HBox info4 = new HBox(); 
    	HBox info5 = new HBox();
    	HBox info6 = new HBox();
    	HBox spacer = new HBox();
    	
    	//CSS IDs
    	info1.setId("discussion-hbox");
    	info2.setId("discussion-hbox");
    	info4.setId("discussion-hbox");
    	//Center the HBox contents
    	info1.setStyle("-fx-alignment: center");
    	info2.setStyle("-fx-alignment: center");
    	info4.setStyle("-fx-alignment: center");
    	
    	

    	//add the family together
    	info1.getChildren().addAll(idLabel, idNumber, spaceLabel1, userNameLabel, userNameText, spaceLabel3, resolvedLabel, resolvedText, spaceLabel4, tagLabel, questionTag);
    	spacer.getChildren().add(spaceLabel2);
    	info2.getChildren().addAll(questionTitleLabel);
    	info3.getChildren().addAll();
    	info4.getChildren().addAll(questionText);
    	info5.getChildren().addAll(preferredAnswerLabelTitle, preferredAnswerText);
    	
    	//This is the content of the right side of the border pane which carries all of the answers and question data
    	questionContentTitle.getChildren().addAll(info1, spacer, info2, info3, info4, info5, info6);

    	//Get the updated Questions List and AnswersList from the Database
    	QuestionsList questionsList = databaseHelper.loadQuestionsList();
    	AnswersList answersList = databaseHelper.loadAnswersList();
    	
    	//get size of questionsList given from the database
    	int size = questionsList.getSize();
    	listOfAllQuestions.getChildren().clear(); //reset for each loading
    	
    	//This little guy will dynamically make buttons on the left side of the screen as we have questions in the database
    	if(size > 0) {
    		for (int i = 0; i < size; i++) {  
    			
    			//The creation of a new button
				Button newButton = new Button (questionsList.getQuestionFromIndex(i).getTitle());
				newButton.setId("discussion-control-vertical");//CSS
                newButton.setMinWidth(183);
                newButton.setMaxWidth(183);
				
				//Setting a question Object into each button
				newButton.setUserData(questionsList.getQuestionFromIndex(i));						
				newButton.setOnAction( a-> { 														
					//the Action of each button is realative to the question object inside
					//remake the Question Object on Click
					Question question = (Question) newButton.getUserData(); 
					currentQuestion = question;
					
					//Set all of the respective Labels
					idNumber.setText(String.valueOf(currentQuestion.getId()));
					
					questionTag.setText(databaseHelper.getTag(currentQuestion.getId()));
					
					questionTitleLabel.setText(currentQuestion.getTitle());
					
					System.out.println(databaseHelper.getTag(currentQuestion.getId())); //console output
					
					//Make sure to make the username label to respect the authors' Anonymous request
					if(!currentQuestion.getHideUserName()) {userNameText.setText(currentQuestion.getUserName());}
					else {userNameText.setText("Anonymous"); }
					
					//Update the Resolved Text Label, YES OR NO in Green and RED
					if(currentQuestion.getResolved()) { 
						resolvedText.setText(" Yes");
						resolvedText.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
					}
					else { 
						resolvedText.setText(" No"); 
						resolvedText.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
					}
					//Format the Preffered answer Text, if there is one
					if(currentQuestion.getPreferredAnswerId() != 0) {
						if(answersList.getAnswerFromId(currentQuestion.getPreferredAnswerId()) == null){
							preferredAnswerText.setText("");
						}
						else {
							preferredAnswerText.setText(answersList.getAnswerFromId(currentQuestion.getPreferredAnswerId()).getAnswerText());
						}
					}
					//if there is no prefferred answer, reset the preferredAnswerText to ""
					else {
						preferredAnswerText.setText("");
					}
					 //Set the Question Text Itself
					questionText.setText(currentQuestion.getQuestionText());
					questionText.setWrapText(true);	//for formatting


					//Load Associated Answers into Right side 
					int size2 = answersList.getSize();
					allAnswers.getChildren().clear();
					String [] rolesList = {"admin", "staff", "instructor", "reviewer", "student", "user"};
					
					// Ensure that the list of answers is not empty
					if(size2 > 0) {
						
						// Loop through possible roles and add each answer associated with that role
						// This will ensure that answers from higher roles is added first
						for (int k = 0; k < rolesList.length; k++){

							// loop through answerList
							for (int j = 0; j < size2; j++) { 

								//Check Question ID, Check if it's a Level 0 Answer, Check if it's highest role matches the RolesList[k]
								if((answersList.getAnswerFromIndex(j).getQuestionLinkID() == currentQuestion.getId()) && (answersList.getAnswerFromIndex(j).getHighestRole().equals(rolesList[k]))  && (answersList.getAnswerFromIndex(j).getLevel() == 0)) { // update this line
									
									//Create a new Hbox for each answer, including a button and a label
									HBox newHBox = new HBox(5);
									VBox newVBox = new VBox();
									newVBox.setId("answer-box");
									
									//Dynamix element creation
									Button setAsPreferredAnswer = new Button ("Preferred Answer");
									setAsPreferredAnswer.setId("answer-box-buttons");
									setAsPreferredAnswer.setStyle("-fx-font-size: 8px;");
									setAsPreferredAnswer.setPrefHeight(12);
									
									//Storing the answer obj, in each preferred ans. button, for future reference
									setAsPreferredAnswer.setUserData(answersList.getAnswerFromIndex(j));
								
									//Set Button Clicks
									setAsPreferredAnswer.setOnAction( b-> {
										//remake the Answer Object on Click
										Answer answerObj = (Answer) setAsPreferredAnswer.getUserData(); 
										currentAnswer = answerObj;
										currentQuestion.setPreferredAnswerId(currentAnswer.getId());
										currentQuestion.setResolved(true);
										updateResolvedLabelText(currentQuestion.getResolved());
										//update the preffered answer ID number in the database too
										try {
											databaseHelper.updateQuestion(currentQuestion);
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										preferredAnswerText.setText(currentAnswer.getAnswerText());
										//Reload the page
										
									});
									
									//If you are not the creator of the question, then you don't get to See the preffered answer button
									if(!answersList.getAnswerFromIndex(j).getUserName().equals(user.getUserName())) {
										setAsPreferredAnswer.setVisible(false);
									}
									

									//edit and delete buttons set up
									Button editAnswer = new Button ("Edit");
									editAnswer.setId("answer-box-buttons");
									editAnswer.setStyle("-fx-font-size: 8px;");
									editAnswer.setPrefHeight(12);
									Button deleteAnswer = new Button ("Delete");
									deleteAnswer.setId("answer-box-buttons");
									deleteAnswer.setStyle("-fx-text-fill: red; -fx-font-size: 8px;");
									deleteAnswer.setPrefHeight(12);
									Button replyAnswer = new Button ("Reply");
									replyAnswer.setId("answer-box-buttons");
									replyAnswer.setStyle("-fx-text-fill: black; -fx-font-size: 8px;");
									replyAnswer.setPrefHeight(12);
									
									//give each button an answer obj for future reference
									editAnswer.setUserData(answersList.getAnswerFromIndex(j));
									deleteAnswer.setUserData(answersList.getAnswerFromIndex(j));
									replyAnswer.setUserData(answersList.getAnswerFromIndex(j));
									
									//Set all to Invisible by Default
									setAsPreferredAnswer.setVisible(false);
									editAnswer.setVisible(false);
									deleteAnswer.setVisible(false);
									replyAnswer.setVisible(false);
									
									//Edit Button
									editAnswer.setOnAction( b-> {
										Answer answerObj = (Answer) editAnswer.getUserData(); //remake the Answer Object on Click
										currentAnswer = answerObj;
										
										//Make sure only the answer creator is the only one able to edit their answer
										if(currentAnswer.getUserName().equals(user.getUserName())) {
											//currentQuestion.setPreferredAnswerId(currentAnswer.getId());
											AnswerQuestionPage answerQuestionPage = new AnswerQuestionPage(databaseHelper);
											answerQuestionPage.showUpdateAnswer(primaryStage, user, currentQuestion, currentAnswer);
										}
									});
									//delete Button
									deleteAnswer.setOnAction( b-> {
										Answer answerObj = (Answer) deleteAnswer.getUserData(); //remake the Answer Object on Click
										currentAnswer = answerObj;
										
										if(currentAnswer.getUserName().equals(user.getUserName()) || user.getIsAdmin()) {
											try {
												databaseHelper.deleteAnswer(currentAnswer.getId());
											} catch (SQLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											//recalls the stage, to refresh 
											ViewDiscussions viewDiscussions = new ViewDiscussions(databaseHelper); 
											viewDiscussions.show(primaryStage, user, currentQuestion);
										}
									});
									//Reply Button
									replyAnswer.setOnAction( b-> {
										Answer answerObj = (Answer) editAnswer.getUserData(); //remake the Answer Object on Click
										currentAnswer = answerObj;

										AnswerQuestionPage answerQuestionPage = new AnswerQuestionPage(databaseHelper);
										answerQuestionPage.showReply(primaryStage, user, currentQuestion, currentAnswer);
									});
									
									//Make Buttons only appear when Mouse Enters AnswerVBox
									newVBox.setOnMouseEntered(event -> {
										setAsPreferredAnswer.setVisible(true);
										editAnswer.setVisible(true);
										deleteAnswer.setVisible(true);
										replyAnswer.setVisible(true);
									});
									newVBox.setOnMouseExited(event -> {
										setAsPreferredAnswer.setVisible(false);
										editAnswer.setVisible(false);
										deleteAnswer.setVisible(false);
										replyAnswer.setVisible(false);
									});
									
									
									
									
									
									//Set the Username, and make sure it can be anonymous
									Label usersname = new Label();
									usersname.setId("discussion-info");
									usersname.setStyle("-fx-font-weight: bold;");
									usersname.setUserData(answersList.getAnswerFromIndex(j));
									
									//Answer newAnswer = (Answer) usersname.getUserData();
									if(answersList.getAnswerFromIndex(j).getHideUserName()) { 
										usersname.setText("User: Anonymous");
									} 
									else {
										usersname.setText("User: " + answersList.getAnswerFromIndex(j).getUserName()); 
										
									}
									//add to hbox
									newHBox.getChildren().addAll(usersname, setAsPreferredAnswer, editAnswer, deleteAnswer, replyAnswer);
									
									//The actual Answer Text
									Label answer = new Label(answersList.getAnswerFromIndex(j).getAnswerText());
									answer.setId("discussion-info");
									
									setAsPreferredAnswer.setMinWidth(30);
									
									//make sure the answer doesnt go off the page, and stays in view
									answer.setWrapText(true);
									
									//add the remaining elements
									newVBox.getChildren().addAll(newHBox, answer);
									newVBox.setStyle("-fx-border-color: grey");
									allAnswers.getChildren().add(newVBox);

									//Recursive Add Replies Function
									//For Adding all Subrepplies in an orderly manner.

									showReplies(answersList.getAnswerFromIndex(j), answersList, primaryStage, user);

								}//If Check Question ID, Check if it's a Level 0 Answer, Check if it's highest role matches the RolesList[k]
							}//loop through the answerList
						
						}//loop through the roles list (for ranking level 0 answers)
						
					}//if answerList size != 0
				});//end new button(questions on the left side) action creation
				
				
				//Add the question Buttons to the Stage basically.
				listOfAllQuestions.getChildren().add(newButton);
				if(questionsList.getQuestionFromIndex(i).getId() == loadThisQuestion.getId()) {
					newButton.fire();
				}
			}//for all questions in the list
        }//if the questionsList != empty 
    	//END dynamic stage set up
        
    	//Answer Question Button
    	Button answerQuestion = new Button ("Answer Question");
    	answerQuestion.setId("discussion-control-horizontal");//CSS
    			answerQuestion.setOnAction( a-> {
    				if(currentQuestion != null) {
    					AnswerQuestionPage answerQuestionPage = new AnswerQuestionPage(databaseHelper); 
    					answerQuestionPage.show(primaryStage, user, currentQuestion);
    				} else { errorLabel.setText("Please Select A Question to Answer");}
    				
    			});	 
    	
    	//Update Question Button
    	Button updateCurrentQuestion = new Button ("Update Question");
    	updateCurrentQuestion.setId("discussion-control-horizontal");//CSS
    	updateCurrentQuestion.setOnAction( a-> {
    		if(currentQuestion != null) {
    			if(currentQuestion.getUserName().equals(user.getUserName())) {
    				NewQuestion updateQuestion = new NewQuestion(databaseHelper); 
    				updateQuestion.showUpdate(primaryStage, user, currentQuestion);
    			}
    			else {
    				errorLabel.setText("You are not the Author of this Question");
    			}
    		} else {errorLabel.setText("Please Select A Question to Update");} 
 
    	});	
    		
    		
    	//Deletes the current question with Specific Admin Rights
        Button deleteCurrentQuestion = new Button ("Delete Question");
        deleteCurrentQuestion.setId("discussion-control-horizontal");//CSS
        deleteCurrentQuestion.setOnAction( a-> {
        	if(currentQuestion != null) {
        		if(currentQuestion.getUserName().equals(user.getUserName()) || user.getIsAdmin() ) {
        			try {
        				databaseHelper.deleteAnswersLinked(currentQuestion.getId());
        				databaseHelper.deleteQuesiton(currentQuestion.getId());
        			} catch (SQLException e) {
        				e.printStackTrace();
        			}     			
        			//recalls the stage, to refresh question Building
        			ViewDiscussions viewDiscussions = new ViewDiscussions(databaseHelper); 
        			Question questionNull = new Question(0, "null", "null", "null", false, false, 0, false, 0);//a blank question to load through the ViewDiscussion Page, used to save 424 lines of code
        			viewDiscussions.show(primaryStage, user, questionNull);
        		}
        		else { errorLabel.setText("You are not the Author of this Question");}
        		
        	} else {errorLabel.setText("Please Select A Question to Delete");}
        });	
 
        //Search For A Question Button
		Button search = new Button ("Search");
		search.setId("discussion-control-horizontal");//CSS
		search.setOnAction( a-> {
			SearchDiscussions searchDisc = new SearchDiscussions(databaseHelper);
			searchDisc.show(primaryStage, user);
		});
		
		//Search for an Exact Question Button
		Button searchExact = new Button ("Exact Search");
		searchExact.setId("discussion-control-horizontal");//CSS
        searchExact.setOnAction( a-> {
            SearchExactQuestion searchEx = new SearchExactQuestion(databaseHelper);
            searchEx.show(primaryStage, user);
        });
    			
    	//Putting the Buttons inside of the HBOX
    	topHBox.getChildren().addAll(logout, userButton, newQuestion, refresh, answerQuestion, updateCurrentQuestion, deleteCurrentQuestion, search,searchExact);
    	topSection.getChildren().addAll(topHBox, errorLabel);
    	questionContent.getChildren().addAll(questionContentTitle ,answersLabelTitle, allAnswers);
    	
    	//Setting up the reset of the page
    	ScrollPane scrollPaneLeft = new ScrollPane(listOfAllQuestions);//Question Titles
    	ScrollPane scrollPaneRight = new ScrollPane(questionContent); //Reading Currently selected questoin and answers
    	//For CSS Styling
    	scrollPaneRight.setId("question-content-box");
    	scrollPaneLeft.setId("question-list-box");
    	
    	
    	//seting some widths of the page
    	scrollPaneLeft.setPrefWidth(200);
    	scrollPaneRight.setFitToWidth(true);
    	
    	//set up the elements orientation
    	layout.setLeft(scrollPaneLeft);
    	layout.setCenter(scrollPaneRight);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    info2.setMinWidth(scrollPaneRight.getWidth());
	    info4.setMinWidth(scrollPaneRight.getWidth());
	    
	    Scene userScene = new Scene(layout, 1000, 400);
	    userScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("View Discussion Page");
	    
	    primaryStage.show();
    }
	
		
 
}