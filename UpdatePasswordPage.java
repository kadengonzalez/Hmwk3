package databasePart1;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// ARE YOU WORKING RIGHT NOW******************************

public class UpdatePasswordPage {
	
	private final DatabaseHelper databaseHelper;

    public UpdatePasswordPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
	public void show(Stage primaryStage, User user) {
		// Create new layout
		VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
		// Create page elements
		Label title = new Label("You have used a One Time Password\nPlease enter a new Password");
		
	    TextField inputTextField = new TextField();
	    
	    Button collectButton = new Button("Enter");
	    
	    Label responseLabel = new Label("Your new password is saved. Thank you!");
	    
		Label passwordErrorLabel = new Label("");
	    responseLabel.setVisible(false); // Hide until new password is established

		
		// On ENTER button click
	    collectButton.setOnAction(e -> {
	    	// Collect the user's input
			String userInput = inputTextField.getText();
			String errMessage = PasswordEvaluator.evaluatePassword(userInput); //get potential error message from password check
	    	
			
			if(errMessage.isEmpty()) { // Password is valid
	    		responseLabel.setVisible(true); // display to user that it was accepted
	    		
	    		// Set the new password for user in database
				try { 
					databaseHelper.setPassword(user.getUserName(), userInput);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Make user login once password is updated
				new UserLoginPage(databaseHelper).show(primaryStage); 
				
	    	} else {// Password is not valid
				errMessage = ("Please Follow Password Requirments: " + errMessage); // Collect the error message
			}
			
	    	passwordErrorLabel.setText(errMessage); // Display password error
	    });	    
	    
	    
		// Add the children to the page
	    layout.getChildren().addAll(title, inputTextField, collectButton, passwordErrorLabel, responseLabel);
	    
	    
	    // Configure and display page
	    Scene changePasswordScene = new Scene(layout, 400, 200);
	    changePasswordScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());

	    primaryStage.setScene(changePasswordScene);;
	    primaryStage.setTitle("Change Password Page");
	    primaryStage.show();
	}	
}