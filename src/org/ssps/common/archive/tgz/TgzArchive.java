package org.ssps.common.archive.tgz;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.log4j.Logger;
import org.ssps.common.archive.Archive;
import org.ssps.common.archive.CompressedArchiveUtils;
import org.ssps.common.archive.TarArchiveUtils;
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.exceptions.SspsException;

public class TgzArchive implements Archive {

	private static final Logger logger = Logger.getLogger(TgzArchive.class);


	private String getArchiveFileExtension(final String originalName) {
		if (originalName.endsWith(".tar")) {
			return originalName;
		}

		return originalName + ".tar";
	}

	private String getCompressedFileExtension(final String originalName) {
		if (originalName.endsWith(".gz")) {
			return originalName;
		}

		return originalName + ".gz";
	}

	private String replaceCompressedFileExtension(final String originalName) {
		if (originalName.endsWith(".gz")) {
			return originalName.replaceAll(".gz", "");
		}

		return originalName;
	}
	

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
