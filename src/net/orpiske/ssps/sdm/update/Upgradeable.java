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
package net.orpiske.ssps.sdm.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.version.slot.Slot;
import net.orpiske.ssps.common.version.slot.SlotComparatorFactory;

import org.apache.commons.configuration.PropertiesConfiguration;

public class Upgradeable {
	private static final PropertiesConfiguration config = 
			ConfigurationWrapper.getConfig();
	
	private SoftwareInventoryDto dto; 
	private List<PackageInfo> candidates = new ArrayList<PackageInfo>();
	
	
	public Upgradeable(final SoftwareInventoryDto dto) {
		this.dto = dto;
	}
	
	private String getSlot(final PackageInfo packageInfo) {
		String slot = packageInfo.getSlot();
		
		if (slot == null) {
			slot = config.getString("default.package.slot", 
					SlotComparatorFactory.DEFAULT_SLOT);
		}
		
		return slot;
	}
	
	
	public void addCandidate(final PackageInfo packageInfo) {	
		String slotMask = getSlot(packageInfo);
		Slot slot = new Slot(slotMask);
		
		if (slot.fits(dto.getVersion(), packageInfo.getVersion())) { 
			int result = dto.getVersion().compareTo(packageInfo.getVersion());
			
			if (result == -1) {
				candidates.add(packageInfo);
			}
		}	
	}
	
	public SoftwareInventoryDto getDto() {
		return dto;
	}


	public void setDto(SoftwareInventoryDto dto) {
		this.dto = dto;
	}


	public List<PackageInfo> getCandidates() {
		return candidates;
	}


	public void setCandidates(List<PackageInfo> candidates) {
		this.candidates = candidates;
	}
	
	
	public PackageInfo getLatest() {
		Collections.sort(candidates);
		
		if (candidates.size() == 0) {
			return null;
		}
		
		return candidates.get(candidates.size() - 1);
	}


	public boolean hasUpdates() {
		if (candidates.size() > 0) {
			return true;
		}
		
		return false;
	}

}
