package net.orpiske.ssps.sdm.managers;

import java.util.List;

import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;

public class InventoryUtils {
	private RegistryManager registryManager;
	
	public InventoryUtils(RegistryManager registryManager)  {
		this.registryManager = registryManager;
	}
	
	
	public SoftwareInventoryDto getInstalledRecord(final String groupId, final String packageName, 
			final String version) throws RegistryException, MultipleInstalledPackages {
		List<SoftwareInventoryDto> list = registryManager.search(packageName);
		
		if (list.size() == 1) {
			return list.get(0);
		}
		
	
		if (list.size() > 1) {
			if (version == null || groupId == null) {
				throw new MultipleInstalledPackages(packageName, list);
			}
		}

		
		for (SoftwareInventoryDto dto : list) {
			if (dto.getVersion().equals(version) || version == null) {
				if (dto.getGroupId().equals(groupId) || groupId == null) {
					return dto;
				}
			}
		}
		
		return null;
	}
}
