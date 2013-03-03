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
package net.orpiske.ssps.common.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract access to a database
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public interface DatabaseManager {
	
	/**
	 * Commits the transaction data to the database
	 * @throws SQLException
	 */
	void commit() throws SQLException;
	
	
	/**
	 * Rollbacks the transaction data
	 * @throws SQLException
	 */
	void rollback() throws SQLException;
	 
	
	/**
	 * Gets the underlying (SQL) connection object
	 * @return The connection object
	 */
	Connection getConnection();
	
	

}
