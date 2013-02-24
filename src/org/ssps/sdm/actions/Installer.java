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

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import net.orpiske.sdm.adm.AdmProcessor;
import net.orpiske.sdm.adm.exceptions.AdmException;
import net.orpiske.sdm.adm.util.ResourceExchangeFactory;
import net.orpiske.ssps.adm.Adm;
import net.orpiske.spm.common.adm.AdmDocument;
import net.orpiske.ssps.common.resource.Resource;
import net.orpiske.ssps.common.resource.ResourceExchange;
import net.orpiske.ssps.common.resource.ResourceInfo;
import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;
import net.orpiske.ssps.common.xml.exceptions.XmlDocumentException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
	
	private boolean isRemote;
	private boolean isHelp;
	private String repositoryPath;

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
		options.addOption("a", "adm-file", true, "adm file");
		options.addOption(null, "remote", false, 
				"instructs the installer to download the ADM file from a remote location");
		options.addOption(null, "install-only", false, 
				"does not fetch the file");
		options.addOption("r", "repository", true, 
				"the location where to install the contents of the adm file");

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isRemote = cmdLine.hasOption("remote");
		isHelp = cmdLine.hasOption("help");
		
		repositoryPath = cmdLine.getOptionValue('r');
		
		if (repositoryPath == null) {
			repositoryPath = FilenameUtils.getFullPath("./");
		}
	}
	
	public void install(String path) throws XmlDocumentException, AdmException {
		AdmDocument admDocument;
	
		
		admDocument = new AdmDocument(path);

		Adm adm = admDocument.getDocument();

		AdmProcessor processor = new AdmProcessor(adm, repositoryPath);
		

		processor.process();
	}
	
	public void install(InputStream stream) throws XmlDocumentException, AdmException, URISyntaxException, ResourceExchangeException {
		AdmDocument admDocument;
	
		admDocument = new AdmDocument(stream);
	

		Adm adm = admDocument.getDocument();

		AdmProcessor processor = new AdmProcessor(adm, repositoryPath);
		

		processor.process();
		
	}

	private void install() throws XmlDocumentException, AdmException, ResourceExchangeException, URISyntaxException {
		String path = cmdLine.getOptionValue('a');
		
		if (path == null) {
			System.err.println("Missing adm file information");
			help(options, -1);
		}
		
		
		if (isRemote) {
			ResourceExchange resourceExchange = 
					ResourceExchangeFactory.newResourceExchange();
			
			URI uri = new URI(path);
			
			ResourceInfo resourceInfo = resourceExchange.info(uri);
			logger.info("Downloading " + resourceInfo.getSize() 
					+ " from the server");
			
			Resource<InputStream> resource = resourceExchange.get(uri);
			install(resource.getPayload());
			
			IOUtils.closeQuietly(resource.getPayload());
			
			resourceExchange.release();
		}
		else { 
			install(path);
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
		} catch (XmlDocumentException e) {
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
	}

}
