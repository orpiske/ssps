/**
 Copyright 2013 Otavio Rodolfo Piske

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package net.orpiske.spm.actions

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options


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