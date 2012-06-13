package org.ssps.sdm.adm.rules;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.ssps.sdm.adm.exceptions.RuleException;

import net.orpiske.ssps.adm.CopyRule;

public class CopyRuleProcessor extends AbstractRuleProcessor {

	@Override
	public void run(Object object) throws RuleException {
		run((CopyRule) object);
	}
	
	private void run(CopyRule rule) throws RuleException {
		String from = rule.getFrom();
		String to = rule.getTo();
		
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
