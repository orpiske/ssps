package org.ssps.sdm.adm.stages;

import java.util.List;

import net.orpiske.ssps.adm.AbstractRule;
import net.orpiske.ssps.adm.PrepareStage;

import org.apache.log4j.Logger;
import org.ssps.sdm.adm.StageProcessor;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.adm.exceptions.StageException;

public class PrepareStageProcessor extends StageProcessor<PrepareStage>{
	private static final Logger logger = Logger.getLogger(PrepareStageProcessor.class);
	
	public void run(PrepareStage stage) throws StageException {
		List<AbstractRule> rules = stage.getEchoOrMkdirOrCopy();
		
		logger.debug("Processing the prepare stage");
		try {
			for (Object rule : rules) {
				
				super.processRules(rule);
			}
		}
		catch (RuleException e) {
			e.printStackTrace();
		}	
	}

}
