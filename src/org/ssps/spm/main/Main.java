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
package org.ssps.spm.main;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.logger.LoggerUtils;
import org.ssps.spm.archive.Archiver;
import org.ssps.spm.utils.Constants;

public class Main {
	private static Options options;

	public static void initLogger() throws FileNotFoundException {
		LoggerUtils.initLogger(Constants.SPM_CONFIG_DIR);
	}

	/**
	 * Initializes the configuration object
	 */
	private static void initConfig() {
		try {
			ConfigurationWrapper.initConfiguration(Constants.SPM_CONFIG_DIR,
					Constants.CONFIG_FILE_NAME);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		} catch (ConfigurationException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		}
	}

	public static void help(int code) {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("spm", options);
		System.exit(code);
	}

	public static CommandLine processCommand(String[] args)
			throws ParseException {
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption(null, "create", false, "create the deliverable file");
		options.addOption(null, "publish", false,
				"send the file to the publication server");
		options.addOption(null, "delete", false,
				"deletes a resource from the repository");

		options.addOption("f", "file", true, "path to the DBM file");
		options.addOption("d", "deliverable", true,
				"path to the deliverable file");
		options.addOption("u", "url", true, "URL to the resource to delete");

		return parser.parse(options, args);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CommandLine cmdLine = processCommand(args);

			initLogger();
			initConfig();

			if (cmdLine.hasOption('h')) {
				help(1);
			}

			String dbmFile = cmdLine.getOptionValue('f');
			if (dbmFile == null) {
				help(-1);
			}

			if (cmdLine.hasOption("create")) {
				Archiver archiver = new Archiver(dbmFile);

				archiver.createArchive();
			} else {
				if (cmdLine.hasOption("publish")) {
					String deliverable = cmdLine.getOptionValue('d');

					PublicationManager manager = new PublicationManager(dbmFile);

					manager.upload(deliverable);
				} else {
					if (cmdLine.hasOption("delete")) {
						PublicationManager manager = new PublicationManager(
								dbmFile);

						manager.delete();
					} else {
						help(1);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

			System.exit(-1);
		} catch (Exception e) {
			System.err
					.println("Unable to execute operation: " + e.getMessage());

			e.printStackTrace();
		}

	}

}
