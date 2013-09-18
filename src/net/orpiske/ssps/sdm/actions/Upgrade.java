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

import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.sdm.managers.UpdateManager;
import net.orpiske.ssps.sdm.managers.UpgradeManager;
import net.orpiske.ssps.sdm.update.Upgradeable;
import net.orpiske.ssps.sdm.utils.PrintUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Upgrade extends ActionInterface {
	private static final Logger logger = Logger.getLogger(Upgrade.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	private boolean view;
	private boolean all;
	
	private String groupId;
	private String packageName;
	
	
	public Upgrade(String[] args) {
		processCommand(args);
	}

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("g", "groupid", true, "package group id");
		options.addOption("p", "package", true, "package name");
		options.addOption(null, "view", false, "do nothing: just view the packages to be" 
				+ " upgraded");
		options.addOption("a", "all", false, "upgrade all available packages");
	

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		view = cmdLine.hasOption("view");
		all = cmdLine.hasOption("all");
		
		if (!view && !all) {
		
			packageName = cmdLine.getOptionValue('p');
			if (packageName == null) {
				help(options, -1);
			}
			
			groupId = cmdLine.getOptionValue('g');
		}
		
	}
		

	@Override
	public void run() {
		
		try {
			if (isHelp) { 
				help(options, 1);
			}
			else {
				UpdateManager updateManager = new UpdateManager();
				
				if (view) {
					List<Upgradeable> up = updateManager.getAllNewerPackages();
					PrintUtils.printUpgradeable(up);
					
				}
				else {

					UpgradeManager manager = new UpgradeManager();
					
					if (all) {
						manager.upgrade();
					}
					else {
						manager.upgrade(groupId, packageName, null);
					}
				}
			}
		} catch (DatabaseInitializationException e) {
			System.err.println("Database initialization error: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Database initialization error: " + e.getMessage(), e);
			}
		} catch (SQLException e) {
			System.err.println("Database exception: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Database exception: " + e.getMessage(), e);
			}
		} 
		catch (SspsException e) {
			System.err.println("Unhandled exception: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unhandled exception: " + e.getMessage(), e);
			}
		} 
	}

}
