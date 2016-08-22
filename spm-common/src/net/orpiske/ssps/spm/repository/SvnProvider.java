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

import net.orpiske.ssps.common.repository.RepositoryInfo;
import net.orpiske.ssps.spm.repository.exception.RepositoryAccessException;
import net.orpiske.ssps.spm.repository.exception.RepositoryAddException;
import net.orpiske.ssps.spm.repository.exception.RepositoryCommitException;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SvnProvider implements Provider {
	private RepositoryInfo repositoryInfo;
	private SVNClientManager clientManager;
	
	
	public SvnProvider(final RepositoryInfo repositoryInfo) {
		this.repositoryInfo = repositoryInfo;
		clientManager = SVNClientManager.newInstance();
		
		//clientManager = SVNClientManager.newInstance(null, userName, password)
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.spm.repository.Provider#add(java.io.File)
	 */
	@Override
	public void add(File file) throws RepositoryAddException,
			RepositoryAccessException {
		
		SVNWCClient wcClient = clientManager.getWCClient();
		
		try {
			wcClient.doAdd(file, false, true, false, true, false);
		} catch (SVNException e) {
			throw new RepositoryAddException("Unable to add file: " + e.getMessage(), e);
		}
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.spm.repository.Provider#commit(java.io.File, java.lang.String)
	 */
	@Override
	public void commit(File file, String message)
			throws RepositoryCommitException, RepositoryAccessException {
		SVNCommitClient client = clientManager.getCommitClient();
		
		try {
			client.doCommit(new File[] { file }, false, message, false, true);
		} catch (SVNException e) {
			throw new RepositoryCommitException("Unable to add file: " + e.getMessage(), e);
		}
	}

}
