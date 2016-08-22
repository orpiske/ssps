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
package net.orpiske.sdm.engine;

import java.io.File;

import net.orpiske.sdm.engine.exceptions.EngineException;

/**
 * The basic interface for all package script processing engines
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public interface Engine {

	/**
	 * Runs the package script in the file pointed by path
	 * @param path the full path to the package script
	 * @throws EngineException if fails to process the script
	 */
	public void run(final String path) throws EngineException;
	
	
	/**
	 * Runs the package script in the file
	 * @param file the package script
	 * @throws EngineException if fails to process the script
	 */
	public void run(final File file) throws EngineException;


	/**
	 * Runs the package script in the file
	 * @param file the package script
	 * @param phases a list of install phase(s) to run   
	 * @throws EngineException if fails to process the script
	 */
	public void run(final File file, String...phases) throws EngineException;
	
	
	/**
	 * Runs the package cleanup script in the file pointed by path
	 * @param path the full path to the package script
	 * @throws EngineException if fails to process the script
	 */
	public void runUninstall(final String path) throws EngineException;
	
	
	/**
	 * Runs the package cleanup script in the file
	 * @param file the package script
	 * @throws EngineException if fails to process the script
	 */
	public void runUninstall(final File file) throws EngineException;
}
