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
import static net.orpiske.ssps.sdm.utils.PrintUtils.printPackageList;

import java.util.List;

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
	private boolean all;
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
		options.addOption("a", "all", false, "searches for all installed packages");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		installed = cmdLine.hasOption('i');
		all = cmdLine.hasOption('a');
		
		packageName = cmdLine.getOptionValue('p');
		if (packageName == null && !all) {
			help(options, -1);
		}
	}
	
	/**
	 * @throws SspsException
	 */
	private void searchRepository() throws SspsException {
		RepositoryFinder finder = new FileSystemRepositoryFinder();
		List<PackageInfo> packages = finder.find(packageName);
		
		if (packages.size() == 0) {
			throw new SspsException("Package not found: " + packageName);
		}
		
		
		printPackageList(packages);
	}

	/**
	 * @throws DatabaseInitializationException
	 * @throws RegistryException
	 */
	private void searchRegistry() throws DatabaseInitializationException,
			RegistryException {
		RegistryManager registryManager = new RegistryManager();
		
		List<SoftwareInventoryDto> list = null; 
		
		if (all) { 
			list = registryManager.search();
		}
		else {
			list = registryManager.search(packageName);
		}
		
		if (list.size() == 0 && !all) {
			System.out.println("The package " + packageName + " is not installed");
			
			return;
		}
		
		printInventoryList(list);
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
