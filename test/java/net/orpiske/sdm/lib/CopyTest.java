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
package net.orpiske.sdm.lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import net.orpiske.sdm.lib.io.IOUtil;
import net.orpiske.ssps.common.digest.Sha1Digest;

import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class CopyTest {
	
	
	@Test
	public void testCopyRule() {
		
		
		String from = getClass().getResource("dummy-a.txt").getPath();
		String to = getClass().getResource(".").getPath() + File.separator 
				+ "dummy-b.txt";
		
		
		try {
			IOUtil.copy(from, to);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		
		String hashTo = null;
		try {
			Sha1Digest digest = new Sha1Digest();
			
			hashTo = digest.calculate(to);
			
			assertEquals("829c3804401b0727f70f73d4415e162400cbe57b", 
					hashTo.toLowerCase());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
