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
package org.ssps.sdm.adm.rules;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;

import net.orpiske.ssps.adm.GroovyRule;

import org.codehaus.groovy.control.CompilationFailedException;
import org.ssps.common.variables.VariablesParser;
import org.ssps.sdm.adm.exceptions.RuleException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class GroovyRuleProcessor extends AbstractRuleProcessor {
	
	private VariablesParser admVariables = VariablesParser.getInstance();
	
	
	private File getFile(GroovyRule rule) {
		String path = admVariables.evaluate(rule.getFile());
		
		return new File(path);
	}
	
	
	private void run(GroovyRule rule) throws RuleException {
		ClassLoader parent = getClass().getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		
		Class<?> groovyClass;
		try {
			groovyClass = loader.parseClass(getFile(rule));
		} catch (CompilationFailedException e) {
			throw new RuleException("The script has errors: " + e.getMessage(),
					e);
		} catch (IOException e) {
			throw new RuleException("Input/output error: " + e.getMessage(),
					e);
		}

		GroovyObject groovyObject;
		try {
			groovyObject = (GroovyObject) groovyClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuleException("Unable to instantiate object: " 
					+ e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new RuleException("Illegal access: " + e.getMessage(),
					e);
		}
		
		groovyObject.invokeMethod("start", null);
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#run(java.lang.Object)
	 */
	@Override
	public void run(Object rule) throws RuleException {
		run((GroovyRule) rule);

	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#cleanup(java.lang.Object)
	 */
	@Override
	protected void cleanup(Object rule) throws RuleException {
		// NOOP
	}

}
