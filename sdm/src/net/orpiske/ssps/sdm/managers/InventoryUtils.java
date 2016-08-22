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

import java.util.List;

import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.version.Version;
import net.orpiske.ssps.sdm.managers.exceptions.MultipleInstalledPackages;


/**
 * Utilities to simplify working with the repository
 */
public final class InventoryUtils {
	private RegistryManager registryManager;

	/**
	 * Constructor
	 * @param registryManager the registry manager
	 */
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
			if (dto.getVersion().equals(Version.toVersion(version)) || version == null) {
				if (dto.getGroupId().equals(groupId) || groupId == null) {
					return dto;
				}
			}
		}
		
		return null;
	}
}
