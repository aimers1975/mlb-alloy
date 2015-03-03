package scheduler;

import java.util.*;

public class ScheduleOutParser {
	String originalOutput;
	ArrayList<String> games;
	ArrayList<String> details;
	Boolean debug = true;

	public ScheduleOutParser(String outputToParse) {
		originalOutput = outputToParse;
		games = new ArrayList<String>();
		details = new ArrayList<String>();
	}

	public ArrayList<String> parseGames() {
		games = new ArrayList<String>();
		int start = originalOutput.indexOf("this/schedule<:games={");
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
          	games.add(parts.nextToken());
          }
     	}
		return games;
	}

	public ArrayList<String> parseGameDetails() {
		details = new ArrayList<String>();

		//this/game<:team1={game$0->team$2, game$1->team$1, game$2->team$2, game$3->team$0, game$4->team$1}
	    //this/game<:team2={game$0->team$1, game$1->team$0, game$2->team$0, game$3->team$2, game$4->team$0}
		int start = originalOutput.indexOf("this/game<:team1={");
		int middle = originalOutput.indexOf("this/game<:team2={", start);
		int middle2 = originalOutput.indexOf("this/game<:gt={",middle);
		int end = originalOutput.indexOf("this/schedule={", middle2);
		debug("The start of teamlist is: " + start);
		debug("The middle of teamlist is: " + middle);
		debug("The middle2 of the teamlist is" + middle2);
		debug("The end of teamlist is: " + end);
		StringTokenizer parts = new StringTokenizer(originalOutput.substring(start,middle),"{");
		StringTokenizer parts2 = new StringTokenizer(originalOutput.substring(middle,middle2), "{");
		StringTokenizer parts3 = new StringTokenizer(originalOutput.substring(middle2, end), "{");
		if(parts.hasMoreTokens()) {
			parts.nextToken();
			parts2.nextToken();
			parts3.nextToken();
		}
		if(parts.hasMoreTokens()) {
			parts = new StringTokenizer(parts.nextToken(), "->, }");
			parts2 = new StringTokenizer(parts2.nextToken(), "->, }");
			parts3 = new StringTokenizer(parts3.nextToken(), "->, }");
		}
		while (parts.hasMoreTokens()) {
          parts.nextToken();
          parts2.nextToken();
          parts3.nextToken();
          if(parts.hasMoreTokens()) {
          	details.add(parts.nextToken());
          	details.add(parts2.nextToken());
          	details.add(parts3.nextToken());
          }
     	}
		return details;		
	}

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }
}