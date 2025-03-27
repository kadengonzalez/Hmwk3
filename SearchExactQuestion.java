package databasePart1;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import databasePart1.Question;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import javafx.scene.control.*;

// Delete question page; given an id, a question is deleted.

public class SearchExactQuestion {
	
	
	private final DatabaseHelper databaseHelper;

    public SearchExactQuestion(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
	public void show(Stage primaryStage, User user) {
		
		Stage newStage = new Stage();
		Pane pane = new Pane();
		
		//Elements of the stage
		Label instructions = new Label();
		instructions.setLayoutX(55);
		instructions.setLayoutY(50);
		instructions.setText("Input the EXACT text you'd like.");
        //instructions.setFont(new Font("Verdana", 10));

	    TextField input = new TextField();
	    input.setLayoutX(55);
		input.setLayoutY(75);
        instructions.setVisible(true);
		
	    Button collectButton = new Button("Search");
	    collectButton.setLayoutX(230);
		collectButton.setLayoutY(125);
	    
	    Label responseLabel = new Label();
	    responseLabel.setLayoutX(100);
		responseLabel.setLayoutY(175);
	    
	    Label tempLabel = new Label();
	    tempLabel.setLayoutX(100);
		tempLabel.setLayoutY(150);
	    

		//Fulfill the user stories
	    
	    collectButton.setOnAction(e -> {

	    	String userInput = input.getText(); //set the textfeild to a var
	    	
	    	boolean inDataBase;

            Question question = databaseHelper.getQuestion(userInput);
            
	    	if(question != null) {
	            ViewDiscussions viewDiscussions = new ViewDiscussions(databaseHelper); 
				viewDiscussions.show(primaryStage, user, question);
	    	}
	    	else {
	    		System.out.println("Hey, not working pal.");
	    	}
            
            //incase they want to delete multiple users
            input.setText("");
            responseLabel.setText("");
            responseLabel.setVisible(false);
	    	
	    });
	    
	    
	    //add everything to the Scene
	    pane.getChildren().add(input);
	    pane.getChildren().add(collectButton);
	    pane.getChildren().add(responseLabel);
		pane.getChildren().add(instructions);
	    pane.getChildren().add(tempLabel);
	    
	    
	    Scene searchExact = new Scene(pane, 400, 200);
	    searchExact.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());

	    newStage.setScene(searchExact);
	    newStage.setTitle("Search Exact Question");
	    
	    newStage.show();
	    
	}
	
}