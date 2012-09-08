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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import net.orpiske.ssps.repository.Repository;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.repository.AdmPathUtils;
import org.ssps.common.resource.Resource;
import org.ssps.common.resource.ResourceExchange;
import org.ssps.common.resource.ResourceInfo;
import org.ssps.common.resource.exceptions.ResourceExchangeException;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.sdm.adm.util.ResourceExchangeFactory;
import org.ssps.sdm.repository.RepositoryDocument;
import org.ssps.sdm.repository.exceptions.InvalidRepository;
import org.ssps.sdm.utils.WorkdirUtils;


/**
 * Implements the fetch action
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Fetcher extends ActionInterface {
	private static final PropertiesConfiguration config = ConfigurationWrapper
			.getConfig();
	private static final Logger logger = Logger.getLogger(Fetcher.class);

	private String url;
	
	private CommandLine cmdLine;
	private Options options;
	private Repository repository;
	private boolean isHelp;
	private boolean isOverwrite;
	
	public Fetcher() throws XmlDocumentException, InvalidRepository {
		init();
	}
	
	public Fetcher(final String[] args) throws XmlDocumentException, InvalidRepository {
		processCommand(args);
		
		if (!isHelp) { 
			init();
		}
	}


	/**
	 * @throws XmlDocumentException
	 * @throws InvalidRepository
	 */
	private void init() throws XmlDocumentException, InvalidRepository {
		RepositoryDocument repositoryDocument = new RepositoryDocument();
		repository = repositoryDocument.getDocument();
		
		String url = repository.getLocation();
		if (url == null) {
			url = config.getString("default.repository.url");
		}

		this.url = url;
	}
	
	
	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#processCommand(java.lang.String[])
	 */
	@Override
	protected void processCommand(final String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("v", "version", true, "deliverable version");
		options.addOption("d", "destination", true, "destination directory (workdir)");
		options.addOption("o", "overwrite", false, "overwrite existing files");

		try { 
			cmdLine = parser.parse(options, args);
		}
		catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		isOverwrite = cmdLine.hasOption('o');
	}
	
	
	/**
	 * @param version
	 * @return
	 */
	private String buildDestinationPath(final String version) {
		String tmpPath;
		String group = repository.getGroup();
		String name = repository.getName();
		
		tmpPath = WorkdirUtils.getPackageWorkDir(group, name, version);
				

		logger.info("No destination directory provided. Using: "
				+ tmpPath);
		
		return tmpPath;
	}

	public void fetch(final String version, String destination) throws IOException, ResourceExchangeException {

		String group = repository.getGroup();
		String name = repository.getName();
		
		String path = (new AdmPathUtils(url)).getPath(group, name, version);

		logger.info("Checking repository for deliverables: " + path);

		if (destination == null) {
			destination = buildDestinationPath(version);
		}

		File newFile = new File(destination + File.separator 
				+ AdmPathUtils.getName(name, version));
		
		if (!newFile.exists() || (newFile.exists() && isOverwrite)) {
			newFile.getParentFile().mkdirs();
			newFile.createNewFile();
		}
		else {
			logger.info("The file already exists. Ignoring");
			return;
		}
		
		
		URI uri;
		try {
			uri = new URI(path);
		} catch (URISyntaxException e) {
			throw new ResourceExchangeException("Invalid URL: " + path);
		}
		
		ResourceExchange resourceExchange = 
				ResourceExchangeFactory.newResourceExchange();
		
		ResourceInfo resourceInfo = resourceExchange.info(uri);
		logger.info("Downloading " + resourceInfo.getSize() 
				+ " from the server");
		
		Resource<InputStream> resource = resourceExchange.get(uri);
		
	
		InputStream input = resource.getPayload();
		
		FileOutputStream output = new FileOutputStream(newFile);

		IOUtils.copy(input, output);
		
		IOUtils.closeQuietly(resource.getPayload());
		IOUtils.closeQuietly(output);
	}


	
	private void fetch() throws IOException, ResourceExchangeException {
		String version = cmdLine.getOptionValue('v');
		if (version == null) {
			System.err.println("You must inform the deliverable version");
			help(options, -1);
		}
		
		String destination = cmdLine.getOptionValue('d');
		
		fetch(version, destination);
	}


	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#run()
	 */
	@Override
	public void run() {
		
		try {
			if (cmdLine.hasOption('h')) { 
				help(options, 1);
			}
			else {
				fetch();
			}
		} catch (IOException e) {
			System.err.println("Unable to fetch resource: " + e.getMessage());
			
			if (logger.isDebugEnabled()) { 
				logger.error("Unable to fetch resource: " + e.getMessage(), e);
			}
		} catch (ResourceExchangeException e) {
			System.err.println("Unable to fetch resource: " + e.getMessage());
			
			if (logger.isDebugEnabled()) { 
				logger.error("Unable to fetch resource: " + e.getMessage(), e);
			}
		}
		
	}

	

}
