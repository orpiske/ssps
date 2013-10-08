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

import net.orpiske.ssps.common.utils.NameConverter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Handles the result of a query
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class CountRsHandler implements ResultSetHandler<Integer> {	
	
	
	/**
	 * Constructor
	 */
	public CountRsHandler() {
		
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	@Override
	public Integer handle(ResultSet rs) throws SQLException {
		Integer dto = null;
		
		// No records to handle :O
		if (!rs.next()) {
            return null;
        }
    
        ResultSetMetaData meta = rs.getMetaData();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
        	Object value = rs.getObject(i);
        	        	
        	try {
        		if (value instanceof Integer) { 
					dto = new Integer((Integer) value);
				}
				
			} catch (Exception e) {
				throw new SQLException("Unable to set/retrieve count value", e);
			}
        }

        return dto;
	}

}
