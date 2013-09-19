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

import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.sdm.managers.InstallationManager;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;
import net.orpiske.ssps.sdm.managers.exceptions.TooManyPackages;

import org.apache.commons.cli.*;
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
	private boolean cleanup;
	private boolean nodeps;
	private boolean view;
	
	private String groupId;
	private String packageName;
	private String version;
	private String[] phases;
	
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
		options.addOption(null, "cleanup", false, "cleanup the work directory after finished");
		options.addOption(null, "reinstall", false, "reinstall already installed packages");
		options.addOption(null, "view", false, "only view the packages to be installed");

		Option phasesOptions = OptionBuilder.withLongOpt("phases").create();
		phasesOptions.setArgs(6);
		phasesOptions.setRequired(false);
		phasesOptions.setDescription("the install phase(s) to run (implies --nodeps)");
				
		options.addOption(phasesOptions);
		
		
		options.addOption(null, "nodeps", false, "does not install dependencies");
		options.addOption("v", "version", true, "version");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		reinstall = cmdLine.hasOption("reinstall");
		cleanup = cmdLine.hasOption("cleanup");
		view = cmdLine.hasOption("view");
		
		packageName = cmdLine.getOptionValue('p');
		if (packageName == null) {
			help(options, -1);
		}
		
		groupId = cmdLine.getOptionValue('g');
		version = cmdLine.getOptionValue('v');
		phases = cmdLine.getOptionValues("phases");
		
		if (phases != null && phases.length > 0) {
			nodeps = true;
		}
		else { 
			nodeps = cmdLine.hasOption("nodeps");
		}
	}
	

	private void printRepositoryPackages(List<PackageInfo> packages) {
		System.out.println("More than one match found. Please specify either the "
				+ "version (-v) or the group (-g) name: ");
		
		printPackageList(packages);
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
				InstallationManager manager = new InstallationManager(phases, nodeps);
				
				
				if (view) {
					manager.view(groupId, packageName, version);
				}
				else { 
					manager.install(groupId, packageName, version, reinstall);
				}
			}
		
			if (cleanup) {
				System.out.print("\rCleaning up workdir search ...");
				WorkdirUtils.cleanup();
				System.out.println("\rCleaning up workdir search ... done!");
			}
		}
		
		catch (EngineException e) {
			System.err.print("Unable to install: " + e.getMessage() +  '\n');

			if (logger.isDebugEnabled()) {
				logger.error("Unable to install: " + e.getMessage(), e);
			}
		} catch (PackageNotFound e) {
			System.err.print(e.getMessage() + '\n');

			if (logger.isDebugEnabled()) {
				logger.error(e.getMessage(), e);
			}
		} catch (TooManyPackages e) {
			printRepositoryPackages(e.getPackages());
			
			if (logger.isDebugEnabled()) {
				logger.error(e.getMessage(), e);
			}
		} catch (RegistryException e) {
			System.err.print(e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error(e.getMessage(), e);
			}
		} catch (MultipleInstalledPackages e) {
		
			System.err.printf("The package %s%s%s is already installed%n",
					(groupId == null? "" : groupId + "/"), 
					packageName, 
					(version == null? "" : "-" + version));
			printInventoryList(e.getSoftwareList());
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

}
