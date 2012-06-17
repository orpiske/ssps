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
package org.ssps.sdm.utils;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.ssps.common.configuration.ConfigurationWrapper;

/**
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class WorkdirUtils {
	private static final PropertiesConfiguration config 
		= ConfigurationWrapper.getConfig();
	
	
	public static String getGetRoot() {
		return config.getString("temp.work.dir", 
				FileUtils.getTempDirectoryPath());
	}
	
	
	public static String getWorkDir() {
		return config.getString("temp.work.dir",
				FileUtils.getTempDirectoryPath() + File.separator + "work");
	}

}
