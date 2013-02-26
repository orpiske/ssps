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
package net.orpiske.sdm.lib;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class OsUtils {
	
	private static String operatingSystem = null;
	
	private static String[] unices = {
		"Mac OS X",
		"FreeBSD",
		"HP-UX",
		"AIX",
		"SunOS",
		"Solaris",
		"IRIX"
	};
	
	
	/**
	 * Returns the operating system name
	 * @return
	 * @see For details http://www.osgi.org/Specifications/Reference 
	 */
	public static String getOperatingSystemName() {
		if (operatingSystem == null) {
			operatingSystem = System.getProperty("os.name");
		}
		
		return operatingSystem;
	}
	
	
	/**
	 * Whether the operating is a Unix operating system (hint: Linux returns false here 
	 * use isNix for that).
	 * @return true if it is a Unix operating system or false otherwise 
	 */
	public static boolean isUnix() {
		String os = getOperatingSystemName();
		
		for (String entry: unices) {
			if (entry.equals(os)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Whether the operating system is a Unix-like (*nix) operating system.
	 * @return true if it's a Unix-like operating system
	 */
	public static boolean isNix() {
		String os = getOperatingSystemName();
		
		if (os.equals("Linux")) {
			return true;
		}
		
		for (String entry: unices) {
			if (entry.equals(os)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Whether it's Windows operating system
	 * @return true it's a Windows operating system or false otherwise
	 */
	public static boolean isWindows() {
		String os = getOperatingSystemName();
		
		if (os.toLowerCase().contains("windows")) {
			return true;
		}
		
		return false;
	}

}
