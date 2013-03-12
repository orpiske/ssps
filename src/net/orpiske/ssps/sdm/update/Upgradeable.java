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
import java.util.List;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.version.ComparisonStrategy;
import net.orpiske.ssps.common.version.DefaultVersionComparator;
import net.orpiske.ssps.common.version.VersionComparator;
import net.orpiske.ssps.common.version.slot.SlotComparator;
import net.orpiske.ssps.common.version.slot.SlotComparatorFactory;

import org.apache.commons.configuration.PropertiesConfiguration;

public class Upgradeable {
	private static final PropertiesConfiguration config = 
			ConfigurationWrapper.getConfig();
	
	private SoftwareInventoryDto dto; 
	private List<PackageInfo> candidates = new ArrayList<PackageInfo>();
	
	
	public Upgradeable(final SoftwareInventoryDto dto) {
		this.dto = dto;
		
		//String slot = dto.
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
		ComparisonStrategy strategy;
		
		String slot = getSlot(packageInfo);
		SlotComparator slotComparator = SlotComparatorFactory.create(slot);
		
		VersionComparator versionComparator = new DefaultVersionComparator();
		
		strategy = new ComparisonStrategy(slotComparator, versionComparator);
		
		int result = strategy.compare(dto.getVersion(), 
				packageInfo.getVersion());
		
		
		if (result == ComparisonStrategy.LESS_THAN) {
			candidates.add(packageInfo);
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


	

}
