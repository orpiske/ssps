package org.ssps.common.configuration;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigurationWrapper {
	private static PropertiesConfiguration config;
	
	/**
	 * Initializes the configuration object
	 * @param propertyName The name of the system property that points to the 
	 * log configuration directory
	 * @param fileName The name of the configuration file
	 * @throws FileNotFoundException
	 * @throws ConfigurationException
	 */
	public static void initConfiguration(final String propertyName, 
			final String fileName) throws FileNotFoundException, ConfigurationException 
	{	
		String configDir = System.getProperty(propertyName);
		
		if (configDir == null) {
			throw new FileNotFoundException("The configuration dir was not found");
		}
		
		config = new PropertiesConfiguration(configDir + File.separator 
				+ fileName);
	}
	
	/**
	 * Gets the configuration object
	 * @return
	 */
	public static PropertiesConfiguration getConfig() {
		return config;
	}

}
