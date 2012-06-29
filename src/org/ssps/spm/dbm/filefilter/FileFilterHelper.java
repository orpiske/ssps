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
import java.util.ArrayList;
import java.util.List;

import org.ssps.common.variables.VariablesParser;
import org.ssps.spm.dbm.DbmException;

import net.orpiske.ssps.dbm.AbstractFileFilter;
import net.orpiske.ssps.dbm.FileFilterRef;
import net.orpiske.ssps.dbm.NameFilter;
import net.orpiske.ssps.dbm.PrefixFilter;
import net.orpiske.ssps.dbm.RegexFilter;
import net.orpiske.ssps.dbm.SuffixFilter;
import net.orpiske.ssps.dbm.WildcardFilter;

/**
 * Helper class for file filtering
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class FileFilterHelper {
	
	private static String getPath(AbstractFileFilter filter) throws DbmException {
		String path = VariablesParser.getInstance().evaluate(filter.getPath());
		
		if (path == null) {
			throw new DbmException("The path for the file filter was not provided");
		}
		
		return path;
	}
	
	private static File[] getFileArray(FileFilterRef fileFilterReference) throws DbmException {
		FileFilterProcessor processor = null;
		AbstractFileFilter filter = null;
		
		filter = fileFilterReference.getNameFilter();
		if (filter != null) {
			processor = new NameFilterProcessor();
			
			String path = getPath(filter);
			return processor.getFileList(filter, path);			
		}
		
		filter = fileFilterReference.getRegexFilter();
		if (filter != null) {
			processor = new RegexFilterProcessor();
			
			String path = getPath(filter);
			return processor.getFileList(filter, path);	
		}
		
		filter = fileFilterReference.getSuffixFilter();
		if (filter != null) {
			processor = new SuffixFilterProcessor();
			
			String path = getPath(filter);
			return processor.getFileList(filter, path);	
		}
		
		filter = fileFilterReference.getPrefixFilter();
		if (filter != null) {
			processor = new PrefixFilterProcessor();
			
			String path = getPath(filter);
			return processor.getFileList(filter, path);	
		}
		
		filter = fileFilterReference.getWildcardFilter();
		if (filter != null) {
			processor = new WildCardFilterProcessor();
			
			String path = getPath(filter);
			return processor.getFileList(filter, path);	
		}
		
		return null;
	}
	
	/**
	 * Gets a list of files that match the input filter
	 * @param fileFilterReference The file filter reference object from the XML
	 * @return a list object containing the files that match the input filter
	 * @throws DbmException If the path for the filters is not provided
	 */
	public static List<String> getFileList(FileFilterRef fileFilterReference) throws DbmException {
		File[] files = getFileArray(fileFilterReference);
		List<String> ret = new ArrayList<String>();
	
	
		for (File file : files) {
			ret.add(file.getPath());
		}
	
		return ret;
	}
}
