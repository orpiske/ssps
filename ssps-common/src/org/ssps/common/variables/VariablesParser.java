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
package org.ssps.common.variables;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


/**
 * This class implements the support for variables within documents
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class VariablesParser {
	
	private static VariablesParser instance;
	private VelocityContext context;
	
	
	@SuppressWarnings("deprecation")
	private VariablesParser() {
		context = new VelocityContext();
		
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,
				org.apache.velocity.runtime.log.NullLogSystem.class);
	}
	
	/**
	 * Register a new variable
	 * @param key the variable name
	 * @param value the value for the variable
	 */
	public void register(final String key, final String value) {
		context.put(key, value);
	}
	
	/**
	 * Evalutes an input expression replacing variables (ie.: ${text}) with 
	 * previously registered values
	 * @param input the input string
	 * @return the text with the variables in itreplaced by their values
	 */
	public String evaluate(final String input) {
		if (input == null) {
			return null;
		}
		
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "variables.parser", input);
		
		return writer.toString();
	}
	
	
	/**
	 * Gets an instance of the object
	 * @return A instance of the VariablesParser object
	 */
	public static VariablesParser getInstance() {
		if (instance == null) {
			instance = new VariablesParser();
		}
		
		return instance;
	}
}
