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
package org.ssps.sdm.adm.rules.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ssps.sdm.adm.util.PrintUtils;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
@SuppressWarnings("rawtypes")
public class ShieldAwareCopier extends DirectoryWalker {
	
	private static Logger logger = Logger.getLogger(ShieldAwareCopier.class);
	
	private File destination;
	
	
	public ShieldAwareCopier(final File destination) {
		super(null, 1);
		this.destination = destination;
	}


	@Override
	protected boolean handleDirectory(File directory, int depth,
			Collection results) throws IOException {
		
		if (logger.isDebugEnabled()) { 
			logger.debug("Processing directory " + directory.getPath() + 
					" at depth " + depth);
		}
		
		if (depth == 0) {
			return true;
		}
		
		File destinationDir = new File(destination, directory.getName());
		destinationDir.mkdirs();
		
		ShieldAwareCopier copier = new ShieldAwareCopier(destinationDir);
		
		copier.copy(directory);
		
		return true;
	}


	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		File destinationFile = new File(destination, file.getName());
		
		if (!ShieldUtils.isShielded(destinationFile)) {
			FileUtils.copyFile(file, destinationFile);
		}
		else {
			PrintUtils.printInfo("Ignoring shielded file " + file.getPath());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void copy(final File source) throws IOException {
		@SuppressWarnings("rawtypes")
		List results = new ArrayList();

		walk(source, results);
		return;
	}

}
