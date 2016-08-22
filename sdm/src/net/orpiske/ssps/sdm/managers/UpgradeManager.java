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
import java.util.List;

import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;
import net.orpiske.ssps.sdm.update.Upgradeable;

public class UpgradeManager {
	private RegistryManager registryManager;
	
	public UpgradeManager() throws DatabaseInitializationException {
		registryManager = new RegistryManager();
	}
	
	
	public void upgrade(final SoftwareInventoryDto dto) throws RegistryException, 
			DatabaseInitializationException, PackageNotFound, EngineException, SQLException {
			
		UpdateManager updateManager = new UpdateManager();
		UninstallManager uninstallManager = new UninstallManager();
		InstallationManager installationManager = new InstallationManager(null, false);
		
			
		PackageInfo info = updateManager.getLatest(dto);
	
		if (info == null) {
			throw new PackageNotFound(dto.getName());
		}
		
		
		uninstallManager.uninstall(dto);
		installationManager.install(info, false);
		
		
	}
	
	public void upgrade(final String groupId, final String packageName, 
			final String version) throws RegistryException, 
			DatabaseInitializationException, PackageNotFound, MultipleInstalledPackages, 
			EngineException, SQLException {
			
		SoftwareInventoryDto dto = (new InventoryUtils(registryManager))
				.getInstalledRecord(groupId, packageName, version);
		
		upgrade(dto);
	}
	
	
	public void upgrade() throws RegistryException, DatabaseInitializationException, 
			PackageNotFound, EngineException, SQLException {
		UpdateManager updateManager = new UpdateManager();
		List<Upgradeable> list = updateManager.getAllNewerPackages();
	
		for (Upgradeable up : list) {
			if (up.hasUpdates()) { 
				upgrade(up.getDto());
			}
		}
	}

}
