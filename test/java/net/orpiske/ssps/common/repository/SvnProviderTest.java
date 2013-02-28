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
package net.orpiske.ssps.common.repository;

import java.io.IOException;

import net.orpiske.ssps.common.repository.exception.RepositoryUpdateException;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SvnProviderTest {
	
	@Before
	public void init() throws ConfigurationException {
		// RepositorySettings.initConfiguration();
	}
	
	@Test 
	public void cloneTest() throws IOException, RepositoryUpdateException, ConfigurationException {
		
		
		// SvnProvider provider = new SvnProvider("test");
		
		// provider.update();
	}

}
