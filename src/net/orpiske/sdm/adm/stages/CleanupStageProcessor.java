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
import net.orpiske.sdm.adm.util.PrintUtils;
import net.orpiske.ssps.adm.CleanupStage;

/**
 * Implements the cleanup stage
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class CleanupStageProcessor extends StageProcessor<CleanupStage>{
	
	@Override
	public void run(CleanupStage stage) throws StageException {
		PrintUtils.printStartStage("Cleanup");
		
		List<Object> rules = stage.getEchoOrMkdirOrCopy();

		try {
			for (Object rule : rules) {
				
				super.processRules(rule);
			}
		}
		catch (RuleException e) {
			PrintUtils.printEndWithError("Cleanup", e);
			throw new StageException("Rule error ", e);
		}
		
		PrintUtils.printEndStage("Cleanup");
	}

}
