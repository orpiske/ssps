package net.orpiske.ssps.sdm.managers;

import static net.orpiske.ssps.sdm.utils.PrintUtils.printParseable;

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
	
	
	public void upgrade(final SoftwareInventoryDto dto) throws RegistryException, DatabaseInitializationException, PackageNotFound, EngineException {
			
		UpdateManager updateManager = new UpdateManager();
		UninstallManager uninstallManager = new UninstallManager();
		InstallationManager installationManager = new InstallationManager();
		
			
		PackageInfo info = updateManager.getLatest(dto);
	
		if (info == null) {
			throw new PackageNotFound(dto.getName());
		}
		
		
		uninstallManager.uninstall(dto);
		installationManager.install(info, false);
		
		
	}
	
	public void upgrade(final String groupId, final String packageName, 
			final String version) throws RegistryException, DatabaseInitializationException, PackageNotFound, MultipleInstalledPackages, EngineException {
			
		SoftwareInventoryDto dto = (new InventoryUtils(registryManager))
				.getInstalledRecord(groupId, packageName, version);
		
		upgrade(dto);
	}
	
	
	public void upgrade() throws RegistryException, DatabaseInitializationException, PackageNotFound, MultipleInstalledPackages, EngineException {
		UpdateManager updateManager = new UpdateManager();
		List<Upgradeable> list = updateManager.getAllNewerPackages();
	
		for (Upgradeable up : list) {
			if (up.hasUpdates()) { 
				upgrade(up.getDto());
			}
		}
	}

}
