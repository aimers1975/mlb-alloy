package scheduler;

public class SchedulerConstants {
    public static final String APP_LABEL = "Set Team Scheduling Parameters";
    public static final String APP_TITLE = "Team Scheduler";
    public static final String EVALUATE_BUTTON = "Evaluate Model";
    public static final String INPUTALS_TEXT = "Type Alloy Code Here.";
    public static final String EXIT_MENUITEM = "Exit";
    public static final String FILE_JMENU = "Exit";
    public static final String SAVE_MODEL_LOCATION = "scheduler\\model.als";
    public static final String TEMPLATE_MODEL_LOCATION = "scheduler\\model.template";
    public static final String SAVE_SCHEDULE_LOCATION = "scheduler\\schedule.out";
    public static final String INPUT_TEAM_LIST = "scheduler\\teams.txt";
    public static final String OUTPUT_CACHE_LOCATION = "scheduler\\outputCache\\";
    public static final String CREATE_FILE_ERROR = "Failed to create model file.";
    public static final String EXIT_TOOLTIP = "Exit application";
    public static final String RUN_FAILURE = "Failure to run analyzer, or no instance found...";
    public static final String RUN_SUCCESS = "Evaluation started!";
    public static final String ADD_PRED_HAS_HALF_HOME_CHECKBOX = "Every team has half home games.";
    public static final String ADD_NO_FOUR_GAME_AWAY_STANDS = "No four game away stands.";
    public static final String ADD_PRED_NO_CONSECUTIVE_SERIES = "No two teams have consecutive.";
    public static final String NUM_TEAM_GROUP_LABEL = "Number team groups:";
    public static final String NUM_TEAMS_PER_GROUP_LABEL = "Number teams per group: ";
    public static final String NUM_SERIES_LABEL = "Specify limit on number of series: ";
    public static final String NUM_FOUR_GAME_SERIES_LABEL = "Specify the number of four game series: ";
    public static final String NUM_RUNS_LABEL = "Specify limit on number of runs: ";
    public static final String DAY_RANGE_START_LABEL = "Specify start of this day range: ";
    public static final String DAY_RANGE_END_LABEL = "Specify end of this day range: ";
    public static final String CUSTOM_PRED_IN_SHOW_LABEL = "Custom predicate call for show function: ";
    public static final String CUSTOM_PRED_LABEL = "Custom predicate function: ";
    public static final String TEAM_NUM_GAMES_MIN_LABEL = "Specify the minimum number of games between each two team pair.";
    public static final String TEAM_NUM_GAMES_MAX_LABEL = "Specify the maximum number of games between each two team pair.";
    public static final String TEAM_SINGLE_NUM_GAMES_MIN_LABEL = "Specify the minimum number of games for each team.";
    public static final String TEAM_SINGLE_NUM_GAMES_MAX_LABEL = "Specify the maximum number of games for each team.";
    public static final String INPUT_PLACEHOLDER = "Placeholder for input selections";
    public static final String OUTPUT_PLACEHOLDER = "Placeholder for outputs";
    public static final String STOP_EVAL_BUTTOM = "Stop Current Evaluation";
    public static final String EVAL_STOP_MESSAGE = "Evaluation halted...";
    public static final String SAVE_TO_OVERALL_SCHEDULE_BUTTOM = "Save To Overall Schedule";
    public static final String SHOW_OVERALL_SCHEDULE_BUTTON = "Show Overall Schedule";
    public static final String START_EVAL_MESSAGE ="Starting Evaluation...";
    public static final String[] NUM_LIST_DAY_START = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178"};
    public static final String[] NUM_LIST_DAY_END = {"3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179",
                            "180"};
    public static final String[] NUM_LIST_TEAMS = {"2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179",
                            "180"};
    public static final String[] NUM_LIST_NO_ZERO = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179",
                            "180"};
    public static final String[] NUM_LIST = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179",
                            "180"};
    public static final String[] NUM_LIST_WITH_BLANK = {"No Value","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39",
                            "40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60",
                            "61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80",
                            "81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100",
                            "101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119",
                            "120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139",
                            "140","141","142","143","144","145","146","147","148","149","150","151","152","153","154","155","156","157","158","159",
                            "160","161","162","163","164","165","166","167","168","169","170","171","172","173","174","175","176","177","178","179",
                            "180"};

    
}

