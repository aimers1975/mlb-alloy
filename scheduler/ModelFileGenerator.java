package scheduler;

import java.util.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.PrintWriter;
import scheduler.SchedulerConstants;

public class ModelFileGenerator {

	ArrayList<String> workingModel;
	Boolean debug = true;

	public ModelFileGenerator() {
		workingModel = new ArrayList<String>();
		//Read model template into working model
		try (BufferedReader br = new BufferedReader(new FileReader(scheduler.SchedulerConstants.TEMPLATE_MODEL_LOCATION))) {
			String line;
			while ((line = br.readLine()) != null) {
				workingModel.add(line);
			}
		} catch (Exception e) {
			debug("Error reading from template file.");
		}
		setupPossibleDays(1,15);
		//setupNumberSeries(5);
		setupRuns(30);
		setupNumberTeams(3,5);
		//addPredTeamNumberGames(3,2);
		addPredSetFourGameSeries(2);
		addPredHasHalfHomeGames();
		//addPredNoFourGameAwayStands();
		addPredNoConsecutiveSeries();
		addCustomPred("pred myCustomPred(This:schedule) {\n  //This is a test \n}");
		addCustomPredInShow("  myCustomPred[This]\n");
		writeWorkingModelToFile("scheduler\\amygenerated.als");
		for(int i=0; i<workingModel.size(); i++) {
			debug(workingModel.get(i));
		}
	}

	public void writeWorkingModelToFile(String location) {

        try {
            PrintWriter out = new PrintWriter(location);
            for(String line : workingModel) {
                debug(line);
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
        }

    }

	public void addPredNoConsecutiveSeries() {
		int index = workingModel.indexOf("//%genPredNoConsecutiveSeries%");
		//debug("Index of noFourGameAwayStands: " + index);
		workingModel.set(index,"  noConsecutiveSeries[This]");
	}

	public void addPredNoFourGameAwayStands() {
		int index = workingModel.indexOf("//%genPredNoFourGameAwayStands%");
		//debug("Index of noFourGameAwayStands: " + index);
		workingModel.set(index,"  noFourGameAwayStands[This]");
	}

	public void addPredHasHalfHomeGames() {
		int index = workingModel.indexOf("//%genPredHasHalfHomeGames%");
		//debug("Index of HalfHomeGames: " + index);
		workingModel.set(index,"  hasHalfHomeGames[This]");
	}

	public void addCustomPred(String customPred) {
		int index = workingModel.indexOf("//%genCustomPred%");
		debug("Index of custompred: " + index);
		workingModel.set(index,customPred);
	}

	public void addCustomPredInShow(String customPredInShow) {
		int index = workingModel.indexOf("//%genPredCustomInShow%");
		debug("Index of custompredinshow: " + index);
		workingModel.set(index,customPredInShow);
	}

	public void addPredSetFourGameSeries(int numSeries) {	
		//all a: team | countFourGameSeries[This,a] = 4
		StringBuilder sb = new StringBuilder();
		if(numSeries > -1) {
			sb.append("  all a: team | countFourGameSeries[This,a] = " + String.valueOf(numSeries));
		} 
		//debug(sb.toString());
		int index = workingModel.indexOf("//%setNumberFourGames%");
		//debug("Index of fun Four Games: " + index);
		workingModel.set(index,sb.toString());
		index = workingModel.indexOf("//%genPredSetFourGameSeries%");
		//debug("Index of predFourGames: " + index);
		workingModel.set(index,"  setFourGameSeries[This]");	
	}
	public void addPredTeamNumberGames(int min, int max) {	
		//  all disj a,b: team | count[schedule,a,b] =1// and count[schedule,a,b] <3
		StringBuilder sb = new StringBuilder();
		if(max>min) {
			sb.append("  all disj a,b: team | count[schedule,a,b] > " + String.valueOf(min) + " and count[schedule,a,b] < " + String.valueOf(max) + "\n");
		} else if(max==min) {
			sb.append("  all disj a,b: team | count[schedule,a,b] = " + String.valueOf(min));
		} else {
			sb.append("  all disj a,b: team | count[schedule,a,b] > " + String.valueOf(min));
		}
		//debug(sb.toString());
		int index = workingModel.indexOf("//%setTeamNumberGames%");
		//debug("Index of predNumGames: " + index);
		workingModel.set(index,sb.toString());
		index = workingModel.indexOf("//%genPredTeamNumberGames%");
		//debug("Index of predNumGames: " + index);
		workingModel.set(index,"  teamNumberGames[This]");	
	}

	public void setupNumberSeries(int series) {
		StringBuilder sb = new StringBuilder();
		sb.append("#series=" + series + "\n");
		int index = workingModel.indexOf("//%generateNumSeries%");
		//debug("Index of generate num series: " + index);
		workingModel.set(index,sb.toString());
	}

	public void setupRuns(int runs) {
		StringBuilder sb = new StringBuilder();
		int index = workingModel.indexOf("%generateRuns%");
		//debug("Index of generate num runs: " + index);
		workingModel.set(index,String.valueOf(runs));
	}

	public void setupNumberTeams(int teamGroups, int numPerGroup) {
		StringBuilder sb = new StringBuilder();
		int teams = teamGroups*numPerGroup;
		if(teamGroups == 1) {
			int index = workingModel.indexOf("%addTeams%");
			//debug("Index of generate num teams: " + index);
			workingModel.set(index,"sig team {}");
			index = workingModel.indexOf("%generateNumTeams%");
			//debug("Index of generate num teams: " + index);
			sb.append("#team=" + teams + "\n");
			workingModel.set(index,sb.toString());
		} else {
			int index = workingModel.indexOf("%addTeams%");
			debug("Index of add teams is: " + index);
			sb.append("abstract sig team {}\n");
			for(int i=0; i<teamGroups; i++) {
				sb.append("sig team" + String.valueOf(i) + " extends team {}\n");
			}
			workingModel.set(index, sb.toString());
			sb = new StringBuilder();
			index = workingModel.indexOf("%generateNumTeams%");
			debug("Index of generated num teams");
			for(int i=0; i<teamGroups; i++) {
				sb.append("#team" + String.valueOf(i) + " = " + String.valueOf(numPerGroup) + "\n");
			}
			workingModel.set(index, sb.toString());
		}
	}

	public void setupPossibleDays(int start, int end) {
		String generatePossibleDays;
		String generateDayOrder;
		String unavailableGameOneDays;
		StringBuilder sb = new StringBuilder();
		for(int i=start; i<end; i++) {
			sb.append("D" + i + ",");
		}
		sb.append("D" + end);
		generatePossibleDays = sb.toString();
		sb = new StringBuilder();
		sb.append("D" + String.valueOf(end-1) + " !in series.g1 and D" + end + " !in series.g1\n");
		unavailableGameOneDays = sb.toString();
		sb = new StringBuilder();
		for(int i=start; i<end; i++) {
			sb.append("all x:D" + String.valueOf(i) + " | x.(Ordering.order) = D" + String.valueOf(i+1) + " \n");
		}
		sb.append("all x:D" + String.valueOf(end) + " | x.(Ordering.order) = none\n");
		generateDayOrder = sb.toString();
		int index = workingModel.indexOf("%generatePossibleDays%");
		//debug("Index of generate possible days: " + index);
		workingModel.set(index,generatePossibleDays);
		index = workingModel.indexOf("%generateDayOrder%");
		//debug("Index of generate day order: " + index);
		workingModel.set(index,generateDayOrder);
		index = workingModel.indexOf("%unavailableGameOneDays%");
		//debug("Index of generate unavailable days: " + index);
		workingModel.set(index,unavailableGameOneDays);

	}

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }


}