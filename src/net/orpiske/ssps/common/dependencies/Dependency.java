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
package net.orpiske.ssps.common.dependencies;

import java.util.ArrayList;
import java.util.List;

import net.orpiske.ssps.common.repository.PackageInfo;

/**
 * Abstracts the dependency graph
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Dependency {
	
	private PackageInfo packageInfo;	
	private List<Dependency> dependencies = new ArrayList<Dependency>();
	
	
	/**
	 * Constructor
	 * @param packageInfo package information
	 */
	public Dependency(final PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	
	/**
	 * Gets the package information for this dependency
	 * @return the package information object
	 */
	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	
	/**
	 * Gets a list of dependencies for this dependency
	 * @return A list object
	 */
	public List<Dependency> getDependencies() {
		return dependencies;
	}
	

	/**
	 * Whether the given dependency was declared already or not
	 * @param dependency the dependency to check
	 * @return true if declared or false otherwise
	 */
	public boolean declared(final Dependency dependency) {
		if (dependencies.contains(dependency)) {
			return true;
		}
			
		
		for (Dependency dep : dependencies) {
			if (dep.declared(dependency)) {
				return true;
			}
		}
			
		return false;
	}

	
	/**
	 * Adds a new new dependency to this dependency
	 * @param dependency the dependency to add (it must not be already declared)
	 */
	public void addDependency(final Dependency dependency) {
		if (!declared(dependency)) { 
			dependencies.add(dependency);
		}
	}
}
