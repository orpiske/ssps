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
package net.orpiske.ssps.spm.template.engine;

import java.io.File;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class TemplateEngine {
	private VelocityContext context;
	private Template template;
	
	public TemplateEngine(final File templateFile) {
		context = new VelocityContext();
		
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, 
				org.apache.velocity.runtime.log.NullLogSystem.class);
		
		Velocity.setProperty("resource.loader", "file");
		Velocity.setProperty("resource.loader.class", FileResourceLoader.class);
		
		Velocity.setProperty("file.resource.loader.path", templateFile.getParent());
	
		template = Velocity.getTemplate(templateFile.getName());
	}
	

	
	public void create(final Writer writer, final Object... arguments) {
		
		if (arguments == null || arguments.length == 0) {
			return;
		}
		
		for (Object obj : arguments) {
			context.put(obj.getClass().getName().toLowerCase(), obj);
		}
		
		template.merge(context, writer);
	}

}
