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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class AbstractDaoTest {
	
	private Date date = new Date();
	
	private static DatabaseManager derby;

	private static Properties setup() {
		Properties props = System.getProperties();
		String path = AbstractDaoTest.class.getResource(".").getPath();

		props.setProperty("derby.system.home", path);

		return props;
	}
	
	@BeforeClass
	public static void createTable() throws DatabaseInitializationException, SQLException {
		derby = new DerbyDatabaseManager("test", setup());
		TestDao dao = new TestDao(derby);
		dao.createTable();
	}
	
	@AfterClass
	public static void dropTable() throws DatabaseInitializationException, SQLException {
		TestDao dao = new TestDao(derby);
		dao.dropTable();
	}

	@Test
	public void testDatabaseInsert() throws DatabaseInitializationException,
			SQLException {
		TestDao dao = new TestDao(derby);

		TestDto dto = new TestDto();

		dto.setName("database test");

		
		dto.setDate(date);
		dto.setValue(14);

		dao.insert(dto);
		derby.commit();
	}
	
	
	@Test
	public void testDatabaseRead() throws DatabaseInitializationException,
			SQLException {

		TestDao dao = new TestDao(derby);

		TestDto result = dao.read("database test");

		assertEquals(14, result.getValue());
		assertNotNull(result.getDate());
		assertTrue(date.getYear() == result.getDate().getYear());
		assertTrue(date.getMonth() == result.getDate().getMonth());
		assertTrue(date.getDay() == result.getDate().getDay());
		assertEquals("database test", result.getName());
	}

}
