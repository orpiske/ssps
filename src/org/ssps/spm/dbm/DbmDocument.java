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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import net.orpiske.ssps.dbm.Dbm;

import org.ssps.common.variables.VariablesParser;
import org.ssps.common.xml.XmlParserUtils;
import org.ssps.common.xml.exceptions.XmlDocumentException;

/**
 * Abstracts the Deliverable Build Model (DBM) document
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class DbmDocument  {
	public static final String DEFAULT_SOURCE_DIRECTORY = "${basedir}/src/dbm";
	public static final String DEFAULT_OUTPUT_DIRECTORY = "${basedir}/target/installroot";
	public static final String DEF_DELIVERABLE_OUTPUT_DIRECTORY = "${basedir}/target";

	private String path;
	private Dbm dbm;
	
	/*
	 * 
	 * 
		String baseDir = FilenameUtils.getFullPath(document.getPath());

		context.put("basedir", baseDir);

		context.put("name", document.getProjectName());
		context.put("version", document.getProjectVersion());
	 */

	/**
	 * Constructor
	 * @param path path to the DBM document
	 * @throws XmlDocumentException if the DBM document is incorrect/invalid
	 * @throws FileNotFoundException if the DBM document was not found
	 */
	public DbmDocument(final String path) throws XmlDocumentException, FileNotFoundException {
		this.setPath(path);

		try {
			InputStream stream = new FileInputStream(path);

			dbm = XmlParserUtils.unmarshal(Dbm.class, stream);
			
			
			
			
		} catch (JAXBException e) {
			throw new XmlDocumentException("Unable to umarhsall document", e);
		}
	}
	
	private void registerVariables() {
		VariablesParser variablesParser = VariablesParser.getInstance();
		
		variablesParser.register("basedir", getPath());
		variablesParser.register("name", getProjectName());
		variablesParser.register("version", getProjectVersion());
	}

	/**
	 * Gets the path to the DBM document
	 * @return
	 */
	public String getPath() {
		return path;
	}

	
	private void setPath(String path) {
		this.path = path;
	}

	
	/**
	 * Gets the project group
	 * @return the project group
	 */
	@Deprecated
	public String getProjectGroup() {
		return dbm.getProject().getGroup();
	}

	
	/**
	 * Gets the project name
	 * @return the project name
	 */
	@Deprecated
	public String getProjectName() {	
		return dbm.getProject().getName();
	}

	
	
	/**
	 * Gets the project version
	 * @return the project version
	 */
	@Deprecated
	public String getProjectVersion() {
		return dbm.getProject().getVersion();
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
	@Deprecated
	public String getDeliverableName() {
		return getProjectName() + "-" + getProjectVersion();
	}
	
	
	/**
	 * Gets the full path to the deliverable
	 * @return
	 */
	@Deprecated
	public String getDeliverableFullPath() {
		return getDeliverableOutputDirectory() + File.separator 
				+ getDeliverableName() + ".ugz";
	}

	
	/**
	 * Gets the deliverable output directory
	 * @return
	 */
	@Deprecated
	public String getDeliverableOutputDirectory() {
		return VariablesParser.getInstance().evaluate(DEF_DELIVERABLE_OUTPUT_DIRECTORY);
	}

	/**
	 * Gets the repository user
	 * @return
	 */
	@Deprecated
	public String getRepositoryUser() {
		return dbm.getRepository().getUsername();
	}

	
	/**
	 * Gets the repository password
	 * @return
	 */
	@Deprecated
	public String getRepositoryPassword() {
		return dbm.getRepository().getPassword();
	}

	
	/**
	 * Gets the repository URL
	 * @return
	 */
	@Deprecated
	public String getRepositoryUrl() {
		return dbm.getRepository().getUrl();
	}
}