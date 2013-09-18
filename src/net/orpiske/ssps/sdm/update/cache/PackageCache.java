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
package net.orpiske.ssps.sdm.update.cache;

import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.derby.DerbyManagerFactory;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.cache.PackageCacheDao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 9/18/13
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class PackageCache {

	private List<PackageInfo> packages;
	
	
	public PackageCache(List<PackageInfo> packages) {
		this.packages = packages;
	}
	

	public void updateCache(final String repository) throws DatabaseInitializationException, SQLException {
		DerbyDatabaseManager databaseManager = DerbyManagerFactory.newInstance();
		PackageCacheDao dao = new PackageCacheDao(databaseManager);

		dao.deleteByRepository(repository);
		for (PackageInfo packageInfo : packages) {
			System.out.println("Adding " + packageInfo + " to the cache");
			dao.insert(packageInfo);
		}
	}
}
