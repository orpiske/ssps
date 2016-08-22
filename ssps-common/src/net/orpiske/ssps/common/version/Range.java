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
 * Range class implements support for evaluating whether a given version number fits 
 * within a certain range (ie.: for the range (4.3.1,4.3.99), it checks whether a value is 
 * bigger than or equals to 4.3.1 and smaller than or equal to 4.3.99.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class Range {
	private Version minimumVersion;
	private Version maximumVersion;
	
	
	/**
	 * Constructor
	 * @param minimum Minimum version string
	 * @param maximum Maximum version string
	 */
	public Range(final String minimum, final String maximum) {
		if (minimum != null) { 
			minimumVersion = Version.toVersion(minimum);
		}
		
		if (maximum != null) {
			maximumVersion = Version.toVersion(maximum);
		}
	}

	
	/**
	 * Gets the minimum version in this range
	 * @return the minimum version
	 */
	public Version getMinimumVersion() {
		return minimumVersion;
	}


	/**
	 * Gets thee maximum version in this range
	 * @return the maximum version 
	 */
	public Version getMaximumVersion() {
		return maximumVersion;
	}


	
	private static String toVersionString(final String str) {
		String ret;
		
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
	
	
	/**
	 * Checks whether a given version fits in the range
	 * @param v1 the version to check
	 * @return true if the version (v1) is in range or false otherwise
	 */
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

	
	/**
	 * Gets the minimum version in a range string
	 * @param rangeString the range string
	 * @return the minimum version value
	 */
	public static String minimum(final String rangeString) {
		String[] parts = rangeString.split(","); 
		
		return toVersionString(parts[0]);
	}
	
	
	/**
	 * Gets the maximum version in a range string
	 * @param rangeString the range string
	 * @return the maximum version value
	 */
	public static String maximum(final String rangeString) {
		String[] parts = rangeString.split(","); 
		
		return toVersionString(parts[1]);
	}
	
	
	/**
	 * Constructs a new Range object from a version range string
	 * @param rangeString the range string
	 * @return a new Range object
	 */
	public static Range toRange(final String rangeString) {
		String minimum = minimum(rangeString);
		String maximum = maximum(rangeString);
		
		Range ret = new Range(minimum, maximum);
		
		return ret;
	}
}
