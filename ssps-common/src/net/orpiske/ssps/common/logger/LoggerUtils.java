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
package net.orpiske.ssps.common.logger;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.PropertyConfigurator;


/**
 * Utilities to the logger object
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class LoggerUtils {

	/**
	 * Restricted constructor
	 */
	private LoggerUtils() {}
	
	/**
	 * Initializes the logger
	 * 
	 * @param configDir
	 *            The path to the log configuration directory
	 * @throws FileNotFoundException
	 *             if the configuration file cannot be found
	 */
	public static void initLogger(final String configDir)
			throws FileNotFoundException {
		if (configDir == null) {
			throw new FileNotFoundException(
					"The configuration dir was not found");
		}

		PropertyConfigurator.configure(configDir + File.separator
				+ "log.properties");
	}
}
