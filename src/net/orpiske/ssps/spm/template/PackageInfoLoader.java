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
package net.orpiske.ssps.spm.template;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
class PackageInfoLoader {
	
	private static PackageProperties read(PropertiesConfiguration properties) {
		PackageProperties ret = new PackageProperties(); 
		
		String groupId = properties.getString("package.groupid");
		ret.setGroupId(groupId);
		
		String name = properties.getString("package.name");
		ret.setName(name);
		
		String type = properties.getString("package.type");
		ret.setType(type);
		
		return ret;
	}
	
	public static PackageProperties read(File dir) throws ConfigurationException {
		File propertiesFile = new File(dir, "template.properties");
		PropertiesConfiguration properties = null;
		
		properties = new PropertiesConfiguration(propertiesFile);
		return read(properties);
	}

}
