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
package org.ssps.sdm.adm.rules.support;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Shield utils
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class ShieldUtils {
	
	private static final Logger logger = Logger.getLogger(ShieldUtils.class);
	
	public static final String SHIELD_EXT = ".shield";
	
	private ShieldUtils() {}
	
	
	/**
	 * Checks if a file is shielded
	 * @param dir the parent directory for the file
	 * @param name the name of the file
	 * @return true if the file is shielded or false otherwise
	 */
	public static boolean isShielded(File dir, String name) {
		File shield = new File(dir, name + SHIELD_EXT);
		
		if (logger.isDebugEnabled()) { 
			logger.debug("Checking if the resource is shielded: " + name);
		}
		
		if (shield.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug("File " + name + " is shielded");
			}
			
			return true;
		}
		
		return false;
	}

	
	/**
	 * Checks if a file is shielded
	 * @param file the file to check
	 * @return true if the file is shielded of false otherwise
	 */
	public static boolean isShielded(File file) {
		File shield = new File(file.getPath() + SHIELD_EXT);

		if (logger.isDebugEnabled()) {
			logger.debug("Checking if the resource is shielded: " 
					+ file.getPath());
		}
		
		if (shield.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug("File " + file.getPath() + " is shielded");
			}
			
			return true;
		}
		
		return false;
	}
}
