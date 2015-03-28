package scheduler;

import java.util.*;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import scheduler.Day;
import scheduler.SchedulerConstants;

public class Schedule {

	Day[] seasonSchedule;
	HashSet<String> allTeams; 
	Boolean debug = true;

	public Schedule() {
		seasonSchedule = new Day[180];
		allTeams = new HashSet<String>();
		for(int i=0; i<seasonSchedule.length; i++) {
			seasonSchedule[i] = new Day();
		}
	}

	public void addGameToSchedule(String home, String away, String gametime, String field, int day) {
		Game addingGame = new Game(home,away,gametime,field);
		allTeams.add(home);
		allTeams.add(away);
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

	public int countGamesForTeam(String team) {
		int count = 0;
        for(Day thisDay : seasonSchedule) {
            for(Game thisGame : thisDay.games) {
                if((thisGame.hometeam).equals(team) || (thisGame.awayteam).equals(team)) {
                    count++;
                }
            }
        }
        //debug("The count is: " + count);
        return count;
	}


	public boolean eachTeamHas176Games(String[] teams) {
		boolean result = true;
		for(String thisTeam : teams) {
			if(countGamesForTeam(thisTeam) != 176) {
				debugToFile("Team: " + thisTeam + " has only: " + countGamesForTeam(thisTeam) + " games! Not yet a full schedule!");
				result = false;
			} 
		}
		return result;
	}

	public int countHomeGamesForTeam(String team) {
		int count = 0;
        for(Day thisDay : seasonSchedule) {
            for(Game thisGame : thisDay.games) {
                if((thisGame.hometeam).equals(team)) {
                    count++;
                }
            }
        }
        //debug("The count is: " + count);
        return count;		
	}

	public boolean eachTeamHasHalfHomeGames(String[] teams) {
		boolean result = true;
		for(String thisTeam : teams) {
			if(countHomeGamesForTeam(thisTeam) != (countGamesForTeam(thisTeam)/2)) {
				debugToFile("Team: " + thisTeam + " has " + countHomeGamesForTeam(thisTeam) + " out of " + countGamesForTeam(thisTeam) + " home games");
				result = false;
			}
		}
		return result;
	}

	public boolean repOk() {
		boolean checkAll = false;
		String[] a = new String[allTeams.size()];
		checkAll = eachTeamHas176Games(allTeams.toArray(a)) && checkAll;
		checkAll = eachTeamHasHalfHomeGames(allTeams.toArray(a)) && checkAll;
		return true;
	}

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }

    private void debugToFile(String saveOutput) {
        debug("Saving analyzer output for future use to: " + scheduler.SchedulerConstants.SAVE_REPOK_LOCATION);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(scheduler.SchedulerConstants.SAVE_REPOK_LOCATION,true)));
            out.println(saveOutput + "\n");
            out.close();
        } catch (Exception e) {
            System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
        }
    } 
}