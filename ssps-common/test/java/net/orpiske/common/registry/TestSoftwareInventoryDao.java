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
package net.orpiske.common.registry;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDao;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class TestSoftwareInventoryDao extends SoftwareInventoryDao {

	/**
	 * Constructor
	 * @param databaseManager A database manager object
	 * @throws DatabaseInitializationException
	 */
	public TestSoftwareInventoryDao(DatabaseManager databaseManager)
			throws DatabaseInitializationException {
		super(databaseManager);
	}
	
	
	public void dropTable() throws SQLException {
		DatabaseManager databaseManager = getDatabaseManager();
		Connection conn = databaseManager.getConnection();
		
		Statement s = conn.createStatement();
		
		s.execute("DROP TABLE software_inventory");
	}

}
