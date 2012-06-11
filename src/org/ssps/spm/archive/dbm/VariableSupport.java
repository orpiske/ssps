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
package org.ssps.spm.archive.dbm;

import java.io.StringWriter;

import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class VariableSupport {

	public static String parse(final String input, final DbmDocument document) {
		Velocity.init();

		VelocityContext context = new VelocityContext();

		String baseDir = FilenameUtils.getFullPath(document.getPath());

		context.put("basedir", baseDir);

		context.put("name", document.getProjectName());
		context.put("version", document.getProjectVersion());

		StringWriter w = new StringWriter();
		Velocity.evaluate(context, w, "variables.parse", input);

		String tmp = w.toString();
		return tmp;
	}

}
