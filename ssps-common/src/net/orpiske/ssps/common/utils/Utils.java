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
package net.orpiske.ssps.common.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * General utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Utils {
	
	private static String USER_LOCAL_DIRECTORY = ".sdm";
	
	private static String USER_HOME = null;
	
	/**
	 * Restricted constructor
	 */
	private Utils() {}


	private static String getUserHomeOrOverride() {
		String userHome = System.getenv("USER_LOCAL_DIRECTORY");

		if (userHome == null) {
			userHome = FileUtils.getUserDirectoryPath();
		}

		return userHome + File.separator + USER_LOCAL_DIRECTORY;
	}


	/**
	 * Gets the user SDM directory path (ie.: $HOME/.sdm) or the appropriate override
	 * @return the user SDM directory path
	 */
	public static String getSdmDirectoryPath() {
		if (USER_HOME == null) {
			USER_HOME = getUserHomeOrOverride();
		}
		
		return USER_HOME;
	}
	
	
	/**
	 * Gets the user SDM directory path (ie.: $HOME/.sdm) as a File object
	 * @return the user SDM directory path as a File object
	 */
	public static File getSdmDirectoryPathFile() {
		String userDirectory = getSdmDirectoryPath();
		return new File(userDirectory);
	}
	
}
