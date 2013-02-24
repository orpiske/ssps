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
package net.orpiske.sdm.adm.stages;

import java.util.List;

import net.orpiske.sdm.adm.StageProcessor;
import net.orpiske.sdm.adm.exceptions.RuleException;
import net.orpiske.sdm.adm.exceptions.StageException;
import net.orpiske.ssps.adm.AbstractRule;
import net.orpiske.ssps.adm.PrepareStage;

import net.orpiske.sdm.adm.util.PrintUtils;

/**
 * Implements the prepare stage
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class PrepareStageProcessor extends StageProcessor<PrepareStage>{
	
	@Override
	public void run(PrepareStage stage) throws StageException {
		PrintUtils.printStartStage("Prepare");
		
		List<Object> rules = stage.getEchoOrMkdirOrCopy();
		
		try {
			for (Object rule : rules) {
				
				super.processRules(rule);
			}
		}
		catch (RuleException e) {
			PrintUtils.printEndWithError("Prepare", e);
			throw new StageException(e.getMessage(), e);
		}	
		
		PrintUtils.printEndStage("Prepare");
	}

}
