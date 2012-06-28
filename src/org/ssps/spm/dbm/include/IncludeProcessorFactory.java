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
package org.ssps.spm.dbm.include;

import org.ssps.spm.dbm.DbmException;
import org.ssps.spm.dbm.include.maven.MavenProcessor;

import net.orpiske.ssps.dbm.Include;
import net.orpiske.ssps.dbm.IncludeType;

/**
 * Factory for include processors
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public final class IncludeProcessorFactory {
	
	/**
	 * Restricted constructor
	 */
	private IncludeProcessorFactory() {}
	
	/**
	 * Gets the include processor for the include
	 * @param include the include
	 * @return the most appropriate processor for 'include
	 * @throws DbmException 
	 */
	public static IncludeProcessor getProcessor(final Include include) throws DbmException {
		IncludeProcessor ret = null;
		if (include.getIncludeType().equals(IncludeType.MAVEN)) {
			ret = new MavenProcessor();
			ret.load(include);
		}
		
		return ret;
	}
 
}
