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
package net.orpiske.ssps.common.repository.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * Repository utilities
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class RepositoryUtils {
	
	private static String REPOSITORIES = "repositories";
	private static String DEFAULT_REPOSITORY_NAME = "default";
	
	
	/**
	 * Gets the path to the package file
	 * @param dir the package directory (ie.: $HOME/.sdm/repository/net.orpiske/sdm/1.0.0/pkg)
	 * @param packageName the name of the package
	 * @return The complete path to the package file
	 */
	public static String getPackageFilePath(final String dir, final String packageName) {
		return dir + File.separator + packageName + ".groovy";
	}
	
	/**
	 * Gets the package directory
	 * @param repositoryPath the repository path
	 * @param packageFQPN the fully qualified package name
	 * @return the path to the package directory
	 */
	public static String getPackageDir(final String repositoryPath, 
			final String packageFQPN) {
		return repositoryPath + File.separator + packageFQPN + 	File.separator + "pkg";
	}
	
	
	/**
	 * Gets the fully qualified package name
	 * @param groupId the group id
	 * @param packageName the package name
	 * @param version the version
	 * @return A string object with the FQPN
	 */
	public static String getFQPN(final String groupId, final String packageName, 
			final String version) {
		return groupId + File.separator + packageName + File.separator + 
				version;
		
	}
	
	
	/**
	 * Gets the path to the user repository (by default $HOME/.sdm/repository)
	 * @return the path to the user repository
	 */
	public static String getUserRepository() {
		return FileUtils.getUserDirectoryPath() + File.separator 
				+ ".sdm" + File.separator + REPOSITORIES;
	}
	
	
	/**
	 * Gets the path to the user repository (by default $HOME/.sdm/repository)
	 * @return the path to the user repository
	 */
	public static String getUserDefaultRepository() {
		return FileUtils.getUserDirectoryPath() + File.separator 
				+ ".sdm" + File.separator + REPOSITORIES + File.separator 
				+ DEFAULT_REPOSITORY_NAME;
	}

}
