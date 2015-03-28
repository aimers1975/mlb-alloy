package scheduler;

import java.util.*;

public class Game {
	String hometeam;
	String awayteam;
	String time;
	String location;	

	public Game(String home, String away, String gametime, String field) {
		hometeam = home;
		awayteam = away;
		time = gametime;
		location = field;
	}	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("    Home: " + hometeam + "\n");
		sb.append("    Away: " + awayteam + "\n");
		sb.append("    Time: " + time + "\n");
		sb.append("    Location: " + location + "\n");
		return sb.toString();
	}
}