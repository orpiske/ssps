/**
 Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.ssps.common.scm.svn;

import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;
import net.orpiske.ssps.common.scm.Scm;
import net.orpiske.ssps.common.scm.ScmBranch;
import net.orpiske.ssps.common.scm.ScmCredentials;
import net.orpiske.ssps.common.scm.exceptions.*;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;
import org.tmatesoft.svn.core.wc2.SvnUpdate;

import java.io.File;

/**
 * SVN SCM support
 */
public class SvnSCM implements Scm {
	private static final Logger logger = Logger.getLogger(SvnSCM.class);

	private ScmCredentials credentials;
	private SVNClientManager clientManager;

	public SvnSCM() {
		clientManager = SVNClientManager.newInstance();
	}

	private void create(final String url, final File repositoryDir) throws ScmCheckoutException {
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnCheckout checkout;

		svnOperationFactory.setAuthenticationManager(newAuthManager());
		checkout = svnOperationFactory.createCheckout();

		logger.info("Repository does not exist. Checking out from "	+ url);

		checkout.setSingleTarget(SvnTarget.fromFile(repositoryDir));
		try {
			checkout.setSource(SvnTarget.fromURL(SVNURL.parseURIEncoded(url)));
			checkout.run();
		}
		catch (SVNException e) {
			throw new ScmCheckoutException(e.getMessage(), e);
		}
		finally {
			svnOperationFactory.dispose();
		}
	}


	private void update(final File repositoryDir) throws ScmUpdateException {
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		final SvnUpdate update;

		svnOperationFactory.setAuthenticationManager(newAuthManager());
		update = svnOperationFactory.createUpdate();


		logger.info("Updating local repository at " + repositoryDir.getPath());


		update.setSingleTarget(SvnTarget.fromFile(repositoryDir));
		try {
			update.run();
		}
		catch (SVNException e) {
			throw new ScmUpdateException(e.getMessage(), e);
		}
		finally {
			svnOperationFactory.dispose();
		}
	}

	private ISVNAuthenticationManager newAuthManager() {
		if (credentials != null) {
			return SVNWCUtil.createDefaultAuthenticationManager(credentials.getUserName(),
				credentials.getPassword());
		}

		return null;
	}

	@Override
	public void setCredentials(ScmCredentials credentials) {
		this.credentials = credentials;

		clientManager.setAuthenticationManager(newAuthManager());
	}

	@Override
	public void setBranch(ScmBranch branch) {
		// NO-OP: the branch is part of the URL in SVN.
	}

	@Override
	public void checkout(String url, File path) throws ScmCheckoutException, DuplicateCheckoutException {
		if (!path.exists()) {
			if (!path.mkdirs()) {
				throw new ScmCheckoutException("Unable to create repository directory");
			}

			create(url, path);
		}
		else {
			throw new DuplicateCheckoutException("Destination path already exists");
		}
	}

	@Override
	public void update(String path) throws ScmUpdateException {
		File repositoryDir = new File(path);

		update(repositoryDir);
	}



	/* (non-Javadoc)
	* @see net.orpiske.ssps.spm.repository.Provider#add(java.io.File)
	*/
	@Override
	public void add(File file) throws FileAddException, ScmAccessException {

		SVNWCClient wcClient = clientManager.getWCClient();

		try {
			wcClient.doAdd(file, false, true, false, true, false);
		} catch (SVNException e) {
			throw new FileAddException("Unable to add file: " + e.getMessage(), e);
		}

	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.spm.repository.Provider#commit(java.io.File, java.lang.String)
	 */
	@Override
	public void commit(File file, String message)
			throws FileCommitException, ScmAccessException {
		SVNCommitClient client = clientManager.getCommitClient();

		try {
			client.doCommit(new File[] { file }, false, message, false, true);
		} catch (SVNException e) {
			throw new FileCommitException("Unable to commit file: " + e.getMessage(), e);
		}
	}
}
