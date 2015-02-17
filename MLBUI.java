import javax.swing.JFrame;
import javax.swing.JLabel;

//import statements
//Check if window closes automatically. Otherwise add suitable code
public class MLBUI extends JFrame {

	public static void main(String args[]) {
		new MLBUI();
	}
	MLBUI() {
		JLabel appTitle = new JLabel("Team Scheduling UI");
		add(appTitle);
		this.setSize(500, 500);
		// pack();
		setVisible(true);
	}
}