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

/**
 * Abstracts package information from the repositories
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public final class PackageInfo {
	
	public enum PackageType {
		BINARY,
		SOURCE,
	}
	
	private String groupId;
	private String name;
	private String version;
	private PackageType packageType = PackageInfo.PackageType.BINARY;
	private String path;
	
	private String repository;
	
	/**
	 * Gets the package group id
	 * @return the group id
	 */
	public String getGroupId() {
		return groupId;
	}
	
	/**
	 * Sets the package group id
	 * @param groupId the group id
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * Sets the package name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the package name
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the package version
	 * @return the package version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the package version
	 * @param version the version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Gets the package type
	 * @return the type
	 */
	public PackageType getPackageType() {
		return packageType;
	}
	
	/**
	 * Sets the package type
	 * @param packageType the package type
	 */
	public void setPackageType(PackageType packageType) {
		this.packageType = packageType;
	}
	
	/**
	 * Gets the package path (ie.: the path to the .groovy file in the file system)
	 * @return
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Sets the package path
	 * @param path the path (ie.: the path to the .groovy file in the file system)
	 */
	public void setPath(String path) {
		this.path = path;
	}

	
	/**
	 * Gets the name of the repository which contains/should contain the package 
	 * @return the name of the repository
	 */
	public String getRepository() {
		return repository;
	}

	
	/**
	 * Sets the name of the repository which contains/should contain the package 
	 * @return the name of the repository
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	@Override
	public String toString() {
		return "PackageInfo [groupId=" + groupId + ", name=" + name
				+ ", version=" + version + ", packageType=" + packageType
				+ ", path=" + path + ", repository=" + repository + "]";
	}
	
	
	
}
