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

import org.apache.log4j.Logger;

/**
 * Default version comparator. It breaks version strings (n.n.n) in parts 
 * and check each one of them
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class DefaultVersionComparator implements VersionComparator {
	private static final Logger logger = Logger.getLogger(DefaultVersionComparator.class);
	
	private static final int LESS_THAN = -1;
	private static final int EQUALS = 0;
	private static final int GREATER_THAN = 1;
	
	
	private int getNumberForString(final String text) {
		if (text == null) {
			return 0;
		}
		
		String newStr = text.replaceFirst("-", "").toUpperCase();
		
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
		
		if (v1 == null) {
			if (v2 == null) {
				return EQUALS;
			}
			else {
				return LESS_THAN;
			}
		}
		else {
			if (v2 == null) {
				return GREATER_THAN;
			}
		}
		
		parts1 = v1.split("\\.");
		parts2 = v2.split("\\.");
		
		for (int i = 0; i < parts1.length; i++) {
			int n1;
			int n2;
			
			try {
				n1 = Integer.valueOf(parts1[i].trim());
			}
			catch (Exception e) {
				n1 = getNumberForString(parts1[i]);
				
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
			
			try {
				n2 = Integer.valueOf(parts2[i].trim());
			}
			catch (Exception e) {
				n2 = getNumberForString(parts2[i]);
				
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
			
			if (n1 < n2) {
				return LESS_THAN;
			}
			
			if (n1 > n2) {
				return GREATER_THAN;
			}
			
		}
		
		return EQUALS;
	}
	
	
	/**
	 * Utility method to compare two (version) strings and check if whether one (v1) is 
	 * bigger, smaller or equal to than the other (v2)
	 * @param v1 The version to compare
	 * @param v2 The version to compare against
	 * @return ComparisonStrategy.EQUALS if equals, ComparisonStrategy.LESS_THAN
	 * if v2 is less than the base (v2) version or ComparisonStrategy.GREATER_THAN
	 * otherwise.
	 */
	public static int compareStatic(final String v1, final String v2) {
		DefaultVersionComparator comparator = new DefaultVersionComparator();
		
		return comparator.compare(v1, v2);
	}
}
