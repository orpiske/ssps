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
package net.orpiske.sdm.registry;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.orpiske.sdm.registry.exceptions.RegistryException;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.derby.DerbyManagerFactory;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDao;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.utils.InstallDirUtils;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.log4j.Logger;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class RegistryManager {	
	private DerbyDatabaseManager databaseManager;
	SoftwareInventoryDao inventory;
	
	public RegistryManager() throws DatabaseInitializationException {
		databaseManager = DerbyManagerFactory.newInstance();
		inventory = new SoftwareInventoryDao(databaseManager);
	}
	
	public void register(File file) throws RegistryException {
		PackageInfo packageInfo = RepositoryUtils.readPackageInfo(file);
		
		SoftwareInventoryDto dto = new SoftwareInventoryDto();
		
		dto.setGroupId(packageInfo.getGroupId());
		dto.setName(packageInfo.getName());
		dto.setType("B");
		dto.setVersion(packageInfo.getVersion());
		dto.setInstallDate(new Date());
		
		String installdir = InstallDirUtils.getInstallDir();
		dto.setInstallDir(installdir + File.separator + packageInfo.getName() + "-" 
		+ packageInfo.getVersion());
		
		
		try {
			inventory.insert(dto);
		} catch (SQLException e) {
			throw new RegistryException("Unable to add new package to the inventory", e);
		}
	}
	
	public void reinstall(File file) throws RegistryException {
		PackageInfo packageInfo = RepositoryUtils.readPackageInfo(file);
		
		SoftwareInventoryDto dto = new SoftwareInventoryDto();
		
		dto.setGroupId(packageInfo.getGroupId());
		dto.setName(packageInfo.getName());
		dto.setType("B");
		dto.setVersion(packageInfo.getVersion());
		dto.setInstallDate(new Date());
		
		String installdir = InstallDirUtils.getInstallDir();
		dto.setInstallDir(installdir + File.separator + packageInfo.getName() + "-" 
		+ packageInfo.getVersion());
		
		
		try {
			SoftwareInventoryDto newDto = inventory.updateReinstalled(dto);
			
			if (newDto == null) {
				inventory.insert(dto);
			}
		} catch (SQLException e) {
			throw new RegistryException("Unable to add new package to the inventory", e);
		}
	}
	
	
	public List<SoftwareInventoryDto> search(final String name) throws RegistryException {
		List<SoftwareInventoryDto> list;
		
		try {
			list = inventory.getByName(name);
		} catch (SQLException e) {
			throw new RegistryException("Unable to search for " + name, e);
		}
		
		return list;
	}
	
	
	public List<SoftwareInventoryDto> search() throws RegistryException {
		List<SoftwareInventoryDto> list;
		
		try {
			list = inventory.getAll();
		} catch (SQLException e) {
			throw new RegistryException("Unable to search for all packages ", e);
		}
		
		return list;
	}
	
	
	public void delete(SoftwareInventoryDto dto) throws RegistryException {
		try {
			inventory.delete(dto);
		} catch (SQLException e) {
			throw new RegistryException("Unable to delete package: " + e.getMessage(), e);
		}
	}
	
	
	/**
	 * @throws DatabaseInitializationException
	 * @throws RegistryException
	 */
	public SoftwareInventoryDto searchRegistry(final String name, final String version, 
			final String groupId) throws RegistryException 
	{
		
		List<SoftwareInventoryDto> list = search(name);
		
		for (SoftwareInventoryDto dto : list) {
			if (!dto.getVersion().equals(version) && version != null) {
				continue;
			}
			
			if (!dto.getGroupId().equals(groupId) && groupId != null) {
				continue;
			}
			
			return dto;
		}
		
		return null;
		
	
	}
	

}
