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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

/**
 * A database manager for the embedded Apache Derby database
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com> 
 */
public class DerbyDatabaseManager implements DatabaseManager {
	private static final Logger logger = Logger
			.getLogger(DerbyDatabaseManager.class);

	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String protocol = "jdbc:derby:";

	private String databaseName;
	private Connection conn;

	public DerbyDatabaseManager(final String databaseName,
			final Properties properties) throws DatabaseInitializationException {
		this.databaseName = databaseName;

		loadDriver();
		setup(properties);
	}

	/**
	 * Loads the embedded Apache Derby driver.
	 * 
	 * @throws DatabaseInitializationException
	 *             if unable to load, instantiate or access the driver.
	 */
	private void loadDriver() throws DatabaseInitializationException {

		try {
			Class.forName(DRIVER).newInstance();
			System.out.println("Loaded the appropriate driver");
		} catch (ClassNotFoundException cnfe) {
			logger.debug("Unable to load JDBC driver " + DRIVER);
			throw new DatabaseInitializationException(
					"Unable to load JDBC Driver", cnfe);
		} catch (InstantiationException ie) {
			logger.debug("Unable to instantiate JDBC driver " + DRIVER);
			throw new DatabaseInitializationException(
					"Unable to instantiate JDBC Driver", ie);
		} catch (IllegalAccessException iae) {
			logger.debug("Not allowed to access JDBC driver " + DRIVER);
			throw new DatabaseInitializationException(
					"Not allowed to access JDBC driver", iae);
		}
	}

	private void setup(Properties properties)
			throws DatabaseInitializationException {
		try {
			conn = DriverManager.getConnection(protocol + databaseName
					+ ";create=true", properties);
		} catch (SQLException e) {
			throw new DatabaseInitializationException(
					"Unable to connect to the embedded" + " database", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.orpiske.ssps.common.db.DatabaseManager#commit()
	 */
	@Override
	public void commit() throws SQLException {
		if (conn != null) {
			conn.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.orpiske.ssps.common.db.DatabaseManager#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		if (conn != null) {
			conn.rollback();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.orpiske.ssps.common.db.DatabaseManager#getConnection()
	 */
	@Override
	public Connection getConnection() {
		return conn;
	}

	/* (non-Javadoc)
	 * @see net.orpiske.ssps.common.db.DatabaseManager#close()
	 */
	@Override
	public void close() {
		DbUtils.closeQuietly(conn);
		
	}

}
