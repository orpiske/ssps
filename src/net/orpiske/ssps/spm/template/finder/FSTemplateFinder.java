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
package net.orpiske.ssps.spm.template.finder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.PropertiesConfiguration;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.spm.template.Template;
import net.orpiske.ssps.spm.utils.Utils;

public class FSTemplateFinder implements TemplateFinder {
	
	private static final PropertiesConfiguration config = ConfigurationWrapper.getConfig();
	
	private File templateDir;
	
	public FSTemplateFinder() {
		setTemplateDirectory(null);
	}
	
	public FSTemplateFinder(final String templateDir) {
		setTemplateDirectory(templateDir);
	}
	
	private String getTemplateDirectoryPath(final String dir) {
		String path; 
		
		if (dir == null) {
			path = config.getString("sdm.template.dir");
			
			if (path == null) {
				path = Utils.getSpmDirectoryPath() + File.separator + "templates";
			}
		}
		else {
			path = dir;
		}
		
		return path;
	}
	
	private void setTemplateDirectory(final String dir) {
		String path = getTemplateDirectoryPath(dir);
		templateDir = new File(path);
		
	}

	public File find(String name) throws IOException {
		File ret = new File(templateDir, name);
		
		if (!ret.exists()) {
			throw new IOException("The file " + ret.getPath() + " does not exist");
		}
		else {
			if (!ret.canRead()) {
				throw new IOException("The file " + ret.getPath() + " is not readable");
			}
		}
		
		return ret;
	}

}
