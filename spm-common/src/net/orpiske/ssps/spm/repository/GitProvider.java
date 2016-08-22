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
package net.orpiske.ssps.spm.repository;

import java.io.File;
import java.io.IOException;

import net.orpiske.ssps.common.repository.RepositoryInfo;
import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;
import net.orpiske.ssps.spm.repository.exception.RepositoryAccessException;
import net.orpiske.ssps.spm.repository.exception.RepositoryAddException;
import net.orpiske.ssps.spm.repository.exception.RepositoryCommitException;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class GitProvider implements Provider {
	
	private RepositoryInfo repositoryInfo;
	
	public GitProvider(RepositoryInfo repositoryInfo) {
		this.repositoryInfo = repositoryInfo;
	}
	
	/**
	 * @param file
	 * @return
	 * @throws RepositoryAccessException 
	 */
	private Repository accessRepository(final File file)
			throws RepositoryAccessException {
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
			throw new RepositoryAccessException(e.getMessage(), e);
		}
		return repository;
	}

	

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.spm.repository.Provider#add(java.io.File)
	 */
	@Override
	public void add(final File file) throws RepositoryAddException, RepositoryAccessException {
		Repository repository = accessRepository(file);
		
		Git git = new Git(repository);
		AddCommand pullCommand = git.add();
		
		try {
			pullCommand.addFilepattern(file.getName()).call();
		} catch (NoFilepatternException e1) {
			throw new RepositoryAddException("Unable to add file or directory: " 
					+ e1.getMessage(), e1);
		} catch (GitAPIException e1) {
			throw new RepositoryAddException("Unable to add file or directory: " 
					+ e1.getMessage(), e1);
		}
		
	
	}


	/* (non-Javadoc)
	 * @see net.orpiske.ssps.spm.repository.Provider#commit(java.io.File)
	 */
	@Override
	public void commit(final File file, final String message) throws RepositoryCommitException, RepositoryAccessException {
		Repository repository = accessRepository(file);
		
		Git git = new Git(repository);
		CommitCommand pullCommand = git.commit();
		
		
		try {
			pullCommand.setMessage(message).call();
		} catch (NoHeadException e) {
			throw new RepositoryCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (NoMessageException e) {
			throw new RepositoryCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (UnmergedPathsException e) {
			throw new RepositoryCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (ConcurrentRefUpdateException e) {
			throw new RepositoryCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (WrongRepositoryStateException e) {
			throw new RepositoryCommitException("Unable to commit: " + e.getMessage(), e);
		} catch (GitAPIException e) {
			throw new RepositoryCommitException("Unable to commit: " + e.getMessage(), e);
		}
		
		
	}

}
