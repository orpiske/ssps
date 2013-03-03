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
package net.orpiske.ssps.common.utils;


/**
 * Naming conversion utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class NameConverter {
	
	private NameConverter() {}
	
	/**
	 * This method converts the usual DB naming standard for column (ie.: test_id, 
	 * group_name, install_dir) to regular Java property naming (ie.: testId, groupId, 
	 * installDir, etc). 
	 * 
	 * It also converts the name from upper case to lower case.
	 * 
	 * @param sqlString
	 * @return
	 */
	public static String sqlToProperty(final String sqlString) {
		StringBuffer buffer = new StringBuffer(64);
		String[] parts = sqlString.split("_");
		
		
		if (parts == null) {
			return sqlString.toLowerCase();
		}
		
		if (parts.length == 1) {
			return sqlString.toLowerCase();
		}
		
		buffer.append(parts[0].toLowerCase());
		
		for (int i = 1; i < parts.length; i++) {
			String part = parts[i];
			
			String initial = part.substring(0, 1).toUpperCase();
			String rest = part.substring(1, part.length()).toLowerCase();
			
			buffer.append(initial);
			buffer.append(rest);
		}
				
				
		return buffer.toString();
	}

}
