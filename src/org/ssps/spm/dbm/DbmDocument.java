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

import org.apache.log4j.Logger;
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
	private static final Logger logger = Logger.getLogger(DbmDocument.class);
	
	public static final String DEFAULT_SOURCE_DIRECTORY = "${basedir}/src/dbm";
	public static final String DEFAULT_OUTPUT_DIRECTORY = "${basedir}/target/installroot";
	public static final String DEF_DELIVERABLE_OUTPUT_DIRECTORY = "${basedir}/target";

	private String path;
	private Dbm dbm;
	private DbmProcessor dbmProcessor;
	
	/**
	 * Constructor
	 * @param path path to the DBM document
	 * @throws XmlDocumentException if the DBM document is incorrect/invalid
	 * @throws FileNotFoundException if the DBM document was not found
	 * @throws DbmException If the DBM file is invalid and/or includes cannot 
	 * be read
	 */
	public DbmDocument(final String path) throws XmlDocumentException, FileNotFoundException, DbmException {
		this.setPath(path);

		try {
			logger.trace("Opening file " + path);
			InputStream stream = new FileInputStream(path);

			dbm = XmlParserUtils.unmarshal(Dbm.class, stream);
			
			registerVariables();
			
			setDbmProcessor(new DbmProcessor(dbm));
			
		} catch (JAXBException e) {
			throw new XmlDocumentException("Unable to umarhsall document: " 
					+ e.getMessage(), e);
		} 
	}
	
	private void registerVariables() {
		logger.trace("Registering variables");
		VariablesParser variablesParser = VariablesParser.getInstance();
		
		variablesParser.register("basedir", "./");
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

	public DbmProcessor getDbmProcessor() {
		return dbmProcessor;
	}

	public void setDbmProcessor(DbmProcessor dbmProcessor) {
		this.dbmProcessor = dbmProcessor;
	}
}