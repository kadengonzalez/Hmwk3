package databasePart1;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.*;


public class InstructorHomePage {

	private final DatabaseHelper databaseHelper;

    public InstructorHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	public void show(Stage primaryStage, User user) {
		DisplayUtilities displayUtilities = new DisplayUtilities();
		
		//Set Up the Layout
		VBox layout = new VBox(10);
		layout.setId("standard-page-vbox-layout"); //Configure CSS
		
		
		// Create hello label		
		Label helloLabel = displayUtilities.createLabel("Hello Instructor", 200, 100);
		
		
		// Create logout button
		Button logoutButton = new Button("Logout"); 
		
		logoutButton.setOnAction( a-> {
				UserLoginPage userLoginPage = new UserLoginPage(databaseHelper); 
				userLoginPage.show(primaryStage);
		});

		
		// Add all elements to layout
		layout.getChildren().addAll(helloLabel, logoutButton);
		
		
		// Configure and set window visible
		Scene scene = new Scene(layout, 800, 400);
		scene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("UpdateRolesPage");
		primaryStage.show();
	}
}