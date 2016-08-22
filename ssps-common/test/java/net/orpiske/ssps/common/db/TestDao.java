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
import java.sql.Statement;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class TestDao extends AbstractDao {
	
	public TestDao(final DatabaseManager databaseManager) {
		super(databaseManager);
	}

	
	public void createTable() throws SQLException {
		DatabaseManager databaseManager = getDatabaseManager();
		Connection conn = databaseManager.getConnection();
		
		Statement s = conn.createStatement();
		
		s.execute("CREATE TABLE TEST (NAME VARCHAR(30), DATE TIMESTAMP, VALUE INT)");
	}
	
	public void dropTable() throws SQLException {
		DatabaseManager databaseManager = getDatabaseManager();
		Connection conn = databaseManager.getConnection();
		
		Statement s = conn.createStatement();
		
		s.execute("DROP TABLE TEST");
	}
	
	
	public int insert(TestDto dto) throws SQLException {
		return runUpdate("INSERT INTO TEST VALUES(?, ?, ?)", dto.getName(), 
					dto.getDate(), dto.getValue());
	}
	
	
	public TestDto read(String name) throws SQLException {
		SimpleRsHandler<TestDto> handler = new SimpleRsHandler<TestDto>(new TestDto());
		
		return runQuery("SELECT * FROM TEST WHERE NAME = ?", handler, name);
	}
	
	
	public int count() throws SQLException {
		return runQueryCount("SELECT count(*) from test");
	}
}
