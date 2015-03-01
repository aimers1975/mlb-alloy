package scheduler;

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



//import statements
//Check if window closes automatically. Otherwise add suitable code
public class MLBUI extends JFrame {

    MlbAppControl analyzer;
    Boolean debug = true;
    JLabel appInfo = new JLabel(scheduler.SchedulerConstants.APP_LABEL);
    //JButton quitButton = new JButton("Quit");
    JButton saveModelButton = new JButton("Save Model");
    JButton evaluateButton = new JButton("Evaluate");
    JPanel scrollPanel = new JPanel();
    JTextArea inputALS = new JTextArea("Type Alloy Code Here.",200,200);
    JScrollPane scroll = new JScrollPane(inputALS);
    JMenuBar menubar = new JMenuBar();
    ImageIcon icon = new ImageIcon("alert.jpg");
    JMenu file = new JMenu("File");
    JMenuItem eMenuItem = new JMenuItem("Exit", icon);

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
		setTitle("Team Scheduler");
        setSize(800, 500);
        setLocationRelativeTo(null);
        createMenuBar();      
        createLayout(appInfo, scroll, saveModelButton, evaluateButton);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

    private void saveModelToFile() {

        String[] lines = inputALS.getText().split("\\n");
        try {
            PrintWriter out = new PrintWriter("scheduler\\model.als");
            for(String line : lines) {
                debug(line);
                out.println(line);
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Failed to create model file.");
        }

    }

    private String[] getAnalyzerInput() {
        String[] analyzerInputs = new String[1];
        Path path = Paths.get("scheduler\\model.als");
        Path absolutePath = path.toAbsolutePath();
        analyzerInputs[0] = absolutePath.toString();
        return analyzerInputs;
    }

    private void createMenuBar() {

        file.setMnemonic(KeyEvent.VK_F);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
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
        saveModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                saveModelToFile();
                debug("Model saved!");
            }
        });
        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    analyzer.runAnalysis(getAnalyzerInput());
                } catch(Exception e) {
                    debug("Failure to run analyzer");
                }
                debug("Evaluation started!");
            }
        });
        createTextAreaScroll();
    }

    private void createTextAreaScroll() {
        scrollPanel.setSize(230,230);
        scrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.add(scroll); 
    }

	private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
                .addComponent(arg[2])
                .addComponent(arg[3])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
                .addComponent(arg[2])
                .addComponent(arg[3])
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