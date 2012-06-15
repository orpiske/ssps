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

import java.io.File;

import net.orpiske.ssps.adm.MkdirRule;

import org.ssps.sdm.adm.AdmVariables;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.adm.util.PrintUtils;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class MkdirRuleProcessor extends AbstractRuleProcessor {
	private AdmVariables admVariables = AdmVariables.getInstance();
	
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

}
