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

import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.version.VersionComparator;

public class Upgradeable {
	
	private SoftwareInventoryDto dto; 
	private List<PackageInfo> candidates = new ArrayList<PackageInfo>();
	
	public Upgradeable(final SoftwareInventoryDto dto) {
		this.dto = dto;
	}
	
	
	public void addCandidate(final PackageInfo packageInfo) {
		int result = VersionComparator.compareStatic(dto.getVersion(), 
				packageInfo.getVersion());
		
		if (result == VersionComparator.LESS_THAN) {
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
