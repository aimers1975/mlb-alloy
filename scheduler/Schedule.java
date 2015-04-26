package scheduler;

import java.util.*;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import scheduler.Day;
import scheduler.SchedulerConstants;

public class Schedule {

	Day[] seasonSchedule;
	HashSet<String> allTeams; 
	Boolean debug = true;

	public Schedule() {
		seasonSchedule = new Day[181];
		allTeams = new HashSet<String>();
		for(int i=0; i<seasonSchedule.length; i++) {
			seasonSchedule[i] = new Day();
		}
	}

	public Boolean[] findTeamsDaysOff(String team) {
		Boolean[] daysOff = new Boolean[181];
		for(int i=0; i< daysOff.length; i++) {
			daysOff[i] = true;
		}
		int i = 1;
		for(Day thisDay : seasonSchedule) {
			for(Game thisGame: thisDay.games) {
				if((thisGame.hometeam).contains(team) || (thisGame.awayteam).contains(team)) {
					daysOff[i] = false; 
				}
			}
			i++;
		}
		return daysOff;
	}

	public void addGameToSchedule(String home, String away, String gametime, String field, int day) {
		Game addingGame = new Game(home,away,gametime,field);
		allTeams.add(home);
		allTeams.add(away);
		addGame(addingGame,day);
	}

	public Boolean[] showNoGameDays() {
		Boolean[] returnArray = new Boolean[181];
		int dayCount = 0;
		for(Day thisDay : seasonSchedule){
			if(thisDay.games.isEmpty()) {
				returnArray[dayCount] = true;
			} else {
				returnArray[dayCount] = false;
			}
			dayCount++;
		}
		return returnArray;
	}

	private void addGame(Game addGame, int day) {		
		Day checkDay = seasonSchedule[day];
		checkDay.games.add(addGame);
		seasonSchedule[day] = checkDay;
	}

	public void removeGame(int gameNum, int day) {
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
                if((thisGame.hometeam).contains(team) || (thisGame.awayteam).contains(team)) {
                    count++;
                }
            }
        }
        //debug("The count is: " + count);
        return count;
	}


	public boolean eachTeamHas162Games(String[] teams) {
		boolean result = true;
		for(String thisTeam : teams) {
			if(countGamesForTeam(thisTeam) != 162) {
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
                if(((thisGame.hometeam).trim()).equals(team)) {
                    count++;
                }
            }
        }
        //debug("The " + team + " count is: " + count);
        return count;		
	}

	public int countDivisionGamesForTeam(String team) {
		int count = 0;
		String[] listToCheck = new String[5];
		if(Arrays.asList(scheduler.SchedulerConstants.AL_EAST_LIST).contains(team)) {
			listToCheck = scheduler.SchedulerConstants.AL_EAST_LIST;
		} else if (Arrays.asList(scheduler.SchedulerConstants.AL_WEST_LIST).contains(team)) {
			listToCheck = scheduler.SchedulerConstants.AL_WEST_LIST;
		} else if (Arrays.asList(scheduler.SchedulerConstants.AL_CENTRAL_LIST).contains(team)) {
			listToCheck = scheduler.SchedulerConstants.AL_CENTRAL_LIST;
		} else if (Arrays.asList(scheduler.SchedulerConstants.NL_EAST_LIST).contains(team)) {
			listToCheck = scheduler.SchedulerConstants.NL_EAST_LIST;
		} else if (Arrays.asList(scheduler.SchedulerConstants.NL_CENTRAL_LIST).contains(team)) {
			listToCheck = scheduler.SchedulerConstants.NL_CENTRAL_LIST;
		} else if (Arrays.asList(scheduler.SchedulerConstants.NL_WEST_LIST).contains(team)) {
			listToCheck = scheduler.SchedulerConstants.NL_WEST_LIST;
		}
 		//Get division for this team
		//Check opponents to figure if in division
        for(Day thisDay : seasonSchedule) {
            for(Game thisGame : thisDay.games) {
            	String ht = (thisGame.hometeam).trim();
            	String at = (thisGame.awayteam).trim();
                if(ht.equals(team)){
                	if(Arrays.asList(listToCheck).contains(at)) {
                		count++;	
                	}
                } else if (at.equals(team)) {
                	if(Arrays.asList(listToCheck).contains(ht)) {
                		count++;
                	}
                }
            }
        }
        //debug("The " + team + " count is: " + count);
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
		checkAll = eachTeamHas162Games(allTeams.toArray(a)) && checkAll;
		checkAll = eachTeamHasHalfHomeGames(allTeams.toArray(a)) && checkAll;
		return true;
	}

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }

    private void debugToFile(String saveOutput) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(scheduler.SchedulerConstants.SAVE_REPOK_LOCATION,true)));
            out.println(saveOutput + "\n");
            out.close();
        } catch (Exception e) {
            System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
        }
    } 

    public void loadScheduleFromFile() {
    	//Read a line, if its a new day, increment day
    	//if its a new game increment game
    	//read in teams, time, location
    	//save game 
        try (BufferedReader br = new BufferedReader(new FileReader(scheduler.SchedulerConstants.SAVE_SCHEDULE_LOCATION))) {
            String line;
            String homeTeam;
            String awayTeam;
            String time;
            String location;
            int currentDay = 0;
            int currentGame = 0;
            line = br.readLine();
            while (line != null) {
            	//debug(line);
            	if(line.contains("Day")) {
            		line = br.readLine();
            		currentGame=0;
            		while(line != null && !line.contains("Day")) {       			
	            		if(line.contains("Game")) {
	            			line = br.readLine();
	            			if(line.contains("Home")) {
	            				StringTokenizer st = new StringTokenizer(line, ":");
	            				st.nextToken();
	            				if(st.hasMoreTokens()) {
	            					homeTeam = st.nextToken();
	            				} else {
	            					debug("No home team name found");
	            					break;
	            				}
	            			} else {
	            				debug("Home string not found");
	            				break;
	            			}
	            			line = br.readLine();
	            			if(line.contains("Away")) {
	            				StringTokenizer st = new StringTokenizer(line, ":");
	            				st.nextToken();
	            				if(st.hasMoreTokens()) {
	            					awayTeam = st.nextToken();
	            				} else {
	            					debug("No home team name found");
	            					break;
	            				}
	            			} else {
	            				debug("Away string not found.");
	            				break;
	            			}
	            			line = br.readLine();
	            			if(line.contains("Time")) {
	            				StringTokenizer st = new StringTokenizer(line, ":");
	            				st.nextToken();
	            				if(st.hasMoreTokens()) {
	            					time = st.nextToken();
	            				} else {
	            					debug("No time found");
	            					break;
	            				}
	            			} else {
	            				debug("Time string not found.");
	            				break;
	            			}
	            			line = br.readLine();
	            			if(line.contains("Location")) {
	            				StringTokenizer st = new StringTokenizer(line, ":");
	            				st.nextToken();
	            				if(st.hasMoreTokens()) {
	            					location = st.nextToken();
	            				} else {
	            					debug("No time found");
	            					break;
	            				}
	            			} else {
	            				debug("Time string not found.");
	            				break;
	            			}
	            			addGameToSchedule(homeTeam, awayTeam, time, location, currentDay);
	            			line = br.readLine();
	            			currentGame++;
	            			debug("Getting next line: " + line);
	            		} else {
	            			debug("Incorrect Schedule.out format. Expecting next game.");
	            			continue;
	            		}
	            	}
            	} else {
            		debug("Incorrect Schedule.out format. Expecting next day.");
            		continue;
            	}
            	currentDay++;
            }
        } catch (Exception e) {
            debug("Exception occurred loading schedule from file.");
        }

    }
}