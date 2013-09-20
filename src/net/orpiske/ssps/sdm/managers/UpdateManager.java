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
package net.orpiske.ssps.sdm.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.derby.DerbyManagerFactory;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.dependencies.cache.DependencyCacheDao;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.RepositoryManager;
import net.orpiske.ssps.common.repository.search.FileSystemRepositoryFinder;
import net.orpiske.ssps.common.repository.search.RepositoryFinder;
import net.orpiske.ssps.common.repository.search.cache.PackageCacheDao;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;
import net.orpiske.ssps.sdm.update.Upgradeable;
import net.orpiske.ssps.common.dependencies.Dependency;
import net.orpiske.ssps.common.dependencies.cache.DependencyCacheDto;
import net.orpiske.ssps.common.dependencies.cache.DependencyCacheDao;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class UpdateManager {
	
	private RegistryManager registryManager;
	private RepositoryManager repositoryManager = new RepositoryManager();

	private DerbyDatabaseManager databaseManager;
	private PackageCacheDao dao;
	private DependencyCacheDao depCacheDao;
	
	public UpdateManager() throws DatabaseInitializationException {
		registryManager = new RegistryManager();

		databaseManager = DerbyManagerFactory.newInstance();
		dao = new PackageCacheDao(databaseManager);
		depCacheDao = new DependencyCacheDao(databaseManager);
	}
	
	public List<Upgradeable> getAllNewerPackages() throws RegistryException,
			DatabaseInitializationException, SQLException 
	{
		List<SoftwareInventoryDto> list;
		
		List<Upgradeable> ret = new ArrayList<Upgradeable>();
		
		list = registryManager.search();
		
		for (SoftwareInventoryDto dto : list) {
			Upgradeable up = new Upgradeable(dto);

			List<PackageInfo> packages = 
					dao.getByNameAndGroup(dto.getGroupId(), dto.getName());
			
			for (PackageInfo packageInfo: packages) {
				up.addCandidate(packageInfo);
			}
			
			ret.add(up);
		}
		
		return ret;
	}
	
	
	public PackageInfo getLatest(final SoftwareInventoryDto dto) throws PackageNotFound, 
			DatabaseInitializationException, SQLException 
	{
		List<PackageInfo> packages = dao.getByNameAndGroup(dto.getGroupId(), dto.getName());
		
		if (packages.size() == 0) {
			throw new PackageNotFound(dto.getName());
		}

		Upgradeable up = new Upgradeable(dto);
		for (PackageInfo packageInfo: packages) {
			up.addCandidate(packageInfo);
		}

		return up.getLatest();
	}

	
	public void update(String...repositories) throws DatabaseInitializationException, SQLException {
		if (repositories == null) {
			repositoryManager.update();
		}
		else {
			repositoryManager.update(repositories);
		}
		
		rebuildCache(repositories);
	}
	
	
	public void rebuildCache(String...repositories) throws SQLException {
		if (repositories == null) {
			RepositoryFinder finder =  new FileSystemRepositoryFinder();
			List<PackageInfo> packages = finder.allPackages();

			dao.deleteAll();
			depCacheDao.deleteAll();
			for (PackageInfo packageInfo: packages) {
				dao.insert(packageInfo);

				depCacheDao.insert(packageInfo);
			}

		}
		else {
			for (String repository: repositories) {
				RepositoryFinder finder =  new FileSystemRepositoryFinder(repository);

				List<PackageInfo> packages = finder.allPackages();

				dao.deleteByRepository(repository);
				depCacheDao.deleteByRepository(repository);
				for (PackageInfo packageInfo: packages) {
					dao.insert(packageInfo);
					depCacheDao.insert(packageInfo);
				}
			}
		}
	}
}
