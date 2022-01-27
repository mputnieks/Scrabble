package Server;

import java.util.ArrayList;
import java.util.List;

import model.Player;

public class PlayerList {
 private static PlayerList instance = null;
 
 private PlayerList() {} // private constructor prevents anyone else from instantiating.
 
 public static PlayerList getInstance() {
	 if(instance == null) {
		 instance = new PlayerList();
	 }
	 return instance;
 }
 
 // NOT THREAD SAFE!!! IMPLEMENT SINCHRONIZATION LATER!
 private List<Player> players = new ArrayList<>();
 public List<Player> getPlayers(){
	 return this.players;
 }
 
}
