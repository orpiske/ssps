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
package net.orpiske.ssps.common.packages;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public interface Package {


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
