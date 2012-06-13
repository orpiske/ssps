package org.ssps.sdm.adm;

import net.orpiske.ssps.adm.AbstractRule;
import net.orpiske.ssps.adm.AbstractStage;

import org.apache.log4j.Logger;
import org.ssps.sdm.adm.exceptions.RuleEngineException;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.adm.exceptions.StageException;
import org.ssps.sdm.adm.rules.AbstractRuleProcessor;
import org.ssps.sdm.adm.rules.RuleProcessorFactory;

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
