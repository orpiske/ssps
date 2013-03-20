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

import java.util.ArrayList;
import java.util.List;

import net.orpiske.sdm.registry.RegistryManager;
import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.search.FileSystemRepositoryFinder;
import net.orpiske.ssps.common.repository.search.RepositoryFinder;
import net.orpiske.ssps.sdm.managers.exceptions.PackageNotFound;
import net.orpiske.ssps.sdm.update.Upgradeable;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class UpdateManager {
	
	private RepositoryFinder finder;
	private RegistryManager registryManager;
	
	public UpdateManager() throws DatabaseInitializationException {
		finder = new FileSystemRepositoryFinder();
		registryManager = new RegistryManager();
	}
	
	public List<Upgradeable> getAllNewerPackages() throws RegistryException, DatabaseInitializationException {
		List<SoftwareInventoryDto> list = null;
		
		List<Upgradeable> ret = new ArrayList<Upgradeable>();
		
		list = registryManager.search();
		
		for (SoftwareInventoryDto dto : list) {
			Upgradeable up = new Upgradeable(dto);
			
			List<PackageInfo> packages = finder.find(dto.getGroupId(), 
					dto.getName(), null);
			
			for (PackageInfo packageInfo: packages) {
				up.addCandidate(packageInfo);
			}
			
			ret.add(up);
		}
		
		return ret;
	}
	
	
	
	public PackageInfo getLatest(final SoftwareInventoryDto dto) throws PackageNotFound {	
		Upgradeable up = new Upgradeable(dto);
		
		List<PackageInfo> packages = finder.find(dto.getGroupId(), 
					dto.getName(), null);
		
		if (packages.size() == 0) {
			throw new PackageNotFound(dto.getName());
		}
		
		for (PackageInfo packageInfo: packages) {
			up.addCandidate(packageInfo);
		}

		return up.getLatest();
	}

}
