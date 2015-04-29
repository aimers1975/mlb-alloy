package scheduler;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;
import java.util.Calendar;
import java.util.ArrayList;

public class MlbAppControl {
	
    VizGUI viz = null;
    A4Reporter rep;
    Boolean debug = true;

	public MlbAppControl() {
	  rep = new A4Reporter() {
	    // For example, here we choose to display each "warning" by printing it to System.out
	    @Override public void warning(ErrorWarning msg) {
	        debug("Relevance Warning:\n"+(msg.toString().trim())+"\n\n");
	        System.out.flush();
	    }};
	}

	public void debug(String msg) {
		if(debug) {
			System.out.println(this.getClass().getSimpleName() + ": " + msg);
		}
	}

	public ArrayList<String> runAnalysis(String[] inputfiles) throws Err {
            ArrayList<String> returnAns = new ArrayList<String>();
	        for(String filename:inputfiles) {
            // Parse+typecheck the model
            debug("=========== Parsing+Typechecking "+filename+" =============");
            debug("Creating module.");
            Module world = CompUtil.parseEverything_fromFile(rep, null, filename);
            debug("Creating options");
            // Choose some default options for how you want to execute the commands
            A4Options options = new A4Options();
            debug("Creating solver");
            options.solver = A4Options.SatSolver.SAT4J;
            debug("looping through commands");
            for (Command command: world.getAllCommands()) {
                // Execute the command
                debug("============ Command "+command+": ============");
                long millisStart = Calendar.getInstance().getTimeInMillis();
                A4Solution ans = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), command, options);
                long millisEnd = Calendar.getInstance().getTimeInMillis();
                long resultTime = millisEnd - millisStart;
                debug("The time to solve was: " + resultTime + " milliseconds.");
                A4Solution ans2 = ans;
                returnAns.add("Solve time:: " + resultTime + " :: milliseconds\n" + ans2.toString());
                int countAnswers = 0;
                while(ans2.next() != null && countAnswers < 20) {
                    countAnswers++;
                    ans2 = ans2.next();
                    returnAns.add("Solve time:: " + resultTime + " :: milliseconds\n" + ans2.toString());
                }
                debug("The answer count was: " + countAnswers);
                //A4Solution ans = ans.next();
                //long newTime = 0;
                //.resultSAT(command,newTime,ans);
                //debug("Checking the time: " + newTime);
                // Print the outcome 
                //System.out.println(returnAns);
                // If satisfiable...
                if (ans.satisfiable()) {
                    // You can query "ans" to find out the values of each set or type.
                    // This can be useful for debugging.
                    //
                    // You can also write the outcome to an XML file
                    //ans.writeXML("alloy_example_output.xml");
                    //
                    // You can then visualize the XML file by calling this:
                    //if (viz==null) {
                    //    viz = new VizGUI(false, "alloy_example_output.xml", null);
                    //} else {
                    //    viz.loadXML("alloy_example_output.xml", true);
                    //}
                }
            }
        }
        //System.out.println(returnAns);
        return returnAns;

	}
}