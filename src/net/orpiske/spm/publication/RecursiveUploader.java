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
package net.orpiske.spm.publication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;

import org.apache.commons.io.DirectoryWalker;
import org.apache.log4j.Logger;

/**
 * Recursively uploads everything in a directory. Unused for now.
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
@SuppressWarnings("rawtypes")
public class RecursiveUploader extends DirectoryWalker {
	
	private static final Logger logger = Logger.getLogger(RecursiveUploader.class);
	
	private PublicationManager publicationManager;
	private boolean overwrite;
	
	public RecursiveUploader(final PublicationManager publicationManager, 
			boolean overwrite) 
	{
		this.publicationManager = publicationManager;
		this.overwrite = overwrite;
	}
	
	@Override
	protected boolean handleDirectory(File directory, int depth,
			Collection results) throws IOException {
		return true;
	}
	
	
	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		try {
			publicationManager.upload(file, overwrite);
		} catch (ResourceExchangeException e) {
			logger.error("Unable to upload file " + file.getPath() + ": " 
					+ e.getMessage(), e);
			
			throw new IOException("Unable to upload file " + file.getPath() + ": " 
					+ e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List upload(final File startDirectory) throws IOException {
		List results = new ArrayList();

		if (logger.isDebugEnabled()) { 
			logger.debug("Uploading " + startDirectory.getPath());
		}
		
		walk(startDirectory, results);
		return results;
	}

}
