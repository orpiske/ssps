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
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.archive.usa.UsaArchive;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.spm.dbm.DbmDocument;
import org.ssps.spm.dbm.DbmException;

/**
 * Archiver archives the archive (ok, not the archive, but the artifacts + 
 * deployment descriptors .. :)
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Archiver {
	private UsaArchive usaArchive;
	private DbmDocument dbmDocument;

	/**
	 * Constructor
	 * @param dbmFile The path to the DBM file (usually: dbm.xml) on the 
	 * existing directory
	 * @throws XmlDocumentException If the DBM file is incorrectly setup
	 * @throws FileNotFoundException If the DBM archive cannot be found
	 */
	public Archiver(final String dbmFile) throws XmlDocumentException, FileNotFoundException {
		dbmDocument = new DbmDocument(dbmFile);
		usaArchive = new UsaArchive();
	}

	private File getBuildSourceDirectory() {
		String sourceDir = dbmDocument.getBuildSourceDirectory();

		return new File(sourceDir);
	}

	private File getBuildOutputDirectory() {
		String outputDir = dbmDocument.getBuildOutputDirectory();

		return new File(outputDir);
	}

	private void createBuildRoot() throws DbmException {
		File sourceDir = getBuildSourceDirectory();
		File outputDir = getBuildOutputDirectory();

		if (!sourceDir.exists()) {
			throw new DbmException("The source directory "
					+ sourceDir.getPath() + " does not exist");
		}

		try {
			FileUtils.copyDirectory(sourceDir, outputDir);
		} catch (IOException e) {
			throw new DbmException("Unable to copy " + sourceDir.getPath()
					+ " to " + outputDir.getPath(), e);
		}
	}

	private void copyArtifact() throws DbmException {
		File sourceFile = new File(dbmDocument.getBuildArtifact());
		File destDir = new File(dbmDocument.getBuildOutputDirectory()
				+ File.separator + "artifacts" + File.separator + "install"
				+ File.separator + "default");

		try {
			FileUtils.copyFileToDirectory(sourceFile, destDir);
		} catch (IOException e) {
			throw new DbmException("Unable to copy " + sourceFile.getPath()
					+ " to " + destDir.getPath(), e);
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

		String buildDirectory = dbmDocument.getBuildOutputDirectory();
		String deliverableName = dbmDocument.getDeliverableName();

		usaArchive.pack(buildDirectory,
				dbmDocument.getDeliverableOutputDirectory() + File.separator
						+ deliverableName);

	}

}
