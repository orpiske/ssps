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

import java.io.File;

import net.orpiske.sdm.engine.Engine;
import net.orpiske.sdm.engine.GroovyEngine;
import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.derby.DerbyManagerFactory;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.cache.PackageCacheDao;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;
import net.orpiske.ssps.common.dependencies.Dependency;

import org.apache.commons.io.FileUtils;

import java.sql.SQLException;
import java.util.List;

public class UninstallManager {
	
	private RegistryManager registryManager;
	private DerbyDatabaseManager databaseManager;
	private PackageCacheDao dao;
	
	public UninstallManager() throws DatabaseInitializationException {
		registryManager = new RegistryManager();
		databaseManager = DerbyManagerFactory.newInstance();
		dao = new PackageCacheDao(databaseManager);
	}
	
	private void runUninstallScript(PackageInfo packageInfo) throws EngineException {
		if (packageInfo == null) {
			System.err.println("Package not found in the repository. Skipping script " +
					"cleanup routines");
			
			return;
		}
		
		Engine engine = new GroovyEngine();
		File file = new File(packageInfo.getPath());
		
		engine.runUninstall(file);
	}
	
	private void runUninstallScript(final String groupId, final String packageName, 
			final String version) throws EngineException, SQLException
	{
		List<PackageInfo> packages = dao.getByNameAndGroupAndVersion(groupId, packageName,
				version);
		
		if (packages != null && packages.size() > 0) {
			PackageInfo packageInfo = packages.get(0);

			runUninstallScript(packageInfo);
		}
		
	}
	
	public void uninstall(final SoftwareInventoryDto dto) throws EngineException, RegistryException, SQLException {
		File file = new File(dto.getInstallDir());
		
		if (!file.exists()) {
			System.err.println("The package " + dto.getName() + " is marked as installed "
					+ "but the installation dir could not be found");
		}
		else {
			runUninstallScript(dto.getGroupId(), dto.getName(), dto.getVersion().toString());
			
			System.out.print("\rRemoving the package files from " + dto.getInstallDir() 
					+ "...");
			FileUtils.deleteQuietly(file);
			System.out.println("\rRemoving the package files from " + dto.getInstallDir() 
					+ "... Done");
		}
		
		System.out.print("\nRemoving package from the registry ...");
		registryManager.delete(dto);
		System.out.println("\nRemoving package from the registry ... done");
	}

	private PackageInfo getPackage(final String groupId, final String packageName, 
								   final String version) throws SQLException
	{
		List<PackageInfo> packages = dao.getByNameAndGroupAndVersion(groupId, 
				packageName, version);
		

		if (packages.size() == 0) {
			System.err.printf("Unable to calculate dependencies because there is no "
					+ "package file for %s%n", 
					RepositoryUtils.getFQPN(groupId, packageName, version));
			return null;
		}
		else {
			if (packages.size() > 1) {
				System.err.println("Unable to resolve dependencies because there are %i "
						+ "packages with the same name. The package will be removed but " 
						+ "you will have to remove the dependencies one by one");
				
				return null;
			}
		}
		
		return packages.get(0);
	}


	public void uninstall(final String groupId, final String packageName, 
			final String version, boolean deep) throws RegistryException, MultipleInstalledPackages, PackageNotFound, EngineException, SQLException, DatabaseInitializationException {
		SoftwareInventoryDto dto = (new InventoryUtils(registryManager))
					.getInstalledRecord(groupId, packageName, version);
				
		
		if (dto == null) {
			throw new PackageNotFound(packageName);
		}
		
		if (deep) {
			DependencyManager dependencyManager = new DependencyManager();
			PackageInfo packageInfo = getPackage(groupId,packageName,version);
			
			if (packageInfo != null) {
				Dependency dependency = dependencyManager.resolve(packageInfo);
				
				
				for (Dependency removableDependency : dependency.getDependencies()) {
					PackageInfo removablePackage = removableDependency.getPackageInfo();
					
					try { 
						uninstall(removablePackage.getGroupId(), removablePackage.getName(),
							removablePackage.getVersion().toString(), false);
					}
					catch (PackageNotFound pnfException) {
						System.err.println(pnfException.getMessage() + " (ignored)");
					}
				}
			}
		}
		
		uninstall(dto);
	}

}
