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
    JButton stopCurrentEvaluationButton = new JButton(scheduler.SchedulerConstants.STOP_EVAL_BUTTOM);
    JButton saveToOverallScheduleButton = new JButton(scheduler.SchedulerConstants.SAVE_TO_OVERALL_SCHEDULE_BUTTOM);
    JButton showOverallScheduleButton = new JButton(scheduler.SchedulerConstants.SHOW_OVERALL_SCHEDULE_BUTTON);
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
    JComboBox dayRangeStart;
    JLabel dayRangeEndLabel = new JLabel(scheduler.SchedulerConstants.DAY_RANGE_END_LABEL);
    JComboBox dayRangeEnd;
    JLabel teamNumGamesMinLabel = new JLabel(scheduler.SchedulerConstants.TEAM_NUM_GAMES_MIN_LABEL);
    JComboBox teamNumGamesMin;
    JLabel teamNumGamesMaxLabel = new JLabel(scheduler.SchedulerConstants.TEAM_NUM_GAMES_MAX_LABEL);
    JComboBox teamNumGamesMax;
    JCheckBox addPredHasHalfHomeGames = new JCheckBox(scheduler.SchedulerConstants.ADD_PRED_HAS_HALF_HOME_CHECKBOX);
    JCheckBox addNoFourGameAwayStands = new JCheckBox(scheduler.SchedulerConstants.ADD_NO_FOUR_GAME_AWAY_STANDS);
    JCheckBox addPredNoConsecutiveSeries = new JCheckBox(scheduler.SchedulerConstants.ADD_PRED_NO_CONSECUTIVE_SERIES);
    JLabel customPredInShowLabel = new JLabel(scheduler.SchedulerConstants.CUSTOM_PRED_IN_SHOW_LABEL);
    JTextField customPredInShow = new JTextField();
    JLabel customPredLabel = new JLabel(scheduler.SchedulerConstants.CUSTOM_PRED_LABEL);
    JTextArea customPred = new JTextArea(1,1);
    JScrollPane customPredScrollPane = new JScrollPane(customPred);
    JPanel scrollPanelInputs = new JPanel(new GridLayout(26, 1));
    JScrollPane inputScroll;
    JPanel outputPanel = new JPanel();
    JTextArea outputPlaceholder = new JTextArea(200, 200);
    JScrollPane outputScroll = new JScrollPane(outputPlaceholder);
    JMenuBar menubar = new JMenuBar();
    JMenu file = new JMenu(scheduler.SchedulerConstants.FILE_JMENU);
    JMenuItem eMenuItem = new JMenuItem(scheduler.SchedulerConstants.EXIT_MENUITEM);
    String analyzerOutputString = "";
    ScheduleOutParser parser;
    StringBuilder currentParameters;
    HashMap<String,String> outputCache = new HashMap<String,String>();
    AnalyzerThread evalThread;

	public MLBUI() {
		initUI();
        analyzer = new MlbAppControl();

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
        createLayout(appInfo, scroll, evaluateButton, inputScroll, outputScroll, stopCurrentEvaluationButton);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

    private void saveModelToFile() {

        //Check input parameters and create the model
        ModelFileGenerator newModel = new ModelFileGenerator();
        currentParameters = new StringBuilder();
        String numTeamGroupsString = String.valueOf(numTeamGroups.getSelectedItem());
        currentParameters.append(numTeamGroupsString + "-");
        String numTeamsPerGroupString = String.valueOf(numTeamsPerGroup.getSelectedItem());
        currentParameters.append(numTeamsPerGroupString + "-");
        String numSeriesString = String.valueOf(numSeries.getSelectedItem());
        currentParameters.append(numSeriesString + "-");
        String numRunsString = String.valueOf(numRuns.getSelectedItem());
        currentParameters.append(numRunsString + "-");
        String dayRangeStartString = String.valueOf(dayRangeStart.getSelectedItem());
        currentParameters.append(dayRangeStartString + "-");
        String dayRangeEndString = String.valueOf(dayRangeEnd.getSelectedItem());
        currentParameters.append(dayRangeEndString + "-");
        String teamNumGamesMinString = String.valueOf(teamNumGamesMin.getSelectedItem());
        currentParameters.append(teamNumGamesMinString + "-");
        String teamNumGamesMaxString = String.valueOf(teamNumGamesMax.getSelectedItem());
        currentParameters.append(teamNumGamesMaxString + "-");
        String numFourGameSeriesString = String.valueOf(numFourGameSeries.getSelectedItem());
        currentParameters.append(numFourGameSeriesString + "-");
        if(addNoFourGameAwayStands.isSelected()) {
            debug("No four game away stands is selected.");
            newModel.addPredNoFourGameAwayStands();
            currentParameters.append("T" + "-");
        } else {
            currentParameters.append("F" + "-");
        }
        if(addPredHasHalfHomeGames.isSelected()) {
            debug("Has half home games is selected.");
            newModel.addPredHasHalfHomeGames();
            currentParameters.append("T" + "-");
        } else {
            currentParameters.append("F" + "-");
        }
        if(addPredNoConsecutiveSeries.isSelected()){
            debug("No consecutive is selected");
            newModel.addPredNoConsecutiveSeries();
            currentParameters.append("T" + "-");
        } else {
            currentParameters.append("F" + "-");
        }
        debug("Current parameters is: " + currentParameters.toString());
        checkCachedOutput(currentParameters.toString());
        debug("Number team groups: " + numTeamGroupsString);
        debug("Number of teams per group: " + numTeamsPerGroupString);
        newModel.setupNumberTeams(Integer.parseInt(numTeamGroupsString),Integer.parseInt(numTeamsPerGroupString));

        debug("Number of series specified: " + numSeriesString);
        if(!numSeriesString.equals("No Value")) {
            newModel.setupNumberSeries(Integer.parseInt(numSeriesString));
        }

        debug("Number of runs specified: " + numRunsString);
        if(!numSeriesString.equals("No Value")) {
            if(Integer.parseInt(numRunsString) < Integer.parseInt(numSeriesString)) {
                newModel.setupRuns(Integer.parseInt(numSeriesString));
            } else {
                newModel.setupRuns(Integer.parseInt(numRunsString));
            }
        } else {
            newModel.setupRuns(Integer.parseInt(numRunsString));
        }

        debug("Day range start: " + dayRangeStartString);
        debug("Day range end: " + dayRangeEndString);
        if(Integer.parseInt(dayRangeStartString) < Integer.parseInt(dayRangeEndString)) {
            newModel.setupPossibleDays(Integer.parseInt(dayRangeStartString),Integer.parseInt(dayRangeEndString));
        }

        debug("Team number games min: " + teamNumGamesMinString);
        debug("Team number games max: " + teamNumGamesMaxString);
        if(Integer.parseInt(teamNumGamesMinString) < Integer.parseInt(teamNumGamesMaxString)) {
            newModel.addPredTeamNumberGames(Integer.parseInt(teamNumGamesMinString),Integer.parseInt(teamNumGamesMaxString));
        }

        debug("Number of four game series per team: " + numFourGameSeriesString);
        if(!numFourGameSeriesString.equals("No Value")) {
            newModel.addPredSetFourGameSeries(Integer.parseInt(numFourGameSeriesString));
        }
        String customPredString = customPred.getText();
        String customPredInShowString = customPredInShow.getText();
        debug("Additional custom predicate added: " + customPredString);
        debug("Predicate call added to show: " + customPredInShowString);
        if(customPredString != null && customPredInShowString != null) {
            newModel.addCustomPred(customPredString);
            newModel.addCustomPredInShow(customPredInShowString);
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
        numTeamGroups = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST);
        scrollPanelInputs.setMinimumSize(new Dimension(300, 300));  
        scrollPanelInputs.add(numTeamGroupsLabel);
        scrollPanelInputs.add(numTeamGroups);
        scrollPanelInputs.add(numTeamsPerGroupLabel);
        numTeamsPerGroup = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST);
        scrollPanelInputs.add(numTeamsPerGroup);
        scrollPanelInputs.add(numSeriesLabel);
        numSeries = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(numSeries);
        scrollPanelInputs.add(numFourGameSeriesLabel);
        numFourGameSeries = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(numFourGameSeries);
        scrollPanelInputs.add(dayRangeStartLabel);
        dayRangeStart = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST);
        scrollPanelInputs.add(dayRangeStart);
        scrollPanelInputs.add(dayRangeEndLabel);
        dayRangeEnd = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST);
        scrollPanelInputs.add(dayRangeEnd);
        scrollPanelInputs.add(teamNumGamesMinLabel);
        teamNumGamesMin = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(teamNumGamesMin);
        scrollPanelInputs.add(teamNumGamesMaxLabel);
        teamNumGamesMax = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
        scrollPanelInputs.add(teamNumGamesMax);
        scrollPanelInputs.add(addNoFourGameAwayStands);
        scrollPanelInputs.add(addPredNoConsecutiveSeries);
        scrollPanelInputs.add(addPredHasHalfHomeGames);
        numRuns = new JComboBox<String>(scheduler.SchedulerConstants.NUM_LIST_WITH_BLANK);
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
                    saveModelToFile();
                    debug("Model saved!");
                    evalThread = new AnalyzerThread("Testing");
                    evalThread.start();
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

    }

    private void saveAnalyzerOutput(String saveOutput, String parametersUsed) {
        debug("Saving analyzer output for future use to: " + scheduler.SchedulerConstants.OUTPUT_CACHE_LOCATION + parametersUsed);
        try {
            PrintWriter out = new PrintWriter(scheduler.SchedulerConstants.OUTPUT_CACHE_LOCATION + parametersUsed);
            String[] temp = saveOutput.split("\\n");
            for(String line : temp) {
                debug(line);
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
        }
    }

    private String checkCachedOutput(String inputParams) {
        String returnOutput = new String();
        returnOutput = outputCache.get(currentParameters.toString());
        debug("Cached output: " + returnOutput);
        return returnOutput;
    }


	private void createLayout(JComponent... arg) {

        GroupLayout gl = new GroupLayout(getContentPane());
        getContentPane().setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
//createLayout(appInfo, scroll, evaluateButton, inputScroll, outputScroll, stopCurrentEvaluationButton);
        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[0])
                .addComponent(arg[3]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[1])
                .addComponent(arg[2])
                .addComponent(arg[5]))
            .addComponent(arg[4])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(arg[0])
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[1])
                .addComponent(arg[3])
                .addComponent(arg[4]))
            .addComponent(arg[2])
            .addComponent(arg[5])
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
            analyzerOutputString = analyzer.runAnalysis(getAnalyzerInput());
            if(!isInterrupted()) {
                outputCache.put(currentParameters.toString(),analyzerOutputString);
                saveAnalyzerOutput(analyzerOutputString,currentParameters.toString());
                //debug(analyzerOutputString);
                parser = new ScheduleOutParser(analyzerOutputString);
                ArrayList<String> series = parser.parseSeries();
                outputPlaceholder.removeAll();
                debug("Series size is: " + series.size());
                for(int i=0; i<series.size(); i++) {
                    //debug(series.get(i));
                    outputPlaceholder.append(series.get(i) + ": \n");
                    ArrayList<String> temp = parser.getTeamsForSeries(series.get(i));
                    for(int k=0; k<temp.size(); k++) {
                        outputPlaceholder.append("     " + temp.get(k) + "\n");
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
                    outputPlaceholder.append("Schedule: " + currentTeams.get(i) + " is: \n");
                    ArrayList teamSchedule = parser.getTeamSchedule(currentTeams.get(i));
                    for(int j=0; j< teamSchedule.size(); j++) {
                        outputPlaceholder.append("  " + teamSchedule.get(j) + "\n");
                    } 
                }
            }
        } catch(Exception e) {
            outputPlaceholder.setText(scheduler.SchedulerConstants.RUN_FAILURE);
            debug(scheduler.SchedulerConstants.RUN_FAILURE);
        }
        debug(scheduler.SchedulerConstants.RUN_SUCCESS);
    }
    }
}