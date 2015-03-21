package scheduler;

import java.util.*;
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


public class MLBUI extends JFrame {

    MlbAppControl analyzer;
    Boolean debug = true;
    JLabel appInfo = new JLabel(scheduler.SchedulerConstants.APP_LABEL);
    JButton evaluateButton = new JButton(scheduler.SchedulerConstants.EVALUATE_BUTTON);
    JPanel scrollPanel = new JPanel();
    JTextArea inputALS = new JTextArea(scheduler.SchedulerConstants.INPUTALS_TEXT,200,200);
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
        setSize(800, 500);
        setLocationRelativeTo(null);
        createMenuBar();  
        createMainAppBody();
        createInputScroll ();
        createLayout(appInfo, scroll, evaluateButton, inputScroll, outputScroll);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

    private void saveModelToFile() {

        //String[] lines = inputALS.getText().split("\\n");
        ModelFileGenerator newModel = new ModelFileGenerator();
        /*try {
            PrintWriter out = new PrintWriter(scheduler.SchedulerConstants.SAVE_MODEL_LOCATION);
            for(String line : lines) {
                //debug(line);
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
        }*/
        if(addNoFourGameAwayStands.isSelected()) {
            debug("No four game away stands is selected.");
            newModel.addPredNoFourGameAwayStands();
        }
        if(addPredHasHalfHomeGames.isSelected()) {
            debug("Has half home games is selected.");
            newModel.addPredHasHalfHomeGames();
        }
        if(addPredNoConsecutiveSeries.isSelected()){
            debug("No consecutive is selected");
            newModel.addPredNoConsecutiveSeries();
        }
        String numTeamGroupsString = String.valueOf(numTeamGroups.getSelectedItem());
        String numTeamsPerGroupString = String.valueOf(numTeamsPerGroup.getSelectedItem());
        debug("Number team groups: " + numTeamGroupsString);
        debug("Number of teams per group: " + numTeamsPerGroupString);
        newModel.setupNumberTeams(Integer.parseInt(numTeamGroupsString),Integer.parseInt(numTeamsPerGroupString));
        String numSeriesString = String.valueOf(numSeries.getSelectedItem());
        debug("Number of series specified: " + numSeriesString);
        if(!numSeriesString.equals("No Value")) {
            newModel.setupNumberSeries(Integer.parseInt(numSeriesString));
        }
        String numRunsString = String.valueOf(numRuns.getSelectedItem());
        if(!numSeriesString.equals("No Value")) {
            if(Integer.parseInt(numRunsString) < Integer.parseInt(numSeriesString)) {
                newModel.setupRuns(Integer.parseInt(numSeriesString));
            } else {
                newModel.setupRuns(Integer.parseInt(numRunsString));
            }
        } else {
            newModel.setupRuns(Integer.parseInt(numRunsString));
        }
        String dayRangeStartString = String.valueOf(dayRangeStart.getSelectedItem());
        String dayRangeEndString = String.valueOf(dayRangeEnd.getSelectedItem());
        if(Integer.parseInt(dayRangeStartString) < Integer.parseInt(dayRangeEndString)) {
            newModel.setupPossibleDays(Integer.parseInt(dayRangeStartString),Integer.parseInt(dayRangeEndString));
        }
        String teamNumGamesMinString = String.valueOf(teamNumGamesMin.getSelectedItem());
        String teamNumGamesMaxString = String.valueOf(teamNumGamesMax.getSelectedItem());
        if(Integer.parseInt(teamNumGamesMinString) < Integer.parseInt(teamNumGamesMaxString)) {
            newModel.addPredTeamNumberGames(Integer.parseInt(teamNumGamesMinString),Integer.parseInt(teamNumGamesMaxString));
        }
        String numFourGameSeriesString = String.valueOf(numFourGameSeries.getSelectedItem());
        if(!numFourGameSeriesString.equals("No Value")) {
            newModel.addPredSetFourGameSeries(Integer.parseInt(numFourGameSeriesString));
        }
        String customPredString = customPred.getText();
        String customPredInShowString = customPredInShow.getText();
        debug("Number of four game series per team: " + numFourGameSeriesString);
        debug("Number of runs specified: " + numRunsString);
        debug("Day range start: " + dayRangeStartString);
        debug("Day range end: " + dayRangeEndString);
        debug("Team number games min: " + teamNumGamesMinString);
        debug("Team number games max: " + teamNumGamesMaxString);
        debug("Additional custom predicate added: " + customPredString);
        debug("Predicate call added to show: " + customPredInShowString);
    
        //addCustomPred("pred myCustomPred(This:schedule) {\n  //This is a test \n}");
        //addCustomPredInShow("  myCustomPred[This]\n");
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
                System.exit(0);
            }
        });
        file.add(eMenuItem);
        menubar.add(file);
        setJMenuBar(menubar);
    }

    private void createInputScroll() {
        String[] numList = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179",
                            "180"};
        String[] numListWithBlank = {"No Value","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179",
                            "180"};
        numTeamGroups = new JComboBox<String>(numList);
        scrollPanelInputs.setMinimumSize(new Dimension(300, 300));  
        scrollPanelInputs.add(numTeamGroupsLabel);
        scrollPanelInputs.add(numTeamGroups);
        scrollPanelInputs.add(numTeamsPerGroupLabel);
        numTeamsPerGroup = new JComboBox<String>(numList);
        scrollPanelInputs.add(numTeamsPerGroup);
        scrollPanelInputs.add(numSeriesLabel);
        numSeries = new JComboBox<String>(numListWithBlank);
        scrollPanelInputs.add(numSeries);
        scrollPanelInputs.add(numFourGameSeriesLabel);
        numFourGameSeries = new JComboBox<String>(numListWithBlank);
        scrollPanelInputs.add(numFourGameSeries);
        scrollPanelInputs.add(dayRangeStartLabel);
        dayRangeStart = new JComboBox<String>(numList);
        scrollPanelInputs.add(dayRangeStart);
        scrollPanelInputs.add(dayRangeEndLabel);
        dayRangeEnd = new JComboBox<String>(numList);
        scrollPanelInputs.add(dayRangeEnd);
        scrollPanelInputs.add(teamNumGamesMinLabel);
        teamNumGamesMin = new JComboBox<String>(numListWithBlank);
        scrollPanelInputs.add(teamNumGamesMin);
        scrollPanelInputs.add(teamNumGamesMaxLabel);
        teamNumGamesMax = new JComboBox<String>(numListWithBlank);
        scrollPanelInputs.add(teamNumGamesMax);
        scrollPanelInputs.add(addNoFourGameAwayStands);
        scrollPanelInputs.add(addPredNoConsecutiveSeries);
        scrollPanelInputs.add(addPredHasHalfHomeGames);
        numRuns = new JComboBox<String>(numListWithBlank);
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
                try {
                    saveModelToFile();
                    debug("Model saved!");
                    analyzerOutputString = analyzer.runAnalysis(getAnalyzerInput());
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
                } catch(Exception e) {
                    debug(scheduler.SchedulerConstants.RUN_FAILURE);
                }
                debug(scheduler.SchedulerConstants.RUN_SUCCESS);
            }
        });
    }


	private void createLayout(JComponent... arg) {

        GroupLayout gl = new GroupLayout(getContentPane());
        getContentPane().setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[0])
                .addComponent(arg[3]))
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[1])
                .addComponent(arg[2]))
            .addComponent(arg[4])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(arg[0])
            .addGroup(gl.createParallelGroup()
                .addComponent(arg[1])
                .addComponent(arg[3])
                .addComponent(arg[4]))
            .addComponent(arg[2])
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
}