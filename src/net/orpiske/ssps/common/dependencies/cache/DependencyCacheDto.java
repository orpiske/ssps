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
package net.orpiske.ssps.common.dependencies.cache;

import net.orpiske.ssps.common.version.Version;

/**
 * Dependency cache DTO
 */
public class DependencyCacheDto {
	private String groupId;
	private String name;
	private Version version;
	private String repository;
	private String dependencyQualifiedName;
	private String dependencyVersionRange;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getDependencyQualifiedName() {
		return dependencyQualifiedName;
	}

	public void setDependencyQualifiedName(String dependencyQualifiedName) {
		this.dependencyQualifiedName = dependencyQualifiedName;
	}

	public String getDependencyVersionRange() {
		return dependencyVersionRange;
	}

	public void setDependencyVersionRange(String dependencyVersionRange) {
		this.dependencyVersionRange = dependencyVersionRange;
	}
}
