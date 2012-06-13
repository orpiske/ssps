package org.ssps.sdm.adm.rules;

import net.orpiske.ssps.adm.EchoRule;

import org.ssps.sdm.adm.exceptions.RuleException;

public class EchoRuleProcessor extends AbstractRuleProcessor {
	
	private void run(EchoRule rule) {
		String level = rule.getLevel();
		
		if (level == null) {
			level = "info";
		}
		
		System.out.println("[" + level.toUpperCase() + "]: " + rule.getMessage());
	}

	@Override
	public void run(Object rule) throws RuleException {
		run((EchoRule) rule);
	}

}
