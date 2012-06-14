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

import net.orpiske.ssps.repository.Credentials;
import net.orpiske.ssps.repository.Repository;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.sdm.repository.RepositoryDocument;

/**
 * Initializes a repository
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Initializer extends ActionInterface {
	
	private CommandLine cmdLine;
	private Options options;
	
	
	public Initializer(String[] args) {
		processCommand(args);
	}
	

	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#help(int)
	 */
	@Override
	protected void help(int code) {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("sdm-init", options);
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
	
		options.addOption("g", "group", true, "deliverable group");
		options.addOption("n", "name", true, "deliverable name");
		options.addOption("l", "location", true, "repository location/URL");
		
		options.addOption("u", "user", true, "repository username");
		options.addOption("p", "password", true, "repository password");
		
		try { 
			cmdLine = parser.parse(options, args);
		}
		catch (ParseException e) {
			help(-1);
		}
	}
	
	
	private String getOpt(char value) {
		String ret = cmdLine.getOptionValue(value);
		
		if (ret == null) {
			help(-1);
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#run()
	 */
	@Override
	public void run() {
		String group = getOpt('g');
		String name = getOpt('n');
		String location = getOpt('l');
		
		Repository repository = new Repository();
		
		repository.setGroup(group);
		repository.setName(name);
		repository.setLocation(location);
		
		Credentials credentials = null;
		String user = cmdLine.getOptionValue('u');
		
		if (user != null) {
			credentials = new Credentials();
			
			String password = cmdLine.getOptionValue('p');
			credentials.setUsername(user);
			credentials.setPassword(password);
		}
		
		repository.setCredentials(credentials);
		
		try {
			@SuppressWarnings("unused")
			RepositoryDocument repoDocument = new RepositoryDocument(repository);
		} catch (XmlDocumentException e) {
			System.err.println("Unable to setup repository: " + e.getMessage());
		}
		
	}

}
