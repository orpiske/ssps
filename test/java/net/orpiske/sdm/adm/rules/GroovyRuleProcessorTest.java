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

import net.orpiske.sdm.engine.Engine;
import net.orpiske.sdm.engine.GroovyEngine;
import net.orpiske.sdm.engine.exceptions.EngineException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class GroovyRuleProcessorTest {
	
	
	@Test
	@Ignore
	public void testSimpleFile() throws EngineException {
		Engine engine = new GroovyEngine();
		String path = getClass().getResource("dummy.groovy").getPath();
		
		engine.run(path);
	}


}
