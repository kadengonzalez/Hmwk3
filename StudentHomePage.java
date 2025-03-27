package databasePart1;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.*;


public class StudentHomePage {

	private final DatabaseHelper databaseHelper;

    public StudentHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	public void show(Stage primaryStage, User user) {	
		//Create Page layout
		VBox vLayout = new VBox(10);
		vLayout.setId("standard-page-vbox-layout");
		
		HBox hLayout = new HBox(10);
		DisplayUtilities displayUtilities = new DisplayUtilities();
		
		
		// Create hello label		
		Label helloLabel = displayUtilities.createLabel("Hello Student", 200, 100);
		
		
		// Button to send student to ViewDiscussion page 
		Button viewDiscussionButton = new Button ("View Discussion");
		
		viewDiscussionButton.setOnAction( a-> {
			ViewDiscussions viewDiscussions = new ViewDiscussions(databaseHelper);
			Question question = new Question(0, "null", "null", "null", false, false, 0, false, 0);//a blank question to load through the ViewDiscussion Page, used to save 424 lines of code
			viewDiscussions.show(primaryStage, user, question);
		});

		
		// Create logout button
		Button logoutButton = new Button("Logout");
		
		logoutButton.setOnAction( a-> {
			UserLoginPage userLoginPage = new UserLoginPage(databaseHelper); 
			userLoginPage.show(primaryStage);
		});

		
		// Add all elements to layout
		hLayout.getChildren().addAll(viewDiscussionButton, logoutButton);
		vLayout.getChildren().addAll(helloLabel, hLayout);
		
		// Configure and set window visible
		Scene scene = new Scene(vLayout, 800, 400);
        scene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setTitle("StudentHomePage");
		primaryStage.show();
	}
}