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
package net.orpiske.ssps.common.version;

/**
 * Default version comparator. It breaks version strings (n.n.n) in parts 
 * and check each one of them
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class DefaultVersionComparator implements VersionComparator {
	private static final int LESS_THAN = -1;
	private static final int EQUALS = 0;
	private static final int GREATER_THAN = 1;
	
	
	private int getNumberForString(final String text) {
		if (text == null) {
			return 0;
		}
		
		text.replaceFirst("-", "");
		String newStr = text.toUpperCase();
		
		
		if (newStr.contains("RC")) {
			return -1;
		}
		
		if (newStr.contains("BETA")) {
			return -10;
		}
		
		if (newStr.contains("SNAPSHOT")) {
			return -20;
		}
		
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.ssps.common.version.VersionComparator#compare(java.lang.String, java.lang.String)
	 */
	@Override
	public int compare(final String v1, final String v2) {
		String[] parts1;
		String[] parts2;
		
		if (v1 == null && v2 != null) {
			return LESS_THAN;
		}
		
		parts1 = v1.split("\\.");
		parts2 = v2.split("\\.");
		
		for (int i = 0; i < parts1.length; i++) {
			int n1;
			int n2;
			
			try {
				n1 = Integer.valueOf(parts1[i]);
			}
			catch (Exception e) {
				n1 = getNumberForString(parts1[i]);
			}
			
			try {
				n2 = Integer.valueOf(parts2[i]);
			}
			catch (Exception e) {
				n2 = getNumberForString(parts2[i]);
			}
			
			if (n1 < n2) {
				return LESS_THAN;
			}
			
			if (n2 < n1) {
				return GREATER_THAN;
			}
			
		}
		
		return EQUALS;
	}
}
