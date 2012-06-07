package org.ssps.common.archive.usa;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.ssps.common.archive.SspsArchive;

public class UsaArchive implements SspsArchive {
	
	private static final Logger logger = Logger.getLogger(UsaArchive.class);
	
	private long pack(File destination, String source) throws ArchiveException, IOException {
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		
		OutputStream outStream = new FileOutputStream(destination);
		
		ArchiveOutputStream outputStream = 
				factory.createArchiveOutputStream(
						ArchiveStreamFactory.TAR, outStream);
		
		if (outputStream == null) {
			logger.error("Unable to create a new archive output stream: null object");
			
			return -1;
		}
		
		File startDirectory = new File(source);
		
		RecursiveArchiver archiver = new RecursiveArchiver(outputStream);
		archiver.archive(startDirectory);
		
		outputStream.flush();
		outputStream.close();
		
		return outputStream.getBytesWritten();
	}
	
	private long compress(File destination, File source) throws CompressorException, IOException {
		CompressorStreamFactory factory = new CompressorStreamFactory();
		
		OutputStream outStream = new FileOutputStream(destination);
		FileInputStream fin = new FileInputStream(source);
		BufferedInputStream bin = new BufferedInputStream(fin);
		
		CompressorOutputStream gzippedOut = 
				factory.createCompressorOutputStream(
						CompressorStreamFactory.GZIP, outStream);
		
		IOUtils.copy(bin, gzippedOut);
		
		gzippedOut.flush();
		gzippedOut.close();
		
		bin.close();
		fin.close();
		outStream.close();
		
		return 0;
	}
	
	private long uncompress(File destination, File source) throws IOException {
		FileInputStream fin = new FileInputStream(source);
		BufferedInputStream bin = new BufferedInputStream(fin);
		
		FileOutputStream out = new FileOutputStream(destination);
		
		GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bin);

		IOUtils.copy(gzIn, out);
		
		gzIn.close();
		
		fin.close();
		bin.close();
		out.close();
		
		return gzIn.getBytesRead();
	}
	
	protected String getArchiveFileExtension(final String originalName) {
		if (originalName.endsWith(".usa")) {
			return originalName;
		}
		
		return originalName + ".usa";
	}
	
	
	protected String getCompressedFileExtension(final String originalName) {
		if (originalName.endsWith(".ugz")) {
			return originalName;
		}
		
		return originalName + ".ugz";
	}
	
	protected String replaceCompressedFileExtension(final String originalName) {
		if (originalName.endsWith(".ugz")) {
			return originalName.replaceAll(".ugz", ".usa");
		}
		
		return originalName;
	}
	
	protected String replaceArchiveFileExtension(final String originalName) {
		if (originalName.endsWith(".usa")) {
			return originalName.replaceAll(".usa", "");
		}
		
		return originalName;
	}

	public long pack(String destination, String source) throws ArchiveException, IOException {
		File archiveFile = new File(getArchiveFileExtension(destination));
		
		long uncompressedProcessed = pack(archiveFile, source);
		logger.info("Processed " + uncompressedProcessed + " bytes");
		
		File compressedFile = new File(getCompressedFileExtension(destination));
		
		try {
			return compress(compressedFile, archiveFile);
		} catch (CompressorException e) {
			e.printStackTrace();
		}
		
		archiveFile.delete();
		
		return 0;
		
	}
	
	private long unpack(File source, File destination) throws ArchiveException, IOException {
		ArchiveStreamFactory factory = new ArchiveStreamFactory();
		
		OutputStream outStream = new FileOutputStream(destination);
		
		FileInputStream inputFileStream = new FileInputStream(source);
		
		ArchiveInputStream stream = 
				factory.createArchiveInputStream(ArchiveStreamFactory.TAR, 
						inputFileStream);
		
		IOUtils.copy(stream, outStream);
		
		inputFileStream.close();
		stream.close();
		outStream.close();
		
		return stream.getBytesRead();
	}

	public long unpack(String source, String destination) throws ArchiveException, IOException {
		File compressedFileSource = new File(source);
		File uncompressedArchiveFile = new File(destination + File.separator 
				+ replaceCompressedFileExtension(source));
		
		long size = uncompress(uncompressedArchiveFile, compressedFileSource);
		
		File destinationDirectory = new File(destination);
		
		
		return unpack(destinationDirectory, uncompressedArchiveFile);

	}

}
