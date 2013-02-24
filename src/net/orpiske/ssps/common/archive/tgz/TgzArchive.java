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
package net.orpiske.ssps.common.archive.tgz;

import java.io.File;
import java.io.IOException;

import net.orpiske.ssps.common.archive.Archive;
import net.orpiske.ssps.common.archive.CompressedArchiveUtils;
import net.orpiske.ssps.common.archive.TarArchiveUtils;
import net.orpiske.ssps.common.archive.exceptions.SspsArchiveException;
import net.orpiske.ssps.common.exceptions.SspsException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.log4j.Logger;


/**
 * TAR + Gzip archive support
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class TgzArchive implements Archive {

	private static final Logger logger = Logger.getLogger(TgzArchive.class);

	/**
	 * Default file type extension
	 */
	public static final String TGZ_EXTENSION = ".gz";

	private String getArchiveFileExtension(final String originalName) {
		if (originalName.endsWith(TarArchiveUtils.TAR_EXTENSION)) {
			return originalName;
		}

		return originalName + TarArchiveUtils.TAR_EXTENSION;
	}

	private String getCompressedFileExtension(final String originalName) {
		if (originalName.endsWith(TGZ_EXTENSION)) {
			return originalName;
		}

		return originalName + TGZ_EXTENSION;
	}

	private String replaceCompressedFileExtension(final String originalName) {
		if (originalName.endsWith(".gz")) {
			return originalName.replaceAll(".gz", "");
		}

		return originalName;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.ssps.common.archive.Archive#pack(java.lang.String, java.lang.String)
	 */
	public long pack(String source, String destination)
			throws SspsArchiveException {
		File archiveFile = new File(getArchiveFileExtension(destination));

		try {
			TarArchiveUtils.pack(source, archiveFile, false);
		} catch (ArchiveException e1) {
			throw new SspsArchiveException("Unable to archive file", e1);
		} catch (IOException e1) {
			throw new SspsArchiveException("Unable to archive file: I/O error",
					e1);
		}

		File compressedFile = new File(getCompressedFileExtension(destination));

		try {
			CompressedArchiveUtils.compress(archiveFile, compressedFile, CompressorStreamFactory.BZIP2);
			
			return 0;
		} catch (CompressorException e) {
			throw new SspsArchiveException("Unable to compress archive file", e);
		} catch (IOException e) {
			throw new SspsArchiveException(
					"Unable to compress archive file: I/O error", e);
		} finally {
			if (archiveFile.exists()) {
				archiveFile.delete();

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ssps.common.archive.Archive#unpack(java.lang.String, java.lang.String)
	 */
	public long unpack(String source, String destination)
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

		logger.info("Unpacking " + source + " to "
				+ uncompressedArchiveFile.getPath());

		try {
			CompressedArchiveUtils.gzUncompress(compressedFileSource, uncompressedArchiveFile);
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
