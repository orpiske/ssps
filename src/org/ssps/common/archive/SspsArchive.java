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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;


/**
 * SSPS Archive interface
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public interface SspsArchive {
	
	/**
	 * Packs all the files in 'directory' into the 'destination' file
	 * @param destination The destination file name. Must provide the full path 
	 * @param source The source directory containing the archive files
	 * @return The number of bytes of the packed file
	 * @throws FileNotFoundException 
	 * @throws ArchiveException 
	 * @throws IOException 
	 */
	long pack(final String destination, final String source) throws FileNotFoundException, ArchiveException, IOException;
	
	
	/**
	 * Unpack 'file' to 'directory'
	 * @param file The packed file
	 * @param source The destination directory
	 * @return The number of bytes unpacked
	 * @throws IOException 
	 * @throws ArchiveException 
	 */
	long unpack(final String file, final String source) throws ArchiveException, IOException;
	
}
