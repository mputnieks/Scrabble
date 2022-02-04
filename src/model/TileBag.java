package model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TileBag {
	
	private List<Tile> tiles;
	
	public TileBag(List<Tile> full_tile_kit) {
		this.tiles = full_tile_kit;
	}
	
	public static TileBag createTileBag(String filename) throws IOException{
		List<Tile> all_tiles = new ArrayList<Tile>();
		Path filePath = new File(filename).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		for (int i = 0; i<stringList.size(); i++) {
			String[] data = stringList.get(i).split(";"); //every data should have 4 members
			int amount = Integer.parseInt(data[0]);
			String name = data[1];
			int value = Integer.parseInt(data[2]);
			for (int j = 0; j<amount; j++) {
				all_tiles.add(new Tile(name, value));
			}
		}
		return new TileBag(all_tiles);
	}
	
	public boolean isEmpty() {
		return tiles.isEmpty();
	}
	
	public Tile getRandomTile() {
		int index = (int)(Math.random()*tiles.size());
		Tile tile = tiles.get(index);
		tiles.remove(index);
		return tile;
	}
	
	public Tile exchangeTile(Tile t) {
		Tile new_tile = getRandomTile();
		tiles.add(t);
		return new_tile;
	}
	
	public Tile getTileByName(String name) {
		for (int i = 0; i<tiles.size(); i++) {
			if (name.equals(tiles.get(i).getName())) {
				Tile t = tiles.get(i);
				tiles.remove(i);
				return t;
			}
		}
		return null;
	}

	public void addTiles(List<Tile> t) {
		tiles.addAll(t);
	}
}
