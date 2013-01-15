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
package org.ssps.sdm.adm.rules;

import static org.ssps.sdm.adm.util.PrintUtils.printInfo;
import static org.ssps.sdm.adm.util.PrintUtils.staticInfoMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import net.orpiske.ssps.adm.DownloadRule;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.ssps.common.resource.DefaultResourceExchange;
import org.ssps.common.resource.Resource;
import org.ssps.common.resource.ResourceExchange;
import org.ssps.common.resource.ResourceInfo;
import org.ssps.common.resource.exceptions.ResourceExchangeException;
import org.ssps.common.utils.URLUtils;
import org.ssps.common.variables.VariablesParser;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.utils.WorkdirUtils;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class DownloadRuleProcessor extends AbstractRuleProcessor {
	private VariablesParser admVariables = VariablesParser.getInstance();

	private void copy(final Resource<InputStream> resource,
			final OutputStream output) throws IOException {
		InputStream input = resource.getPayload();

		long i = 0;

		for (i = 0; i < resource.getResourceInfo().getSize(); i++) {
			output.write(input.read());

			if ((i % (1024 * 512)) == 0) {
				staticInfoMessage("Downloaded " + i + " bytes out of "
						+ resource.getResourceInfo().getSize());
			}
		}
		staticInfoMessage("Downloaded " + i + " bytes out of "
				+ resource.getResourceInfo().getSize() + "\n");

		output.flush();

	}

	private String getFilename(final DownloadRule rule, final String url)
			throws MalformedURLException, URISyntaxException {
		String name = admVariables.evaluate(rule.getName());

		if (name != null) {
			return name;
		}

		return URLUtils.getFilename(url);
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
	private File setupOutputFile(DownloadRule rule, String url)
			throws MalformedURLException, URISyntaxException, IOException {
		String fileName = getFilename(rule, url);
		String workDir = WorkdirUtils.getWorkDir();
		String fullName = workDir + File.separator + fileName;

		File outputFile = new File(fullName);

		outputFile.getParentFile().mkdirs();
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		} else {
			if (rule.isOverwrite()) {
				outputFile.delete();
				outputFile.createNewFile();
			} else {
				printInfo("Destination file " + fullName + " already exists");
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
	private void saveDownload(File outputFile, Resource<InputStream> resource)
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

	private void run(DownloadRule rule) throws RuleException {
		try {
			String url = admVariables.evaluate(rule.getUrl());
			URI uri = new URI(url);

			File outputFile = setupOutputFile(rule, url);

			ResourceExchange resourceExchange = new DefaultResourceExchange();
			ResourceInfo resourceInfo = resourceExchange.info(uri);

			try {
				long outSize = FileUtils.sizeOf(outputFile);
				long sourceSize = resourceInfo.getSize();

				if (sourceSize == outSize) {
					printInfo("Destination file and source file appears to be the same");

					return;
				} else {
					Resource<InputStream> resource = resourceExchange.get(uri);
					
					saveDownload(outputFile, resource);
				}
			} finally {
				printInfo("Releasing resources");
				resourceExchange.release();
			}
		} catch (URISyntaxException e) {
			throw new RuleException("Invalid URI: " + rule.getUrl(), e);
		} catch (ResourceExchangeException e) {
			throw new RuleException("Unable to download file from "
					+ rule.getUrl() + ": " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuleException("I/O error: " + e.getMessage(), e);
		}

	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#run(java.lang.Object)
	 */
	@Override
	public void run(Object rule) throws RuleException {
		run((DownloadRule) rule);
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#cleanup(java.lang.Object)
	 */
	@Override
	protected void cleanup(Object rule) throws RuleException {
		// NOP
		
	}

}
