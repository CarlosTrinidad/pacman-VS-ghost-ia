package game.entries.ghosts;

import game.core.Game;

public interface GhostAI {
	public final int BLINKY = 0;
	public final int PINKY = 1;
	public final int CLYDE = 2;
	public final int INKY = 3;
	
	public int[] execute(int ghostType, Game game, long timeDue);
}
