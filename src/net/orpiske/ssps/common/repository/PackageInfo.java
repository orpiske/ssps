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

import java.util.LinkedHashMap;

import net.orpiske.ssps.common.version.Version;

/**
 * Abstracts package information from the repositories
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public final class PackageInfo implements Comparable<PackageInfo> {
	
	public enum PackageType {
		BINARY,
		SOURCE,
	}
	
	private String groupId;
	private String name;
	private Version version;
	private PackageType packageType = PackageInfo.PackageType.BINARY;
	private String path;
	private String url;
	private String slot;
	private LinkedHashMap<String, String> dependencies;
	
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
	public Version getVersion() {
		return version;
	}
	
	/**
	 * Sets the package version
	 * @param version the version
	 */
	public void setVersion(Version version) {
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
	
	/**
	 * Gets the package download URL
	 * @return the package download URL
	 */
	public String getUrl() {
		return url;
	}

	
	/**
	 * Sets the package download URL
	 * @param url the package download URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	
	/**
	 * Gets the package slot
	 * @return the package version slot
	 */
	public String getSlot() {
		return slot;
	}

	
	/**
	 * Sets the package version slot
	 * @param slot the package version slot
	 */
	public void setSlot(String slot) {
		this.slot = slot;
	}

	

	@Override
	public String toString() {
		return "PackageInfo [groupId=" + groupId + ", name=" + name
				+ ", version=" + version + ", packageType=" + packageType
				+ ", path=" + path + ", url=" + url + ", slot=" + slot
				+ ", dependencies=" + dependencies + ", repository="
				+ repository + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PackageInfo obj) {
		
		if (obj == null) {
			throw new NullPointerException("Cannot compare package information to a null "
					+ "object");
		}
		
		if (obj == this) {
			return 0;
		}
		
		if (getGroupId() == null) {
			if (obj.getGroupId() != null) {
				return -1;
			}
		}
		else {
			int groupIdDiff = getGroupId().compareTo(obj.getGroupId());
			
			if (groupIdDiff != 0) {
				return groupIdDiff;
			}
		}
		
		if (getName() == null) {
			if (obj.getName() != null) {
				return -1;
			}
		}
		else {
			int nameDiff = getName().compareTo(obj.getName());
			
			if (nameDiff != 0) {
				return nameDiff;
			}
		}
		
		
		if (getVersion() == null) {
			if (obj.getVersion() != null) {
				return -1;
			}
		}
		
		return getVersion().compareTo(obj.getVersion());
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
 		
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		
		
		PackageInfo other = (PackageInfo) obj;
		
		if (getGroupId() == null) {
			if (other.getGroupId() != null) {
				return false;
			}
		}
		
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		}
		
		
		if (getVersion() == null) {
			if (other.getVersion() != null) {
				return false;
			}
		}
		
		return true;
	}

	public LinkedHashMap<String, String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(LinkedHashMap<String, String> dependencies) {
		this.dependencies = dependencies;
	}
}
