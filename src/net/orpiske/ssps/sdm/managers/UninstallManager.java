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
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.FileSystemRepositoryFinder;
import net.orpiske.ssps.common.repository.search.RepositoryFinder;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;

import org.apache.commons.io.FileUtils;

public class UninstallManager {
	
	private RegistryManager registryManager;
	
	public UninstallManager() throws DatabaseInitializationException {
		registryManager = new RegistryManager();
	}
	
	
	
	public void runUninstallScript(PackageInfo packageInfo) throws EngineException {
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
			final String version) throws EngineException {
		RepositoryFinder finder = new FileSystemRepositoryFinder();
		PackageInfo packageInfo = finder.findFirst(groupId, packageName, version);
		
		
		runUninstallScript(packageInfo);
	}
	
	public void uninstall(final SoftwareInventoryDto dto) throws EngineException, RegistryException {
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
	
	public void uninstall(final String groupId, final String packageName, 
			final String version) throws RegistryException, MultipleInstalledPackages, PackageNotFound, EngineException  {
		SoftwareInventoryDto dto = (new InventoryUtils(registryManager))
					.getInstalledRecord(groupId, packageName, version);
				
		
		if (dto == null) {
			throw new PackageNotFound(packageName);
		}
		
		uninstall(dto);
	}

}
