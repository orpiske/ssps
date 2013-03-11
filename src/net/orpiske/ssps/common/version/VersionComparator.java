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

import java.util.Comparator;

public class VersionComparator implements Comparator {
	
	public static final int LESS_THAN = -1;
	public static final int EQUALS = 0;
	public static final int BIGGER_THAN = 1;
	
	private String baseVersion;
	
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
			return -2;
		}
		
		if (newStr.contains("SNAPSHOT")) {
			return -3;
		}
		
		return 0;
	}
	
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
				return BIGGER_THAN;
			}
			
		}
		
		return EQUALS;
	}

	@Override
	public int compare(Object o1, Object o2) {
		return compare((String) o1, (String) o2);
	}
	
	public static int compareStatic(final String v1, final String v2) {
		VersionComparator comparator = new VersionComparator();
		
		return comparator.compare(v1, v2);
	}

}
