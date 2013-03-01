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

import java.io.File;
import java.io.FileFilter;

import net.orpiske.ssps.common.repository.exception.RepositorySetupException;
import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.log4j.Logger;

/**
 * Utility class that helps manipulating the repositories
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class RepositoryManager {
	
	private static final Logger logger = Logger.getLogger(RepositoryManager.class);
	
	
	/**
	 * Default constructor
	 */
	public RepositoryManager() {
		
	}
	
	
	/**
	 * Adds a new repository
	 * @param repositoryInfo The repository information
	 * @throws RepositorySetupException If unable to create the repository on the FS
	 * @throws RepositoryUpdateException If unable to update the repository from the 
	 * remote data
	 */
	public void add(final RepositoryInfo repositoryInfo) throws RepositorySetupException, RepositoryUpdateException {
		Provider provider = ProviderFactory.newProvider(repositoryInfo);
		
		provider.initialize();
		
		RepositorySettings.addNewRepository(repositoryInfo);
	}
	
	
	
	/**
	 * Updates all the repositories
	 */
	public void update() {
		File userRepository = RepositoryUtils.getUserRepositoryFile();
		
		
		File[] repositories = 
					userRepository.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);

		for (File repository : repositories) {
			Provider provider = ProviderFactory.newProvider(repository);
			
			try {
				logger.info("Updating repository " + repository.getName());
				provider.update();
				logger.info("Sucessfully updated repository " + repository.getName());
			} catch (RepositoryUpdateException e) {
				logger.error("Unable to update repository " + repository.getName() 
						+ ": " + e.getMessage(), e);
			}
		}
	}

}
