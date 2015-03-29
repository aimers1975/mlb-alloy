package scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.PrintWriter;


public class Mapper {
	Schedule fullSchedule;
	HashMap<String,ArrayList<String>> teamListHashMap;
    HashMap<String,ArrayList<String>> teamDivListHashMap;
    ArrayList<String> leagueNameList;
	Boolean debug = true;


	public Mapper() {
		fullSchedule = new Schedule();
		teamListHashMap = new HashMap<String,ArrayList<String>>();
        teamDivListHashMap = new HashMap<String,ArrayList<String>>();
        leagueNameList = new ArrayList<String>();
		//loadTeams();
	}

    public ArrayList<String> getAllTeams() {
        return leagueNameList;
    }

    public int countGamesForTeam(String team) {
        return fullSchedule.countGamesForTeam(team);
    }

	public void loadTeams() {
        try (BufferedReader br = new BufferedReader(new FileReader(scheduler.SchedulerConstants.INPUT_TEAM_LIST))) {
            String line;
            String divisionName = new String();
            ArrayList<String> divisionTeams = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
            	//debug("Teamlist: " + line);
            	//if line contains "::" save to name string
            	//loop through until ":: encountered", save name string, loop some more
            	if(line.contains("::")) {
            		if(divisionTeams.size() > 0) {
            			debug("Adding to map: " + divisionName + " and size of teamlist is: " + divisionTeams.size());
            			teamListHashMap.put(divisionName,divisionTeams);
            		}
            		divisionName = line.replace("::", "");
            		divisionTeams = new ArrayList<String>();
            	} else {
            		if(!line.equals("")) {
            			divisionTeams.add(line);
            		}
            	}
            }
            if(divisionTeams.size() > 0) {
            	debug("Adding to map: " + divisionName + " and size of teamlist is: " + divisionTeams.size());
            	teamListHashMap.put(divisionName,divisionTeams);
            }
            Set<String> keys = teamListHashMap.keySet();
            Iterator<String> divisions = keys.iterator();
            divisionName = new String();
            divisionTeams = new ArrayList<String>();
            ArrayList<String> divisionTeamsAL = new ArrayList<String>();
            String nextDivision = new String();
            while(divisions.hasNext()) {
                String thisDivision = divisions.next();
                StringTokenizer st = new StringTokenizer(thisDivision,"-");
                divisionName = st.nextToken();
                if(divisionName.equals("NL")) {
                    divisionTeams.addAll(teamListHashMap.get(thisDivision));
                    debug("Division teams league size is now: " + divisionTeams.size());
                } else if (divisionName.equals("AL")) {
                    divisionTeamsAL.addAll(teamListHashMap.get(thisDivision));
                    debug("Division teams AL league size is now: " + divisionTeamsAL.size());
                }
            }
            if(divisionTeams.size() >0 && divisionTeamsAL.size() > 0) {
                debug("Adding AL and NL to map");
                teamDivListHashMap.put("AL",divisionTeams);
                teamDivListHashMap.put("NL",divisionTeamsAL);
                leagueNameList.addAll(divisionTeams);
                leagueNameList.addAll(divisionTeamsAL);
            }

        } catch (Exception e) {
            debug("Exception occurred.");
        }
	}

	public void createDivisionSchedule(ScheduleOutParser thisParser) {
		ArrayList<String> series = thisParser.parseSeries();
		ArrayList<String> teams = thisParser.getAllTeams();
		ArrayList<String> divisionNames;
		HashMap<String,String> teamNameMap = new HashMap<String,String>();
		Set<String> keys = teamListHashMap.keySet();
		Iterator<String> divisions = keys.iterator();
		while(divisions.hasNext()) {
			String thisDivision = divisions.next();
			ArrayList<String> divNames = teamListHashMap.get(thisDivision);
			for(int i=0;i<teams.size();i++) {
				debug("Adding team: " + teams.get(i) + " and mapping to: " + divNames.get(i));
			    teamNameMap.put(teams.get(i),divNames.get(i));
		    }
		    for(String thisSeries : series) {
		    	ArrayList<String> thisSeriesTeams = thisParser.getTeamsForSeries(thisSeries);
		    	ArrayList<String> thisSeriesGames = thisParser.getGamesForSeries(thisSeries);
		    	for(String game : thisSeriesGames) {
		    		//(String home, String away, String gametime, String field, int day)
		    		if(thisSeriesTeams.size() == 2 && thisSeriesGames.size() > 0) {
		    			String home = teamNameMap.get(thisSeriesTeams.get(1));
		    			String away = teamNameMap.get(thisSeriesTeams.get(0));
		    			StringTokenizer dayNum = new StringTokenizer(game, "D");
		    			dayNum = new StringTokenizer(dayNum.nextToken(), "$");
		    			int day = Integer.parseInt(dayNum.nextToken());
		    			fullSchedule.addGameToSchedule(home,away,"12pm CST",home+" Field", day);
		    		}
		    	}
		    }
            fullSchedule.repOk();
			saveScheduleOutput(fullSchedule.toString());
		}
	}


    public void createInterLeagueSchedule(ScheduleOutParser thisParser) {
        ArrayList<String> series = thisParser.parseSeries();
        ArrayList<String> teams = thisParser.getAllTeams();
        ArrayList<String> divisionNames;
        HashMap<String,String> teamNameMap = new HashMap<String,String>();
        ArrayList<String> divNames = leagueNameList;
        for(int i=0;i<teams.size();i++) {
            debug("Adding team: " + teams.get(i) + " and mapping to: " + divNames.get(i));
            teamNameMap.put(teams.get(i),divNames.get(i));
        }
        for(String thisSeries : series) {
            ArrayList<String> thisSeriesTeams = thisParser.getTeamsForSeries(thisSeries);
            ArrayList<String> thisSeriesGames = thisParser.getGamesForSeries(thisSeries);
            for(String game : thisSeriesGames) {
                //(String home, String away, String gametime, String field, int day)
                if(thisSeriesTeams.size() == 2 && thisSeriesGames.size() > 0) {
                    String home = teamNameMap.get(thisSeriesTeams.get(1));
                    String away = teamNameMap.get(thisSeriesTeams.get(0));
                    StringTokenizer dayNum = new StringTokenizer(game, "D");
                    dayNum = new StringTokenizer(dayNum.nextToken(), "$");
                    int day = Integer.parseInt(dayNum.nextToken());
                    fullSchedule.addGameToSchedule(home,away,"12pm CST",home+" Field", day);
                }
            }
        }
        fullSchedule.repOk();
        saveScheduleOutput(fullSchedule.toString());
    }

    public String toString() {
        return fullSchedule.toString();
    }


	public void createInterDivisionSchedule(ScheduleOutParser thisParser) {
	//loop through all series
	//get team names
	//map team name to team string
	//create a game for each day
	//add game to schedule
    ArrayList<String> series = thisParser.parseSeries();
    ArrayList<String> teams = thisParser.getAllTeams();
    ArrayList<String> divisionNames;
    HashMap<String,String> teamNameMap = new HashMap<String,String>();
    Set<String> keys = teamDivListHashMap.keySet();
    debug("keys size is: " + keys.size());
    Iterator<String> divisions = keys.iterator();
    debug("Starting interdiv schedule creation");
    while(divisions.hasNext()) {
        StringTokenizer st = new StringTokenizer(divisions.next(),"-");
        String divisionName = st.nextToken();
        ArrayList<String> divNames = teamDivListHashMap.get(divisionName);
        debug("Division names size is: " + divNames.size());
        debug("Teams size: " + teams.size());
        for(int i=0;i<teams.size();i++) {
            debug("Adding team: " + teams.get(i) + " and mapping to: " + divNames.get(i));
            teamNameMap.put(teams.get(i),divNames.get(i));
        }
        debug("Series size is: " + series.size());
        for(String thisSeries : series) {
            ArrayList<String> thisSeriesTeams = thisParser.getTeamsForSeries(thisSeries);
            ArrayList<String> thisSeriesGames = thisParser.getGamesForSeries(thisSeries);
            for(String game : thisSeriesGames) {
                //(String home, String away, String gametime, String field, int day)
                if(thisSeriesTeams.size() == 2 && thisSeriesGames.size() > 0) {
                    String home = teamNameMap.get(thisSeriesTeams.get(1));
                    String away = teamNameMap.get(thisSeriesTeams.get(0));
                    StringTokenizer dayNum = new StringTokenizer(game, "D");
                    dayNum = new StringTokenizer(dayNum.nextToken(), "$");
                    int day = Integer.parseInt(dayNum.nextToken());
                    fullSchedule.addGameToSchedule(home,away,"12pm CST",home+" Field", day);
                }
            }
        }
        fullSchedule.repOk();
        debug("Saving schedule out.");
        saveScheduleOutput(fullSchedule.toString());
    }

}

    private void saveScheduleOutput(String saveOutput) {
        debug("Saving analyzer output for future use to: " + scheduler.SchedulerConstants.SAVE_SCHEDULE_LOCATION);
        try {
            PrintWriter out = new PrintWriter(scheduler.SchedulerConstants.SAVE_SCHEDULE_LOCATION);
            String[] temp = saveOutput.split("\\n");
            for(String line : temp) {
                //debug(line);
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
        }
    }

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }
}