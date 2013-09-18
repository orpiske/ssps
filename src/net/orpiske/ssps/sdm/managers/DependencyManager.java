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

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.derby.DerbyManagerFactory;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.dependencies.Dependency;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.cache.PackageCacheDao;
import net.orpiske.ssps.common.repository.utils.PackageUtils;
import net.orpiske.ssps.common.version.Range;
import net.orpiske.ssps.common.version.Version;

import org.apache.log4j.Logger;

public class DependencyManager {
	private static final Logger logger = Logger.getLogger(DependencyManager.class);
	
	public DependencyManager() {
		
	}
	
	
	private PackageInfo resolve(final String groupId, final String packageName, 
			final String versionRange) throws SQLException, DatabaseInitializationException {
		Range range = Range.toRange(versionRange);
		
		DerbyDatabaseManager databaseManager = DerbyManagerFactory.newInstance();
		PackageCacheDao dao = new PackageCacheDao(databaseManager);

		List<PackageInfo> packages = dao.getByNameAndGroup(groupId, packageName);
		
		Collections.sort(packages);
		
		for (int i = packages.size(); i != 0; i--) {
			PackageInfo packageInfo = packages.get(i - 1);
			
			
			Version packageVersion = packageInfo.getVersion();
			int comparison = packageVersion.compareTo(range.getMaximumVersion());
			
			if (comparison <= 0) {
				comparison = packageVersion.compareTo(range.getMinimumVersion());
				
				if (comparison >= 0) {
					return packageInfo;
				}
			}
		}
		
		return null;
	}
	
	public Dependency resolve(final PackageInfo packageInfo) throws SQLException, 
			DatabaseInitializationException 
	{
		HashMap<String, String> map;
		
		Dependency dependency = new Dependency(packageInfo);
		
		map = packageInfo.getDependencies();
		if (map == null) {
			return dependency;
		}
		
		for (Entry<String, String> entry : map.entrySet()) { 
			String packageFqdn = entry.getKey();
			String versionRange = entry.getValue();
			
			String groupId = PackageUtils.getGroupId(packageFqdn);
			String packageName = PackageUtils.getPackageName(packageFqdn);
			
			PackageInfo dependencyPackageInfo = resolve(groupId, packageName, 
					versionRange);
			
			if (dependencyPackageInfo == null) {
				logger.warn("The package for " + groupId + "/" + packageName 
						+ " wasn't found"); 
			}
			else { 			
				Dependency dependencyPackage = resolve(dependencyPackageInfo);
				
				dependency.addDependency(dependencyPackage);
			}
		}
		
		return dependency;
	}

}
