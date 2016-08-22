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
package net.orpiske.ssps.sdm.managers.exceptions;

import java.util.List;

import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;

/**
 * Multiple installed packages exception
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class MultipleInstalledPackages extends SspsException {
	
	private List<SoftwareInventoryDto> softwareList;

	public MultipleInstalledPackages(String name) {
		super("Multiple installed packages found: " + name);
	}

	public MultipleInstalledPackages(String name, List<SoftwareInventoryDto> softwareList) {
		super("Multiple installed packages found: " + name);
		
		this.softwareList = softwareList;
	}

	public List<SoftwareInventoryDto> getSoftwareList() {
		return softwareList;
	}
}
