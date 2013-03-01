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

import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.repository.exception.RepositorySetupException;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class RepositorySettings {
	
	private static final Logger logger = Logger.getLogger(RepositorySettings.class);
	
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
	public static void initConfiguration() throws SspsException  {
		
		String repositoryPath = RepositoryUtils.getUserRepository();
		String path = repositoryPath + File.separator + "repositories.conf";
		
		if (config == null) {
			
			File file = new File(path);
			
			if (!file.exists()) { 
			
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					config = new PropertiesConfiguration(path);
				} catch (ConfigurationException e) {
					throw new SspsException("Unable to load repository configuration: "
							+ e.getMessage(), e);
				}	
				
				try {
					config.save();
				} catch (ConfigurationException e) {
					throw new SspsException("Unable to save repository configuration: "
								+ e.getMessage(), e);
				}
			}
			else {
				try {
					config = new PropertiesConfiguration(path);
				} catch (ConfigurationException e) {
					throw new SspsException("Unable to load repository configuration: "
								+ e.getMessage(), e);
				}
			}
	
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
	
	
	private static void addUserConfig(final RepositoryInfo repositoryInfo) throws RepositorySetupException {
		String repositoryPath = RepositoryUtils.getUserRepository();
		
		String path = repositoryPath + File.separator + repositoryInfo.getName() 
				+ File.separator + "user.conf";
		
		File file = new File(path);
		
		if (!file.exists()) { 
			try {
				file.createNewFile();
			} catch (IOException e) {
				
			}
		}
		else {
			throw new RepositorySetupException("This user already exists");
		}
		
	
		PropertiesConfiguration userConfig;
		try {
			userConfig = new PropertiesConfiguration(path);
			
			userConfig.addProperty(repositoryInfo.getName() + ".auth.user", 
					repositoryInfo.getUserName());
			userConfig.addProperty(repositoryInfo.getName() + ".auth.password", 
					repositoryInfo.getPassword());
			userConfig.save();
		} catch (ConfigurationException e) {
			throw new RepositorySetupException("Unable to save user configuration to " 
					+ path + ":" + e.getMessage(), e);
		}
	
	}
	
	
	public static void addNewRepository(final RepositoryInfo repositoryInfo) throws RepositorySetupException {
		addUserConfig(repositoryInfo);
	
	}

}
