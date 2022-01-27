package model;

import Main.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import view.VisualsManager;

public class NextMoveButton {

	private enum ButtonMode {MOVE, SKIP};
	
	private Group sprite;
	private Button btn;
	private ButtonMode mode = ButtonMode.SKIP;
	
	public NextMoveButton(Controller c) {
		this.sprite = VisualsManager.getNextMoveButton();
		
		this.btn = new Button("SKIP");
        btn.setOnAction(new EventHandler<ActionEvent>() {   
            @Override  
            public void handle(ActionEvent arg0) {
            	if (mode == ButtonMode.MOVE) {
            		c.doMove();
            	}
            	else {
            		c.skipMove();
            	}
            }
        } );
        btn.setTranslateX(Field.FIELD_SIZE/2);
        btn.setTranslateY(Field.FIELD_SIZE/2);
        sprite.getChildren().add(btn);
	}
	
	public void update(Board board) {
		if (board.hasNonFixed()) {
			mode = ButtonMode.MOVE;
			btn.setText("MOVE");
		}else {
			mode = ButtonMode.SKIP;
			btn.setText("SKIP");
		}
	}
	
	public void initGraphics() {
		Controller.addToRoot(sprite);
	}	
}