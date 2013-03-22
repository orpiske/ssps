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

public class Range {
	private Version minimumVersion;
	private Version maximumVersion;
	
	public Range(final String minimum, final String maximum) {
		if (minimum != null) { 
			minimumVersion = Version.toVersion(minimum);
		}
		
		if (maximum != null) {
			maximumVersion = Version.toVersion(maximum);
		}
	}

	
	public Version getMinimumVersion() {
		return minimumVersion;
	}


	public Version getMaximumVersion() {
		return maximumVersion;
	}

	
	protected static String toVersionString(final String str) {
		String ret = null;
		
		if (str == null) {
			return null;
		}

		ret = str.replaceAll("\\(", "");
		ret = ret.replaceAll("\\)", "");
		
		if (ret.trim().equals("")) {
			ret = null;
		}
		
		return ret;
	}
	
	public boolean inRange(final Version v1) {
		int eval = minimumVersion.compareTo(v1);
		
		if (eval <= 0) {
			eval = maximumVersion.compareTo(v1);
			if (eval >= 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String minimum(final String rangeString) {
		String[] parts = rangeString.split(","); 
		
		return toVersionString(parts[0]);
	}
	
	
	public static String maximum(final String rangeString) {
		String[] parts = rangeString.split(","); 
		
		return toVersionString(parts[1]);
	}
	
	public static Range toRange(final String rangeString) {
		String minimum = minimum(rangeString);
		String maximum = maximum(rangeString);
		
		Range ret = new Range(minimum, maximum);
		
		return ret;
	}
}
