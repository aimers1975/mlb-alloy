package scheduler; 

import scheduler.Game;
import java.util.*;

public class Day {
	ArrayList<Game> games;

	public Day() {
		games = new ArrayList<Game>();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<games.size(); i++) {
			sb.append("  Game: " + i + "\n");
			sb.append(games.get(i).toString());
		}
		return sb.toString();
	}
}