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

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import net.orpiske.ssps.common.db.AbstractDaoTest;
import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.registry.SoftwareInventoryDao;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A very poor atempt to test the software inventory dao.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SoftwareInventoryDaoTest {
	
	private static DatabaseManager derby;

	private static Properties setup() {
		Properties props = System.getProperties();
		String path = AbstractDaoTest.class.getResource(".").getPath();

		props.setProperty("derby.system.home", path);

		return props;
	}
	
	@BeforeClass
	public static void createTable() throws DatabaseInitializationException, SQLException {
		derby = new DerbyDatabaseManager("registry", setup());
		SoftwareInventoryDao dao = new TestSoftwareInventoryDao(derby);
		dao.createTable();
	}
	
	@AfterClass
	public static void dropTable() throws DatabaseInitializationException, SQLException {
		TestSoftwareInventoryDao dao = new TestSoftwareInventoryDao(derby);
		dao.dropTable();
	}
	
	@Test
	public void testReadWrite() throws DatabaseInitializationException, SQLException {
		SoftwareInventoryDao dao = new SoftwareInventoryDao(derby);
		SoftwareInventoryDto dto = new SoftwareInventoryDto();
		
		dto.setGroupId("net.orpiske.ssps");
		dto.setName("sdm");
		dto.setVersion("0.2.0");
		dto.setType("b");
		dto.setInstallDir("/tmp/none");
		dto.setInstallDate(new Date());
		
		dao.insert(dto);
		
		SoftwareInventoryDto readDto = dao.getByName("sdm").get(0);
		
		assertEquals("net.orpiske.ssps", readDto.getGroupId());
		assertEquals("sdm", readDto.getName());
		assertEquals("0.2.0", readDto.getVersion());
		assertEquals("b", readDto.getType());
		assertEquals("/tmp/none", readDto.getInstallDir());
		
		assertNotNull(readDto.getInstallDate());
		
		
		SoftwareInventoryDto readDto2 = dao.getByKeys("net.orpiske.ssps", "sdm", 
				"0.2.0", "b");
		
		assertEquals("net.orpiske.ssps", readDto2.getGroupId());
		assertEquals("sdm", readDto2.getName());
		assertEquals("0.2.0", readDto2.getVersion());
		assertEquals("b", readDto2.getType());
		assertEquals("/tmp/none", readDto2.getInstallDir());
		
		assertNotNull(readDto2.getInstallDate());
		
		readDto2 = dao.updateVersion("0.2.1", readDto2);
		
		readDto = dao.getByName("sdm").get(0);
		
		assertEquals("0.2.1", readDto.getVersion());
		
		dao.delete(readDto);
	}
}
