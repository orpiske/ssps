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
package org.ssps.spm.dbm.filefilter;

import java.io.File;
import java.io.FileFilter;

import net.orpiske.ssps.dbm.AbstractFileFilter;

import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class WildCardFilterProcessor implements FileFilterProcessor {

	
	/* (non-Javadoc)
	 * @see org.ssps.spm.dbm.filefilter.FileFilterProcessor#getFileList(net.orpiske.ssps.dbm.AbstractFileFilter)
	 */
	public File[] getFileList(final AbstractFileFilter filter, final String path) {
		File dir = new File(path);
		FileFilter fileFilter = null; 
		
		if (filter.isNegate()) { 
			fileFilter = new NotFileFilter(new WildcardFileFilter(filter.getFilter()));
			
		}
		else {
			fileFilter = new WildcardFileFilter(filter.getFilter());			
		}
		
		return dir.listFiles(fileFilter);
	}

}
