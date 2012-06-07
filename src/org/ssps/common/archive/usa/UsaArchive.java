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
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.exceptions.SspsException;

public class UsaArchive implements SspsArchive {

    private static final Logger logger = Logger.getLogger(UsaArchive.class);

    /**
     * Silently closes a stream (ignores exceptions).
     * 
     * @param outStream
     */
    private void silentlyCloseStream(final OutputStream outStream) {
	if (outStream == null) {
	    return;
	}

	try {
	    outStream.close();
	} catch (IOException e) {
	    logger.error("Unable to close stream", e);
	}
    }

    /**
     * Silently closes a stream (ignores exceptions).
     * 
     * @param inputStream
     */
    private void silentlyCloseStream(final InputStream inputStream) {
	if (inputStream == null) {
	    return;
	}

	try {
	    inputStream.close();
	} catch (IOException e) {
	    logger.error("Unable to close stream", e);
	}
    }

    /**
     * Lower level pack operation
     * 
     * @param source
     *            source file
     * @param destination
     *            destination file
     * @return
     * @throws ArchiveException
     * @throws IOException
     */
    private long pack(String source, File destination) throws ArchiveException,
	    IOException {

	ArchiveStreamFactory factory = new ArchiveStreamFactory();

	OutputStream outStream = new FileOutputStream(destination);

	ArchiveOutputStream outputStream;
	try {
	    outputStream = factory.createArchiveOutputStream(
		    ArchiveStreamFactory.TAR, outStream);
	} catch (ArchiveException e) {
	    silentlyCloseStream(outStream);

	    throw e;
	}

	File startDirectory = new File(source);

	RecursiveArchiver archiver = new RecursiveArchiver(outputStream);
	try {
	    archiver.archive(startDirectory);
	    outputStream.flush();
	    outputStream.close();

	    outStream.close();
	    outputStream.close();

	} catch (IOException e) {
	    silentlyCloseStream(outStream);
	    silentlyCloseStream(outputStream);

	    throw e;
	}

	long ret = outputStream.getBytesWritten();
	logger.info("Packed " + ret + " bytes");

	return ret;
    }

    private long compress(File source, File destination)
	    throws CompressorException, IOException {
	CompressorStreamFactory factory = new CompressorStreamFactory();
	OutputStream outStream = new FileOutputStream(destination);
	CompressorOutputStream gzippedOut = null;
	
	try {
	    gzippedOut = factory.createCompressorOutputStream(
		    CompressorStreamFactory.GZIP, outStream);
	} catch (CompressorException e) {
	    silentlyCloseStream(gzippedOut);

	    throw e;
	}

	FileInputStream fin = null;
	BufferedInputStream bin = null;

	try {
	    fin = new FileInputStream(source);
	    bin = new BufferedInputStream(fin);
	    
	    IOUtils.copy(bin, gzippedOut);

	    gzippedOut.flush();
	    gzippedOut.close();

	    bin.close();
	    fin.close();
	    outStream.close();
	} catch (IOException e) {
	    silentlyCloseStream(gzippedOut);
	    silentlyCloseStream(bin);
	    silentlyCloseStream(fin);
	    silentlyCloseStream(outStream);

	    throw e;
	}

	return 0;
    }

    private long uncompress(File source, File destination) throws IOException {
	FileOutputStream out = new FileOutputStream(destination);

	FileInputStream fin = null;
	BufferedInputStream bin = null;
	GzipCompressorInputStream gzIn = null;
	
	try { 
	    fin = new FileInputStream(source);
	    bin = new BufferedInputStream(fin);
	    gzIn = new GzipCompressorInputStream(bin);

	    IOUtils.copy(gzIn, out);

	    gzIn.close();

            fin.close();
            bin.close();
            out.close();
	}
	catch (IOException e) {
	    silentlyCloseStream(out);
	    
	    silentlyCloseStream(fin);
	    silentlyCloseStream(bin);
	    silentlyCloseStream(gzIn);
	    
	    throw e;
	}

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

    private long unpack(File source, File destination) throws SspsException, ArchiveException, IOException {
	ArchiveStreamFactory factory = new ArchiveStreamFactory();
	
	
	FileInputStream inputFileStream = new FileInputStream(source);

	ArchiveInputStream archiveStream;
	try {
	    archiveStream = factory.createArchiveInputStream(
	    	ArchiveStreamFactory.TAR, inputFileStream);
	} catch (ArchiveException e) {
	  silentlyCloseStream(inputFileStream);
	  
	  throw e;
	}

	OutputStream outStream = null;
	try {
        	TarArchiveEntry entry = (TarArchiveEntry) archiveStream.getNextEntry();
        	
        	while (entry != null) {
        	    File outFile = new File(destination, entry.getName());
        
        	    if (entry.isDirectory()) {
        		if (!outFile.exists()) {
        		    if (!outFile.mkdirs()) {
        			throw new SspsException("Unable to create directory: "
        				+ outFile.getPath());
        		    }
        		} else {
        		    logger.warn("Directory " + outFile.getPath()
        			    + " already exists. Ignoring ...");
        		}
        	    } else {
        		outStream = new FileOutputStream(outFile);
        
        		IOUtils.copy(archiveStream, outStream);
        		outStream.close();
        	    }
        
        	    entry = (TarArchiveEntry) archiveStream.getNextEntry();
        	}
        	
        	inputFileStream.close();
        	archiveStream.close();
	}
	catch (IOException e) {
	    silentlyCloseStream(outStream);
	    silentlyCloseStream(inputFileStream);
	    silentlyCloseStream(archiveStream);
	    
	    throw e;
	}

	return archiveStream.getBytesRead();
    }

    public long pack(String source, String destination) throws SspsArchiveException {
	File archiveFile = new File(getArchiveFileExtension(destination));
	
	try {
	    pack(source, archiveFile);
	} catch (ArchiveException e1) {
	    throw new SspsArchiveException("Unable to archive file", e1);
	} catch (IOException e1) {
	    throw new SspsArchiveException("Unable to archive file: I/O error",
		    e1);
	}

	File compressedFile = new File(getCompressedFileExtension(destination));

	try {
	    return compress(archiveFile, compressedFile);
	} catch (CompressorException e) {
	    throw new SspsArchiveException("Unable to compress archive file", e);
	} catch (IOException e) {
	    throw new SspsArchiveException(
		    "Unable to compress archive file: I/O error", e);
	} 
    }

    public long unpack(String source, String destination) throws SspsArchiveException {
	File compressedFileSource = new File(source);
	
	String uncompressedArchiveFileName = destination
		+ File.separator
		+ replaceCompressedFileExtension(compressedFileSource.getName());
	
	File uncompressedArchiveFile = new File(uncompressedArchiveFileName);
	if (uncompressedArchiveFile.exists()) {
	    throw new SspsArchiveException("A previously uncompressed file exists: "
		    + uncompressedArchiveFile);
	}

	logger.info("Unpacking " + source + " to "
		+ uncompressedArchiveFile.getPath());
	
	try {
	    uncompress(compressedFileSource, uncompressedArchiveFile);
	} catch (IOException e) {
	    throw new SspsArchiveException(
		    "Unable to uncompress archive file: I/O error", e);
	}

	File destinationDirectory = new File(destination);

	try {
	    return unpack(uncompressedArchiveFile, destinationDirectory);
	} catch (SspsException e) {
	    throw new SspsArchiveException("Unable to unpack file", e);
	} catch (ArchiveException e) {
	    throw new SspsArchiveException("Unable to unpack file", e);
	} catch (IOException e) {
	    throw new SspsArchiveException(
		    "Unable to uncompress archive file: I/O error", e);
	}
	finally {
	    if (uncompressedArchiveFile.exists()) {
		uncompressedArchiveFile.delete();
	    }
	}

    }
}
