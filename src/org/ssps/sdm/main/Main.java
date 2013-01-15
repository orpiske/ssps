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

import java.io.FileNotFoundException;
import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.logger.LoggerUtils;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.sdm.actions.Installer;
import org.ssps.sdm.repository.exceptions.InvalidRepository;
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
		System.out.println("   init");
		System.out.println("   deploy");
		System.out.println("   fetch");
		System.out.println("   install");
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
			
			try { 	
				if (first.equals("install")) {
					Installer installer = new Installer(newArgs);
					
					installer.run();
					return;
				}
				
				if (first.equals("--version")) {
					System.out.println("Simple Software Provisioning System: sdm " +
							Constants.VERSION);
					
					return;
				}
				
				help(1);
			}
			catch (XmlDocumentException e) {
				System.err.println("Invalid document: " + e.getMessage());
			} catch (InvalidRepository e) {
				System.err.println("The repository is not correctly setup. Did you run init?");
			}
	}

}
