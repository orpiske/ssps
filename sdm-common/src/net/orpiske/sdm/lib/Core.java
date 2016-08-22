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
package net.orpiske.sdm.lib;

import java.io.File;
import java.io.IOException;

import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.lib.io.IOUtil;
import net.orpiske.ssps.common.repository.utils.InstallDirUtils;

/**
 * Core utilities
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Core {
	

	/**
	 * Installs a package into the installation directory
	 * @param name The package name
	 * @param version The package version
	 * @throws IOException In case of I/O errors
	 */
	public static void performInstall(final String name, final String version) throws IOException
	{
		String workDir = WorkdirUtils.getWorkDir();
			
		performInstall(workDir + File.separator + name + "-" + version, name, version);	
	}
	
	
	/**
	 * Installs a package into the installation directory
	 * @param source The source folder
	 * @param name The package name
	 * @param version The package version
	 * @throws IOException In case of I/O errors
	 */
	public static void performInstall(final String source, final String name, 
			final String version) throws IOException
	{
		String installdir = InstallDirUtils.getInstallDir();
		String dest = installdir + File.separator + name + "-" + version;
		
		System.out.println("Installing " + name + "-" + version + " to " + dest);
		
		IOUtil.copy(source, dest);
	}

}
