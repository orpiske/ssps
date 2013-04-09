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

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;
import org.tmatesoft.svn.core.wc2.SvnUpdate;

/**
 * Svn Repository Provider
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SvnProvider implements Provider {
	
	private static Logger logger = Logger.getLogger(SvnProvider.class);
	
	private RepositoryInfo repositoryInfo;
	
	/**
	 * Default constructor
	 * @param repositoryInfo repository information
	 */
	public SvnProvider(final RepositoryInfo repositoryInfo) {
		this.repositoryInfo = repositoryInfo;
	}
	

	private void create(final File repositoryDir) throws RepositoryUpdateException {	
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnCheckout checkout = svnOperationFactory.createCheckout();
		
		logger.info("Repository does not exist. Checking out from " 
				+ repositoryInfo.getUrl());
		
		checkout.setSingleTarget(SvnTarget.fromFile(repositoryDir));
		try {
			checkout.setSource(SvnTarget.fromURL(
					SVNURL.parseURIEncoded(repositoryInfo.getUrl())));
			checkout.run();
		} 
		catch (SVNException e) {
			throw new RepositoryUpdateException(e.getMessage(), e);
		}
		finally {
			svnOperationFactory.dispose();
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.Provider#create()
	 */
	public void create() throws RepositoryUpdateException {
		String repositoryPath = repositoryInfo.getLocalPath();
		File repositoryDir = new File(repositoryPath);
		
		if (!repositoryDir.exists()) {
			if (!repositoryDir.mkdirs()) {
				throw new RepositoryUpdateException("Unable to create repository directory");
			}
			
			create(repositoryDir);
		}
		else {
			logger.warn("The local repository already exists and will not be recreated");
		}
	}
	
	
	private void update(final File repositoryDir) throws RepositoryUpdateException {	
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnUpdate update = svnOperationFactory.createUpdate();
		
		logger.info("Refreshing local repository with remote copy from " + 
				repositoryInfo.getUrl());
		
		update.setSingleTarget(SvnTarget.fromFile(repositoryDir));
		try {
			update.run();
		} 
		catch (SVNException e) {
			throw new RepositoryUpdateException(e.getMessage(), e);
		}
		finally {
			svnOperationFactory.dispose();
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.Provider#update()
	 */
	public void update() throws RepositoryUpdateException {	
		String repositoryPath = repositoryInfo.getLocalPath();
		File repositoryDir = new File(repositoryPath);
		
		update(repositoryDir);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.ssps.common.repository.Provider#initialize()
	 */
	public void initialize() throws RepositoryUpdateException {	
		String repositoryPath = repositoryInfo.getLocalPath();
		File repositoryDir = new File(repositoryPath);
		
		if (!repositoryDir.exists()) {
			if (!repositoryDir.mkdirs()) {
				throw new RepositoryUpdateException("Unable to create repository directory");
			}
			
			create(repositoryDir);
		}
		else {
			update(repositoryDir);
		}
	}
}
