package org.ssps.common.logger;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.PropertyConfigurator;

public class LoggerUtils {
	
	/**
	 * Initializes the logger
	 * @param propertyName The name of the system property that points to the 
	 * log configuration directory
	 * @throws FileNotFoundException if the configuration file cannot be found
	 */
	public static void initLogger(final String propertyName) throws FileNotFoundException {
		String configDir = System.getProperty(propertyName);
		
		if (configDir == null) {
			throw new FileNotFoundException("The configuration dir was not found");
		}
		
		PropertyConfigurator.configure(configDir + File.separator 
				+ "log.properties");
	}
}
