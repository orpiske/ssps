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
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.logger.LoggerUtils;
import org.ssps.spm.actions.ArchiveCreator;
import org.ssps.spm.actions.Publisher;
import org.ssps.spm.actions.RepositoryManager;
import org.ssps.spm.archive.Archiver;
import org.ssps.spm.utils.Constants;

public class Main {

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
		System.out.println("Usage: sdm <action>\n");
		
		System.out.println("Actions:");
		System.out.println("   create");
		System.out.println("   publish");
		System.out.println("   delete");
		
		System.exit(code);
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				help(1);
			}
			
			String first = args[0];
			String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
			
			try {
				initLogger();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			initConfig();

			if (first.equals("help")) {
				help(1);
			}
			
			if (first.equals("create")) {
				ArchiveCreator creator = new ArchiveCreator(newArgs);
				
				creator.run();
				
				return;
			}
			
			if (first.equals("publish")) {
				Publisher publisher = new Publisher(newArgs);
				
				publisher.run();
				
				return;
			}
			
			if (first.equals("publish")) {
				RepositoryManager manager = new RepositoryManager(newArgs);
				
				manager.run();
				
				return;
			}
		} catch (Exception e) {
			System.err.println("Unable to execute operation: " 
					+ e.getMessage());
		}
		

	}

}
