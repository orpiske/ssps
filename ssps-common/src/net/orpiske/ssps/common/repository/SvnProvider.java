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

import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;

import net.orpiske.ssps.common.scm.DefaultCredentials;
import net.orpiske.ssps.common.scm.ScmCredentials;
import net.orpiske.ssps.common.scm.exceptions.DuplicateCheckoutException;
import net.orpiske.ssps.common.scm.exceptions.ScmCheckoutException;
import net.orpiske.ssps.common.scm.exceptions.ScmUpdateException;
import net.orpiske.ssps.common.scm.svn.SvnSCM;
import org.apache.log4j.Logger;


import net.orpiske.ssps.common.scm.Scm;
/**
 * Svn Repository Provider
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SvnProvider implements Provider {
	
	private static Logger logger = Logger.getLogger(SvnProvider.class);
	
	private RepositoryInfo repositoryInfo;
	private Scm scm = new SvnSCM();
	
	/**
	 * Default constructor
	 * @param repositoryInfo repository information
	 */
	public SvnProvider(final RepositoryInfo repositoryInfo) {
		this.repositoryInfo = repositoryInfo;

		ScmCredentials credentials = new DefaultCredentials(
				repositoryInfo.getUserName(), repositoryInfo.getPassword());
		scm.setCredentials(credentials);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.Provider#create()
	 */
	public void create() throws RepositoryUpdateException {
		String repositoryPath = repositoryInfo.getLocalPath();
		File repositoryDir = new File(repositoryPath);

		try {
			scm.checkout(repositoryInfo.getUrl(), repositoryDir);
		} catch (ScmCheckoutException e) {
			throw new RepositoryUpdateException("Unable to checkout repository", e);
		} catch (DuplicateCheckoutException e) {
			logger.warn("The local repository already exists and will not be recreated", e);
		}
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.Provider#update()
	 */
	public void update() throws RepositoryUpdateException {
		try {
			scm.update(repositoryInfo.getLocalPath());
		} catch (ScmUpdateException e) {
			throw new RepositoryUpdateException("Unable to update repository", e);
		}

	}
	
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.Provider#initialize()
	 */
	public void initialize() throws RepositoryUpdateException {	
		String repositoryPath = repositoryInfo.getLocalPath();
		File repositoryDir = new File(repositoryPath);
		
		if (!repositoryDir.exists()) {
			create();
		}
		else {
			update();
		}
	}
}
