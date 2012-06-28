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

import net.orpiske.ssps.dbm.Include;

import org.ssps.spm.dbm.DbmException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public interface IncludeProcessor {

	/**
	 * Loads an include file
	 * @param include the include
	 * @throws DbmException if the included file cannot be read
	 */
	void load(final Include include) throws DbmException;
	
	
	/**
	 * Gets a value from the include file
	 * @param expr The expression used to obtain the value from the included 
	 * file
	 * @return the value or null if not found
	 */
	String getValue(final String expr);
}
