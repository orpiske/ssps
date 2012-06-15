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
import java.io.IOException;

import net.orpiske.ssps.repository.Repository;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.sdm.adm.exceptions.AdmException;
import org.ssps.sdm.repository.RepositoryDocument;
import org.ssps.sdm.repository.exceptions.InvalidRepository;
import org.ssps.sdm.utils.WorkdirUtils;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Deployer extends ActionInterface {
	
	private static final Logger logger = Logger.getLogger(Deployer.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	private Repository repository;
	
	private String version;
	
	public Deployer(final String[] args) throws XmlDocumentException, InvalidRepository {
		processCommand(args);
		
		RepositoryDocument repositoryDocument = new RepositoryDocument();
		repository = repositoryDocument.getDocument();
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#help(int)
	 */
	@Override
	protected void help(int code) {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("sdm", options);
		System.exit(code);
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#processCommand(java.lang.String[])
	 */
	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("v", "version", true, "version to deploy");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(-1);
		}

	}
	
	private String getPackageWorkdir() {
		String name = repository.getName();
		
		return WorkdirUtils.getWorkDir() + File.separator + name + 
				File.separator + version;
	}
	
	
	private void fetch() throws XmlDocumentException, InvalidRepository, IOException {
		Fetcher fetcher = new Fetcher();
		
		final String destination = WorkdirUtils.getWorkDir();
		
		fetcher.fetch(version, destination);
	}
	
	private void unpack() throws SspsArchiveException {
		Unpacker unpacker = new Unpacker();
		
		String name = repository.getName();
		
		String source = WorkdirUtils.getWorkDir() + File.separator + name 
				+ "-" + version + ".ugz";
		
		final String destination = getPackageWorkdir();
		
		unpacker.unpack(source, destination);
	}
	
	private void install() throws XmlDocumentException, AdmException, InvalidRepository {
		String admFilePath = getPackageWorkdir() + File.separator 
				+ "installroot" + File.separator + "adm.xml";
		Installer installer = new Installer();
		
		installer.install(admFilePath);
	}
	

	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#run()
	 */
	@Override
	public void run() {
		try {
			if (cmdLine.hasOption('h')) { 
				help(1);
			}
			else {
				version = cmdLine.getOptionValue('v');
				if (version == null) {
					System.err.println("You must inform the version to deploy");
					
					help(-1);
				}
				
				fetch();
				unpack();
				install();
			}
		} catch (XmlDocumentException e) {
			System.err.println("Unable to install: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to install: " + e.getMessage(), e);
			}
		} catch (AdmException e) {
			System.err.println("Invalid package: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Invalid package: " + e.getMessage(), e);
			}
		} catch (InvalidRepository e) {
			System.err.println("The repository is not correctly setup. Did you run init?");

			if (logger.isDebugEnabled()) {
				logger.error("Invalid repository: " + e.getMessage(), e);
			}
		} catch (IOException e) {
			System.err.println("Input/ouput error: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Input/ouput error: " + e.getMessage(), e);
			}
		} catch (SspsArchiveException e) {
			System.err.println("Unable to process SSPS archive: " 
					+ e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to process SSPS archive: " + e.getMessage(),
						e);
			}
		} 

	}

}
