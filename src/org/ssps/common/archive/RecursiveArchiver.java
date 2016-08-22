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
package org.ssps.common.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * This class implements a recursive archiving functionality. It navigates 
 * through directories and sub-directories and adds then to an archive.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
@SuppressWarnings("rawtypes")
public class RecursiveArchiver extends DirectoryWalker {
	private static Logger logger = Logger.getLogger(RecursiveArchiver.class);

	private ArchiveOutputStream outputStream;
	
	/**
	 * Controls whether or not part of the path should be removed from the 
	 * archive
	 */
	private boolean stripPath;

	
	/**
	 * Constructor
	 * @param outputStream the output stream used to create the archive
	 */
	public RecursiveArchiver(final ArchiveOutputStream outputStream) {
		super();

		this.outputStream = outputStream;
	}
	
	
	/**
	 * Constructor
	 * @param outputStream the output stream used to create the archive
	 * @param stripPath whether to strip part of the path from the archive 
	 * entries. Right now, removes all text before 'installroot' from being
	 * added to an entry path.
	 */
	public RecursiveArchiver(final ArchiveOutputStream outputStream, boolean stripPath) {
		super();

		this.outputStream = outputStream;
		setStripPath(stripPath);
	}

	
	/**
	 * Stars the recursive archiving
	 * @param startDirectory the start directory to be processed
	 * @return A list of processed files
	 * @throws IOException in case of lower level I/O errors
	 */
	@SuppressWarnings("unchecked")
	public List archive(final File startDirectory) throws IOException {
		List results = new ArrayList();

		if (logger.isDebugEnabled()) { 
			logger.debug("Archiving " + startDirectory.getPath());
		}
		
		walk(startDirectory, results);
		return results;
	}

	/**
	 * Removes all text before 'installroot' from the path
	 * @param path
	 * @return
	 */
	private String stripPath(final String path) {
		int pos = path.indexOf("installroot");
		return path.substring(pos);

	}

	@Override
	protected boolean handleDirectory(File directory, int depth,
			Collection results) throws IOException {

		String path;
		if (isStripPath()) { 
			 path = stripPath(directory.getPath());
		}
		else {
			path = directory.getPath();
		}
		
		logger.trace("Archiving directory " + path);
		TarArchiveEntry entry = new TarArchiveEntry(directory, path);

		outputStream.putArchiveEntry(entry);
		outputStream.closeArchiveEntry();

		return true;
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		
		String path;
		if (isStripPath()) { 
			 path = stripPath(file.getPath());
		}
		else {
			path = file.getPath();
		}

		logger.trace("Archiving file " + path);

		TarArchiveEntry entry = new TarArchiveEntry(file, path);

		outputStream.putArchiveEntry(entry);
		IOUtils.copy(new FileInputStream(file), outputStream);

		outputStream.closeArchiveEntry();
	}

	
	/**
	 * Whether or not it is configured to remove strings from the path
	 * @return true if configured to remove or false otherwise
	 */
	public boolean isStripPath() {
		return stripPath;
	}

	
	/**
	 * Sets whether or not it is configured to remove strings from the path
	 * @param stripPath true to remove strings from the path or false otherwise
	 */
	public void setStripPath(boolean stripPath) {
		this.stripPath = stripPath;
	}

}
