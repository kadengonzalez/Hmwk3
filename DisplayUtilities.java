package databasePart1;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import databasePart1.*;

@SuppressWarnings("unused")
public class DisplayUtilities{
	
	// *************************************************************************************************
	// The following are for creating Button and setting properties for Button
	// *************************************************************************************************
	public Button createButton (String text, int x, int y) {
		Button temp = new Button();
		temp.setText(text);
		temp.setLayoutX(x);
		temp.setLayoutY(y);
		return temp;
	}
	
	
	public void setButtonGrey(Button button) {
		button.setStyle("-fx-background-color: #D3D3D3;");
	}
	
	
	public void setButtonGreen(Button button) {
		button.setStyle("-fx-background-color: #90EE90;");
	}
	
	
	public void setButtonRed(Button button) {
		button.setStyle("-fx-background-color: #FFCCCB;");
	}
	
	
	public void setButtonLocation(Button button, double xVal, double yVal) {
		button.setLayoutX(xVal);
		button.setLayoutY(yVal);
	}
	
	
	
	// *************************************************************************************************
	// The following are for creating Labels
	// *************************************************************************************************
	public Label createLabel (String text, int x, int y) {
		Label temp = new Label();
		temp.setText(text);
		temp.setLayoutX(x);
		temp.setLayoutY(y);
		return temp;
	}
	
	
	// *************************************************************************************************
	// The following are for creating TextFIELD's
	// *************************************************************************************************
	public TextField createTextField(int x, int y) {
		TextField temp = new TextField();
		temp.setLayoutX(x);
		temp.setLayoutY(y);
		return temp;
	}
	
	
	// *************************************************************************************************
	// The following are for creating TextAREA's
	// *************************************************************************************************
	public TextArea createTextArea (String text, Boolean wrap, Boolean edit, int xLocation, int yLocation, int xPrefSize, int yPrefSize) {
		TextArea temp = new TextArea();
		temp.setText(text);
		temp.setWrapText(wrap);
		temp.setEditable(edit);	
		temp.setLayoutX(xLocation);
		temp.setLayoutY(yLocation);
		temp.setPrefSize(xPrefSize, yPrefSize);
		return temp;
	}
	
	
}