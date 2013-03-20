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
import static net.orpiske.ssps.sdm.utils.PrintUtils.printParseable;

import java.io.IOException;
import java.util.List;

import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.sdm.update.UpdateManager;

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
	
	private String groupId;
	private String packageName;
	
	private RegistryManager registryManager;
	
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
		
	}
	
	
	/**
	 * @throws RegistryException
	 * @throws SspsException
	 */
	private List<SoftwareInventoryDto> getInventoryRecord() throws RegistryException, SspsException {
		List<SoftwareInventoryDto> list = registryManager.search(packageName);
		
		
		return list;
	}

	@Override
	public void run() {
		
		
		
		try {
			if (isHelp) { 
				help(options, 1);
			}
			else {
				registryManager = new RegistryManager();
				
				List<SoftwareInventoryDto> list = getInventoryRecord();
				
				
				UpdateManager updateManager = new UpdateManager();
				
				for (SoftwareInventoryDto dto: list) { 
					PackageInfo info = updateManager.getLatest(dto);
				
					if (info != null) {
						printParseable(info);
					}
				}
			}
		} catch (DatabaseInitializationException e) {
			System.err.println("Database initialization error: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Database initialization error: " + e.getMessage(), e);
			}
		} catch (SspsException e) {
			System.err.println("Unhandled exception: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unhandled exception: " + e.getMessage(), e);
			}
		} 
	}

}
