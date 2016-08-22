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

import java.sql.SQLException;
import java.util.Properties;

import net.orpiske.ssps.common.db.AbstractDaoTest;
import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDao;

import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SoftwareInventorySetup {
	
	private static DatabaseManager derby;

	private static Properties setup() {
		Properties props = System.getProperties();
		String path = AbstractDaoTest.class.getResource(".").getPath();

		props.setProperty("derby.system.home", path);

		return props;
	}
	
	@Test
	public void createTableTest() throws DatabaseInitializationException, SQLException {
		derby = new DerbyDatabaseManager("registry", setup(), true);
		SoftwareInventoryDao dao = new TestSoftwareInventoryDao(derby);
		dao.createTable();
	}
	
	

}
