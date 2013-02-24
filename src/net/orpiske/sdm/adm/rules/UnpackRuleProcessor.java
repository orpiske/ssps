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
package net.orpiske.sdm.adm.rules;

import static net.orpiske.sdm.adm.util.PrintUtils.staticWarningMessage;

import java.io.File;

import net.orpiske.sdm.adm.exceptions.RuleException;
import net.orpiske.ssps.adm.UnpackRule;
import net.orpiske.ssps.common.archive.Archive;
import net.orpiske.ssps.common.archive.exceptions.SspsArchiveException;
import net.orpiske.ssps.common.archive.tbz.TbzArchive;
import net.orpiske.ssps.common.archive.tgz.TgzArchive;
import net.orpiske.ssps.common.variables.VariablesParser;

import org.apache.commons.io.FileUtils;

/**
 * Implements the unpack rule
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class UnpackRuleProcessor extends AbstractRuleProcessor {
	
	public static final String DESTINATION_DIRECTORY = "${workdir}/${name}-${version}";
	
	private VariablesParser admVariables = VariablesParser.getInstance();
	
	
	
	
	private void cleanup(UnpackRule rule) {
		staticWarningMessage("Cleaning up due to errors");
		
		String destination = admVariables.evaluate(rule.getDestination());
		
		if (destination == null) {
			destination = admVariables.evaluate(DESTINATION_DIRECTORY);
		}
		
		File orphanDirectory = new File(destination);
		
		FileUtils.deleteQuietly(orphanDirectory);
	}
	
	private void run(UnpackRule rule) throws RuleException {
		Archive archive;
		
		if (rule.getFormat().equals("tbz2")) {
			archive = new TbzArchive();
		}
		else {
			if (rule.getFormat().equals("tgz")) {
				archive = new TgzArchive();
			}
			else {
				throw new RuleException("Unsupported format: " + rule.getFormat());
			}
		}
		
		try {
			String source = admVariables.evaluate(rule.getSource());
			String destination = admVariables.evaluate(rule.getDestination());
			
			if (destination == null) {
				destination = admVariables.evaluate(DESTINATION_DIRECTORY);
			}
			else {
				staticWarningMessage("Usage of 'destination' is discouraged");
			}
			
			archive.unpack(source, destination);
		} catch (SspsArchiveException e) {
			cleanup(rule);
			
			throw new RuleException(e.getMessage(), e);
 
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#run(java.lang.Object)
	 */
	@Override
	public void run(Object rule) throws RuleException {
		run((UnpackRule) rule);
		
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.adm.rules.AbstractRuleProcessor#cleanup(java.lang.Object)
	 */
	@Override
	protected void cleanup(Object rule) throws RuleException {
		cleanup((UnpackRule) rule);
	}

	
}
