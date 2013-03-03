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

import java.io.File;

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
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;


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
	
	private String repositoryPath;
	
	private String groupId;
	private String packageName;
	private String version;

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
		options.addOption(null, "reinstall", false, "reinstall already installed packages");
		options.addOption("v", "version", true, "version");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		reinstall = cmdLine.hasOption("reinstall");
		
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
	 * @throws DatabaseInitializationException
	 * @throws RegistryException
	 */
	private SoftwareInventoryDto searchRegistry() throws DatabaseInitializationException,
			RegistryException {
		RegistryManager registryManager = new RegistryManager();
		
		SoftwareInventoryDto dto = registryManager.search(packageName);
		
		return dto;
		
	
	}
	
	

	private void install() throws SspsException {
		RepositoryFinder finder = new FileSystemRepositoryFinder();
		PackageInfo packageInfo = finder.findFirst(packageName);
		
		if (packageInfo == null) {
			throw new SspsException("Package not found: " + packageName);
		}
		
		SoftwareInventoryDto dto = searchRegistry(); 
		if (dto != null && !reinstall) {
			System.err.println("The package " + packageName + " is already installed. " +
					"Use --reinstall or remove it before trying again!");
			
			return;
		}
		
		
		Engine engine = new GroovyEngine();
		RegistryManager registryManager = new RegistryManager();
		
		File file = new File(packageInfo.getPath());
		
		engine.run(file);
		
		if (reinstall) {
			registryManager.reinstall(file);
		}
		else { 
			registryManager.register(file);
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
				install();
			}
		
		}
		catch (EngineException e) {
			System.err.println("Unable to install: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to install: " + e.getMessage(), e);
			}
		} catch (SspsException e) {
			System.err.println(e.getMessage());
		}
	}

}
