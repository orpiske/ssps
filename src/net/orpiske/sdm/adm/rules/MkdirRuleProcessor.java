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
package net.orpiske.sdm.adm.rules;

import java.io.File;

import net.orpiske.sdm.adm.exceptions.RuleException;
import net.orpiske.ssps.adm.MkdirRule;
import net.orpiske.ssps.common.variables.VariablesParser;

import net.orpiske.sdm.adm.util.PrintUtils;

/**
 * Implements the mkdir rule
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class MkdirRuleProcessor extends AbstractRuleProcessor {
	private VariablesParser admVariables = VariablesParser.getInstance();
	
	public void run(MkdirRule rule) throws RuleException {
		String directory = admVariables.evaluate(rule.getDir());
		
		File dir = new File(directory);
		
		if (!dir.exists()) { 
			dir.mkdirs();
		}
		else {
			PrintUtils.printInfo("Directory " + directory + " already exists");
			
		}
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#run(java.lang.Object)
	 */
	@Override
	public void run(Object rule) throws RuleException {
		run((MkdirRule) rule);
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#cleanup(java.lang.Object)
	 */
	@Override
	protected void cleanup(Object rule) throws RuleException {
		// NOP
		
	}

}
