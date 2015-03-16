package scheduler;

import java.util.*;

public class ScheduleOutParser {
	String originalOutput;
	ArrayList<String> series;
	ArrayList<String> details;
	Boolean debug = true;

	public ScheduleOutParser(String outputToParse) {
		originalOutput = outputToParse;
		series = new ArrayList<String>();
		details = new ArrayList<String>();
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
		return series;
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
		debug("The start of teamlist is: " + start);
		debug("The middle of teamlist is: " + middle);
		debug("The middle2 of the teamlist is" + middle2);
		debug("The middle3 of the teamlist is" + middle3);
		debug("The middle4 of the teamlist is" + middle4);
		debug("The middle5 of the teamlist is" + middle5);
		debug("The end of teamlist is: " + end);
		StringTokenizer parts = new StringTokenizer(originalOutput.substring(start,middle),"{");
		debug("substring: " + originalOutput.substring(start,middle));
		StringTokenizer parts2 = new StringTokenizer(originalOutput.substring(middle,middle2), "{");
		debug("substring: " + originalOutput.substring(middle,middle2));
		StringTokenizer parts3 = new StringTokenizer(originalOutput.substring(middle2, middle3), "{");
		debug("substring: " + originalOutput.substring(middle2,middle3));
		StringTokenizer parts4 = new StringTokenizer(originalOutput.substring(middle3, middle4), "{");
		debug("substring: " + originalOutput.substring(middle3,middle4));
		StringTokenizer parts5 = new StringTokenizer(originalOutput.substring(middle4, middle5), "{");
		debug("substring: " + originalOutput.substring(middle4,middle5));
		StringTokenizer parts6 = new StringTokenizer(originalOutput.substring(middle5, end), "{");
		debug("substring: " + originalOutput.substring(middle5,end));
		if(parts.hasMoreTokens()) {
			debug(parts.nextToken());
			debug(parts2.nextToken());
			debug(parts3.nextToken());
			debug(parts4.nextToken());
			debug(parts5.nextToken());
			//debug(parts6.nextToken());
		}
		if(parts.hasMoreTokens()) {
			parts = new StringTokenizer(parts.nextToken(), "->, }");
			debug(parts.toString());
			parts2 = new StringTokenizer(parts2.nextToken(), "->, }");
			debug(parts2.toString());
			parts3 = new StringTokenizer(parts3.nextToken(), "->, }");
			debug(parts3.toString());
			parts4 = new StringTokenizer(parts4.nextToken(), "->, }");
			debug(parts4.toString());
			parts5 = new StringTokenizer(parts5.nextToken(), "->, }");
			debug(parts5.toString());
			//parts6 = new StringTokenizer(parts6.nextToken(), "->, }");
			//debug(parts6.toString());
		}
		while (parts.hasMoreTokens()) {
          debug(parts.nextToken());
          debug(parts2.nextToken());
          debug(parts3.nextToken());
          debug(parts4.nextToken());
          debug(parts5.nextToken());
          //debug(parts6.nextToken());
          if(parts.hasMoreTokens()) {
          	details.add(parts.nextToken());
          	details.add(parts2.nextToken());
          	details.add(parts3.nextToken());
          	details.add(parts4.nextToken());
          	details.add(parts5.nextToken());
          	//details.add(parts6.nextToken());
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