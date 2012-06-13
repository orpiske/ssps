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
package org.ssps.common.archive;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;

/**
 * Utilites for manipulating compressed archives
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class CompressedArchiveUtils {
	
	private static void validateDestination(File destination)
			throws IOException {
		File parent = destination.getParentFile();
		
		if (!parent.exists()) {
			parent.mkdirs();
		}
		
		if (destination.exists()) {
			if (destination.isDirectory()) {
				throw new IOException("The destination file is a directory");
			}
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
	 * @return
	 * @throws CompressorException
	 * @throws IOException
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
	 * @param source
	 * @param destination
	 * @return
	 * @throws IOException
	 */
	public static long gzUncompress(File source, File destination) throws IOException {
		FileOutputStream out;
		
		validateDestination(destination);
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
	 * @param source
	 * @param destination
	 * @return
	 * @throws IOException
	 */
	public static long bzipUncompress(File source, File destination) throws IOException {
		FileOutputStream out; 
		
		validateDestination(destination);
		
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

	

}
