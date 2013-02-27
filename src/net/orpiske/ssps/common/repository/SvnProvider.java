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
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;
import org.tmatesoft.svn.core.wc2.SvnUpdate;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SvnProvider implements Provider {
	
	private static Logger logger = Logger.getLogger(SvnProvider.class);
	
	private String name;
	
	private SVNClientManager clientManager;
	
	private String sourceURI;
	
	public SvnProvider(final String name) {
		this.name = name;
		
		DefaultSVNOptions options = new DefaultSVNOptions();
		
		PropertiesConfiguration config = RepositorySettings.getConfig();
		String userName = config.getString(name + ".auth.user", null);
		String password = config.getString(name + "auth.password", null);
		sourceURI = config.getString(name + ".source.url");
	
		
		clientManager = SVNClientManager.newInstance(options, userName, 
				password); 
	}
	

	
	public void checkout(final File repositoryDir) throws RepositoryUpdateException {	
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnCheckout checkout = svnOperationFactory.createCheckout();
		
		logger.info("Repository does not exist. Checking out from " + sourceURI);
		
		checkout.setSingleTarget(SvnTarget.fromFile(repositoryDir));
		try {
			checkout.setSource(SvnTarget.fromURL(SVNURL.parseURIEncoded(sourceURI)));
			checkout.run();
		} 
		catch (SVNException e) {
			throw new RepositoryUpdateException(e.getMessage(), e);
		}
		finally {
			svnOperationFactory.dispose();
		}
	}
	
	
	public void update(final File repositoryDir) throws RepositoryUpdateException {	
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnUpdate update = svnOperationFactory.createUpdate();
		
		logger.info("Refreshing local repository with remote copy from " + sourceURI);
		
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
	
	
	public void update() throws RepositoryUpdateException {	
		String repositoryPath = RepositoryUtils.getUserRepository() + File.separator 
				+ name;
		File repositoryDir = new File(repositoryPath);
		
		if (!repositoryDir.exists()) {
			repositoryDir.mkdirs();
			
			checkout(repositoryDir);
		}
		else {
			update(repositoryDir);
		}
		
	
	}
	

}
