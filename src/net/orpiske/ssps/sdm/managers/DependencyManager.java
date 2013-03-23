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
package net.orpiske.ssps.sdm.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.utils.PackageUtils;

public class DependencyManager {
	private static final Logger logger = Logger.getLogger(DependencyManager.class);
	
	private List<PackageInfo> dependencies = new ArrayList<PackageInfo>();
	
	public DependencyManager() {
		
	}
	
	public List<PackageInfo> resolve(final PackageInfo packageInfo) {
		HashMap<String, String> map = null;
		
		map = packageInfo.getDependencies();
		for (Entry<String, String> dependency : map.entrySet()) { 
			String packageFqdn = dependency.getKey();
			String versionRange = dependency.getValue();
			
			String groupId = PackageUtils.getGroupId(packageFqdn);
			String packageName = PackageUtils.getPackageName(packageFqdn);
			
			// logger.debug("Resolving " + groupId + "/" + packageName);
			System.out.println("Group ID: " + groupId);
			System.out.println("Package name: " + packageName);
			System.out.println("Version range: " + versionRange);
		}
		
		
		return null;
	}

}
