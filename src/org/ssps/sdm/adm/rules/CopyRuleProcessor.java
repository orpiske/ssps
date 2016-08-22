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
import java.io.FileNotFoundException;
import java.io.IOException;

import net.orpiske.ssps.adm.CopyRule;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.ssps.sdm.adm.AdmVariables;
import org.ssps.sdm.adm.exceptions.RuleException;
import org.ssps.sdm.adm.rules.support.ShieldAwareCopier;
import org.ssps.sdm.adm.rules.support.ShieldFileFilter;
import org.ssps.sdm.adm.rules.support.ShieldUtils;
import org.ssps.sdm.adm.util.PrintUtils;

/**
 * Implements the copy rule
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class CopyRuleProcessor extends AbstractRuleProcessor {
	
	private AdmVariables admVariables = AdmVariables.getInstance();

	@Override
	public void run(Object object) throws RuleException {
		run((CopyRule) object);
	}
	
	private void createParentDirectories(File dir) {
		if (!dir.exists()) {
			dir.getParentFile().mkdirs();
		}
	}
	
	
	private void run(CopyRule rule) throws RuleException {
		String from = admVariables.evaluate(rule.getFrom());
		String to = admVariables.evaluate(rule.getTo());
		
		try {
			File fromFile = new File(from);
			File toFile = new File(to);
			
			createParentDirectories(toFile);
			
			if (fromFile.isDirectory()) {
				
				ShieldAwareCopier copier = new ShieldAwareCopier(toFile);
				
				copier.copy(fromFile);
				
			}
			else {
				if (ShieldUtils.isShielded(toFile)) {
					PrintUtils.printInfo("Ignoring shilded file " + to);
				}
				else { 
					FileUtils.copyFile(fromFile, toFile);
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuleException("File not found", e);
		} catch (IOException e) {
			throw new RuleException("Input/output error", e);
		}
	}

}
