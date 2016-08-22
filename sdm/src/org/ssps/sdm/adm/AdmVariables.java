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
package org.ssps.sdm.adm;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


/**
 * This class implements the support for variables within the ADM document
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class AdmVariables {
	
	private static AdmVariables instance;
	private VelocityContext context;
	
	
	private AdmVariables() {
		context = new VelocityContext();
		
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,
				org.apache.velocity.runtime.log.NullLogSystem.class);
	}
	
	public void register(final String key, final String value) {
		context.put(key, value);
	}
	
	public String evaluate(final String input) {
		if (input == null) {
			return null;
		}
		
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "adm.variables", input);
		
		return writer.toString();
	}
	
	public static AdmVariables getInstance() {
		if (instance == null) {
			instance = new AdmVariables();
		}
		
		return instance;
	}
}
