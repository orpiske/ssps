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

import java.sql.SQLException;
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
	private static final Logger logger = Logger.getLogger(Update.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	private boolean rebuildCacheOnly;
	private String[] repositories;
	
	public Update(String[] args) {
		processCommand(args);
	}

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption(null, "rebuild-cache-only", false, 
				"only rebuilds the cache without updating from the remote repository");

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
		rebuildCacheOnly = cmdLine.hasOption("rebuild-cache-only");


		repositories = cmdLine.getOptionValues("repositories");
	}
	

	@Override
	public int run() {
		
			
		if (isHelp) { 
			help(options, 1);
			
			return 0;
		}
		else {
			try {
				UpdateManager updateManager = new UpdateManager();

				if (rebuildCacheOnly) {
					updateManager.rebuildCache(repositories);	
				}
				else {
					updateManager.update(repositories);
				}
				
				List<Upgradeable> up = updateManager.getAllNewerPackages();
				
				if (up.size() > 0) { 
					PrintUtils.printUpgradeable(up);
				}
				else {
					System.out.println("There are no packages to upgrade");
				}
				
				return 0;
			} catch (RegistryException e) {
				System.err.print(e.getMessage());

				if (logger.isDebugEnabled()) {
					logger.error(e.getMessage(), e);
				}

				return 5;
			} catch (DatabaseInitializationException e) {
				System.err.println("Unable to update: " + e.getMessage());

				if (logger.isDebugEnabled()) {
					logger.error("Unable to update: " + e.getMessage(), e);
				}

				return 1;
			}
			catch (SQLException e) {
				System.err.println(e.getMessage());

				if (logger.isDebugEnabled()) {
					logger.error("SQL Exception: " + e.getMessage(), e);
				}

				return 3;
			}
			
		}
	
	}

}
