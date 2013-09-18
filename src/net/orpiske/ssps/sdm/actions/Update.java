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
package net.orpiske.ssps.sdm.actions;

import java.util.List;

import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.repository.RepositoryManager;
import net.orpiske.ssps.sdm.managers.UpdateManager;
import net.orpiske.ssps.sdm.update.Upgradeable;
import net.orpiske.ssps.sdm.utils.PrintUtils;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

/**
 * Implements update action
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Update extends ActionInterface {
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	private String[] repositories;
	
	public Update(String[] args) {
		processCommand(args);
	}

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");

		Option reposOptions = OptionBuilder.withLongOpt("repositories").create();
		reposOptions.setArgs(255);
		reposOptions.setRequired(false);
		reposOptions.setDescription("the repository(ies) to update");
		options.addOption(reposOptions);
		
		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");


		repositories = cmdLine.getOptionValues("repositories");
	}
	

	@Override
	public void run() {
		RepositoryManager repositoryManager = new RepositoryManager();
			
		if (isHelp) { 
			help(options, 1);
		}
		else {
			
			if (repositories == null) {
				repositoryManager.update();
			}
			else {
				repositoryManager.update(repositories);
			}
			
			try {
				UpdateManager updateManager = new UpdateManager();
				
				
				List<Upgradeable> up = updateManager.getAllNewerPackages();
				
				PrintUtils.printUpgradeable(up);
			} catch (RegistryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DatabaseInitializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}

}
