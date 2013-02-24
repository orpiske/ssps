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

import static org.junit.Assert.fail;
import net.orpiske.sdm.adm.exceptions.RuleException;
import net.orpiske.sdm.adm.rules.GroovyRuleProcessor;
import net.orpiske.ssps.adm.GroovyRule;

import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class GroovyRuleProcessorTest {
	
	@Test
	public void testSimpleFile() {
		GroovyRule rule = new GroovyRule();
		
		String file = getClass().getResource("hello.groovy").getPath();
		
		rule.setFile(file);
		
		GroovyRuleProcessor processor = new GroovyRuleProcessor();
		
		try {
			processor.run(rule);
		} catch (RuleException e) {
			fail(e.getMessage());
		}
	}

}
