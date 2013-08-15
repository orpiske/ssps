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
package net.orpiske.sdm.packages;

import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.lib.rdc.RuntimeDataContainer;
import net.orpiske.ssps.common.repository.utils.InstallDirUtils;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public interface Package {

	/**
	 * The temporary work directory
	 */
	public static final String workDir = WorkdirUtils.getWorkDir();

	/**
	 * The installation directory
	 */
	public static final String installDir = InstallDirUtils.getInstallDir();

	/**
	 * The run time data container holds relevant run time data that can be used by the scripts
	 */
	public RuntimeDataContainer rdc = RuntimeDataContainer.getInstance();

	/**
	 * Fetch phase: downloads the package file/files
	 * @param url The file address
	 */
	void fetch(final String url);

	/**
	 * Extract phase: extracts the package resources
	 * @param artifactName The path to the file to extract
	 */
	void extract(final String artifactName);

	/**
	 * Run the preparation steps
	 */
	void prepare();

	/**
	 * Build phase
	 */
	void build();

	/**
	 * Verify phase
	 */
	void verify();

	/**
	 * Install phase
	 */
	void install();

	/**
	 * Complete phase
	 */
	void finish();

	/**
	 * Cleanup phase
	 */
	void cleanup();


	/**
	 * Uninstall phase
	 * @param path The installation path of the package
	 */
	void uninstall(final String path);


}
