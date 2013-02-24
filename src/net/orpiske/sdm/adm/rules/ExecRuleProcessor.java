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

import java.io.IOException;

import net.orpiske.sdm.adm.exceptions.RuleException;
import net.orpiske.ssps.adm.ExecRule;
import net.orpiske.ssps.common.variables.VariablesParser;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class ExecRuleProcessor  extends AbstractRuleProcessor {
	
	private VariablesParser admVariables = VariablesParser.getInstance();
	
	public void run(ExecRule rule) throws RuleException {
		CommandLine cmd = new CommandLine(admVariables.evaluate(rule.getFile()));
		
		cmd.addArguments(rule.getArgs());
		
		DefaultExecutor executor = new DefaultExecutor();
		try {
			int exitValue = executor.execute(cmd);
			
			if (exitValue != 0) {
				throw new RuleException("The application did not run successfully");
			}
		} catch (ExecuteException e) {
			throw new RuleException("Unable to execute: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuleException("Input/output error: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#run(java.lang.Object)
	 */
	@Override
	public void run(Object rule) throws RuleException {
		run((ExecRule) rule);
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#cleanup(java.lang.Object)
	 */
	@Override
	protected void cleanup(Object rule) throws RuleException {
		// TODO Auto-generated method stub
		
	}

}
