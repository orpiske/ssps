package org.ssps.sdm.adm.rules;

import java.util.HashMap;

import net.orpiske.ssps.adm.CopyRule;
import net.orpiske.ssps.adm.EchoRule;
import net.orpiske.ssps.adm.UnpackRule;

import org.ssps.sdm.adm.exceptions.RuleEngineException;


public final class RuleProcessorFactory {
	
	private static HashMap<Class<?>, Class<? extends AbstractRuleProcessor>> 
		rulesMap;
	
	static {
		populateRulesMap();
	}
	
	
	private static void populateRulesMap() {
		rulesMap = new HashMap<Class<?>, Class<? extends AbstractRuleProcessor>>();
		
		rulesMap.put(UnpackRule.class, UnpackRuleProcessor.class);
		rulesMap.put(CopyRule.class, CopyRuleProcessor.class);
		rulesMap.put(EchoRule.class, EchoRuleProcessor.class);
	}
	
	public static AbstractRuleProcessor getRule(Class<?> type) {
		Class<? extends AbstractRuleProcessor> clazz = rulesMap.get(type);
		
		if (clazz == null) {
			return null;
		}
		
		try {
			AbstractRuleProcessor rule = clazz.newInstance();
			
			return rule;
		
			/*
			 * These exceptions should never be thrown by a properly setup 
			 * rule
			 */
		} catch (InstantiationException e) {
			throw new RuleEngineException("Invalid rule object", e);
		} catch (IllegalAccessException e) {
			throw new RuleEngineException("Invalid rule object", e);
		}
 	}
}
