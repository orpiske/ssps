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
package net.orpiske.ssps.common.registry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import net.orpiske.ssps.common.db.AbstractDao;
import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.SimpleRsHandler;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;

import org.apache.commons.dbutils.QueryLoader;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SoftwareInventoryDao extends AbstractDao {
	
	private Map<String, String> queries;
	
	
	public SoftwareInventoryDao(final DatabaseManager databaseManager) throws DatabaseInitializationException {
		super(databaseManager);
		
		QueryLoader queryLoader = QueryLoader.instance();
		
		try { 
			queries = queryLoader.load("/net/orpiske/ssps/common/registry/SoftwareInventory.properties");
		}
		catch (IOException e) {
			throw new DatabaseInitializationException("Unable to load queries: " 
					+ e.getMessage(), e);
		}
	}
	
	public void createTable() throws SQLException {
		DatabaseManager databaseManager = getDatabaseManager();
		Connection conn = databaseManager.getConnection();
		
		String query = queries.get("createDb");
		
		System.out.println("Query = " + query);
		
		Statement s = conn.createStatement();
		s.execute(query);
	}

	
	public int insert(final SoftwareInventoryDto dto) throws SQLException {
		String query = queries.get("insert");
		
		return runUpdate(query, dto.getGroupId(), dto.getName(), dto.getVersion(),
					dto.getType(), dto.getInstallDir(), dto.getInstallDate());
	}
	
	
	public SoftwareInventoryDto getByName(final String name) throws SQLException {
		String query = queries.get("queryByName");
		
		SimpleRsHandler<SoftwareInventoryDto> handler = 
				new SimpleRsHandler<SoftwareInventoryDto>(new SoftwareInventoryDto());
		
		return runQuery(query, handler, name);
	}
	
	
	public SoftwareInventoryDto getByKeys(final String groupId, final String name, 
			final String version, final String type) throws SQLException		
	{
		String query = queries.get("queryByKeys");
		
		SimpleRsHandler<SoftwareInventoryDto> handler = 
				new SimpleRsHandler<SoftwareInventoryDto>(new SoftwareInventoryDto());
		
		return runQuery(query, handler, groupId, name, version, type);
	}
	
	
	public int delete(final SoftwareInventoryDto dto) throws SQLException {
		String query = queries.get("deleteByKeys");
		
		return runUpdate(query, dto.getGroupId(), dto.getName(), dto.getVersion(), 
				dto.getType());
	}
	
	
	public SoftwareInventoryDto updateVersion(final String newVersion, 
			final SoftwareInventoryDto dto) throws SQLException 
	{
		String query = queries.get("updateVersion");
		
		int ret = runUpdate(query, newVersion, dto.getGroupId(), dto.getName(), dto.getVersion(), 
				dto.getType());
		
		if (ret == 1) {
			dto.setVersion(newVersion);
		}
		
		return dto;
	}

}
