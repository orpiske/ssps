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

import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class DatabaseTest {

	private Properties setup() {
		Properties props = System.getProperties();
		String path = getClass().getResource(".").getPath();

		props.setProperty("derby.system.home", path);

		return props;
	}

	@Test
	public void testDatabaseInitialization()
			throws DatabaseInitializationException {

		DatabaseManager derby = new DerbyDatabaseManager("test", setup());
		@SuppressWarnings("unused")
		TestDao dao = new TestDao(derby);
	}

	@Test
	public void testDatabaseCreation() throws DatabaseInitializationException,
			SQLException {

		DatabaseManager derby = new DerbyDatabaseManager("test", setup());
		TestDao dao = new TestDao(derby);

		dao.createTable();
	}
	
	
	@Test
	public void testDatabaseDrop() throws DatabaseInitializationException,
			SQLException {

		DatabaseManager derby = new DerbyDatabaseManager("test", setup());
		TestDao dao = new TestDao(derby);

		dao.dropTable();
	}

	@Test
	public void testDatabaseCount() throws DatabaseInitializationException,
			SQLException {

		DatabaseManager derby = new DerbyDatabaseManager("test", setup());
		TestDao dao = new TestDao(derby);
		
		try {
			dao.createTable();
		}
		catch (SQLException e) {
			System.out.println("Possible error while trying to create the database");
		}

		int count = dao.count();
		assertEquals(0, count);
		
		dao.insert(new TestDto("test1", new Date(), 1));
		dao.insert(new TestDto("test2", new Date(), 2));
		dao.insert(new TestDto("test3", new Date(), 3));


		count = dao.count();
		assertEquals(3, count);
	}

	

}
