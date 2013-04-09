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
package net.orpiske.ssps.common.registry;

import java.util.Date;

import net.orpiske.ssps.common.version.Version;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class SoftwareInventoryDto {
	private String groupId;
	private String name;
	private Version version;
	private String type;
	private String installDir;
	private Date installDate;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(String value) {
		this.version = Version.toVersion(value);
	}
	
	public void setVersion(Version version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstallDir() {
		return installDir;
	}

	public void setInstallDir(String installDir) {
		this.installDir = installDir;
	}

	public Date getInstallDate() {
		if (installDate != null) {
			return new Date(installDate.getTime());
		}
		
		return null;
	}

	public void setInstallDate(Date installDate) {
		this.installDate = (installDate == null) ? null : new Date(installDate.getTime());
	}

}
