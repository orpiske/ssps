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

import net.orpiske.ssps.adm.AbstractRule;
import net.orpiske.ssps.adm.AbstractStage;

import org.apache.log4j.Logger;
import org.ssps.sdm.adm.exceptions.RuleEngineException;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.adm.exceptions.StageException;
import org.ssps.sdm.adm.rules.AbstractRuleProcessor;
import org.ssps.sdm.adm.rules.RuleProcessorFactory;

/**
 * The stage processor abstracts part of the work required to process each of 
 * the stages in an ADM file
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 * @param <T>
 */
public abstract class StageProcessor<T extends AbstractStage> {
	
	private static Logger logger = Logger.getLogger(StageProcessor.class);
	
	protected void processRules(final Object object) throws RuleException {
		
		if (object instanceof AbstractRule) {
			if (logger.isTraceEnabled()) { 
				logger.trace("Processing object of type " + object.getClass());
			}
			
			AbstractRuleProcessor ruleProcessor = 
					RuleProcessorFactory.getRule(object.getClass());
			
			if (ruleProcessor == null) {
				throw new RuleEngineException("The engine does not have an " 
						+ "associated processor for " + object.getClass());
			}
			
			
			ruleProcessor.run(object);
		}
		else {
			if (object != null) {
				logger.error("Unknown object of type " + object.getClass());
			}
		}
	}

	public abstract void run(T stage) throws StageException;
}
