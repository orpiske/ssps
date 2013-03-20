package net.orpiske.ssps.sdm.managers;

import java.io.File;
import java.util.List;

import net.orpiske.sdm.engine.Engine;
import net.orpiske.sdm.engine.GroovyEngine;
import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.exceptions.SspsException;
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
	
	public InstallationManager() throws DatabaseInitializationException {
		registryManager = new RegistryManager();
	}
	
	/**
	 * @return
	 * @throws SspsException
	 */
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


	/**
	 * @return 
	 * @throws RegistryException
	 * @throws MultipleInstalledPackages 
	 * @throws SspsException
	 */
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
	
	public void install(PackageInfo packageInfo, boolean reinstall) throws EngineException, RegistryException  {
		File file = new File(packageInfo.getPath());
		Engine engine = new GroovyEngine();
		
		engine.run(file);
		
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
		install(packageInfo, reinstall);
	}

}
