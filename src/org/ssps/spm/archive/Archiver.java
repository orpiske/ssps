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
package org.ssps.spm.archive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.archive.usa.UsaArchive;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.spm.dbm.DbmDocument;
import org.ssps.spm.dbm.DbmException;
import org.ssps.spm.dbm.DbmProcessor;

/**
 * Archiver archives the archive (ok, not the archive, but the artifacts + 
 * deployment descriptors .. :)
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Archiver {
	private static Logger logger = Logger.getLogger(Archiver.class);
	
	private UsaArchive usaArchive;
	private DbmProcessor dbmProcessor;

	/**
	 * Constructor
	 * @param dbmFile The path to the DBM file (usually: dbm.xml) on the 
	 * existing directory
	 * @throws XmlDocumentException If the DBM file is incorrectly setup
	 * @throws FileNotFoundException If the DBM archive cannot be found
	 * @throws DbmException 
	 */
	public Archiver(final String dbmFile) throws XmlDocumentException, FileNotFoundException, DbmException {
		logger.trace("Creating a new DBM document object");
		DbmDocument dbmDocument = new DbmDocument(dbmFile);
		
		dbmProcessor = dbmDocument.getDbmProcessor();
		
		logger.trace("Creating a new USA archive processor");
		usaArchive = new UsaArchive();
	}

	private File getBuildSourceDirectory() {
		String sourceDir = dbmProcessor.getBuildSourceDirectory();

		return new File(sourceDir);
	}

	private File getBuildOutputDirectory() {
		String outputDir = dbmProcessor.getBuildOutputDirectory();

		return new File(outputDir);
	}

	private void createBuildRoot() throws DbmException {
		logger.info("Creating the build root");
		
		File sourceDir = getBuildSourceDirectory();
		File outputDir = getBuildOutputDirectory();

		if (!sourceDir.exists()) {
			throw new DbmException("The source directory "
					+ sourceDir.getPath() + " does not exist");
		}
		
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		try {
			FileUtils.copyDirectory(sourceDir, outputDir);
		} catch (IOException e) {
			throw new DbmException("Unable to copy " + sourceDir.getPath()
					+ " to " + outputDir.getPath(), e);
		}
	}

	private void copyArtifact() throws DbmException {
		logger.info("Copying the artifact");
		File sourceFile = new File(dbmProcessor.getBuildArtifact());
		
		String destDirPath = dbmProcessor.getBuildOutputDirectory()
				+ File.separator + "artifacts" + File.separator + "install"
				+ File.separator + "default";
		
		logger.trace("Artifact destination directory: " + destDirPath);
		File destDir = new File(destDirPath);
		
		try {
			FileUtils.copyFileToDirectory(sourceFile, destDir);
		} catch (IOException e) {
			throw new DbmException("Unable to copy " + sourceFile.getPath()
					+ " to " + destDir.getPath() + ": " + e.getMessage(), e);
		}
	}

	
	/**
	 * Creates a new archive
	 * @throws DbmException if the DBM document is incorrect or invalid
	 * @throws SspsArchiveException if unable to create a new SSPS Archive
	 */
	public void createArchive() throws DbmException, SspsArchiveException {
		createBuildRoot();
		copyArtifact();

		
		String buildDirectory = dbmProcessor.getBuildOutputDirectory();
		String deliverableName = dbmProcessor.getDeliverableName();

		logger.info("Packing the deliverable");
		usaArchive.pack(buildDirectory,
				dbmProcessor.getDeliverableOutputDirectory() + File.separator
						+ deliverableName);

	}

}
