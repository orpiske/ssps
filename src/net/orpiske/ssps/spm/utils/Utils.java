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
package net.orpiske.ssps.spm.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * General utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Utils {
	
	private static String USER_LOCAL_DIRECTORY = ".spm";
	
	/**
	 * Restricted constructor
	 */
	private Utils() {};

	/**
	 * Gets the user SPM directory path (ie.: $HOME/.spm)
	 * @return the user SPM directory path
	 */
	public static String getSpmDirectoryPath() {
		return FileUtils.getUserDirectoryPath() + File.separator + USER_LOCAL_DIRECTORY;
	}
	
	
	/**
	 * Gets the user SPM directory path (ie.: $HOME/.spm) as a File object
	 * @return the user SPM directory path as a File object
	 */
	public static File getSpmDirectoryPathFile() {
		String userDirectory = getSpmDirectoryPath();
		return new File(userDirectory);
	}
	
}
