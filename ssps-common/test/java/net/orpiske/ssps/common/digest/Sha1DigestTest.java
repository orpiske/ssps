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
package net.orpiske.ssps.common.digest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Sha1DigestTest {
	
	private static final String MESSAGE_FILE;
		
		
	static { 
		URL url = Sha1DigestTest.class.getResource("message.txt");
		
		MESSAGE_FILE = url.getPath();
	}
	
	private static final String MESSAGE_DIGEST = "6367c48dd193d56ea7b0baad25b19455e529f5ee";

	/**
	 * Test method for {@link net.orpiske.ssps.common.digest.Sha1Digest#calculate(java.io.InputStream)}.
	 * @throws IOException 
	 */
	@Test
	public void testCalculateInputStream() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("message.txt");
		
		Sha1Digest sha1Digest = new Sha1Digest();
		
		String ret = sha1Digest.calculate(inputStream);
		
		assertEquals("The message digest do not match", MESSAGE_DIGEST, ret);
	}


	/**
	 * Test method for {@link net.orpiske.ssps.common.digest.Sha1Digest#verify(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testVerify() throws IOException {		
		Sha1Digest sha1Digest = new Sha1Digest();
		
		boolean ret = sha1Digest.verify(MESSAGE_FILE, MESSAGE_DIGEST);
		
		assertTrue("The message digest do not match", ret);
	}

	/**
	 * Test method for {@link net.orpiske.ssps.common.digest.Sha1Digest#save(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testSave() throws IOException {
		Sha1Digest sha1Digest = new Sha1Digest();
		
		sha1Digest.save(MESSAGE_FILE);
		
		sha1Digest.verify(MESSAGE_FILE);
	}

}
