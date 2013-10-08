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
package net.orpiske.ssps.common.scm.git;


import net.orpiske.ssps.common.scm.Scm;
import net.orpiske.ssps.common.scm.ScmBranch;
import net.orpiske.ssps.common.scm.ScmCredentials;
import net.orpiske.ssps.common.scm.exceptions.*;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Git SCM support
 */
public class GitSCM implements Scm {
	private static final Logger logger = Logger.getLogger(GitSCM.class);
	
	private ScmBranch branch;
	private ScmCredentials credentials;

	/**
	 * Clones de repository
	 * @param url URL
	 * @param repositoryDir destination directory
	 * @throws ScmCheckoutException if unable to checkout
	 */
	private void clone(final String url, final File repositoryDir) throws ScmCheckoutException {
		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setURI(url);
		cloneCommand.setDirectory(repositoryDir);
		cloneCommand.setProgressMonitor(new TextProgressMonitor());
		
		//final String branch = repositoryInfo.getRepositoryVersion();
		if (branch != null && !branch.getName().isEmpty()) {
			cloneCommand.setBranch(branch.getName());
		}

		try {
			logger.info("SCM repository does not exist. Cloning from " + url);

			cloneCommand.call();
		} catch (InvalidRemoteException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			throw new ScmCheckoutException(e.getMessage(), e);
		} catch (TransportException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			throw new ScmCheckoutException(e.getMessage(), e);
		} catch (GitAPIException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}

			throw new ScmCheckoutException(e.getMessage(), e);
		}
	}

	private void update(final File repositoryDir) throws ScmUpdateException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = null;

		try {
			repository = builder.setGitDir(repositoryDir)
					.readEnvironment()
					.findGitDir()
					.build();
		} catch (IOException e) {
			throw new ScmUpdateException(e.getMessage(), e);
		}

		Git git = new Git(repository);
		PullCommand pullCommand = git.pull();

		pullCommand.setProgressMonitor(new TextProgressMonitor());


		try {
			pullCommand.call();
		} catch (Exception e) {
			e.printStackTrace();

			throw new ScmUpdateException(e.getMessage(), e);
		}
	}

	/**
	 * Access the repository
	 * @param file the repository directory or file
	 * @return A repository pointer object
	 * @throws ScmAccessException
	 */
	private Repository accessRepository(final File file)
			throws ScmAccessException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = null;

		try {
			if (file.isDirectory()) {
				repository = builder.setGitDir(file)
						.readEnvironment()
						.findGitDir()
						.build();
			}
			else {
				repository = builder.setGitDir(file.getParentFile())
						.readEnvironment()
						.findGitDir()
						.build();
			}

		} catch (IOException e) {
			throw new ScmAccessException(e.getMessage(), e);
		}
		return repository;
	}



	/* (non-Javadoc)
	 * @see net.orpiske.ssps.spm.repository.Provider#add(java.io.File)
	 */
	//@Override
	public void add(final File file) throws FileAddException, ScmAccessException {
		Repository repository = accessRepository(file);

		Git git = new Git(repository);
		AddCommand pullCommand = git.add();

		try {
			pullCommand.addFilepattern(file.getName()).call();
		} catch (NoFilepatternException e1) {
			throw new FileAddException("Unable to add file or directory: "
					+ e1.getMessage(), e1);
		} catch (GitAPIException e1) {
			throw new FileAddException("Unable to add file or directory: "
					+ e1.getMessage(), e1);
		}


	}


	/* (non-Javadoc)
	 * @see net.orpiske.ssps.spm.repository.Provider#commit(java.io.File)
	 */
	//@Override
	public void commit(final File file, final String message) throws FileCommitException, ScmAccessException {
		Repository repository = accessRepository(file);

		Git git = new Git(repository);
		CommitCommand pullCommand = git.commit();


		try {
			pullCommand.setMessage(message).call();
		} catch (NoHeadException e) {
			throw new FileCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (NoMessageException e) {
			throw new FileCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (UnmergedPathsException e) {
			throw new FileCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (ConcurrentRefUpdateException e) {
			throw new FileCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (WrongRepositoryStateException e) {
			throw new FileCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (GitAPIException e) {
			throw new FileCommitException("Unable to commit: " + e.getMessage(), e);
		}


	}
	
	
	@Override
	public void setCredentials(ScmCredentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public void setBranch(ScmBranch branch) {
		this.branch = branch;
	}

	@Override
	public void checkout(final String url, final File path) throws ScmCheckoutException, DuplicateCheckoutException {

		if (!path.exists()) {
			if (!path.mkdirs()) {
				throw new ScmCheckoutException("Unable to create repository directory");
			}

			clone(url, path);
		}
		else {
			throw new DuplicateCheckoutException("Destination path already exists");
		}
	}

	@Override
	public void update(String path) throws ScmUpdateException {
		File gitDir = new File(path + File.separator + ".git");
		update(gitDir);
	}
}
