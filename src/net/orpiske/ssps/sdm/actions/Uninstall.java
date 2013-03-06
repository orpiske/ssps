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

import java.io.File;
import java.util.List;

import net.orpiske.sdm.engine.Engine;
import net.orpiske.sdm.engine.GroovyEngine;
import net.orpiske.sdm.engine.exceptions.EngineException;
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
import org.apache.commons.io.FileUtils;
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
	
	private RegistryManager registryManager;
	
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
	
	private void runUninstallScript() throws EngineException {
		RepositoryFinder finder = new FileSystemRepositoryFinder();
		PackageInfo packageInfo = finder.findFirst(packageName);
		
		if (packageInfo == null) {
			System.err.println("Package not found in the repository. Skipping script " +
					"cleanup routines");
			
			return;
		}
		
		Engine engine = new GroovyEngine();
		File file = new File(packageInfo.getPath());
		engine.runUninstall(file);
	}
	
	
	/**
	 * @throws RegistryException
	 * @throws SspsException
	 */
	private SoftwareInventoryDto checkIfInstalled() throws RegistryException, SspsException {
		List<SoftwareInventoryDto> list = registryManager.search(packageName);
		
		if (list.size() == 1) {
			return list.get(0);
		}
		
	
		if (list.size() > 1) {
			if (version == null || groupId == null) {				
				printInventoryList(list);
				
				throw new SspsException("Multiple installed packages found on the " 
						+ "database (use -g and -v)");
			}
		}

		for (SoftwareInventoryDto dto : list) {
			if (dto.getVersion().equals(version) || version == null) {
				if (dto.getGroupId().equals(groupId) || groupId == null) {
					return dto;
				}
			}
		}
		
		return null;
	}
	
	private void uninstall() throws SspsException {
		SoftwareInventoryDto dto = checkIfInstalled();
				
		
		if (dto == null) {
			System.err.println("The package " + packageName + " is not installed!");
			
			return;
		}
		
		File file = new File(dto.getInstallDir());
		
		if (!file.exists()) {
			System.err.println("The package " + packageName + " is marked as installed "
					+ "but the installation dir could not be found");
		}
		else {
			runUninstallScript();
			
			System.out.print("\rRemoving the package files from " + dto.getInstallDir() 
					+ "...");
			FileUtils.deleteQuietly(file);
			System.out.println("\rRemoving the package files from " + dto.getInstallDir() 
					+ "... Done");
		}
		
		System.out.print("\nRemoving package from the registry ...");
		registryManager.delete(dto);
		System.out.println("\nRemoving package from the registry ... done");
	}
	
	
	@Override
	public void run() {
		try {
			if (isHelp) { 
				help(options, 1);
			}
			else {
				registryManager = new RegistryManager();	
				
				uninstall();
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
		} catch (SspsException e) {
			System.err.println(e.getMessage());
		} 
	}

}
