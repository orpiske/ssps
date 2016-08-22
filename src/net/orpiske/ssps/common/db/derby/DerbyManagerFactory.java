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
package net.orpiske.ssps.common.db.derby;

import java.util.Properties;

import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.utils.Utils;

/**
 * A factory class for Derby database manager
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class DerbyManagerFactory {
	
	/**
	 * Restricted constructor
	 */
	private DerbyManagerFactory() {}
	
	/**
	 * Creates a new database manager instance
	 * @return a new database manager instance
	 * @throws DatabaseInitializationException
	 */
	public static DerbyDatabaseManager newInstance() throws DatabaseInitializationException {
		Properties props = System.getProperties();
		props.setProperty("derby.system.home", Utils.getSdmDirectoryPath());
		
		return new DerbyDatabaseManager("registry", props);
	}
	
}
