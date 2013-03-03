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
package net.orpiske.ssps.common.repository.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.utils.RepositoryUtils;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * Walks through the repository
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
@SuppressWarnings("rawtypes")
public class RepositoryWalker extends DirectoryWalker {
	
	private static final Logger logger = Logger.getLogger(RepositoryWalker.class);
	
	private ArrayList<PackageInfo> packageList = new ArrayList<PackageInfo>();
	


	
	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException 
			
	{
		String ext = FilenameUtils.getExtension(file.getName());
		
		if (("groovy").equals(ext)) {
			PackageInfo packageInfo = RepositoryUtils.readPackageInfo(file);
		
			packageList.add(packageInfo);
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<PackageInfo> load(final File repository) {

		if (logger.isDebugEnabled()) { 
			logger.debug("Looking up repository " + repository.getName());
		}
		
		try {
			walk(repository, new ArrayList());
		} catch (IOException e) {
			logger.error("Unable to walk the whole directory: " + e.getMessage(), e);
			logger.error("Returning a partial list of all the repository data due to errors");
		}
		
		return packageList;
	}

}
