import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.EventQueue;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;


//import statements
//Check if window closes automatically. Otherwise add suitable code
public class MLBUI extends JFrame {

	public MLBUI() {
		initUI();
	}

	private void initUI() {
		setTitle("Team Scheduler");
        setSize(500, 500);
        setLocationRelativeTo(null);
        JLabel appInfo = new JLabel("Set Team Scheduling Parameters");
        JButton quitButton = new JButton("Quit");
        JButton evaluateButton = new JButton("Evaluate");
        JTextField inputALS = new JTextField("Input Alloy Code");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("Evaluation started!");
            }
        });
        createLayout(appInfo, inputALS, evaluateButton, quitButton);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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