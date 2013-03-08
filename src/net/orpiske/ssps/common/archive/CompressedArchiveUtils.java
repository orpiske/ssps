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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;


import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.io.IOUtils;

/**
 * Utilites for manipulating compressed archives
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class CompressedArchiveUtils {
	
	/**
	 * Default constructor
	 */
	private CompressedArchiveUtils() {}
	
	/**
	 * Prepares the destination directory by creating directories if needed 
	 * @param destination
	 * @throws IOException
	 */
	public static void prepareDestination(File destination)
			throws IOException {
		File parent = destination.getParentFile();
		
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

	/**
	 * Compress a file
	 * 
	 * @param source
	 *            source file
	 * @param destination
	 *            destination file
	 * @param format
	 *            compression format
	 * @throws CompressorException if the archiver name is not known
	 * @throws IOException for lower level I/O errors
	 */
	public static void compress(File source, File destination, String format)
			throws CompressorException, IOException {
		CompressorStreamFactory factory = new CompressorStreamFactory();
		OutputStream outStream = new FileOutputStream(destination);
		CompressorOutputStream compressedOut = null;
	
		try {
			compressedOut = factory
					.createCompressorOutputStream(format, outStream);
		} catch (CompressorException e) {
			IOUtils.closeQuietly(compressedOut);
	
			throw e;
		}
	
		FileInputStream fin = null;
		BufferedInputStream bin = null;
	
		try {
			fin = new FileInputStream(source);
			bin = new BufferedInputStream(fin);
	
			IOUtils.copy(bin, compressedOut);
	
			compressedOut.flush();
			compressedOut.close();
	
			bin.close();
			fin.close();
			outStream.close();
		} catch (IOException e) {
			IOUtils.closeQuietly(compressedOut);
			IOUtils.closeQuietly(bin);
			IOUtils.closeQuietly(fin);
			IOUtils.closeQuietly(outStream);
	
			throw e;
		}
	}

	/**
	 * Uncompress a file
	 * @param source the source file to be uncompressed
	 * @param destination the destination directory
	 * @return the number of bytes read
	 * @throws IOException for lower level I/O errors
	 */
	public static long gzUncompress(File source, File destination) throws IOException {
		FileOutputStream out;
		
		prepareDestination(destination);
		out = new FileOutputStream(destination);
	
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
		} catch (IOException e) {
			IOUtils.closeQuietly(out);
	
			IOUtils.closeQuietly(fin);
			IOUtils.closeQuietly(bin);
			IOUtils.closeQuietly(gzIn);
	
			throw e;
		}
	
		return gzIn.getBytesRead();
	}
	
	/**
	 * Uncompress a file
	 * @param source the source file to be uncompressed
	 * @param destination the destination directory
	 * @return the number of bytes read
	 * @throws IOException for lower level I/O errors
	 */
	public static long bzipUncompress(File source, File destination) throws IOException {
		FileOutputStream out; 
		
		prepareDestination(destination);
		
		out = new FileOutputStream(destination);
	
		FileInputStream fin = null;
		BufferedInputStream bin = null;
		BZip2CompressorInputStream bzIn = null;
	
		try {
			fin = new FileInputStream(source);
			bin = new BufferedInputStream(fin);
			bzIn = new BZip2CompressorInputStream(bin);
	
			IOUtils.copy(bzIn, out);
	
			bzIn.close();
	
			fin.close();
			bin.close();
			out.close();
		} catch (IOException e) {
			IOUtils.closeQuietly(out);
	
			IOUtils.closeQuietly(fin);
			IOUtils.closeQuietly(bin);
			IOUtils.closeQuietly(bzIn);
	
			throw e;
		}
	
		return bzIn.getBytesRead();
	}
	
	
	/**
	 * Uncompress a file
	 * @param source the source file to be uncompressed
	 * @param destination the destination directory
	 * @return the number of bytes read
	 * @throws IOException for lower level I/O errors
	 */
	public static long zipUncompress(File source, File destination) throws IOException {
		prepareDestination(destination);
		
		ZipFile zipFile = new ZipFile(source);
		
		Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
		ZipArchiveEntry entry = entries.nextElement();
		
		while (entry != null) {
			InputStream fin = zipFile.getInputStream(entry);
			BufferedInputStream bin = new BufferedInputStream(fin);
			
			FileOutputStream out = new FileOutputStream(destination);	
			
			IOUtils.copy(fin, out);
			
			IOUtils.closeQuietly(out);
		
			IOUtils.closeQuietly(fin);
			IOUtils.closeQuietly(bin);
		}
		
		return 0;
	}
}
