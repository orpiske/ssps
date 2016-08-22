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
package net.orpiske.ssps.common.repository;

import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;

/**
 * The interface on which repository providers are created
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public interface Provider {
	
	/**
	 * Creates a new repository
	 * @throws RepositoryUpdateException If unable to update data from remote location
	 */
	public void create() throws RepositoryUpdateException;
	
	/**
	 * Updates the repository
	 * @throws RepositoryUpdateException If unable to update data from remote location
	 */
	public void update() throws RepositoryUpdateException;
	
	
	
	/**
	 * Initializes the repository
	 * @throws RepositoryUpdateException If unable to update data from remote location
	 */
	public void initialize() throws RepositoryUpdateException;
}
