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
package net.orpiske.sdm.common;

import java.io.File;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

/**
 * Work dir utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class WorkdirUtils {
	private static final PropertiesConfiguration config 
		= ConfigurationWrapper.getConfig();
	
	/**
	 * Restricted constructor
	 */
	private WorkdirUtils() {}
	
	
	/**
	 * Gets the work dir
	 * @return the work dir
	 */
	public static String getWorkDir() {
		return config.getString("temp.work.dir",
				FileUtils.getTempDirectoryPath() + File.separator + "work");
	}
	
	
	/**
	 * Gets the work dir
	 * @return the work dir as a File object
	 */
	public static File getWorkDirFile() {
		String workDir = getWorkDir();
		return new File(workDir);
	}
	
	
	/**
	 * Cleanup the work dir cache
	 */
	public static void cleanup() {
		File workDir = getWorkDirFile();
		
		FileUtils.deleteQuietly(workDir);
	}

}
