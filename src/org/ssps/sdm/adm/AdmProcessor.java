package org.ssps.sdm.adm;

import org.apache.log4j.Logger;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.adm.exceptions.StageException;
import org.ssps.sdm.adm.stages.PrepareStageProcessor;

import net.orpiske.ssps.adm.Adm;
import net.orpiske.ssps.adm.PrepareStage;

/**
 * Process the Artifact Deployment Module (ADM) rules
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class AdmProcessor {
	private static final Logger logger = Logger.getLogger(AdmProcessor.class);
	private Adm adm;
	
	/**
	 * Constructor
	 * @param adm The ADM object
	 */
	public AdmProcessor(final Adm adm) {
		this.adm = adm;
	}
	
	

	/**
	 * Runs the prepare stage rules
	 */
	public void prepareStage() {
		logger.debug("Running the prepare stage");
		
		PrepareStageProcessor processor = new PrepareStageProcessor();
		PrepareStage stage = adm.getStages().getPrepare();
		
		try {
			processor.run(stage);
		} catch (StageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
