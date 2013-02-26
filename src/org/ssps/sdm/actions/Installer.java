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

import net.orpiske.sdm.common.GroupIdUtils;
import net.orpiske.sdm.common.RepositoryUtils;
import net.orpiske.sdm.common.VersionUtils;
import net.orpiske.sdm.engine.Engine;
import net.orpiske.sdm.engine.GroovyEngine;
import net.orpiske.sdm.engine.exceptions.EngineException;

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
		options.addOption("v", "version", true, "version");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		
		repositoryPath = cmdLine.getOptionValue('r');
		if (repositoryPath == null) {
			repositoryPath = RepositoryUtils.getUserRepository();
		}
		
		packageName = cmdLine.getOptionValue('p');
		if (packageName == null) {
			help(options, -1);
		}
		
		groupId = cmdLine.getOptionValue('g');
		if (groupId == null) {
			groupId = GroupIdUtils.resolveGroupId(null);
		}
		
		version = cmdLine.getOptionValue('v');
		if (version == null) {
			version = VersionUtils.resolveLatest(null, null);
		}
		
	}
	
	

	private void install() throws EngineException {
		String packageFQPN = RepositoryUtils.getFQPN(groupId, packageName, 
				version);
		
		String dir = RepositoryUtils.getPackageDir(repositoryPath, packageFQPN);
		String packageFile = RepositoryUtils.getPackageFilePath(dir, packageName);
		
		Engine engine = new GroovyEngine();
		
		engine.run(packageFile);
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
		}
		
		/*
		catch (XmlDocumentException e) {
			System.err.println("Unable to install: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to install: " + e.getMessage(), e);
			}
		} catch (AdmException e) {
			System.err.println(e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error(e.getMessage(), e);
			}
		} catch (ResourceExchangeException e) {
			System.err.println("Unable to install: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to install: " + e.getMessage(), e);
			}
		} catch (URISyntaxException e) {
			System.err.println("Invalid ADM resource: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Invalid ADM resource: " + e.getMessage(), e);
			}
		} 
		*/
	}

}
