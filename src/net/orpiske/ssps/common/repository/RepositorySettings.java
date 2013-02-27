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
package net.orpiske.ssps.common.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class RepositorySettings {
	private static PropertiesConfiguration config;
	
	/**
	 * Initializes the configuration object
	 * 
	 * @param configDir
	 * 			  The configuration directory containing the configuration file
	 * @param fileName
	 *            The name of the configuration file
	 * @throws FileNotFoundException
	 * @throws ConfigurationException
	 */
	public static void initConfiguration() throws ConfigurationException {
		
		String repositoryPath = RepositoryUtils.getUserRepository();
		
		if (config == null) {
			String path = repositoryPath + File.separator + "repositories.conf";
			File file = new File(path);
			
			if (!file.exists()) { 
			
				try {
					file.createNewFile();
				} catch (IOException e) {
					
				}
			}
			
			config = new PropertiesConfiguration(repositoryPath + File.separator
					+ "repositories.conf");	
			
		}
	}
	
	
	/**
	 * Gets the configuration object
	 * 
	 * @return the instance of the configuration object
	 */
	public static PropertiesConfiguration getConfig() {
		return config;
	}

}
