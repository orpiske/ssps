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
package net.orpiske.sdm.lib.io;

import java.io.File;
import java.io.IOException;

import net.orpiske.sdm.lib.io.support.ShieldAwareCopier;
import net.orpiske.sdm.lib.io.support.ShieldUtils;

import org.apache.commons.io.FileUtils;

/**
 * Implements the copy rule.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class IOUtil {
	
	private static boolean createParentDirectories(File dir) {
		if (!dir.getParentFile().exists()) {
			return dir.getParentFile().mkdirs();
		}
		
		return true;
	}
	
	
	/**
	 * Protects a file or resource from being written
	 * @param resource the resource to protect
	 * @throws IOException
	 */
	public static void shield(final String resource) throws IOException {
		File shieldedFile = new File(resource);
		
		if (!shieldedFile.exists()) {
			System.out.println("Resource " + resource + " does not exist");
			
			return;
		}
		
		File shielded = new File(resource + ShieldUtils.SHIELD_EXT);
	
		if (!shielded.exists()) {
			
			if (!shielded.createNewFile()) {
				System.err.println("Unable to create shield file " + shielded.getPath());
			}
				
			System.out.println("Resource " + resource + " was shielded");
		}
		else {
			System.out.println("Resource " + resource + " already shielded");
		}
		
		shielded.deleteOnExit();
	}
	
	
	/**
	 * Copies a file or directory
	 * @param from the source file or directory
	 * @param to the destination file or directory
	 * @throws IOException if the source is not found or there are access restrictions 
	 */
	public static void copy(final String from, final String to) throws IOException {
		copy(from, to, false);
	}
	
	
	/**
	 * Copies a file or directory
	 * @param from the source file or directory
	 * @param to the destination file or directory
	 * @param overwrite whether or not to overwrite the destination file/directory
	 * @throws IOException if the source is not found or there are access restrictions 
	 */
	public static void copy(final String from, final String to, boolean overwrite) throws IOException {
		File fromFile = new File(from);
		File toFile = new File(to);
		
		if (toFile.exists()) {
			if (!overwrite) {
				return;
			}
		}
		
		if (!fromFile.exists()) {
			throw new IOException("File or directory not found: " + from);
		}
		
		if (!createParentDirectories(toFile)) {
			throw new IOException("Unable to create parent directories: " + toFile.getParent());
		}
		
		if (fromFile.isDirectory()) {
			
			ShieldAwareCopier copier = new ShieldAwareCopier(toFile);
			
			copier.copy(fromFile);
			
		}
		else {
			if (ShieldUtils.isShielded(toFile)) {
				System.out.println("Ignoring shielded file " + to);
			}
			else { 
				FileUtils.copyFile(fromFile, toFile);
			}
		}
		
	}
	
	
	/**
	 * Creates a directory
	 * @param directory the directory to create
	 */
	public static boolean mkdir(final String directory) {
		File dir = new File(directory);
		
		if (!dir.exists()) { 
			return dir.mkdirs();
		}
		else {
			System.out.println("Directory " + directory + " already exists");
		}
		
		return false;
	}
}
