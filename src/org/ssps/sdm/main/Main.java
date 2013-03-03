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
package org.ssps.sdm.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.logger.LoggerUtils;
import net.orpiske.ssps.common.repository.RepositorySettings;
import net.orpiske.ssps.common.utils.Utils;

import org.apache.commons.configuration.ConfigurationException;
import org.ssps.sdm.actions.AddRepository;
import org.ssps.sdm.actions.Installer;
import org.ssps.sdm.actions.Update;
import org.ssps.sdm.utils.Constants;

/**
 * Main class
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Main {

	public static void initLogger() throws FileNotFoundException {
		LoggerUtils.initLogger(Constants.SDM_CONFIG_DIR);
	}

	public static void help(int code) {
		System.out.println("Usage: sdm <action>\n");
		
		System.out.println("Actions:");
		System.out.println("   add-repository");
		System.out.println("   install");
		System.out.println("   update");
		System.out.println("----------");
		System.out.println("   help");
		
		System.exit(code);
	}

	/**
	 * Initializes the configuration object
	 */
	private static void initConfig() {
		try {
			ConfigurationWrapper.initConfiguration(Constants.SDM_CONFIG_DIR,
					Constants.CONFIG_FILE_NAME);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		} catch (ConfigurationException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		}
		
		
		try {
			RepositorySettings.initConfiguration();
		} catch (SspsException e) {
			e.printStackTrace();
			
			System.err.println(e.getMessage());
			System.exit(-3);
		}
	}
	
	
	private static Properties initDatabase() throws DatabaseInitializationException {
		Properties props = System.getProperties();
		props.setProperty("derby.system.home", Utils.getSdmDirectoryPath());
		
		DerbyDatabaseManager derby = new DerbyDatabaseManager("registry", props);

		derby.close();
		return props;
	}
	
	private static void initUserSdmDirectory() {
		File userDirectory = Utils.getSdmDirectoryPathFile();
		
		if (!userDirectory.exists()) {
			userDirectory.mkdirs();
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
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
			
			initUserSdmDirectory();
			try {
				initDatabase();
			} catch (DatabaseInitializationException e) {
				e.printStackTrace();
				System.exit(-2);
			}
			
		
			if (first.equals("install")) {
				Installer installer = new Installer(newArgs);
				
				installer.run();
				return;
			}
			
			if (first.equals("add-repository")) {
				AddRepository addRepository = new AddRepository(newArgs);
				
				addRepository.run();
				return;
			}
			
			if (first.equals("update")) {
				Update update = new Update(newArgs);
				
				update.run();
				return;
			}
			
			if (first.equals("--version")) {
				System.out.println("Simple Software Provisioning System: sdm " +
						Constants.VERSION);
				
				return;
			}
			
			help(1);
			
	}

}
