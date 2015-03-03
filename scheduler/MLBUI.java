package scheduler;

import java.util.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.EventQueue;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import scheduler.MlbAppControl;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import scheduler.SchedulerConstants;


public class MLBUI extends JFrame {

    MlbAppControl analyzer;
    Boolean debug = true;
    JLabel appInfo = new JLabel(scheduler.SchedulerConstants.APP_LABEL);
    JButton evaluateButton = new JButton(scheduler.SchedulerConstants.EVALUATE_BUTTON);
    JPanel scrollPanel = new JPanel();
    JTextArea inputALS = new JTextArea(scheduler.SchedulerConstants.INPUTALS_TEXT,200,200);
    JScrollPane scroll = new JScrollPane(inputALS);
    JPanel scrollPanelInputs = new JPanel();
    JTextArea inputPlaceholder = new JTextArea(scheduler.SchedulerConstants.INPUT_PLACEHOLDER,200,200);
    JScrollPane inputScroll = new JScrollPane(inputPlaceholder);
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
        createLayout(appInfo, scroll, evaluateButton, inputScroll, outputScroll);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

    private void saveModelToFile() {

        String[] lines = inputALS.getText().split("\\n");
        try {
            PrintWriter out = new PrintWriter(scheduler.SchedulerConstants.SAVE_MODEL_LOCATION);
            for(String line : lines) {
                debug(line);
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println(scheduler.SchedulerConstants.CREATE_FILE_ERROR);
        }

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

    private void createMainAppBody() {

        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    saveModelToFile();
                    debug("Model saved!");
                    analyzerOutputString = analyzer.runAnalysis(getAnalyzerInput());
                    debug(analyzerOutputString);
                    parser = new ScheduleOutParser(analyzerOutputString);
                    ArrayList<String> games = parser.parseGames();
                    ArrayList<String> details = parser.parseGameDetails();
                    outputPlaceholder.removeAll();
                    for(int i=0; i<games.size(); i++) {
                        debug(games.get(i));
                        outputPlaceholder.append(games.get(i) + ": \n");
                        debug("    " + details.get(i*3));
                        outputPlaceholder.append("    " + details.get(i*3) + "\n");
                        debug("    " + details.get(i*3+1));
                        outputPlaceholder.append("    " + details.get(i*3+1) + "\n");
                        debug("    " + details.get(i*3+2));
                        outputPlaceholder.append("    " + details.get(i*3+2) + "\n");
                    }
                } catch(Exception e) {
                    debug(scheduler.SchedulerConstants.RUN_FAILURE);
                }
                debug(scheduler.SchedulerConstants.RUN_SUCCESS);
            }
        });
        createTextAreaScroll(scrollPanel, scroll);
        createTextAreaScroll(scrollPanelInputs, inputScroll);
        createTextAreaScroll(scrollPanelInputs, inputScroll);
    }

    private void createTextAreaScroll(JPanel thisPanel, JScrollPane thisScrollPane) {
        thisPanel.setSize(230,230);
        thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        thisScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        thisPanel.add(thisScrollPane); 
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