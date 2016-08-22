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
 * Abstracts version operations
 * @author Otavio R. Piske
 *
 */
public final class Version implements Comparable<Version>{
	
	private String value;
	
	/**
	 * Constructor
	 */
	public Version() {
		
	}
	
	
	private String getValue() {
		return value;
	}

	
	/**
	 * Sets the value for this version (ie.: 1.2.0, 1.1.9, etc)
	 * @param value The version value
	 */
	private void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	
	@Override
	public int compareTo(Version other) {
		VersionComparator versionComparator = new DefaultVersionComparator();
		
		return versionComparator.compare(getValue(), other.getValue());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		
		if (!(obj instanceof Version)) {
			return false;
		}
 				
		Version other = (Version) obj;
		if (getValue() == null) {
			if (other.getValue() == null) { 
				return true;
			}
			
			return false;
		}
		
		return getValue().equals(other.getValue()); 
	}
	
	
	/**
	 * Constructs a version object from a String
	 * @param value the version number/value
	 * @return A new Version object with the input value
	 */
	public static Version toVersion(final String value) {
		Version version = new Version();
		version.setValue(value);
		
		return version;
	}


	
}
