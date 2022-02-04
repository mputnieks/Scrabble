package model;

public abstract class Player {
	
	    // -- Instance variables -----------------------------------------

		private TileTray tray = new TileTray(10, 0, Field.FIELD_SIZE*9);
	    private String name;
	    private int score;
	    private boolean ready = false;

	    // -- Constructors -----------------------------------------------

	    public Player(String name) {
	        this.name = name;
	        this.score = 0;
	    }

	    // -- Queries ----------------------------------------------------
	    
	    public String getName() { return name; }

	    public TileTray getTray() { return tray; }
	    
	    public int getScore() { return score; }
	    
	    public void setReady() { ready = true; }
	    
	    public boolean isReady() { return ready; }
	    
	    public void addToScore(int n) { score += n; }
	    
	    public void FillTray(TileBag bag) {tray.fill(bag);}

	    public abstract String determineMove(Board board);
	}
