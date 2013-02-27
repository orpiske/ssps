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
import java.io.IOException;

import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class GitProvider implements Provider {
	
	private static final Logger logger = Logger.getLogger(GitProvider.class);
	
	private String userRepositoryPath;
	private String sourceURI;
	
	public GitProvider(final String sourceURI) {
		userRepositoryPath = RepositoryUtils.getUserDefaultRepository();
		this.sourceURI = sourceURI;
	}
	
	private void clone(final File repositoryDir) throws RepositoryUpdateException {
		
		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setURI(sourceURI);
		cloneCommand.setDirectory(repositoryDir);
		cloneCommand.setProgressMonitor(new TextProgressMonitor());
		
		try {
			logger.info("Repository does not exist. Cloning from " + sourceURI);
			
			cloneCommand.call();
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
			
			throw new RepositoryUpdateException(e.getMessage(), e);
		} catch (TransportException e) {
			e.printStackTrace();
			
			throw new RepositoryUpdateException(e.getMessage(), e);
		} catch (GitAPIException e) {
			e.printStackTrace();
			
			throw new RepositoryUpdateException(e.getMessage(), e);
		}
	}
	
	private void refresh(final File repositoryDir) throws RepositoryUpdateException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = null;
		try {
			repository = builder.setGitDir(repositoryDir)
					.readEnvironment() 
					.findGitDir()
					.build();
		} catch (IOException e) {
			throw new RepositoryUpdateException(e.getMessage(), e);
		}
		
		Git git = new Git(repository);
		PullCommand pullCommand = git.pull();
		
		pullCommand.setProgressMonitor(new TextProgressMonitor());
		
		logger.info("Refreshing local repository with remote copy from " + sourceURI);
		try {
			pullCommand.call();
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new RepositoryUpdateException(e.getMessage(), e);
		}
	}
	
	
	public void update() throws IOException, RepositoryUpdateException {
		File repositoryDir = new File(userRepositoryPath);
		
		if (!repositoryDir.exists()) {
			repositoryDir.mkdirs();
			
			clone(repositoryDir);
		}
		else {
			File gitDir = new File(userRepositoryPath + File.separator + ".git");
			refresh(gitDir);
		}
	}
	
}
