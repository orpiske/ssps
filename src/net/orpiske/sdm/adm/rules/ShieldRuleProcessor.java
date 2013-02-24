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
import java.io.IOException;

import net.orpiske.sdm.adm.exceptions.RuleException;
import net.orpiske.sdm.adm.rules.support.ShieldUtils;
import net.orpiske.ssps.adm.ShieldRule;
import net.orpiske.ssps.common.variables.VariablesParser;

import net.orpiske.sdm.adm.util.PrintUtils;

/**
 * Shield rule processor
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class ShieldRuleProcessor extends AbstractRuleProcessor {
	
	private VariablesParser admVariables = VariablesParser.getInstance();
	
	
	public void run(ShieldRule rule) throws RuleException {
		String resource = admVariables.evaluate(rule.getResource());
		
		File shieldedFile = new File(resource);
		
		if (!shieldedFile.exists()) {
			PrintUtils.printInfo("Resource " + resource + " does not exist");
			
			return;
		}
		
		File shield = new File(resource + ShieldUtils.SHIELD_EXT);
	
		if (!shield.exists()) {
			try {
				shield.createNewFile();
				
				PrintUtils.printInfo("Resource " + resource + " was shielded");
			} catch (IOException e) {
				throw new RuleException("Cannot create a shield for resource "
						+ resource);
			}
		}
		else {
			PrintUtils.printInfo("Resource " + resource + " already shielded");
		}
		
		shield.deleteOnExit();

	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#run(java.lang.Object)
	 */
	@Override
	public void run(Object rule) throws RuleException {
		run((ShieldRule) rule);
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#cleanup(java.lang.Object)
	 */
	@Override
	protected void cleanup(Object rule) throws RuleException {
		// NOP
		
	}

}
