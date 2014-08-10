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

import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.utils.Utils;
import net.orpiske.ssps.common.version.Version;
import net.orpiske.ssps.common.version.slot.SlotComparatorFactory;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * Repository utilities
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class RepositoryUtils {
	
	private static final Logger logger = Logger.getLogger(RepositoryUtils.class);
	
	private static String REPOSITORIES = "repositories";
	private static String DEFAULT_REPOSITORY_NAME = "default";
	
	
	/**
	 * Gets the path to the package file
	 * @param dir the package directory (ie.: $HOME/.sdm/repositories/default/net.orpiske/sdm/1.0.0/pkg)
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
	 * Gets the path to the user repository (by default $HOME/.sdm/repositories)
	 * @return the path to the user repository
	 */
	public static String getUserRepository() {
		return Utils.getSdmDirectoryPath() + File.separator + REPOSITORIES;
	}
	
	
	/**
	 * Gets a File object pointing to the user repository (by default $HOME/.sdm/repositories)
	 * @return a File object that points to the user repository
	 */
	public static File getUserRepositoryFile() {
		String userRepositoryPath = getUserRepository();
		
		return new File(userRepositoryPath);
	}
	
	
	/**
	 * Gets the path to the user repository (by default $HOME/.sdm/repositories)
	 * @return the path to the user repository
	 */
	public static String getUserDefaultRepository() {
		return Utils.getSdmDirectoryPath() + File.separator + REPOSITORIES + File.separator 
				+ DEFAULT_REPOSITORY_NAME;
	}
	
	private static void readPackageProperties(final File file, final PackageInfo packageInfo) {
		File settingsFile = new File(file.getPath() + File.separator 
				+ "package.properties");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Reading package properties for " + file.getPath());
			logger.debug("Trying to open package.properties at " + settingsFile.getPath());
		}
		
		
		if (settingsFile.exists() && settingsFile.canRead()) { 
			PropertiesConfiguration packageSettings;
			try {
				packageSettings = new PropertiesConfiguration(settingsFile);
				
				String slot = packageSettings.getString("package.default.slot", 
						SlotComparatorFactory.DEFAULT_SLOT);
				
				packageInfo.setSlot(slot);
			} catch (ConfigurationException e) {
				logger.warn("Unable to read package configuration file at " 
						+ settingsFile.getPath());
				
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
		}
		else {
			packageInfo.setSlot(SlotComparatorFactory.DEFAULT_SLOT);
		}
		
	}
	
	
	public static PackageInfo readPackageInfo(final File file) {
		PackageInfo packageInfo = new PackageInfo();
		
		
		String baseName = FilenameUtils.getBaseName(file.getName());
		packageInfo.setName(baseName);
		
		packageInfo.setPath(file.getPath());
		
		File typeDir = file.getParentFile();
						
		File versionDir = typeDir.getParentFile();
        String version = versionDir.getName();
		packageInfo.setVersion(Version.toVersion(version));
		
		
		File packageNameDir = versionDir.getParentFile();
		readPackageProperties(packageNameDir, packageInfo);
		String packageName = packageNameDir.getName(); 
		
		if (!packageName.equals(baseName)) {
			logger.warn("The package file '" + baseName + ".groovy' and the " +
					"repository package dir '" + packageName + "' doesn't match. " + 
					"This can lead to problems");
		}
		
		
		File groupIdDir = packageNameDir.getParentFile();
		String groupId = groupIdDir.getName();
		packageInfo.setGroupId(groupId);
		
		File repositoryDir = groupIdDir.getParentFile().getParentFile();
		String repository = repositoryDir.getName();
		packageInfo.setRepository(repository);
		
		return packageInfo;
	}

}
