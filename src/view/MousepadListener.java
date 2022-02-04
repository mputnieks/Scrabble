package view;

import Main.Controller;
import Main.Scrabble;
import javafx.scene.Group;
import model.Tile;

public class MousepadListener {

	private Tile picked_tile;
	private int mouseX = -1;
	private int mouseY = -1;
	private Group sprite;
	
	public MousepadListener(Controller controller) {
		sprite = new Group();
		
		Scrabble.root.setOnMouseMoved(e -> {
            mouseX = (int) e.getX();
            mouseY = (int) e.getY();
        });
		
		Scrabble.root.setOnMouseDragged(e -> {
            mouseX = (int) e.getX();
            mouseY = (int) e.getY();
        });
		
		Scrabble.root.setOnMouseClicked(e -> {
			mouseX = (int) e.getX();
            mouseY = (int) e.getY();
            controller.onMouseClick(mouseX, mouseY);
        });
	}
	
	public void setTile(Tile t) {
		if (t != null) {
			sprite.getChildren().add(t.getGraphics());
		}else {
			sprite.getChildren().remove(this.picked_tile.getGraphics());
		}
		this.picked_tile = t;
	}
	
	public Tile getTile() {
		return picked_tile;
	}
	
	public void initGraphics() {
		Controller.addToRoot(sprite);
	}
	
	public int getX() {
		return mouseX;
	}
	public int getY() {
		return mouseY;
	}
	
	public void update() {
		//System.out.println("x: " + getX() + " y: " + getY());
		sprite.setTranslateX(getX()-Scrabble.root.getWidth()/2);
		sprite.setTranslateY(getY()-Scrabble.root.getHeight()/2);
	}
}
