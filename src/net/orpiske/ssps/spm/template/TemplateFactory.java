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
package net.orpiske.ssps.spm.template;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import net.orpiske.ssps.spm.template.exceptions.TemplateException;
import net.orpiske.ssps.spm.template.exceptions.TemplateNotFound;
import net.orpiske.ssps.spm.template.finder.FSTemplateFinder;
import net.orpiske.ssps.spm.template.finder.TemplateFinder;

public class TemplateFactory {

	public static Template create(final String name) throws TemplateException, TemplateNotFound {
		PackageProperties properties = null;
		TemplateFinder finder = new FSTemplateFinder();
		File file = null;
		
		try {
			file = finder.find(name);
			properties = PackageInfoLoader.read(file.getParentFile());
		} catch (ConfigurationException e) {
			throw new TemplateException("The template settings for " + name + 
					" is invalid", e);
		} catch (IOException e) {
			throw new TemplateNotFound(name);
		}
		
		Template template = new Template();
		
		template.setPackageProperties(properties);
		template.setTemplateFile(file);
		
		return template;
	}
}
