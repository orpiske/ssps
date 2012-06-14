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
package org.ssps.spm.archive.dbm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import net.orpiske.ssps.dbm.Dbm;

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

	public DbmDocument(final String path) throws XmlDocumentException, FileNotFoundException {
		this.setPath(path);

		try {
			InputStream stream = new FileInputStream(path);

			dbm = XmlParserUtils.unmarshal(Dbm.class, stream);
		} catch (JAXBException e) {
			throw new XmlDocumentException("Unable to umarhsall document", e);
		}
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	public String getProjectGroup() {
		return dbm.getProject().getGroup();
	}

	public String getProjectName() {	
		return dbm.getProject().getName();
	}

	public String getProjectVersion() {
		return dbm.getProject().getVersion();
	}

	public String getBuildSourceDirectory() {
		String ret = dbm.getBuild().getSourceDirectory();

		if (ret == null) {
			ret = DEFAULT_SOURCE_DIRECTORY;
		}

		return VariableSupport.parse(ret, this);
	}

	public String getBuildOutputDirectory() {
		String ret = dbm.getBuild().getOutputDirectory();

		if (ret == null) {
			ret = DEFAULT_OUTPUT_DIRECTORY;
		}

		return VariableSupport.parse(ret, this);
	}

	public String getBuildArtifact() {
		String ret = dbm.getBuild().getArtifactPath();

		if (ret != null) {
			ret = VariableSupport.parse(ret, this);
		}

		return ret;
	}

	public String getDeliverableName() {
		return getProjectName() + "-" + getProjectVersion();
	}
	
	public String getDeliverableFullPath() {
		return getDeliverableOutputDirectory() + File.separator 
				+ getDeliverableName() + ".ugz";
	}

	public String getDeliverableOutputDirectory() {
		return VariableSupport.parse(DEF_DELIVERABLE_OUTPUT_DIRECTORY, this);
	}

	public String getRepositoryUser() {
		return dbm.getRepository().getUsername();
	}

	public String getRepositoryPassword() {
		return dbm.getRepository().getPassword();
	}

	public String getRepositoryUrl() {
		return dbm.getRepository().getUrl();
	}
}