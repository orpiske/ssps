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
package org.ssps.spm.dbm;

import java.io.File;

import org.apache.log4j.Logger;
import org.ssps.common.variables.VariablesParser;
import org.ssps.spm.dbm.include.IncludeProcessor;
import org.ssps.spm.dbm.include.IncludeProcessorFactory;

import net.orpiske.ssps.dbm.Dbm;
import net.orpiske.ssps.dbm.Include;

/**
 * Processes DBM information
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public final class DbmProcessor {
	private static final Logger logger = Logger.getLogger(DbmProcessor.class);
	
	public static final String DEFAULT_SOURCE_DIRECTORY = "${basedir}/src/dbm";
	public static final String DEFAULT_OUTPUT_DIRECTORY = "${basedir}/target/installroot";
	public static final String DEF_DELIVERABLE_OUTPUT_DIRECTORY = "${basedir}/target";
	
	private Dbm dbm;
	private IncludeProcessor processor;
	
	public DbmProcessor(final Dbm dbm) throws DbmException {
		logger.trace("Creating a new DBM document processor");
		this.dbm = dbm;
		
		
		Include include = dbm.getInclude();
		if (include != null) {
			processor = IncludeProcessorFactory.getProcessor(include);
		}
		
		registerVariables();
	}
	
	
	private void registerVariables() {
		logger.trace("Registering variables");
		VariablesParser variablesParser = VariablesParser.getInstance();
		
		variablesParser.register("name", getProjectName());
		variablesParser.register("version", getProjectVersion());
	}
	
	
	/**
	 * Gets the project group
	 * @return the project group
	 */
	public String getProjectGroup() {
		String ret = null;
		Include include = dbm.getInclude();
		
		if (include != null) {
			String expr = include.getGroupExpression().getExpression();
			
			ret = processor.getValue(expr);
		}
		else {
			ret = dbm.getProject().getGroup();
		}
		
		 
		return ret;
	}
	
	
	/**
	 * Gets the project name
	 * @return the project name
	 */
	public String getProjectName() {
		String ret = null;
		Include include = dbm.getInclude();
		
		if (include != null) {
			String expr = include.getNameExpression().getExpression();
			
			ret = processor.getValue(expr);
		}
		else {
			ret = dbm.getProject().getName();
		}
		
		 
		return ret;
	}
	
	/**
	 * Gets the project version
	 * @return the project version
	 */
	public String getProjectVersion() {
		String ret = null;
		Include include = dbm.getInclude();
		
		if (include != null) {
			String expr = include.getVersionExpression().getExpression();
			
			ret = processor.getValue(expr);
		}
		else {
			ret = dbm.getProject().getVersion();
		}
		
		 
		return ret;
	}
	
	/**
	 * Gets the repository user
	 * @return
	 */
	public String getRepositoryUser() {
		return dbm.getRepository().getUsername();
	}
	
	/**
	 * Gets the repository password
	 * @return
	 */
	public String getRepositoryPassword() {
		return dbm.getRepository().getPassword();
	}
	
	/**
	 * Gets the repository URL
	 * @return
	 */
	public String getRepositoryUrl() {
		return dbm.getRepository().getUrl();
	}
	
	/**
	 * Gets the source directory
	 * @return the source directory
	 */
	public String getBuildSourceDirectory() {
		String ret = dbm.getBuild().getSourceDirectory();

		if (ret == null) {
			ret = DEFAULT_SOURCE_DIRECTORY;
		}
		
		return VariablesParser.getInstance().evaluate(ret);
	}

	
	/**
	 * Gets the build output directory
	 * @return the build output directory
	 */
	public String getBuildOutputDirectory() {
		String ret = dbm.getBuild().getOutputDirectory();

		if (ret == null) {
			ret = DEFAULT_OUTPUT_DIRECTORY;
		}

		return VariablesParser.getInstance().evaluate(ret);
	}

	
	/**
	 * Gets the build artifact
	 * @return
	 */
	public String getBuildArtifact() {
		String ret = dbm.getBuild().getArtifactPath();

		if (ret != null) {
			ret = VariablesParser.getInstance().evaluate(ret);
		}

		return ret;
	}
	
	/**
	 * Gets the name of the deliverable
	 * @return
	 */
	public String getDeliverableName() {
		return getProjectName() + "-" + getProjectVersion();
	}
	
	/**
	 * Gets the deliverable output directory
	 * @return
	 */
	public String getDeliverableOutputDirectory() {
		return VariablesParser.getInstance().evaluate(DEF_DELIVERABLE_OUTPUT_DIRECTORY);
	}
	
	
	/**
	 * Gets the full path to the deliverable
	 * @return
	 */
	public String getDeliverableFullPath() {
		return getDeliverableOutputDirectory() + File.separator 
				+ getDeliverableName() + ".ugz";
	}

}
