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

import java.io.File;
import java.util.List;

import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.engine.Engine;
import net.orpiske.sdm.engine.GroovyEngine;
import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.FileSystemRepositoryFinder;
import net.orpiske.ssps.common.repository.search.RepositoryFinder;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import static net.orpiske.ssps.sdm.utils.PrintUtils.*;


/**
 * Implements the install action
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Installer extends ActionInterface {
	private static final Logger logger = Logger.getLogger(Installer.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	private boolean reinstall;
	private boolean cleanup;
	
	private String repositoryPath;
	
	private String groupId;
	private String packageName;
	private String version;
	private RegistryManager registryManager;

	/**
	 * Constructor
	 * @param args Command-line arguments array
	 */
	public Installer(final String[] args) {
		processCommand(args);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ssps.sdm.actions.ActionInterface#processCommand(java.lang.String[])
	 */
	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("g", "groupid", true, "package group id");
		options.addOption("p", "package", true, "package name");
		options.addOption("r", "repository", true, "repository path");
		options.addOption(null, "cleanup", false, "cleanup the work directory after finished");
		options.addOption(null, "reinstall", false, "reinstall already installed packages");
		options.addOption("v", "version", true, "version");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		reinstall = cmdLine.hasOption("reinstall");
		cleanup = cmdLine.hasOption("cleanup");
		
		
		repositoryPath = cmdLine.getOptionValue('r');
		if (repositoryPath == null) {
			repositoryPath = RepositoryUtils.getUserRepository();
		}
		
		packageName = cmdLine.getOptionValue('p');
		if (packageName == null) {
			help(options, -1);
		}
		
		groupId = cmdLine.getOptionValue('g');
		version = cmdLine.getOptionValue('v');
	}
	
	
	/**
	 * @param packages
	 */
	private void printRepositoryPackages(List<PackageInfo> packages) {
		System.out.println("More than one match found. Please specify either the "
				+ "version (-v) or the group (-g) name: ");
		
		printPackageList(packages);
	}


	/**
	 * @return
	 * @throws SspsException
	 */
	private List<PackageInfo> checkRepositoryColision() throws SspsException {
		RepositoryFinder finder = new FileSystemRepositoryFinder();
		List<PackageInfo> packages = finder.find(groupId, packageName, version);
		
		if (packages.size() == 0) {
			throw new SspsException("Package not found: " + packageName);
		}
		else {
			if (packages.size() > 1) {
				printRepositoryPackages(packages);
				
				throw new SspsException("Too many packages found: " + packageName);
			}
		}
		return packages;
	}


	/**
	 * @throws RegistryException
	 * @throws SspsException
	 */
	private void checkIfInstalled() throws RegistryException, SspsException {
		List<SoftwareInventoryDto> list = registryManager.search(packageName);
			
		for (SoftwareInventoryDto dto : list) {
			if (dto.getVersion().equals(version) || version == null) {
				if (dto.getGroupId().equals(groupId) || groupId == null) {
					if (!reinstall) { 
						System.err.printf("The package %s/%s-%s is already installed\n",
								(groupId == null? dto.getGroupId() : groupId), 
								packageName, 
								(version == null? dto.getVersion() : version));
						printInventoryList(list);
						
						throw new SspsException("Multiple installed packages found " 
								+ packageName);
					}
				}
			}
		}
	}

	private void install() throws SspsException {
		List<PackageInfo> packages = checkRepositoryColision();
		
		checkIfInstalled();
		
		Engine engine = new GroovyEngine();
		
		PackageInfo packageInfo = packages.get(0);
		File file = new File(packageInfo.getPath());
		
		engine.run(file);
		
		if (reinstall) {
			
			System.out.print("\rUpdating record into the registry ...");
			registryManager.reinstall(file);
			System.out.println("\rUpdating record into the registry ... done");
		}
		else {
			System.out.print("\rAdding record into the registry ...");
			registryManager.register(file);
			System.out.println("\rAdding record into the registry ... Done");
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ssps.sdm.actions.ActionInterface#run()
	 */
	@Override
	public void run() {
		try {
			if (isHelp) { 
				help(options, 1);
			}
			else {
				registryManager = new RegistryManager();
				
				install();
			}
		
			if (cleanup) {
				System.out.print("\rCleaning up workdir cache ...");
				WorkdirUtils.cleanup();
				System.out.println("\rCleaning up workdir cache ... done!");
			}
		}
		catch (EngineException e) {
			System.err.print("Unable to install: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to install: " + e.getMessage(), e);
			}
		} catch (SspsException e) {
			
			System.err.println(e.getMessage());
		}
	}

}
