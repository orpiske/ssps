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
import java.util.List;

import net.orpiske.ssps.repository.Credentials;
import net.orpiske.ssps.repository.Repository;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.repository.PathUtils;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.sdm.repository.RepositoryDocument;
import org.ssps.sdm.repository.exceptions.InvalidRepository;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Fetcher extends ActionInterface {
	private static final PropertiesConfiguration config = ConfigurationWrapper
			.getConfig();
	private static final Logger logger = Logger.getLogger(Fetcher.class);

	private Sardine sardine;
	private String url;
	
	private CommandLine cmdLine;
	private Options options;
	private Repository repository;
	

	public Fetcher(final String[] args) throws XmlDocumentException, InvalidRepository {
		processCommand(args);
		
		RepositoryDocument repositoryDocument = new RepositoryDocument();
		repository = repositoryDocument.getDocument();
		
		
		String url = repository.getLocation();
		if (url == null) {
			url = config.getString("default.repository.url");
		}
		
		Credentials credentials = repository.getCredentials();
		
		String username; 
		String password;
		
		if (credentials != null) { 
			username = credentials.getUsername();
			password = credentials.getPassword();
		}
		else {
			username = config.getString("default.repository.username");
			password = config.getString("default.repository.password");
		}

		this.url = url;
		sardine = SardineFactory.begin(username, password);
	}
	
	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#help(org.apache.commons.cli.Options, int)
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
	protected void processCommand(final String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("v", "version", true, "deliverable version");
		options.addOption("d", "destination", true, "destination directory (workdir)");

		try { 
			cmdLine = parser.parse(options, args);
		}
		catch (ParseException e) {
			help(-1);
		}
	}

	private void fetch() throws IOException {

		String group = repository.getGroup();
		String name = repository.getName();
		
		String version = cmdLine.getOptionValue('v');
		if (version == null) {
			System.err.println("You must inform the deliverable version");
			help(-1);
		}
		
		
		String destination = cmdLine.getOptionValue('d');
		
		String path = (new PathUtils(url)).getPath(group, name, version);

		logger.info("Checking remote repository for deliverables " + path);
		List<DavResource> resources = sardine.list(path);

		if (destination == null) {
			destination = config.getString("temp.work.dir",
					FileUtils.getTempDirectoryPath());

			logger.info("No destination directory provided. Using: "
					+ destination);
		}

		for (DavResource resource : resources) {

			if (!resource.isDirectory()) {
				File newFile = new File(destination + File.separator
						+ resource.getName());

				if (newFile.exists()) {
					continue;
				} else {
					newFile.getParentFile().mkdirs();
					newFile.createNewFile();
				}

				InputStream input = sardine.get(resource.getHref().toString());
				FileOutputStream output = new FileOutputStream(newFile);

				IOUtils.copy(input, output);
			}
		}
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
				fetch();
			}
		} catch (IOException e) {
			System.err.println("Unable to fetch resource: " + e.getMessage());
			
			if (logger.isDebugEnabled()) { 
				logger.error("Unable to fetch resource: " + e.getMessage(), e);
			}
		}
		
	}

	

}
