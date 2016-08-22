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

import java.io.File;

import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.lib.exceptions.UnpackException;
import net.orpiske.sdm.lib.exceptions.UnsupportedFormat;
import net.orpiske.ssps.common.archive.Archive;
import net.orpiske.ssps.common.archive.exceptions.SspsArchiveException;
import net.orpiske.ssps.common.archive.tbz.TbzArchive;
import net.orpiske.ssps.common.archive.tgz.TgzArchive;
import net.orpiske.ssps.common.archive.zip.ZipArchive;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;



/**
 * Unpack utility class to handle tar.gz and tar.bz2 files
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class Unpack {
	private static final Logger logger = Logger.getLogger(Unpack.class);
	
	
	private static void cleanup(final String artifact, final String destination) {
		logger.warn("Cleaning up due to errors");
		
		File orphanDirectory = new File(destination);
		
		if (orphanDirectory.exists()) { 
			FileUtils.deleteQuietly(orphanDirectory);
		}
	}
	
	
	/**
	 * Unpacks a file
	 * @param artifact the file to unpack
	 * @param destination the destination folder to unpack the file to
	 */
	public static void unpack(final String artifact, final String destination) {
		Archive archive;
		
		String extension = FilenameUtils.getExtension(artifact);
		
		if (extension.equals("tbz2") || extension.equals("bz2")) {
			archive = new TbzArchive();
		}
		else {
			if (extension.equals("tgz") || extension.equals("gz")) {
				archive = new TgzArchive();
			}
			else {
				if (extension.equals("zip")) {
					archive = new ZipArchive();
				}
				else { 
					throw new UnsupportedFormat("Unsupported format: " 
							+ extension);
				}
			}
		}
		
		try {
			String source = artifact;
			
			archive.unpack(source, destination);
		} catch (SspsArchiveException e) {
			cleanup(artifact, destination);
			
			throw new UnpackException(e.getMessage(), e);
 
		}
	}
	
	
	/**
	 * Unpacks a file to the workdir
	 * @param artifact the file to unpack
	 */
	public static void unpack(final String artifact) {
		unpack(artifact, WorkdirUtils.getWorkDir());
	}
	
	
}
