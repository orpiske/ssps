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
import java.util.List;

import net.orpiske.sdm.engine.Engine;
import net.orpiske.sdm.engine.GroovyEngine;
import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.dependencies.Dependency;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.FileSystemRepositoryFinder;
import net.orpiske.ssps.common.repository.search.RepositoryFinder;
import net.orpiske.ssps.common.version.Version;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;
import net.orpiske.ssps.sdm.managers.exceptions.TooManyPackages;

public class InstallationManager {
	
	private RegistryManager registryManager;
	private String[] phases;
	private boolean nodeps;

	
	public InstallationManager(String[] phases, boolean nodeps) throws DatabaseInitializationException {
		registryManager = new RegistryManager();
		this.phases = phases;
		this.nodeps = nodeps;
	}
	

	private List<PackageInfo> checkRepositoryCollision(final String groupId, 
			final String packageName, final String version) throws PackageNotFound, TooManyPackages
	{
		RepositoryFinder finder = new FileSystemRepositoryFinder();
		List<PackageInfo> packages = finder.find(groupId, packageName, version);
		
		if (packages.size() == 0) {
			throw new PackageNotFound(packageName);
		}
		else {
			if (packages.size() > 1) {
				throw new TooManyPackages(packageName, packages);
			}
		}
		return packages;
	}


	private void checkInventoryCollision(final String groupId, final String packageName, 
			final String version) throws RegistryException, MultipleInstalledPackages 
	{
		List<SoftwareInventoryDto> list = registryManager.search(packageName);
			
		for (SoftwareInventoryDto dto : list) {
			if (dto.getVersion().equals(Version.toVersion(version)) || version == null) {
				if (dto.getGroupId().equals(groupId) || groupId == null) {
					
					throw new MultipleInstalledPackages(packageName, list);
				}
			}
		}
	}
	
	
	private void checkInventoryCollision(final PackageInfo packageInfo) 
			throws RegistryException, MultipleInstalledPackages 
	{
		checkInventoryCollision(packageInfo.getGroupId(), packageInfo.getName(), 
				packageInfo.getVersion().toString());
	}
	
	public void install(PackageInfo packageInfo, boolean reinstall) throws EngineException, RegistryException  {
		File file = new File(packageInfo.getPath());
		Engine engine = new GroovyEngine();
		
		if (phases != null && phases.length > 0) {
			engine.run(file, phases);
		}
		else { 
			engine.run(file);
		}
		
		if (reinstall) {
			
			System.out.print("\rUpdating record into the registry ...");
			registryManager.reinstall(file);
			System.out.println("\rUpdating record into the registry ... done");
		}
		else {
			System.out.print("\rAdding record into the registry ...");
			registryManager.register(file);
			System.out.println("\rAdding record into the registry ... Done");
		}
	}
	
	private void installDependency(PackageInfo packageInfo, boolean reinstall) throws EngineException, RegistryException {
		System.out.println("Checking if package " + packageInfo.fqn() + " is installed");
		
		try {
			checkInventoryCollision(packageInfo);
		} 
		catch (MultipleInstalledPackages e) {
			if (!reinstall) { 
				System.out.println("Package " + packageInfo.fqn() + " is already installed");
				return;
			}
		}
		
		
		install(packageInfo, reinstall);
	}
	
	private void installDependencies(Dependency dependency, boolean reinstall) throws EngineException, RegistryException {
		
		for (Dependency subDependency : dependency.getDependencies()) {
			installDependencies(subDependency, reinstall);
		}
		
		installDependency(dependency.getPackageInfo(), reinstall);
	}
	
	
	public void install(final String groupId, final String packageName, 
			final String version, boolean reinstall) throws PackageNotFound, TooManyPackages, RegistryException, MultipleInstalledPackages, EngineException 
		{
		List<PackageInfo> packages = checkRepositoryCollision(groupId, packageName, version);
		
		
		try { 
			checkInventoryCollision(groupId, packageName, version);
		}
		catch (MultipleInstalledPackages me) {
			if (!reinstall) {
				throw me;
			}
		}

		PackageInfo packageInfo = packages.get(0);
		
		if (nodeps) {
			install(packageInfo, reinstall);
		}
		else {		
			DependencyManager dependencyManager = new DependencyManager();
			Dependency dependency = dependencyManager.resolve(packageInfo);
			installDependencies(dependency, reinstall);
		}
	}

}
