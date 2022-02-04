package view;

import Main.Controller;
import Main.Scrabble;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Tile;

public class BlankTilePopUp {

	public BlankTilePopUp(Tile t, Controller c) {
		
		Group group = VisualsManager.getBlankTilePopUp();
		Label user_id = new Label("BLANK TILE:");
	    TextField tf1 = new TextField();
	    Button b = new Button("Submit");
	    GridPane root = new GridPane();
	    root.addRow(0, user_id, tf1);
	    root.addRow(1, b);
	    b.setOnAction(new EventHandler<ActionEvent>() {   
            @Override  
            public void handle(ActionEvent arg0) {
            	String input = tf1.getText().toString().toUpperCase();
            	if(input.length() == 1) {
            		System.out.println("BLANK = "+input);
            		t.setName(input);
            		Scrabble.root.getChildren().remove(group);
            		c.finishMove();
            	}else {
            		Label error = new Label(input+" Is not a valid letter!");
            		root.addRow(2, error);
            	}
            }
        } );
	    
	    group.getChildren().add(root);
		Scrabble.root.getChildren().add(group);
		
	}
	
}
