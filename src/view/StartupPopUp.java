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

public class StartupPopUp {

	private Group group = VisualsManager.getBlankTilePopUp();
	
	public StartupPopUp(Controller c) {
		Label name = new Label("Nickname: ");
		Label l = new Label("The game can start when all players are ready.");
		Label l2 = new Label("If you are waiting for someone to connect do not click ready.");
		Label waiting = new Label("Waiting for the game to start.");
	    TextField tf1 = new TextField();
	    Button b1 = new Button("New Game");
	    Button b2 = new Button("Join Game");
	    Button b3 = new Button("Ready To Start!");
	    GridPane root = new GridPane();
	    root.addRow(0, name, tf1);
	    root.addRow(1, b1, b2);
	    
	    b1.setOnAction(new EventHandler<ActionEvent>() {   
            @Override  
            public void handle(ActionEvent arg0) {
            	String input = tf1.getText().toString();
            	if(input != null && !input.trim().equals("")) {
            		c.createGame(input);
            		root.getChildren().removeAll(root.getChildren());
            		root.addRow(0, l);
            		root.addRow(1, l2);
            		root.addRow(2, b3);
            	}else {
            		Label error = new Label("Enter a short nickname!");
            		root.addRow(2, error);
            	}
            }
        } );
	    
	    b2.setOnAction(new EventHandler<ActionEvent>() {   
            @Override  
            public void handle(ActionEvent arg0) {
            	String input = tf1.getText().toString();
            	if(input != null  && !input.trim().equals("")) {
            		c.joinGame(input);
            		root.getChildren().removeAll(root.getChildren());
            		root.addRow(0, l);
            		root.addRow(1, l2);
            		root.addRow(2, b3);
            	}else {
            		Label error = new Label("Enter a short nickname!");
            		root.addRow(2, error);
            	}
            }
        } );
	    
	    b3.setOnAction(new EventHandler<ActionEvent>() {   
            @Override  
            public void handle(ActionEvent arg0) {
            	c.notifyReady();
            	root.getChildren().removeAll(root.getChildren());
        		root.addRow(0, waiting);
            }
        } );
	    
	    group.getChildren().add(root);
		Scrabble.root.getChildren().add(group);
		
	}

	public void remove() {
		Scrabble.root.getChildren().remove(group);
	}
	
}
