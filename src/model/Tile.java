package model;

import javafx.scene.Node;
import view.VisualsManager;

/*
 * Tiles can be stored in a player, a game, 
 * or one-to-one on a square.
 */
public class Tile {

	private Node sprite;
	private boolean fixed;
	private String name;
	private int value;
	
	public Tile(String name, int value) {
		this.name = name;
		this.value = value;
		this.sprite = VisualsManager.getTile(name);
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
	    return sprite;
	}
}
