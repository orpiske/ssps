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
import org.ssps.spm.dbm.DbmDocument;

/**
 * Adds support for variables in the DBM file
 * 
 * TODO: this class is way too simple. Needs refactoring. Also needs to stop 
 * using the DbmDocument and use the Dbm class
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class VariableSupport {

	/**
	 * Parses a text string
	 * @param input
	 * @param document
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String parse(final String input, final DbmDocument document) {
		Velocity.init();

		VelocityContext context = new VelocityContext();
		
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,
				org.apache.velocity.runtime.log.NullLogSystem.class);

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
