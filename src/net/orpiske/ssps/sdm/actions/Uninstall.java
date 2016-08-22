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

import static net.orpiske.ssps.sdm.utils.PrintUtils.printInventoryList;
import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.sdm.managers.UninstallManager;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;

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
public class Uninstall extends ActionInterface {
	private static final Logger logger = Logger.getLogger(Uninstall.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	
	private String groupId;
	private String packageName;
	private String version;
	
	public Uninstall(String[] args) {
		processCommand(args);
	}

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("g", "groupid", true, "package group id");
		options.addOption("p", "package", true, "package name");
		options.addOption("v", "version", true, "version");
	

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		
		packageName = cmdLine.getOptionValue('p');
		if (packageName == null) {
			help(options, -1);
		}
		
		groupId = cmdLine.getOptionValue('g');
		version = cmdLine.getOptionValue('v');
	}
	
	
	@Override
	public void run() {
		try {
			if (isHelp) { 
				help(options, 1);
			}
			else {				
				UninstallManager manager = new UninstallManager();
				
				manager.uninstall(groupId, packageName, version);
			}
		} catch (DatabaseInitializationException e) {
			System.err.println("Unable to uninstall: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to uninstall: " + e.getMessage(), e);
			}
		} catch (RegistryException e) {
			System.err.println("Unable to unregister: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to unregister: " + e.getMessage(), e);
			}
		} catch (EngineException e) {
			System.err.println("Unable to run script cleanup: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to run script cleanup: " + e.getMessage(), e);
			}
		} 
		catch (MultipleInstalledPackages e) {
			System.err.printf("The package %s/%s-%s is already installed%n",
					(groupId == null? "{null}" : groupId), 
					packageName, 
					(version == null? "{version}" : version));
			printInventoryList(e.getSoftwareList());
		} catch (PackageNotFound e) {
			System.err.println(e.getMessage());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		} 
	}

}
