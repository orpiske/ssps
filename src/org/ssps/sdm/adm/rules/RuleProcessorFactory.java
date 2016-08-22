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

import java.util.HashMap;

import net.orpiske.ssps.adm.CopyRule;
import net.orpiske.ssps.adm.DownloadRule;
import net.orpiske.ssps.adm.EchoRule;
import net.orpiske.ssps.adm.MkdirRule;
import net.orpiske.ssps.adm.ShieldRule;
import net.orpiske.ssps.adm.UnpackRule;

import org.ssps.sdm.adm.exceptions.RuleEngineException;


/**
 * Process rules
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
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
		rulesMap.put(MkdirRule.class, MkdirRuleProcessor.class);
		rulesMap.put(ShieldRule.class, ShieldRuleProcessor.class);
		rulesMap.put(DownloadRule.class, DownloadRuleProcessor.class);
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
