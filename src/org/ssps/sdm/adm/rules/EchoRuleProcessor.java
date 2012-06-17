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

import net.orpiske.ssps.adm.EchoRule;

import org.ssps.sdm.adm.AdmVariables;
import org.ssps.sdm.adm.exceptions.RuleException;


/**
 * Implements the echo rule
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class EchoRuleProcessor extends AbstractRuleProcessor {
	
	private AdmVariables admVariables = AdmVariables.getInstance();
	
	private void run(EchoRule rule) {
		String level = rule.getLevel();
		
		if (level == null) {
			level = "info";
		}
		
		String message = admVariables.evaluate(rule.getMessage());
		
		if (message == null) {
			message = "null";
		}
		
		System.out.println("[" + level.toUpperCase() + "]: " + message);
	}

	@Override
	public void run(Object rule) throws RuleException {
		run((EchoRule) rule);
	}

}
