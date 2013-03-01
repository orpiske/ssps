/**
   Copyright 2012 Otavio Rodolfo Piske

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
package org.ssps.sdm.actions;

import java.io.IOException;

import net.orpiske.ssps.common.repository.RepositoryManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * Implements update action
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Update extends ActionInterface {
	private static final Logger logger = Logger.getLogger(Update.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	
	public Update(String[] args) {
		processCommand(args);
	}

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		
	}

	@Override
	public void run() {
		RepositoryManager repositoryManager = new RepositoryManager();
			
		if (isHelp) { 
			help(options, 1);
		}
		else {
			repositoryManager.update();
		}
	
	}

}
