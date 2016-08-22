/**
   Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.ssps.common.repository.utils;

/**
 * Package manipulation utilities
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class PackageUtils {
	
	/**
	 * Restricted constructor
	 */
	private PackageUtils() {}

	
	private static int countParts(final String str) {
		int parts = 0; 
		
		int pos = str.indexOf("/", 0);
		
		while (pos != -1) {
			parts++;
			pos = str.indexOf("/", pos + 1);
		}
		
		return parts;
	}
	
	/**
	 * Gets the groupId from a qualified package name (org.apache/apache-ant, 
	 * org.apache/apache-ant, default/org.apache/apache-ant)
	 * @param str the qualified package name
	 * @return The group id
	 */
	public static String getGroupId(final String str) {
		int numParts = countParts(str);
		int groupIdPart = 0;
		
		if (numParts >= 2) {
			groupIdPart = 1; 
		}
		else {
			if (numParts == 0) {
				return null;
			}
		}
		
		String[] parts = str.split("/");
		return parts[groupIdPart];
	}
	
	
	/**
	 * Gets the groupId from a qualified package name (org.apache/apache-ant, 
	 * org.apache/apache-ant, default/org.apache/apache-ant)
	 * @param str the qualified package name
	 * @return The group id
	 */
	public static String getPackageName(final String str) {
		int numParts = countParts(str);
		int namePart = 1;
		
		if (numParts >= 2) {
			namePart = 2; 
		}
		else {
			if (numParts == 0) {
				return null;
			}
		}
		
		String[] parts = str.split("/");
		return parts[namePart];
	}
}
