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
package net.orpiske.sdm.packages;

import static net.orpiske.sdm.lib.net.Downloader.*;
import static net.orpiske.sdm.lib.Unpack.*;

import org.apache.log4j.Logger;


import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class BinaryPackage implements Package {
	private static final Logger logger = Logger.getLogger(BinaryPackage.class);

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#fetch(java.lang.String)
	 */
	@Override
	public void fetch(String url) {
		logger.info("Downloading " + url);
		
		try {
			download(url);
		} catch (ResourceExchangeException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#unpack(java.lang.String)
	 */
	@Override
	public void extract(String artifactName) {
		unpack(artifactName);
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#prepare(java.lang.String)
	 */
	@Override
	public void prepare(String artifactName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#build(java.lang.String)
	 */
	@Override
	public void build(String artifactName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#verify(java.lang.String)
	 */
	@Override
	public void verify(String artifactName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#install(java.lang.String)
	 */
	@Override
	public void install(String artifactName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#uninstall(java.lang.String)
	 */
	@Override
	public void uninstall(String artifactName) {
		// TODO Auto-generated method stub
		
	}
	
	


}
