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

import net.orpiske.ssps.adm.Adm;
import net.orpiske.ssps.adm.CleanupStage;
import net.orpiske.ssps.adm.PrepareStage;
import net.orpiske.ssps.adm.SetupStage;
import net.orpiske.ssps.adm.ValidateStage;
import net.orpiske.ssps.adm.VerifyStage;
import net.orpiske.ssps.repository.Repository;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ssps.common.variables.VariablesParser;
import org.ssps.sdm.adm.exceptions.AdmException;
import org.ssps.sdm.adm.exceptions.StageException;
import org.ssps.sdm.adm.stages.CleanupStageProcessor;
import org.ssps.sdm.adm.stages.PrepareStageProcessor;
import org.ssps.sdm.adm.stages.SetupStageProcessor;
import org.ssps.sdm.adm.stages.ValidateStageProcessor;
import org.ssps.sdm.adm.stages.VerifyStageProcessor;
import org.ssps.sdm.adm.util.PrintUtils;
import org.ssps.sdm.utils.WorkdirUtils;

/**
 * Process the Artifact Deployment Module (ADM) rules
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class AdmProcessor {
	private static final Logger logger = Logger.getLogger(AdmProcessor.class);
	
	private Adm adm;
	private String repositoryPath;
	
	/**
	 * Constructor
	 * @param adm The ADM object
	 */
	public AdmProcessor(final Adm adm, final String repositoryPath) {
		this.adm = adm;
		this.repositoryPath = repositoryPath;
		
		registerVariables();
	}
	
	
	private void registerVariables() {
		if (logger.isTraceEnabled()) {
			logger.trace("Registering variables");
		}
		
		VariablesParser admVariables = VariablesParser.getInstance();
		
		String name = adm.getArtifact().getName();
		admVariables.register("name", name);
		
		String version = adm.getArtifact().getVersion();
		admVariables.register("version", version);
		
		admVariables.register("repository", repositoryPath);
	}
	
	public void process() throws AdmException {
		PrintUtils.printStartStage("Main");
		try {
			prepareStage();
			validateStage();
			setupStage();
			verifyStage();
			cleanupStage();
		} catch (StageException e) {
			throw new AdmException("Error while processing ADM document: "
					+ e.getMessage(), e);
		}
		PrintUtils.printEndStage("Main");
	}
	

	/**
	 * Runs the prepare stage rules
	 * @throws StageException If there are errors in the rules
	 */
	private void prepareStage() throws StageException {
		PrepareStageProcessor processor = new PrepareStageProcessor();
		PrepareStage stage = adm.getStages().getPrepare();
		
		if (stage != null) {
			processor.run(stage);
		}
	}
	
	
	/**
	 * Runs the validate stage rules
	 * @throws StageException If there are errors in the rules
	 */
	private void validateStage() throws StageException {
		ValidateStageProcessor processor = new ValidateStageProcessor();
		ValidateStage stage = adm.getStages().getValidate();
			
		if (stage != null) {
			processor.run(stage);
		}
	}
	
	
	/**
	 * Runs the setup stage rules
	 * @throws StageException If there are errors in the rules
	 */
	private void setupStage() throws StageException {
		SetupStageProcessor processor = new SetupStageProcessor();
		SetupStage stage = adm.getStages().getSetup();
		
		if (stage != null) {
			processor.run(stage);
		}
	}
	
	
	/**
	 * Runs the verify stage rules
	 * @throws StageException If there are errors in the rules
	 */
	private void verifyStage() throws StageException {
		VerifyStageProcessor processor = new VerifyStageProcessor();
		VerifyStage stage = adm.getStages().getVerify();
			
		if (stage != null) {
			processor.run(stage);
		}
	}
	

	/**
	 * Runs the cleanup stage rules
	 * @throws StageException If there are errors in the rules
	 */
	private void cleanupStage() throws StageException {
		CleanupStageProcessor processor = new CleanupStageProcessor();
		CleanupStage stage = adm.getStages().getCleanup();
			
		if (stage != null) {
			processor.run(stage);
		}
	}	
}