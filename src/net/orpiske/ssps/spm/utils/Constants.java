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

public final class Constants {
	
	public static final String VERSION = "0.1.0";
	
	/**
	 * This property is used to set the configuration directory
	 */
	public static final String HOME_PROPERTY = "org.ssps.spm.home";

	public static final String SPM_CONFIG_DIR;

	static {
		SPM_CONFIG_DIR = System.getProperty(HOME_PROPERTY) + File.separator
				+ "conf";
	}

	/**
	 * This constant holds the configuration file name for the backend
	 */
	public static final String CONFIG_FILE_NAME = "spm.properties";
}
