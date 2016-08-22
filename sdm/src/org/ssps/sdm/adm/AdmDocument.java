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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import net.orpiske.ssps.adm.Adm;

import org.apache.commons.io.FilenameUtils;
import org.ssps.common.variables.VariablesParser;
import org.ssps.common.xml.XmlParserUtils;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.sdm.adm.exceptions.RuleEngineException;
import org.ssps.sdm.utils.WorkdirUtils;

/**
 * This class abstracts the ADM document
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class AdmDocument {
	private String path;
	
	private Adm admDocument;
	
	/**
	 * Constructor: this is used to process an input stream (ie.: when processing
	 * ADM files from the network)
	 * @param stream Input stream 
	 * @throws XmlDocumentException if unable to unmarshall the document
	 */
	public AdmDocument(final InputStream stream) throws XmlDocumentException {
		setPath(WorkdirUtils.getWorkDir());
		
		try {
			setDocument(XmlParserUtils.unmarshal(Adm.class, stream));
			
			registerVariables(path);
			
		} catch (JAXBException e) {
			throw new XmlDocumentException("Unable to unmarshall document", e);
		}
	}
	
	/**
	 * Constructor
	 * @param path ADM file path
	 * @throws XmlDocumentException
	 */
	public AdmDocument(final String path) throws XmlDocumentException {
		this.setPath(path);

		try {
			InputStream stream = new FileInputStream(path);

			setDocument(XmlParserUtils.unmarshal(Adm.class, stream));
			
			registerVariables(path);
			
		} catch (FileNotFoundException e) {
			throw new XmlDocumentException(e.getMessage(), e);
		} catch (JAXBException e) {
			throw new XmlDocumentException("Unable to unmarshall document", e);
		}
	}

	private void registerVariables(final String path) {
		VariablesParser admVariables = VariablesParser.getInstance();
		
		File baseDir = new File(path);
		
		try {
			admVariables.register("basedir", baseDir.getParentFile().getCanonicalPath());
		} catch (IOException e) {
			throw new RuleEngineException("Unable to register 'basedir' variable", 
					e);
		}
		
		String filename = FilenameUtils.getName(path);
		admVariables.register("filename", filename);
		
		String workDir = WorkdirUtils.getWorkDir();
		admVariables.register("workdir", workDir);
	}

	
	/**
	 * Sets the ADM file path
	 * @param path
	 */
	private void setPath(String path) {
		this.path = path;
	}


	
	/**
	 * Gets the ADM document object
	 * @return The ADM document object
	 */
	public Adm getDocument() {
		return admDocument;
	}

	
	/**
	 * Sets the ADM document object
	 * @param admDocument The ADM document object
	 */
	public void setDocument(Adm admDocument) {
		this.admDocument = admDocument;
	}

	
	
}
