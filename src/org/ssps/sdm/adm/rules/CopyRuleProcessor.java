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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.orpiske.ssps.adm.CopyRule;

import org.apache.commons.io.IOUtils;
import org.ssps.sdm.adm.AdmVariables;
import org.ssps.sdm.adm.exceptions.RuleException;

public class CopyRuleProcessor extends AbstractRuleProcessor {
	
	private AdmVariables admVariables = AdmVariables.getInstance();

	@Override
	public void run(Object object) throws RuleException {
		run((CopyRule) object);
	}
	
	private void run(CopyRule rule) throws RuleException {
		String from = admVariables.evaluate(rule.getFrom());
		String to = admVariables.evaluate(rule.getTo());
		
		InputStream input;
		OutputStream output;
		
		try {
			input = new FileInputStream(from);
			output = new FileOutputStream(to);
			
			IOUtils.copy(input, output);
		} catch (FileNotFoundException e) {
			throw new RuleException("File not found", e);
		} catch (IOException e) {
			throw new RuleException("Input/output error", e);
		}
	}

}
