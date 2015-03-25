package scheduler;

import java.util.*;

public class ScheduleOutParser {
	String originalOutput;
	ArrayList<String> series;
	ArrayList<String> details;
	ArrayList<String> extraGames;
	ArrayList<String> teams;
	ArrayList<String> games;
	Boolean debug = true;

	public ScheduleOutParser(String outputToParse) {
		originalOutput = outputToParse;
		series = new ArrayList<String>();
		details = new ArrayList<String>();
		extraGames = new ArrayList<String>();
		teams = new ArrayList<String>();
		games = new ArrayList<String>();
	}

    public ArrayList<String> getAllTeams() {
    	HashSet<String> teamHash = new HashSet<String>();
    	for(int i=0; i<teams.size();i++) {
    		teamHash.add(teams.get(i));
    	}
    	Iterator<String> iterator = teamHash.iterator();
    	ArrayList<String> returnArray = new ArrayList<String>();
    	while(iterator.hasNext()) {
    	 	returnArray.add(iterator.next());
    	}
    	return returnArray;


    }

	public ArrayList<String> parseSeries() {
		series = new ArrayList<String>();
		int start = originalOutput.indexOf("this/schedule<:allSeries={");
		int end = originalOutput.indexOf("skolem $show_This={schedule", start);
		//debug("The start of schedule is: " + start);
		//debug("The end of schedule is: " + end);
		StringTokenizer parts = new StringTokenizer(originalOutput.substring(start,end),"{");
		if(parts.hasMoreTokens()) {
			parts.nextToken();
		}
		if(parts.hasMoreTokens()) {
			parts = new StringTokenizer(parts.nextToken(), "->, }");
		}
		while (parts.hasMoreTokens()) {
          parts.nextToken();
          if(parts.hasMoreTokens()) {
          	series.add(parts.nextToken());
          }
     	}
     	parseGameDetails();
		return series;
	}

	public ArrayList<String> getTeamsForSeries(String seriesName) {
		ArrayList<String> seriesTeams = new ArrayList<String>();
		//debug("Finding teams for series: " + seriesName);
		if(series != null && series.size() != 0) {
		    int index = series.indexOf(seriesName);
		    //debug("Adding to teamlist: " + teams.get(index*2));
		    seriesTeams.add(teams.get(index*2));
		    //debug("Adding to teamlist: " + teams.get(index*2+1));
		    seriesTeams.add(teams.get(index*2+1));
		    return seriesTeams;
		}
		return new ArrayList<String>();
	}

	public ArrayList<String> getGamesForSeries(String seriesName) {
		ArrayList<String> seriesGames = new ArrayList<String>();
		//debug("Finding games for series: " + seriesName);
		if(series != null && series.size() != 0) {
		    int index = series.indexOf(seriesName);
		    //debug("Adding to gamelist: " + games.get(index*3));
		    seriesGames.add(games.get(index*3));
		    //debug("Adding to gamelist: " + games.get(index*3+1));
		    seriesGames.add(games.get(index*3+1));
		    //debug("Adding to gamelist: " + games.get(index*3+2));
		    seriesGames.add(games.get(index*3+2));
		    index = extraGames.indexOf(seriesName);
		    if(index != -1) {
		    	String debugGame = extraGames.get(index+1);
		    	//debug("The extra game day is: " + debugGame);
		    	seriesGames.add(extraGames.get(index+1));
		    }
		    //debug("Extra game had a series at: " + index + " for: " + seriesName);
		    return seriesGames;
		}
		return new ArrayList<String>();
	}

	public ArrayList<String> getTeamSchedule(String team) {
		//loop through all series
		//get teams for a series
		//get games if the series includes the team
		ArrayList<String> teamSchedule = new ArrayList<String>();
		for(int i=0; i<series.size(); i++) {
			ArrayList<String> currentTeams = getTeamsForSeries(series.get(i));
			int thisIndex = currentTeams.indexOf(team);
			if(thisIndex == 0) {
				teamSchedule.add("Opponent is Home team.");
			} else if (thisIndex ==1){
				teamSchedule.add("Opponent is Away team.");
			}
			if(thisIndex == 0) {
				teamSchedule.add(currentTeams.get(1));
				teamSchedule.addAll(getGamesForSeries(series.get(i)));
			} else if (thisIndex == 1) {
				teamSchedule.add(currentTeams.get(0));
				teamSchedule.addAll(getGamesForSeries(series.get(i)));				
			}
		}
		return teamSchedule;
	}

	public ArrayList<String> findNoGameDays() {
		return new ArrayList<String>();
	}

	public ArrayList<String> findNoGameDaysByTeam(String team) {
		return new ArrayList<String>();
	}

	public ArrayList<String> parseGameDetails() {
		details = new ArrayList<String>();
		int start = originalOutput.indexOf("this/series<:ateam={");
		int middle = originalOutput.indexOf("this/series<:hteam={", start);
		int middle2 = originalOutput.indexOf("this/series<:g1={",middle);
		int middle3 = originalOutput.indexOf("this/series<:g2={",middle2);
		int middle4 = originalOutput.indexOf("this/series<:g3={",middle3);
		int middle5 = originalOutput.indexOf("this/series<:g4={",middle4);
		int end = originalOutput.indexOf("skolem $show", middle5);
		//debug("The start of teamlist is: " + start);
		//debug("The middle of teamlist is: " + middle);
		//debug("The middle2 of the teamlist is" + middle2);
		//debug("The middle3 of the teamlist is" + middle3);
		//debug("The middle4 of the teamlist is" + middle4);
		//debug("The middle5 of the teamlist is" + middle5);
		//debug("The end of teamlist is: " + end);
		StringTokenizer parts = new StringTokenizer(originalOutput.substring(start,middle),"{");
		//debug("substring: " + originalOutput.substring(start,middle));
		StringTokenizer parts2 = new StringTokenizer(originalOutput.substring(middle,middle2), "{");
		//debug("substring: " + originalOutput.substring(middle,middle2));
		StringTokenizer parts3 = new StringTokenizer(originalOutput.substring(middle2, middle3), "{");
		//debug("substring: " + originalOutput.substring(middle2,middle3));
		StringTokenizer parts4 = new StringTokenizer(originalOutput.substring(middle3, middle4), "{");
		//debug("substring: " + originalOutput.substring(middle3,middle4));
		StringTokenizer parts5 = new StringTokenizer(originalOutput.substring(middle4, middle5), "{");
		//debug("substring: " + originalOutput.substring(middle4,middle5));
		StringTokenizer parts6 = new StringTokenizer(originalOutput.substring(middle5, end), "{");
		//debug("substring: " + originalOutput.substring(middle5,end));
		if(parts.hasMoreTokens()) {
			parts.nextToken();
			parts2.nextToken();
			parts3.nextToken();
			parts4.nextToken();
			parts5.nextToken();
			parts6.nextToken();
		}
		if(parts.hasMoreTokens()) {
			parts = new StringTokenizer(parts.nextToken(), "->, }");
			parts2 = new StringTokenizer(parts2.nextToken(), "->, }");
			parts3 = new StringTokenizer(parts3.nextToken(), "->, }");
			parts4 = new StringTokenizer(parts4.nextToken(), "->, }");
			parts5 = new StringTokenizer(parts5.nextToken(), "->, }");
			parts6 = new StringTokenizer(parts6.nextToken(), "->, }");
			debug(parts6.toString());
		}
		String storeSeries = parts6.nextToken();
		Boolean addExtraGame = false;
		//debug("First store series is: " + storeSeries);
		while (parts.hasMoreTokens()) {
          String thisSeries = parts.nextToken();
          //debug("Checking series: " + thisSeries);
          parts2.nextToken();
          parts3.nextToken();
          parts4.nextToken();
          parts5.nextToken();
          if(thisSeries.equals(storeSeries)) {
            addExtraGame = true;
            extraGames.add(storeSeries);
          }
          if(parts.hasMoreTokens()) {
          	String token = parts.nextToken();
          	details.add(token);
          	teams.add(token);
          	String token2 = parts2.nextToken();
          	details.add(token2);
          	teams.add(token2);
          	String token3 = parts3.nextToken();
          	details.add(token3);
          	games.add(token3);
          	String token4 = parts4.nextToken();
          	details.add(token4);
          	games.add(token4);
          	String token5 = parts5.nextToken();
          	details.add(token5);
          	games.add(token5);
          	if(addExtraGame) {
          	  extraGames.add(parts6.nextToken());
          	  addExtraGame = false;
          	  //debug("Extra game added to: " + thisSeries);
              storeSeries = parts6.nextToken();
              //debug("New store series is: " + storeSeries);
          	}
          }
     	}
		return details;		
	}

	public ArrayList<String> getExtraGames() {
		//debug("Got extra games");
		if(extraGames != null) {
			if(extraGames.size() > 0) {
				return extraGames;
			}
		}
		//debug("Extra games was empty, returning empty arraylist.");
		return new ArrayList<String>();
	} 

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }
}