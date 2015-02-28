package scheduler;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
import java.awt.EventQueue;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import scheduler.MlbAppControl;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;


//import statements
//Check if window closes automatically. Otherwise add suitable code
public class MLBUI extends JFrame {

    MlbAppControl analyzer;
    Boolean debug = true;
    JLabel appInfo = new JLabel("Set Team Scheduling Parameters");
    JButton quitButton = new JButton("Quit");
    JButton saveModelButton = new JButton("Save Model");
    JButton evaluateButton = new JButton("Evaluate");
    JPanel scrollPanel = new JPanel();
    JTextArea inputALS = new JTextArea("Type Alloy Code Here.",200,200);
    JScrollPane scroll = new JScrollPane(inputALS);

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
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
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

        scrollPanel.setSize(230,230);
        scrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //scrollPanel.setBorder(new TitledBorder(new EtchedBorder(), "Display Area"));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.add(scroll);
        
        createLayout(appInfo, scroll, saveModelButton, evaluateButton, quitButton);
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
                .addComponent(arg[4])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addComponent(arg[1])
                .addComponent(arg[2])
                .addComponent(arg[3])
                .addComponent(arg[4])
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