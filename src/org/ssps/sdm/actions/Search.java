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

import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.FileSystemRepositoryFinder;
import net.orpiske.ssps.common.repository.search.RepositoryFinder;

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
public class Search extends ActionInterface {
	private static final Logger logger = Logger.getLogger(Search.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	private boolean installed;
	private String packageName;
	
	public Search(String[] args) {
		processCommand(args);
	}

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("i", "installed", false, "searches for installed packages");
		options.addOption("p", "package", true, "package name");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		installed = cmdLine.hasOption('i');
		
		packageName = cmdLine.getOptionValue('p');
		if (packageName == null) {
			help(options, -1);
		}
	}
	
	/**
	 * @throws SspsException
	 */
	private void searchRepository() throws SspsException {
		RepositoryFinder finder = new FileSystemRepositoryFinder();
		PackageInfo packageInfo = finder.findFirst(packageName);
		
		if (packageInfo == null) {
			throw new SspsException("Package not found: " + packageName);
		}

		System.out.println("------");
		System.out.println("Group ID: " + packageInfo.getGroupId());
		System.out.println("Name: " + packageInfo.getName());
		System.out.println("Version: " + packageInfo.getVersion());
		System.out.println("Type: " + packageInfo.getPackageType());
		System.out.println("File: " + packageInfo.getPath());
	}

	/**
	 * @throws DatabaseInitializationException
	 * @throws RegistryException
	 */
	private void searchRegistry() throws DatabaseInitializationException,
			RegistryException {
		RegistryManager registryManager = new RegistryManager();
		
		SoftwareInventoryDto dto = registryManager.search(packageName);
		
		if (dto == null) {
			System.out.println("The package " + packageName + " is not installed");
			
			return;
		}
		
		System.out.println("Group ID: " + dto.getGroupId());
		System.out.println("Name: " + dto.getName());
		System.out.println("Version: " + dto.getVersion());
		System.out.println("Type: " + dto.getType());
		System.out.println("Installation date: " + dto.getInstallDate());
		System.out.println("Installation directory: " + dto.getInstallDir());
	}

	@Override
	public void run() {
		try {
			if (isHelp) { 
				help(options, 1);
			}
			else {
				
				if (installed) {
					searchRegistry();
				}
				else {
					searchRepository();
				}
				
			}
		}
		catch (DatabaseInitializationException e) {
			System.err.println("Unable to access database: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to access database: " + e.getMessage(), e);
			}
		}
		catch (RegistryException e) {
			System.err.println("Unable to read registry: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to read registry: " + e.getMessage(), e);
			}
		}
		catch(SspsException e) {
			System.err.println(e.getMessage());
		}
	}



}
