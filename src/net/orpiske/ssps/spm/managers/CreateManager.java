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
package net.orpiske.ssps.spm.managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.orpiske.ssps.spm.template.PackageProperties;
import net.orpiske.ssps.spm.template.Template;
import net.orpiske.ssps.spm.template.TemplateFactory;
import net.orpiske.ssps.spm.template.engine.TemplateEngine;
import net.orpiske.ssps.spm.template.exceptions.TemplateException;
import net.orpiske.ssps.spm.template.exceptions.TemplateNotFound;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class CreateManager {
	
	public File getOuputFile(final Template template, final String repository, 
			final String version)
	{
		PackageProperties packageProperties = template.getPackageInfo();
		
		String outPath = repository + File.separator + "packages" + File.separator +
				packageProperties.getGroupId() + File.separator + 
				packageProperties.getName() + File.separator + 
				version + File.separator + "pkg" + File.separator + 
				packageProperties.getName() + ".groovy";
		
		File ret = new File(outPath);
		ret.mkdirs();
		
		return ret;
	}
	
	public void create(final String name, final String repository, final String version) throws TemplateException, TemplateNotFound, IOException {
		Template template = TemplateFactory.create(name);
		
		TemplateEngine engine = new TemplateEngine(template.getTemplateFile());
		File outFile = getOuputFile(template, repository, version);
		FileWriter writer = new FileWriter(outFile);
		
		engine.create(writer, template.getPackageInfo());
		
		writer.close();
	} 

}
