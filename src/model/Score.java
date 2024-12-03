package model;

import model.Field;
import Main.Controller;
import javafx.scene.Group;
import javafx.scene.Node;
import view.VisualsManager;

/*
 * Score to display on top of the screen.
 */
public class Score {

	private Group sprite = new Group();
	private Node sub_sprite;
	private int value;
	private String name;
	
	public Score(int value, String name) {
		this.value = value;
		this.name = name;
		this.sub_sprite = VisualsManager.getScore(name+": "+value+"");
		this.sprite.getChildren().add(this.sub_sprite);
		sprite.setTranslateX(0);
	    sprite.setTranslateY(-Field.FIELD_SIZE*10);
	}

	public void setName(String name) {
		this.name = name;
		updateGraphics();
	}

	public void setValue(int value) {
		this.value = value;
		updateGraphics();
	}

	public void updateGraphics() {
		sprite.getChildren().remove(sub_sprite);
		sub_sprite = VisualsManager.getScore(name+": "+value+"");
		sprite.getChildren().add(sub_sprite);
	}

	public void initGraphics() {
	    Controller.addToRoot(sprite);
	}
}