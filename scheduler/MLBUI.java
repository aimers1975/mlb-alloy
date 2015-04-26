package scheduler;

import java.util.*;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import java.awt.EventQueue;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import scheduler.MlbAppControl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import scheduler.SchedulerConstants;
import scheduler.ModelFileGenerator;
//import scheduler.AnalyzerThread;


public class MLBUI extends JFrame {

    MlbAppControl analyzer;
    Boolean debug = true;
    JLabel appInfo = new JLabel(scheduler.SchedulerConstants.APP_LABEL);
    JButton evaluateButton = new JButton(scheduler.SchedulerConstants.EVALUATE_BUTTON);
    JButton stopCurrentEvaluationButton = new JButton(scheduler.SchedulerConstants.STOP_EVAL_BUTTON);
    JButton saveToOverallScheduleButton = new JButton(scheduler.SchedulerConstants.SAVE_TO_OVERALL_SCHEDULE_BUTTON);
    JButton showFreeDaysButton = new JButton(scheduler.SchedulerConstants.SHOW_FREE_DAYS_BUTTON);
    JButton showOverallScheduleButton = new JButton(scheduler.SchedulerConstants.SHOW_OVERALL_SCHEDULE_BUTTON);
    JButton resetScheduleButton = new JButton(scheduler.SchedulerConstants.RESET_SCHEDULE_BUTTON);
    JButton nextScheduleSolutionButton = new JButton(scheduler.SchedulerConstants.NEXT_SOLUTION_BUTTON);
    JButton previousScheduleSolutionButton = new JButton(scheduler.SchedulerConstants.PREVIOUS_SOLUTION_BUTTON);
    JButton showCurrentTeamStatisticsButton = new JButton(scheduler.SchedulerConstants.SHOW_TEAM_STATISTICS_BUTTON);
    JButton loadLastScheduleButton = new JButton(scheduler.SchedulerConstants.LOAD_LAST_SCHEDULE_BUTTON);
    JButton removeGameButton = new JButton(scheduler.SchedulerConstants.REMOVE_GAME_BUTTON);
    JButton addGameButton = new JButton(scheduler.SchedulerConstants.ADD_GAME_BUTTON);
    JTextField removeGameTextField = new JTextField();
    JTextField addGameTextField = new JTextField();
    JPanel scrollPanel = new JPanel();
    JTextArea inputALS = new JTextArea(200,200);
    JScrollPane scroll = new JScrollPane(inputALS);
    JLabel numTeamGroupsLabel = new JLabel(scheduler.SchedulerConstants.NUM_TEAM_GROUP_LABEL);
    JComboBox numTeamGroups;
    JLabel numTeamsPerGroupLabel = new JLabel(scheduler.SchedulerConstants.NUM_TEAMS_PER_GROUP_LABEL);
    JComboBox numTeamsPerGroup;
    JLabel numSeriesLabel = new JLabel(scheduler.SchedulerConstants.NUM_SERIES_LABEL);
    JComboBox numSeries;
    JLabel numFourGameSeriesLabel = new JLabel(scheduler.SchedulerConstants.NUM_FOUR_GAME_SERIES_LABEL);
    JComboBox numFourGameSeries;
    JLabel numRunsLabel = new JLabel(scheduler.SchedulerConstants.NUM_RUNS_LABEL);
    JComboBox numRuns;
    JLabel dayRangeStartLabel = new JLabel(scheduler.SchedulerConstants.DAY_RANGE_START_LABEL);
    JComboBox dayRangeStart = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_DAY_START);
    JLabel dayRangeEndLabel = new JLabel(scheduler.SchedulerConstants.DAY_RANGE_END_LABEL);
    JComboBox dayRangeEnd;
    JLabel teamNumGamesMinLabel = new JLabel(scheduler.SchedulerConstants.TEAM_NUM_GAMES_MIN_LABEL);
    JComboBox teamNumGamesMin;
    JLabel teamNumGamesMaxLabel = new JLabel(scheduler.SchedulerConstants.TEAM_NUM_GAMES_MAX_LABEL);
    JComboBox teamNumGamesMax;
    JLabel teamSingleNumGamesMinLabel = new JLabel(scheduler.SchedulerConstants.TEAM_SINGLE_NUM_GAMES_MIN_LABEL);
    JComboBox teamSingleNumGamesMin;
    JLabel teamSingleNumGamesMaxLabel = new JLabel(scheduler.SchedulerConstants.TEAM_SINGLE_NUM_GAMES_MAX_LABEL);
    JComboBox teamSingleNumGamesMax;
    JCheckBox addPredHasHalfHomeGames = new JCheckBox(scheduler.SchedulerConstants.ADD_PRED_HAS_HALF_HOME_CHECKBOX);
    JCheckBox addNoFourGameAwayStands = new JCheckBox(scheduler.SchedulerConstants.ADD_NO_FOUR_GAME_AWAY_STANDS);
    JCheckBox addPredNoConsecutiveSeries = new JCheckBox(scheduler.SchedulerConstants.ADD_PRED_NO_CONSECUTIVE_SERIES);
    JLabel customPredInShowLabel = new JLabel(scheduler.SchedulerConstants.CUSTOM_PRED_IN_SHOW_LABEL);
    JTextField customPredInShow = new JTextField();
    JLabel teamNameLabel = new JLabel(scheduler.SchedulerConstants.TEAM_NAME_LABEL);
    JComboBox teamNameComboBox = new JComboBox<String>(scheduler.SchedulerConstants.TEAM_NAME_MENU);
    JLabel customPredLabel = new JLabel(scheduler.SchedulerConstants.CUSTOM_PRED_LABEL);
    JTextArea customPred = new JTextArea(1,1);
    JScrollPane customPredScrollPane = new JScrollPane(customPred);
    JPanel scrollPanelInputs = new JPanel(new GridLayout(30, 1));
    JScrollPane inputScroll;
    JPanel outputPanel = new JPanel();
    JTextArea outputPlaceholder = new JTextArea(200, 200);
    JScrollPane outputScroll = new JScrollPane(outputPlaceholder);
    JMenuBar menubar = new JMenuBar();
    JMenu file = new JMenu(scheduler.SchedulerConstants.FILE_JMENU);
    JMenuItem eMenuItem = new JMenuItem(scheduler.SchedulerConstants.EXIT_MENUITEM);
    ArrayList<String> analyzerOutputStringArrayList;
    ScheduleOutParser parser;
    StringBuilder currentParameters;
    //HashMap<String,String[]> outputCache = new HashMap<String,String>();
    AnalyzerThread evalThread;
    Mapper testmapper;
    Boolean[] daysSaved;
    ArrayList<String> cachedOutput;
    int currentSolution = 0;

	public MLBUI() {
		initUI();
        analyzer = new MlbAppControl();
        testmapper = new Mapper();
        daysSaved = new Boolean[181];
        cachedOutput = new ArrayList<String>();
        analyzerOutputStringArrayList = new ArrayList<String>();
        for(int i=0; i<daysSaved.length; i++) {
            daysSaved[i] = false;
        }


	}

    private void debug(String msg) {
        if(debug) {
            System.out.println(this.getClass().getSimpleName() + ": " + msg);
        }
    }

	private void initUI() {
		setTitle(scheduler.SchedulerConstants.APP_TITLE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        createMenuBar();  
        createMainAppBody();
        createInputScroll ();
        createLayout(appInfo, scroll, evaluateButton, inputScroll, outputScroll, stopCurrentEvaluationButton, saveToOverallScheduleButton, resetScheduleButton,
            showFreeDaysButton, showOverallScheduleButton, teamNameLabel, teamNameComboBox, nextScheduleSolutionButton, previousScheduleSolutionButton,
            showCurrentTeamStatisticsButton, loadLastScheduleButton, removeGameButton, removeGameTextField, dayRangeStartLabel, dayRangeStart, addGameButton,
            addGameTextField);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

    private void showNoGameDays() {
        outputPlaceholder.setText("Days still available with no games:\n");
        daysSaved = testmapper.showNoGameDays();
        for(int i=0; i<daysSaved.length; i++) {
            if(daysSaved[i]==true) {
                outputPlaceholder.append("    Day " + i + "\n");
            }
        }
    }

    private void showOverallSchedule() {
        outputPlaceholder.setText(testmapper.toString());
    }

    private void showCurrentTeamStatistics() {
        String[] allTeams = scheduler.SchedulerConstants.FULL_LEAGUE_LIST;
        outputPlaceholder.setText("");
        for(String reportTeam : allTeams) {
            int numGames = testmapper.countGamesForTeam(reportTeam.trim());
            outputPlaceholder.append(reportTeam + "  " + numGames + "\n");
            numGames = testmapper.countDivisionGamesForTeam(reportTeam.trim());
            outputPlaceholder.append("  Division # of games: " + numGames + "\n");
            outputPlaceholder.append("  Interdivision # of games: TBD\n");
            outputPlaceholder.append("  Interleague # of games: TBD\n");
            numGames = testmapper.countHomeGamesForTeam(reportTeam.trim());
            outputPlaceholder.append("  Total home games: " + numGames + "\n");
        }

    }

    private void updateCurrentParameters() {
        currentParameters = new StringBuilder();
        currentParameters.append(String.valueOf(numTeamGroups.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(numTeamsPerGroup.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(numSeries.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(numFourGameSeries.getSelectedItem()) + "-");
       // currentParameters.append(String.valueOf(dayRangeStart.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(dayRangeEnd.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(teamNumGamesMin.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(teamNumGamesMax.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(teamSingleNumGamesMin.getSelectedItem()) + "-");
        currentParameters.append(String.valueOf(teamSingleNumGamesMax.getSelectedItem()) + "-");
        
        if(addNoFourGameAwayStands.isSelected()) {
            debug("No four game away is sel.");
            currentParameters.append("T" + "-");
        } else {
            currentParameters.append("F" + "-");
        }
        if(addPredHasHalfHomeGames.isSelected()) {
            debug("Half home is sel.");
            currentParameters.append("T" + "-");
        } else {
            currentParameters.append("F" + "-");
        }
        if(addPredNoConsecutiveSeries.isSelected()){
            debug("No consec ser is sel.");
            currentParameters.append("T" + "-");
        } else {
            currentParameters.append("F" + "-");
        }
        currentParameters.append(String.valueOf(numRuns.getSelectedItem()) + "-");
    }

    private void resetSchedule() {
        testmapper = new Mapper();
        for(int i=0; i<daysSaved.length; i++) {
            daysSaved[i] = false;
        }
        try {
            File file = new File(scheduler.SchedulerConstants.SAVE_REPOK_LOCATION);
            file.delete();
        } catch (Exception e) {
            debug("Unable to delete repOk.out, possibly already deleted.");
        }
    }

    private void saveSubScheduleToSchedule() {
        if(parser != null) {
            // ToDo update for day range change.
            int dayStart = Integer.parseInt(String.valueOf(dayRangeStart.getSelectedItem()));
            int dayEnd = dayStart + parser.getEndDay();
            debug("The start day is: " + dayStart + " The day range end is: " + dayEnd);
            outputPlaceholder.setText("Saving to full schedule.\n");
            for(int i=dayStart; i<=dayEnd; i++) {
                if(daysSaved[i] == true) {
                    outputPlaceholder.append("WARNING: Games already saved on day: " + i + "\n");
                } else {
                    daysSaved[i] = true;
                }
            }
            if(parser.getAllTeams().size()==5) {
                debug("Calling create div schedule");
                testmapper.loadTeams();
                ArrayList<String> currentTeamList = getTeamNamesSelected(String.valueOf(teamNameComboBox.getSelectedItem()).trim());
                if(currentTeamList.size() == 5) {
                    testmapper.createSingleDivisionSchedule(parser, currentTeamList, dayStart);
                } else {
                    outputPlaceholder.setText("");
                    outputPlaceholder.setText("Not a valid team # and name combination.");  
                }
            } else if (parser.getAllTeams().size() == 15) {
                debug("Calling create interdivision div schedule");
                testmapper.loadTeams();
                //testmapper.createInterDivisionSchedule(parser);
                ArrayList<String> currentTeamList = getTeamNamesSelected(String.valueOf(teamNameComboBox.getSelectedItem()).trim());
                if(currentTeamList.size() == 15) {
                    testmapper.createMultiDivisionSchedule(parser, currentTeamList, dayStart);
                } else {
                    outputPlaceholder.setText("");
                    outputPlaceholder.setText("Not a valid team # and name combination.");  
                }
            } else if (parser.getAllTeams().size() == 30) {
                testmapper.loadTeams();
                //testmapper.createInterLeagueSchedule(parser);
                ArrayList<String> currentTeamList = getTeamNamesSelected(String.valueOf(teamNameComboBox.getSelectedItem()).trim());
                if(currentTeamList.size() == 30) {
                    testmapper.createMultiDivisionSchedule(parser, currentTeamList, dayStart);
                } else {
                    outputPlaceholder.setText("");
                    outputPlaceholder.setText("Not a valid team # and name combination.");  
                }
            } else {
                outputPlaceholder.setText("");
                outputPlaceholder.append("Not a valid team # combination.");
            }
        }
    }

    public ArrayList<String> getTeamNamesSelected(String teamName) {
        // Get values from team name list dropdown
        ArrayList<String> thisTeamList = new ArrayList<String>();
         switch (teamName) {
            case "Full Leagues":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.FULL_LEAGUE_LIST));
                break;
            case "Full American League":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.AMERICAN_LEAGUE_LIST));
                break;
            case "American League East":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.AL_EAST_LIST));
                break;
            case "American League West":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.AL_WEST_LIST));
                break;
            case "American League Central":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.AL_CENTRAL_LIST));
                break;
            case "Full National League":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.NATIONAL_LEAGUE_LIST));
                break;
            case "National League East":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.NL_EAST_LIST));
                break;
            case "National League West":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.NL_WEST_LIST));
                break;
            case "National League Central":
                thisTeamList = new ArrayList<String>(Arrays.asList(scheduler.SchedulerConstants.NL_CENTRAL_LIST));
                break;
            default:
                debug("Found no team list for this selection.");
        }
        return thisTeamList;
    }

    private void saveModelToFile() {

        //Check input parameters and create the model.  format of current params: 1-5-20-20-1-35-1-3-0-T-T-T-
        // 1-5-20-20-1-20-No Value-No Value-0-F-F-F-
        ModelFileGenerator newModel = new ModelFileGenerator();
        String[] params = currentParameters.toString().split("-");
        debug("Param array length: " + params.length);
        debug("Current parameters is: " + currentParameters.toString());
        debug("Number team groups: " + params[0]);
        debug("Number of teams per group: " + params[1]);
        //params[0] and params[1] = number of team groups and number of teams per group
        newModel.setupNumberTeams(Integer.parseInt(params[0]),Integer.parseInt(params[1]));
        //params[2] number of series
        debug("Number of series specified: " + params[2]);
        if(!params[2].equals("No Value")) {
            newModel.setupNumberSeries(Integer.parseInt(params[2]));
        }
        //params[3] number of four game series per team
        debug("Number of four game series per team: " + params[3]);
        if(!params[3].equals("No Value")) {
            newModel.addPredSetFourGameSeries(Integer.parseInt(params[3]));
        }
        //params[4] and params[5] - day range start and end.
        //debug("Day range start: " + params[4]);
        debug("Day range end: " + params[4]);
        if(Integer.parseInt(params[4]) > 0) {
            newModel.setupPossibleDays(1,Integer.parseInt(params[4]));
        }
        //params[5] and params[6] - number of games against all teams min and max
        debug("Team number games min: " + params[5]);
        debug("Team number games max: " + params[6]);
        if(!params[5].equals("No Value")) {
            if(!params[6].equals("No Value")) {
                if(Integer.parseInt(params[5]) <= Integer.parseInt(params[6])) {
                    newModel.addPredTeamNumberGames(Integer.parseInt(params[5]),Integer.parseInt(params[6]));
                } 
            } else {
                newModel.addPredTeamNumberGames(Integer.parseInt(params[5]),0);              
            }
        } 
        //params[7] and params[8] - number of games against all teams min and max
        debug("Single team number games min: " + params[7]);
        debug("Single team number games max: " + params[8]);
        if(!params[7].equals("No Value")) {
            if(!params[8].equals("No Value")) {
                if(Integer.parseInt(params[7]) <= Integer.parseInt(params[8])) {
                    newModel.addPredSingleTeamNumberGames(Integer.parseInt(params[7]),Integer.parseInt(params[8]));
                }
            } else {
                newModel.addPredSingleTeamNumberGames(Integer.parseInt(params[7]),0);              
            }
        } 

        String customPredString = customPred.getText();
        String customPredInShowString = customPredInShow.getText();
        debug("Additional custom predicate added: " + customPredString);
        debug("Predicate call added to show: " + customPredInShowString);
        if(customPredString != null && customPredInShowString != null) {
            newModel.addCustomPred(customPredString);
            newModel.addCustomPredInShow(customPredInShowString);
        }
        if(params[9].equals("T")) {
            debug("No four game away stands is selected.");
            newModel.addPredNoFourGameAwayStands();
        } 
        if(params[10].equals("T")) {
            debug("Has half home games is selected.");
            newModel.addPredHasHalfHomeGames();
        }
        if(params[11].equals("T")){
            debug("No consecutive is selected");
            newModel.addPredNoConsecutiveSeries();
        }
        debug("Number of runs specified: " + params[12]);
        //Make sure number of runs is at least as many as number of series
        //specified, if number of series was specified otherwise no 
        //instance will be found
        //params[2] number of series
        if(!params[2].equals("No Value")) {
            if(Integer.parseInt(params[12]) < Integer.parseInt(params[2])) {
                debug("NOTE: Not enough runs specified, \ncorrected to " + params[2] + " to match number of series!\n");
                newModel.setupRuns(Integer.parseInt(params[2]));
            } else {
                newModel.setupRuns(Integer.parseInt(params[12]));
            }
        } else {
            newModel.setupRuns(Integer.parseInt(params[12]));
        }
        ArrayList<String> currentModel = newModel.getModel();
        inputALS.setText("");
        for(int i=0;i<currentModel.size(); i++) {
            inputALS.append(currentModel.get(i) + "\n");
        }
        newModel.writeWorkingModelToFile(scheduler.SchedulerConstants.SAVE_MODEL_LOCATION);       
    }

    private String[] getAnalyzerInput() {
        String[] analyzerInputs = new String[1];
        Path path = Paths.get(scheduler.SchedulerConstants.SAVE_MODEL_LOCATION);
        Path absolutePath = path.toAbsolutePath();
        analyzerInputs[0] = absolutePath.toString();
        return analyzerInputs;
    }

    private void createMenuBar() {

        file.setMnemonic(KeyEvent.VK_F);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText(scheduler.SchedulerConstants.EXIT_TOOLTIP);
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(evalThread != null) {
                    evalThread.interrupt();
                }
                System.exit(0);
            }
        });
        file.add(eMenuItem);
        menubar.add(file);
        setJMenuBar(menubar);
    }

    private void createInputScroll() {
        numTeamGroups = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_NO_ZERO);
        scrollPanelInputs.setMinimumSize(new Dimension(300, 300));  
        scrollPanelInputs.add(numTeamGroupsLabel);
        scrollPanelInputs.add(numTeamGroups);
        scrollPanelInputs.add(numTeamsPerGroupLabel);
        numTeamsPerGroup = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_TEAMS);
        scrollPanelInputs.add(numTeamsPerGroup);
        scrollPanelInputs.add(numSeriesLabel);
        numSeries = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(numSeries);
        scrollPanelInputs.add(numFourGameSeriesLabel);
        numFourGameSeries = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(numFourGameSeries);
        //scrollPanelInputs.add(dayRangeStartLabel);
        //dayRangeStart = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_DAY_START);
        //scrollPanelInputs.add(dayRangeStart);
        scrollPanelInputs.add(dayRangeEndLabel);
        dayRangeEnd = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_DAY_END);
        scrollPanelInputs.add(dayRangeEnd);
        scrollPanelInputs.add(teamNumGamesMinLabel);
        teamNumGamesMin = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(teamNumGamesMin);
        scrollPanelInputs.add(teamNumGamesMaxLabel);
        teamNumGamesMax = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(teamNumGamesMax);
        scrollPanelInputs.add(teamSingleNumGamesMinLabel);
        teamSingleNumGamesMin = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(teamSingleNumGamesMin);
        scrollPanelInputs.add(teamSingleNumGamesMaxLabel);
        teamSingleNumGamesMax = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(teamSingleNumGamesMax);
        scrollPanelInputs.add(addNoFourGameAwayStands);
        scrollPanelInputs.add(addPredHasHalfHomeGames);
        scrollPanelInputs.add(addPredNoConsecutiveSeries);
        numRuns = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_TEAMS);
        scrollPanelInputs.add(numRunsLabel);
        scrollPanelInputs.add(numRuns);
        scrollPanelInputs.add(customPredInShowLabel);
        scrollPanelInputs.add(customPredInShow);
        scrollPanelInputs.add(customPredLabel);
        scrollPanelInputs.add(customPredScrollPane);
        inputScroll = new JScrollPane(scrollPanelInputs);
        inputScroll.setMinimumSize(new Dimension(300,300));        
    }

    private void createMainAppBody() {

        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                    updateCurrentParameters();
                    cachedOutput = new ArrayList<String>();
                    cachedOutput = checkCachedOutput(currentParameters.toString());
                    currentSolution = 0;
                    if(cachedOutput.isEmpty()){
                        saveModelToFile();
                        debug("Model saved!");
                        evalThread = new AnalyzerThread("Testing");
                        evalThread.start();
                    } else {
                        try {
                            saveModelToFile();
                            updateOutputUI();
                        } catch (Exception e) {
                            outputPlaceholder.setText(scheduler.SchedulerConstants.RUN_FAILURE);
                            debug(scheduler.SchedulerConstants.RUN_FAILURE);
                        }
                    }
            }
        });
        stopCurrentEvaluationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                    outputPlaceholder.setText(scheduler.SchedulerConstants.EVAL_STOP_MESSAGE);
                    evalThread.interrupt();
                    evalThread = null;
            }
        });
        saveToOverallScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                saveSubScheduleToSchedule();
            }
        });
        resetScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                debug("Clearing full schedule.");
                resetSchedule();
            }
        });
        showFreeDaysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showNoGameDays();
            }
        });
        showOverallScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showOverallSchedule();
            }
        });
        nextScheduleSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(!cachedOutput.isEmpty()) {
                    currentSolution++;
                    if(currentSolution >= cachedOutput.size()) {
                        currentSolution = 0;
                        debug("Reached end of solutions, loop to beginning.");
                    }
                    updateOutputUI();

                } else {
                    outputPlaceholder.setText("");
                    outputPlaceholder.setText("First run analyzer for solution.");  
                }
            }
        });
        previousScheduleSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(!cachedOutput.isEmpty()) {
                    currentSolution--;
                    if(currentSolution <= 0) {
                        currentSolution = cachedOutput.size()-1;
                        debug("Reached end of solutions, loop to beginning.");
                    }
                    updateOutputUI();

                } else {
                    outputPlaceholder.setText("");
                    outputPlaceholder.setText("First run analyzer for solution.");  
                }
            }
        });
        showCurrentTeamStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showCurrentTeamStatistics();
            }
        });
        loadLastScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                debug("Loading last schedule saved.");

                testmapper.loadLastSchedule();
                daysSaved = testmapper.showNoGameDays();
                //loop through each team
                //print days off
                String[] fullLeagueList = scheduler.SchedulerConstants.FULL_LEAGUE_LIST;
                for(String thisTeam : fullLeagueList) {
                    Boolean[] result = testmapper.findTeamsDaysOff(thisTeam);
                    int count = 0;
                    for(int i=0; i<result.length; i++) {
                        if(result[i] == true) {
                            count++;
                        }
                    }
                    debug(thisTeam + " has " + count + " days off.");
                }
            }
        });
        removeGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                debug("Removing game.");
                String removeGameInput = removeGameTextField.getText();
                removeGame(removeGameInput);

            }
        });
        addGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                debug("Adding game.");
                String addGameInput = addGameTextField.getText();
                addGame(addGameInput);

            }
        });

    }

    private void removeGame(String removeGameInput) {
        // (Enter <Day#>, <Game#>-<Game#>)
        // abc - invalid
        // 0,1 - remove day 0 game 1
        // 0,1 - 4
        int day = 0;
        int gameStart = 0;
        int gameEnd = 0;
        StringTokenizer st = new StringTokenizer(removeGameInput, ",-");
        try {
            if(st.hasMoreTokens()) 
                day = Integer.parseInt(st.nextToken().trim());
            if(st.hasMoreTokens()) 
                gameStart = Integer.parseInt(st.nextToken().trim());
            if(st.hasMoreTokens()) {
                gameEnd = Integer.parseInt(st.nextToken().trim());
            } else {
                gameEnd = gameStart;
            }
            if(gameStart <= gameEnd && day < 181) {
                outputPlaceholder.setText("");
                outputPlaceholder.setText("Remove game: " + day + ", " + gameStart + " - " + gameEnd);
                for(int i=gameEnd; i>=gameStart; i--) {
                    try {
                        testmapper.removeGame(i,day);
                    } catch (Exception e) {
                        outputPlaceholder.append("  Day: " + day + " Game: " + i + " out of range.\n");                        
                    }
                }
            } else {
                outputPlaceholder.setText("");
                outputPlaceholder.setText("To remove game, start must be less\n than end and day must be less than 181.");
            }
        } catch (Exception e) {
            outputPlaceholder.setText("");
            outputPlaceholder.setText("Invalid remove day format, Ex. use: 5, 6-7 or 5,6");
            e.printStackTrace();
        }
    }

    private void addGame(String addGameInput) {
        //Input game formate <home team>,<away team>,<time>,<day>
        StringTokenizer st = new StringTokenizer(addGameInput, ",");
        String home = "";
        String away = "";
        String time = "";
        String day = "";
        if(st.hasMoreTokens())
            home = st.nextToken().trim();
        if(st.hasMoreTokens())
            away = st.nextToken().trim();
        if(st.hasMoreTokens())
            time = st.nextToken().trim();
        if(st.hasMoreTokens())
            day = st.nextToken().trim();
        if(!Arrays.asList(scheduler.SchedulerConstants.FULL_LEAGUE_LIST).contains(home)) {
            outputPlaceholder.setText("");
            outputPlaceholder.setText("Invalid home team name.");
            return;
        }
        if(!Arrays.asList(scheduler.SchedulerConstants.FULL_LEAGUE_LIST).contains(away)) {
            outputPlaceholder.setText("");
            outputPlaceholder.setText("Invalid away team name");
            return;
        }
        if (!Arrays.asList(scheduler.SchedulerConstants.TIMES).contains(time)) {
            outputPlaceholder.setText("");
            outputPlaceholder.setText("Invalid time.  \nValid times are: \n5:00 PM, 7:00 PM, 8:00 PM,\n 9:00 PM, 1:00 PM, 3:00 PM,\n 4:00 PM, 10:00 PM");
            return;
        }
        if(0 > Integer.parseInt(day) && Integer.parseInt(day) > 181) {
            outputPlaceholder.setText("");
            outputPlaceholder.setText("Day must be a value 1-180.");
            return;
        }
        testmapper.addGame(home, away, time, (home + " Field"), Integer.parseInt(day));
        outputPlaceholder.setText("");
        outputPlaceholder.setText("Game added: \n  Home: " + home + "\n  Away: " + away + "\n  Time: " + time + "\n  Location: " + (home + " Field")
                                 + "\n  Day: " + day);

    }

    private void saveAnalyzerOutput(ArrayList<String> saveOutput, String parametersUsed) {
        debug("Saving analyzer output for future use to: " + scheduler.SchedulerConstants.OUTPUT_CACHE_LOCATION + parametersUsed);
        if(!saveOutput.isEmpty()) {
            int fileCount = 0;
            Boolean success = (new File(scheduler.SchedulerConstants.OUTPUT_CACHE_LOCATION + parametersUsed)).mkdirs();
            if (!success) { debug("Solution directory failed for " + parametersUsed);} else {
                debug("Solution directory success.");
            }
            try {
                while(!saveOutput.isEmpty()) {
                    PrintWriter out = new PrintWriter(scheduler.SchedulerConstants.OUTPUT_CACHE_LOCATION + parametersUsed + "\\solution" + fileCount);
                    String currentOutput = saveOutput.remove(fileCount);
                    String[] temp = currentOutput.split("\\n");
                    for(String line : temp) {
                        //debug(line);
                        out.println(line);
                    }
                    out.close();
                    fileCount++;
                }
            } catch (Exception e) {
                System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
            }
        }
    }

    private ArrayList<String> checkCachedOutput(String inputParams) {
        ArrayList<String> returnList = new ArrayList<String>();
        Boolean outOfFiles = false;
        int fileCount = 0;
        while(!outOfFiles) {
            String returnOutput = null;
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(scheduler.SchedulerConstants.OUTPUT_CACHE_LOCATION + inputParams + "\\solution" + fileCount))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                returnOutput = sb.toString();
                returnList.add(returnOutput);
            } catch (Exception e) {
                debug("File not found in output cache.");
                outOfFiles = true;
            }
            fileCount++;
            debug(fileCount + " files found in output cache.");
        }
        //debug("Cached output: " + returnOutput);
        return returnList;
    }

    private void updateOutputUI() {
        // TODO: could add a next/back button to the UI to allow scrolling through 
        // multiple solutions, instead of just the first.
        // Todo: The execution time is now coming back in the model
        // add a text output to show the time it took to generate the solution,
        // probably want to create some kind of java class to store the
        // time statistic for each sub schedule.  Or possibly add this info to 
        // the schedule class.
        if(!cachedOutput.isEmpty() && currentSolution < cachedOutput.size()) {
            String thisOutput = cachedOutput.get(currentSolution);
            parser = new ScheduleOutParser(thisOutput);
            ArrayList<String> series = parser.parseSeries();
            outputPlaceholder.setText("");
            debug("Get all teams: " + parser.getAllTeams().size());
            for(int i=0; i<series.size(); i++) {
                //debug(series.get(i));
                outputPlaceholder.append(series.get(i) + ": \n");
                ArrayList<String> temp = parser.getTeamsForSeries(series.get(i));
                if(temp.size() == 2) {
                    outputPlaceholder.append("     Away team: " + temp.get(0) + "\n");
                    outputPlaceholder.append("     Home team: " + temp.get(1) + "\n");
                }
                temp = parser.getGamesForSeries(series.get(i));
                for(int m=0; m<temp.size(); m++) {
                    outputPlaceholder.append("     " + temp.get(m) + "\n");
                }
            }
            outputPlaceholder.append("\n****************\nListing Team Schedules: \n");
            ArrayList<String> currentTeams = parser.getAllTeams();
            debug("The current teams size is: " + currentTeams.size());
            for(int i=0; i< currentTeams.size(); i++) {
                outputPlaceholder.append("Schedule " + currentTeams.get(i) + " is: \n");
                ArrayList teamSchedule = parser.getTeamSchedule(currentTeams.get(i));
                for(int j=0; j< teamSchedule.size(); j++) {
                    outputPlaceholder.append("  " + teamSchedule.get(j) + "\n");
                } 
            }
        }
    }


	private void createLayout(JComponent... arg) {

        GroupLayout gl = new GroupLayout(getContentPane());
        getContentPane().setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
//appInfo, scroll, evaluateButton, inputScroll, outputScroll, stopCurrentEvaluationButton, saveToOverallScheduleButton, resetScheduleButton,
//            showFreeDaysButton, showOverallScheduleButton, teamNameLabel, teamNameComboBox, nextScheduleSolution, previousScheduleSolution,
//            showCurrentTeamStatisticsButton, loadLastScheduleButton, removeGameButton, removeGameTextField, dayRangeStartLabel, dayRangeStart
//            addGameButton, addGameTextField
        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[0])
                .addComponent(arg[3])
                .addComponent(arg[8])
                .addComponent(arg[9])
                .addComponent(arg[6])
                .addComponent(arg[14])
                .addComponent(arg[15]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[1])
                .addComponent(arg[2])
                .addComponent(arg[5])
                .addComponent(arg[7])
                .addGroup(gl.createSequentialGroup()
                    .addComponent(arg[16])
                    .addComponent(arg[17]))
                .addGroup(gl.createSequentialGroup()
                    .addComponent(arg[20])
                    .addComponent(arg[21])))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[4])
                .addComponent(arg[10])
                .addComponent(arg[11])
                .addComponent(arg[12])
                .addComponent(arg[13])
                .addComponent(arg[18])
                .addComponent(arg[19]))
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(arg[0])
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[1])
                .addComponent(arg[3])
                .addComponent(arg[4]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[8])
                .addComponent(arg[2])
                .addComponent(arg[10]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[9])
                .addComponent(arg[5])
                .addComponent(arg[11]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[6])
                .addComponent(arg[7])
                .addComponent(arg[12]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[14])
                .addComponent(arg[13])
                .addComponent(arg[16])
                .addComponent(arg[17]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[15])
                .addComponent(arg[18])
                .addComponent(arg[20])
                .addComponent(arg[21]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[19]))
        );
    }

	public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {        
            @Override
            public void run() {
                MLBUI mlbUi = new MLBUI();
                mlbUi.setVisible(true);
            }
        });
	}

    public class AnalyzerThread extends Thread {

    Boolean debug = true;
 
    public AnalyzerThread(String str) {
        super(str);
    }
 
    public void run() {
        try {
            outputPlaceholder.setText(scheduler.SchedulerConstants.START_EVAL_MESSAGE);
            cachedOutput = analyzer.runAnalysis(getAnalyzerInput());
            currentSolution = 0;
            if(!isInterrupted()) {
                //outputCache.put(currentParameters.toString(),analyzerOutputStringArrayList);
                saveAnalyzerOutput(cachedOutput,currentParameters.toString());
                //debug(analyzerOutputStringArrayList);
                updateOutputUI();
            }
        } catch(Exception e) {
            outputPlaceholder.setText(scheduler.SchedulerConstants.RUN_FAILURE);
            debug(scheduler.SchedulerConstants.RUN_FAILURE);
        }
        debug(scheduler.SchedulerConstants.RUN_SUCCESS);
    }
    }
}