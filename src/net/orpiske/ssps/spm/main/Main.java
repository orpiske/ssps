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
package net.orpiske.ssps.spm.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.common.logger.LoggerUtils;
import net.orpiske.ssps.spm.actions.Create;
import net.orpiske.ssps.spm.utils.Constants;
import net.orpiske.ssps.spm.utils.Utils;

import org.apache.commons.configuration.ConfigurationException;

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


	private static void initUserSpmDirectory() {
		File userDirectory = Utils.getSpmDirectoryPathFile();

		if (!userDirectory.exists()) {
			userDirectory.mkdirs();
		}

	}

	public static void help(int code) {
		System.out.println("Usage: spm <action>\n");

		System.out.println("Actions:");
		System.out.println("   create");

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

			initUserSpmDirectory();

			if (first.equals("help")) {
				help(1);
			}

			if (first.equals("create")) {
				Create create = new Create(newArgs);

				create.run();
				return;
			}

			if (first.equals("--version")) {
				System.out.println("Simple Software Provisioning System: spm " +
						Constants.VERSION);

				return;
			}

			help(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unable to execute operation: "
					+ e.getMessage());

			System.exit(1);
		}
	}

}
