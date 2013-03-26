/**
   Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.ssps.common.archive.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;

import net.orpiske.ssps.common.archive.Archive;
import net.orpiske.ssps.common.archive.CompressedArchiveUtils;
import net.orpiske.ssps.common.archive.PermissionsUtils;
import net.orpiske.ssps.common.archive.exceptions.SspsArchiveException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;

/**
 * Zip archive support
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class ZipArchive implements Archive {

	
	private long extractFile(ZipFile zipFile, ZipArchiveEntry entry,
			File outFile) throws IOException, ZipException,
			FileNotFoundException {
		InputStream fin = null;
		BufferedInputStream bin = null;
		FileOutputStream out = null;

		long ret = 0;
		
		try { 
			fin = zipFile.getInputStream(entry);
			bin = new BufferedInputStream(fin);
			out = new FileOutputStream(outFile);

			ret += IOUtils.copy(fin, out);
			
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(bin);
			IOUtils.closeQuietly(fin);
		}
		finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(bin);
			IOUtils.closeQuietly(fin);
		}
		
		return ret;
	}
	
	/**
	 * Uncompress a file
	 * @param source the source file to be uncompressed
	 * @param destination the destination directory
	 * @return the number of bytes read
	 * @throws IOException for lower level I/O errors
	 */
	public long unpack(File source, File destination) throws IOException {
		ZipFile zipFile = null; 
		long ret = 0;
		
		try { 
			zipFile = new ZipFile(source);
			Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
			
			while (entries.hasMoreElements()) { 
				ZipArchiveEntry entry = entries.nextElement();
			
				File outFile = new File(destination.getPath() + File.separator 
						+ entry.getName());
				
				CompressedArchiveUtils.prepareDestination(outFile);
				
				if (!entry.isDirectory()) {
					ret += extractFile(zipFile, entry, outFile);
				}
				
				int mode = entry.getUnixMode();
				PermissionsUtils.setPermissions(mode, outFile);
			}
		}
		finally { 
			if (zipFile != null) { 
				zipFile.close();
			}
		}
		
		return ret;
	}

	

	@Override
	public long unpack(String source, String destination)
			throws SspsArchiveException {
		
		File sourceFile = new File(source);
		File destinationDir = new File(destination);
		long ret = 0;
		
		try {
			ret = unpack(sourceFile, destinationDir);
		} catch (IOException e) {
			throw new SspsArchiveException("Unable to unpack: " 
					+ e.getMessage(), e);
		}
		
		return ret;
	}

}
