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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import net.orpiske.ssps.adm.DownloadRule;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.ssps.common.resource.DefaultResourceExchange;
import org.ssps.common.resource.ResourceExchange;
import org.ssps.common.resource.exceptions.ResourceExchangeException;
import org.ssps.sdm.adm.AdmVariables;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.utils.WorkdirUtils;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class DownloadRuleProcessor extends AbstractRuleProcessor {
	private AdmVariables admVariables = AdmVariables.getInstance();
	
	
	private void run(DownloadRule rule) throws RuleException {
		try {
			String url = admVariables.evaluate(rule.getUrl());
			URI uri = new URI(url);
			
			String fileName = uri.toURL().getFile();
			String workDir = WorkdirUtils.getWorkDir();
			
			File outputFile = new File(workDir + File.separator + fileName);
			
			outputFile.getParentFile().mkdirs();
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			else {
				outputFile.delete();
				outputFile.createNewFile();
			}
			
			FileOutputStream output = new FileOutputStream(outputFile);
			
			ResourceExchange resourceExchange = new DefaultResourceExchange();
			
			InputStream input = resourceExchange.get(uri);
			
			try { 
				IOUtils.copy(input, output);
			}
			finally { 
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
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

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#run(java.lang.Object)
	 */
	@Override
	public void run(Object rule) throws RuleException {
		run((DownloadRule) rule);
	}

}
