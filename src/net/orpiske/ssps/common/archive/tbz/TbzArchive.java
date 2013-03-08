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
package net.orpiske.ssps.common.archive.tbz;

import java.io.File;
import java.io.IOException;

import net.orpiske.ssps.common.archive.Archive;
import net.orpiske.ssps.common.archive.CompressedArchiveUtils;
import net.orpiske.ssps.common.archive.TarArchiveUtils;
import net.orpiske.ssps.common.archive.exceptions.SspsArchiveException;
import net.orpiske.ssps.common.exceptions.SspsException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.log4j.Logger;

/**
 * TAR + Bzip2 archive support
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class TbzArchive implements Archive {

	private static final Logger logger = Logger.getLogger(TbzArchive.class);

	/**
	 * Default file type extension
	 */
	public static final String TBZ_EXTENSION = ".bz2";


	private String replaceCompressedFileExtension(final String originalName) {
		if (originalName.endsWith(TBZ_EXTENSION)) {
			return originalName.replaceAll(TBZ_EXTENSION, "");
		}

		return originalName;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.ssps.common.archive.Archive#unpack(java.lang.String, java.lang.String)
	 */
	public long unpack(final String source, final String destination)
			throws SspsArchiveException {
		File compressedFileSource = new File(source);

		String uncompressedArchiveFileName = destination
				+ File.separator
				+ replaceCompressedFileExtension(compressedFileSource.getName());

		File uncompressedArchiveFile = new File(uncompressedArchiveFileName);
		if (uncompressedArchiveFile.exists()) {
			throw new SspsArchiveException(
					"A previously uncompressed file exists: "
							+ uncompressedArchiveFile);
		}

		if (logger.isDebugEnabled()) { 
			logger.debug("Unpacking " + source + " to "
					+ uncompressedArchiveFile.getPath());
		}
		else {
			logger.info("Unpacking " + compressedFileSource.getName());
		}

		try {
			CompressedArchiveUtils.bzipUncompress(compressedFileSource, uncompressedArchiveFile);
		} catch (IOException e) {
			throw new SspsArchiveException(
					"Unable to uncompress archive file: I/O error", e);
		}

		File destinationDirectory = new File(destination);

		try {
			return TarArchiveUtils.unpack(uncompressedArchiveFile, destinationDirectory, 
					ArchiveStreamFactory.TAR);
		} catch (SspsException e) {
			throw new SspsArchiveException("Unable to unpack file", e);
		} catch (ArchiveException e) {
			throw new SspsArchiveException("Unable to unpack file", e);
		} catch (IOException e) {
			throw new SspsArchiveException(
					"Unable to uncompress archive file: I/O error", e);
		} finally {
			if (uncompressedArchiveFile.exists()) {
				uncompressedArchiveFile.delete();
			}
		}

	}
}
