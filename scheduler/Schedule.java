package scheduler;

import java.util.*;

public class Schedule {

	static Day[] seasonSchedule;
	static Boolean debug = true;

	public Schedule() {
		seasonSchedule = new Day[180];
		for(int i=0; i<seasonSchedule.length; i++) {
			seasonSchedule[i] = new Day();
		}
	}

	public void addGameToSchedule(String home, String away, String gametime, String field, int day) {
		Game addingGame = new Game(home,away,gametime,field);
		addGame(addingGame,day);
	}

	private void addGame(Game addGame, int day) {		
		Day checkDay = seasonSchedule[day];
		checkDay.games.add(addGame);
		seasonSchedule[day] = checkDay;
	}

	private void removeGame(int gameNum, int day) {
		Day removeGameDay = seasonSchedule[day];
		if(removeGameDay.games.get(gameNum) != null) {
			removeGameDay.games.remove(gameNum);
		}
		seasonSchedule[day] = removeGameDay;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<seasonSchedule.length; i++) {
			sb.append("Day: " + i + "\n");
			if(seasonSchedule[i] != null) {
				sb.append(seasonSchedule[i].toString());
			}
		}
		return sb.toString();
	}

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

	public boolean repOk() {
		return true;
	}

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }

}