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
package net.orpiske.sdm.lib.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.util.ResourceExchangeFactory;
import net.orpiske.ssps.common.resource.Resource;
import net.orpiske.ssps.common.resource.ResourceExchange;
import net.orpiske.ssps.common.resource.ResourceInfo;
import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;
import net.orpiske.ssps.common.utils.URLUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Implements the download rule.
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Downloader {
	
	private static final Logger logger = Logger.getLogger(Downloader.class);
	
	private static void copy(final Resource<InputStream> resource,
			final OutputStream output) throws IOException {
		InputStream input = resource.getPayload();

		long i = 0;
		long total = resource.getResourceInfo().getSize();

		for (i = 0; i < total; i++) {
			output.write(input.read());

			if ((i % (1024 * 512)) == 0) {
				double percentComplete = 0;
				
				if (i > 0) {
					percentComplete = i / (total /100);
				}
				else {
					percentComplete = 0.0;
				}
					
				System.out.print("\r" + percentComplete + "% complete (" + i + " of " + total);
			}
		}
		
		System.out.print("\r100% complete (" + i + " of " + total + "\n");

		output.flush();

	}

	

	/**
	 * Setups the output file
	 * @param rule
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private static File setupOutputFile(String url, boolean overwrite)
			throws MalformedURLException, URISyntaxException, IOException {
		String fileName = URLUtils.getFilename(url);
		String workDir = WorkdirUtils.getWorkDir();
		String fullName = workDir + File.separator + fileName;

		File outputFile = new File(fullName);

		outputFile.getParentFile().mkdirs();
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		} else {
			if (overwrite) {
				outputFile.delete();
				outputFile.createNewFile();
			} else {
				logger.info("Destination file " + fullName + " already exists");
			}
		}
		return outputFile;
	}
	
	/**
	 * Saves the downloaded file
	 * @param outputFile
	 * @param resourceInfo
	 * @param resource
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void saveDownload(File outputFile, Resource<InputStream> resource)
			throws FileNotFoundException, IOException {
		FileOutputStream output = null;
		
		try {
			output = new FileOutputStream(outputFile);

			copy(resource, output);
			outputFile.setLastModified(resource.getResourceInfo()
					.getLastModified());
		} finally {

			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(resource.getPayload());
		}
	}
	
	
	
	/**
	 * Download a file
	 * @param url the URL to the file
	 * @param overwrite whether or not to overwrite existent files
	 * @throws ResourceExchangeException if uanble to download the file
	 */
	public static void download(final String url, boolean overwrite) throws ResourceExchangeException {
		try {
			URI uri = new URI(url);

			File outputFile = setupOutputFile(url, overwrite);

			ResourceExchange resourceExchange = ResourceExchangeFactory.newResourceExchange();
			ResourceInfo resourceInfo = resourceExchange.info(uri);

			try {
				long outSize = FileUtils.sizeOf(outputFile);
				long sourceSize = resourceInfo.getSize();

				if (sourceSize == outSize) {
					logger.info("Destination file and source file appears to be " + 
							"the same. Using cached file instead.");

					return;
				} else {
					Resource<InputStream> resource = resourceExchange.get(uri);
					
					saveDownload(outputFile, resource);
					
					if (logger.isDebugEnabled()) { 
						logger.debug("Downloaded " + outputFile.getPath());
					}
				}
			} finally {
				logger.debug("Releasing resources");
				resourceExchange.release();
			}
		} catch (URISyntaxException e) {
			throw new ResourceExchangeException("Invalid URI: " + url, e);
		} catch (IOException e) {
			throw new ResourceExchangeException("I/O error: " + e.getMessage(), e);
		}

	}
	
	
	/**
	 * Download a file overwriting existent ones
	 * @param url the URL to the file
	 * @throws ResourceExchangeException if uanble to download the file
	 */
	public static void download(final String url) throws ResourceExchangeException {
		download(url, false);
	}
}
