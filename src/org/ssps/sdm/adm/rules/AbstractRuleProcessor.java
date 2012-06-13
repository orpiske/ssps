package org.ssps.sdm.adm.rules;

import org.ssps.sdm.adm.exceptions.RuleException;

import net.orpiske.ssps.adm.AbstractRule;

public abstract class AbstractRuleProcessor {
	
	public abstract void run(Object rule) throws RuleException;
}
