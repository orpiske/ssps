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

import net.orpiske.ssps.common.repository.RepositoryInfo;
import net.orpiske.ssps.common.repository.RepositoryManager;
import net.orpiske.ssps.common.repository.exception.RepositorySetupException;
import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * Implements add-repository action
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class AddRepository extends ActionInterface {
	private static final Logger logger = Logger.getLogger(AddRepository.class);

	private CommandLine cmdLine;
	private Options options;

	private boolean isHelp;

	private RepositoryInfo repositoryInfo;

	public AddRepository(String[] args) {
		processCommand(args);
	}

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("n", "name", true, "repository name");
		options.addOption("u", "url", true, "repository url");
		options.addOption(null, "username", true, "repository user name");
		options.addOption(null, "password", true, "repository password");
        options.addOption(null, "branch", true, "(git only) repository branch");


		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}

		isHelp = cmdLine.hasOption("help");

		String name = cmdLine.getOptionValue('n');
		if (name == null) {
			help(options, -1);
		}

		repositoryInfo = new RepositoryInfo(name);


		String url = cmdLine.getOptionValue('u');
		if (url == null) {
			help(options, -1);
		}
		repositoryInfo.setUrl(url);

		String userName = cmdLine.getOptionValue("username");
		repositoryInfo.setUserName(userName);


		String password = cmdLine.getOptionValue("password");
		repositoryInfo.setPassword(password);

		String branch = cmdLine.getOptionValue("branch");
		repositoryInfo.setRepositoryVersion(branch);
	}

	@Override
	public int run() {

		try {
			if (isHelp) {
				help(options, 1);
			}
			else {
				RepositoryManager repositoryManager = new RepositoryManager();

				repositoryManager.add(repositoryInfo);
			}
			
			return 0;
		} catch (RepositoryUpdateException e) {
			System.err.println("Unable to update repository: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to install: " + e.getMessage(), e);
			}
			
			return 1;
		} catch (RepositorySetupException e) {
			System.err.println("Unable to setup new repository: " + e.getMessage());

			if (logger.isDebugEnabled()) {
				logger.error("Unable to setup new repository: " + e.getMessage(), e);
			}

			return 2;
		}
	}

}
