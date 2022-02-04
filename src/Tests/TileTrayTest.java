package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Tile;
import model.TileBag;
import model.TileTray;
import networking.Protocol;


public class TileTrayTest {
	
	private static final String PATH = "./files/"; // Path to the test folder with special tile mappings
	
    private TileTray tray;
    private Tile t1 = new Tile("1", 1);
    private Tile t2 = new Tile("2", 2);
    private Tile t3 = new Tile("3", 3);
    private Tile t4 = new Tile("4", 4);
    private int field_count = 10;
    private TileBag bag;

    @BeforeEach
    public void setUp() throws Exception {
        tray = new TileTray(field_count, 0, 0);
        try {
			this.bag = TileBag.createTileBag(PATH+"complete_tile_list.txt");
		} catch (IOException e) {
			System.out.println("Error: file complete_tile_list.txt failed loading!");
		}
    }
    
    @Test
    public void testAddRemoveTile() throws Exception {
    	tray.addTile(t1);
    	assertEquals(tray.getTiles().size(), 1);
    	tray.removeTile(t1);
        assertEquals(tray.getTiles().size(), 0);
    }
    
    @Test
    public void testTilesToString() throws Exception {
    	tray.addTile(t1);
    	tray.addTile(t2);
    	tray.addTile(t3);
    	assertTrue(tray.tilesToString().contains(t1.getName()) && tray.tilesToString().contains(t2.getName()) && tray.tilesToString().contains(t3.getName()));
    	tray.removeTile(t1);
        assertEquals(tray.tilesToString().split(Protocol.AS).length, 2);
    }

    @Test
    public void testAddTile() throws Exception {
    	tray.addTile(t1);
        assertEquals(tray.getTiles().get(0), t1);
    }
    
    @Test
    public void testFillTray() throws Exception {
    	tray.fill(bag);
        assertEquals(tray.getTiles().size(), 7);
    }
}