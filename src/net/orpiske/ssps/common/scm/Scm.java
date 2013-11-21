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
package net.orpiske.ssps.common.scm;

import net.orpiske.ssps.common.scm.exceptions.*;

import java.io.File;

/**
 * SCM Interface
 */
public interface Scm {

	/**
	 * Set the login credentials
	 * @param credentials the login credentials
	 */
	void setCredentials(final ScmCredentials credentials);

	/**
	 * Sets the branch to work with
	 * @param branch the branch name
	 */
	void setBranch(final ScmBranch branch);

	/**
	 * Check-out data from a repository
	 * @param url the URL as a String object
	 * @param path the path to checkout to
	 */
	void checkout(final String url, File path) throws ScmCheckoutException, DuplicateCheckoutException;

	/**
	 * Update data in a local repository
	 * @param path the path as a String object
	 */
	void update(final String path) throws ScmUpdateException;


	/**
	 * Adds the file to the repository
	 * @param file the file to add
	 * @throws FileAddException
	 * @throws ScmAccessException if unable to access the SCM
	 */
	void add(File file) throws FileAddException, ScmAccessException;


	/**
	 * Commits the file to the repository
	 * @param file the file to commit
	 * @param message the commit message
	 * @throws FileCommitException
	 * @throws ScmAccessException if unable to access the SCM
	 */
	void commit(File file, String message) 	throws FileCommitException, ScmAccessException;

}
