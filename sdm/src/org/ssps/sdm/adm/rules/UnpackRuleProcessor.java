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

import net.orpiske.ssps.adm.UnpackRule;

import org.ssps.common.archive.Archive;
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.archive.tbz.TbzArchive;
import org.ssps.common.archive.tgz.TgzArchive;
import org.ssps.sdm.adm.AdmVariables;
import org.ssps.sdm.adm.exceptions.RuleException;

/**
 * Implements the unpack rule
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class UnpackRuleProcessor extends AbstractRuleProcessor {
	
	private AdmVariables admVariables = AdmVariables.getInstance();
	
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
			
			archive.unpack(source, destination);
		} catch (SspsArchiveException e) {
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

}
