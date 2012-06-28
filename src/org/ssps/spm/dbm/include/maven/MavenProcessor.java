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
package org.ssps.spm.dbm.include.maven;

import net.orpiske.ssps.dbm.Include;

import org.apache.log4j.Logger;
import org.ssps.spm.dbm.DbmException;
import org.ssps.spm.dbm.include.IncludeProcessor;

/**
 * This class process maven include files into the DBM
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class MavenProcessor implements IncludeProcessor {
	
	private static final Logger logger = Logger.getLogger(MavenProcessor.class);
	
	private MavenDocument mavenDocument;
	
	public MavenProcessor() {
		logger.trace("Creating a new MavenInclude processor");
	}
	
	
	/* (non-Javadoc)
	 * @see org.ssps.spm.dbm.IncludeProcessor#load(java.lang.String)
	 */
	public void load(final Include include) throws DbmException {
		try { 
			mavenDocument = new MavenDocument(include.getIncludeFile());
		}
		catch (Exception e) {
			throw new DbmException(e.getMessage(), e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.ssps.spm.dbm.IncludeProcessor#getValue(java.lang.String)
	 */
	public String getValue(String expr) {
		if (mavenDocument == null) {
			logger.warn("Trying to read values from a unitiliazed document");
			
			return null;
		}
		
		return mavenDocument.findValue(expr);
		
	}
}
