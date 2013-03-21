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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.orpiske.ssps.common.utils.NameConverter;
import net.orpiske.ssps.common.version.Version;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 * Handles the result of a query
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
class SoftwareInventoryRsHandler implements ResultSetHandler<SoftwareInventoryDto> {	
	private SoftwareInventoryDto dto;
	
	/**
	 * Constructor
	 */
	public SoftwareInventoryRsHandler() {
		this.dto = new SoftwareInventoryDto();
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	@Override
	public SoftwareInventoryDto handle(ResultSet rs) throws SQLException {
		
		// No records to handle :O
		if (!rs.next()) {
            return null;
        }
    
        ResultSetMetaData meta = rs.getMetaData();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
        	Object value = rs.getObject(i);
        	String name = meta.getColumnName(i);
        	
        	try {
        		/*
        		 * We convert the column name to a more appropriate and java like name 
        		 * because some columns are usually named as some_thing whereas Java 
        		 * properties are named someThing. This call does this conversion.
        		 */
        		String javaProperty = NameConverter.sqlToProperty(name);
        		
        		if (javaProperty.equals("version")) {
        			Version version = Version.toVersion((String) value);

        			PropertyUtils.setSimpleProperty(dto, javaProperty, version);
        		}
        		else {
        			PropertyUtils.setSimpleProperty(dto, javaProperty, value);	
        		}
				
			} catch (Exception e) {
				throw new SQLException("Unable to set property " + name + " for bean" + 
						dto.getClass(), e);
			}
        }

        return dto;
	}

}
