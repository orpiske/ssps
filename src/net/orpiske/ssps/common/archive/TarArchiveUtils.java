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
package net.orpiske.ssps.common.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.orpiske.ssps.common.exceptions.SspsException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * TAR archive format utilities.
 * 
 * TODO: rename this class to a more appropriate name.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public abstract class TarArchiveUtils implements Archive {

	private static final Logger logger = Logger.getLogger(TarArchiveUtils.class);

	/**
	 * Default file type extension
	 */
	public static final String TAR_EXTENSION = ".tar";

	/**
	 * Lower level pack operation
	 * 
	 * @param source
	 *            source file
	 * @param destination
	 *            destination file
	 * @return the number of bytes processed
	 * @throws ArchiveException
	 * @throws IOException
	 */
	public static long pack(String source, File destination)
			throws ArchiveException, IOException {

		ArchiveStreamFactory factory = new ArchiveStreamFactory();

		OutputStream outStream = new FileOutputStream(destination);

		ArchiveOutputStream outputStream;
		try {
			outputStream = factory.createArchiveOutputStream(
					ArchiveStreamFactory.TAR, outStream);
		} catch (ArchiveException e) {
			IOUtils.closeQuietly(outStream);

			throw e;
		}

		File startDirectory = new File(source);

		RecursiveArchiver archiver = new RecursiveArchiver(outputStream);

		try {
			archiver.archive(startDirectory);
			outputStream.flush();
			outputStream.close();

			outStream.flush();
			outStream.close();

		} catch (IOException e) {
			IOUtils.closeQuietly(outStream);
			IOUtils.closeQuietly(outputStream);

			throw e;
		}

		long ret = outputStream.getBytesWritten();
		
		if (logger.isDebugEnabled()) { 
			logger.debug("Packed " + ret + " bytes");
		}
			
		return ret;
	}


	/**
	 * Unpacks a file
	 * @param source source file
	 * @param destination destination directory. If the directory does not 
	 * exists, it will be created
	 * @param format archive format
	 * @return the number of bytes processed
	 * @throws SspsException
	 * @throws ArchiveException
	 * @throws IOException
	 */
	public static long unpack(File source, File destination, String format) throws SspsException,
			ArchiveException, IOException {
		
		if (!destination.exists()) {
			if (!destination.mkdirs()) {
				throw new IOException("Unable to create destination directory: " 
						+ destination.getPath());
			}
		}
		else {
			if (!destination.isDirectory()) {
				throw new SspsException("The provided destination " 
						+ destination.getPath() + " is not a directory");
			}
		}
		
		ArchiveStreamFactory factory = new ArchiveStreamFactory();

		FileInputStream inputFileStream = new FileInputStream(source);

		ArchiveInputStream archiveStream;
		try {
			archiveStream = factory.createArchiveInputStream(
					format, inputFileStream);
		} catch (ArchiveException e) {
			IOUtils.closeQuietly(inputFileStream);
			throw e;
		}
		
		

		OutputStream outStream = null;
		try {
			TarArchiveEntry entry = (TarArchiveEntry) archiveStream
					.getNextEntry();

			while (entry != null) {
				File outFile = new File(destination, entry.getName());

				if (entry.isDirectory()) {
					if (!outFile.exists()) {
						if (!outFile.mkdirs()) {
							throw new SspsException(
									"Unable to create directory: "
											+ outFile.getPath());
						}
					} else {
						logger.warn("Directory " + outFile.getPath()
								+ " already exists. Ignoring ...");
					}
				} else {
					File parent = outFile.getParentFile();
					if (!parent.exists()) { 
						if (!parent.mkdirs()) {
							throw new IOException("Unable to create parent directories "
									+ parent.getPath());
						}
					}
					
					outStream = new FileOutputStream(outFile);

					IOUtils.copy(archiveStream, outStream);
					outStream.close();
				}

				int mode = entry.getMode();
				
				PermissionsUtils.setPermissions(mode, outFile);

				entry = (TarArchiveEntry) archiveStream.getNextEntry();
			}

			inputFileStream.close();
			archiveStream.close();
		} catch (IOException e) {
			throw e;
		}
		finally {
			IOUtils.closeQuietly(outStream);
			IOUtils.closeQuietly(inputFileStream);
			IOUtils.closeQuietly(archiveStream);
		}

		return archiveStream.getBytesRead();
	}
}
