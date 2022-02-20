package model_testable;

import javafx.scene.Node;

/*
 * Tiles can be stored in a player, a game, 
 * or one-to-one on a square.
 */
public class Tile {

	private boolean fixed;
	private boolean blank = false;
	private String name;
	private int value;
	
	public Tile(String name, int value) {
		if (name.equals("-")) {
			this.blank = true;
		}
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isFixed() {
		return fixed;
	}
	
	public boolean isBlank() {
		return blank;
	}
	
	/**
     * Sets this <code>Tile</code> to be fixed, note: this cannot be undone.
     * @ensures The property <code>fixed</code> of this <code>Tile</code> is set to true.
     */
	public void fixTile() {
		fixed = true;
	}
	
	/**
     * Returns the <code>sprite</code> of this <code>Tile</code>.
     */
	public Node getGraphics() {
	    return null;
	}

	public void setName(String input) {
		this.name = input;
	}
}
