package net.orpiske.spm.actions

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 10/23/13
 * Time: 8:22 AM
 * To change this templateFile use File | Settings | File Templates.
 */

public abstract class AbstractAction {
	/**
	 * Prints the help for the action and exit
	 * @param options the options object
	 * @param code the exit code
	 */
	protected void help(final Options options, int code) {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("sdm", options);
		System.exit(code);
	}

	/**
	 * Process the command line arguments
	 * @param args the command line arguments
	 */
	protected abstract void processCommand(String[] args);

	/**
	 * Runs the action
	 */
	public abstract int run();
	
}