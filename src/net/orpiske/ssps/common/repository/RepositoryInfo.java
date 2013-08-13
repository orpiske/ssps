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

import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

/**
 * Holds repository information
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class RepositoryInfo {
	private String name;
	private String url;
	private String userName;
	private String password;
	
	private String localPath;

    private String repositoryVersion;

    private Object payload;

	
	public RepositoryInfo(final String name) {
		this.name = name;
	}
	
	/**
	 * Gets the repository name
	 * @return the repository name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Gets the repository URL
	 * @return the repository URL
	 */
	public String getUrl() {
		return url;
	}

	
	/**
	 * Sets the repository URL
	 * @param url the repository URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	
	/**
	 * Gets the user name used to access the repository
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	
	/**
	 * Sets the user name used to access the repository
	 * @param userName the user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	/**
	 * Gets the password used to access the repository
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	
	/**
	 * Sets the password used to access the repository
	 * @param password the password used to access the repository
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
	/**
	 * Gets the payload information
	 * @return the payload information
	 */
	public Object getPayload() {
		return payload;
	}

	
	/**
	 * Sets the payload information
	 * @param payload the payload information
	 */
	public void setPayload(Object payload) {
		this.payload = payload;
	}


	/**
	 * Gets the local path to the repository
	 * @return the path
	 */
	public String getLocalPath() {
		
		if (localPath == null) { 
			localPath = RepositoryUtils.getUserRepository() + File.separator + name;
		}
		
		return localPath;
	}


    /**
     * Gets the repository version (aka branch) to use. For SVN, the branch is part of
     * the URL, hence this field is not needed: just pass the whole URL.
     *
     * @return The repository version or null if head/trunk/master
     */
    public String getRepositoryVersion() {
        return repositoryVersion;
    }


    /**
     * Sets the repository version (aka branch) to use. For SVN, the branch is part of
     * the URL, hence this field is not needed: just pass is via the URL.
     *
     * @param repositoryVersion The repository version or null if head/trunk/master
     */
    public void setRepositoryVersion(final String repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }
}
