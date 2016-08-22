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
package net.orpiske.ssps.common.archive;

import java.io.File;

/**
 * File/Directory permissions utils
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class PermissionsUtils {
	
	private PermissionsUtils() {}
	
	
	/**
	 * Given a file mode, set the permissions accordingly
	 * 
	 * @param mode The file/directory mode
	 * @param outFile The outFile to have the permissions set
	 */
	public static void setPermissions(int mode, File outFile) {		
		int canOthersExecute = (mode >> 3) & 1; 
		int canOwnerExecute = (mode >> 6) & 1;
		
		if (canOthersExecute == 1) {
			outFile.setExecutable(true, false);
		}
		else {
			if (canOthersExecute == 0 && canOwnerExecute == 1) {
				outFile.setExecutable(true, true);
			}
		}
	}

}
