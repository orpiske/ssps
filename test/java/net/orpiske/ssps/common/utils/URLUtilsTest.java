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
package net.orpiske.ssps.common.utils;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import net.orpiske.ssps.common.utils.URLUtils;

import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class URLUtilsTest {

	/**
	 * Test method for {@link net.orpiske.ssps.common.utils.URLUtils#getFilename(java.net.URI)}.
	 * @throws MalformedURLException
	 * @throws URISyntaxException 
	 */
	@Test
	public void testGetFilename() throws MalformedURLException, URISyntaxException {
		String filename = URLUtils.getFilename(
			"http://www.orpiske.net/ssps/files/0.1.x/adm/jackrabbit-2.4.2.xml");
		
		assertEquals("jackrabbit-2.4.2.xml", filename);
	}
	
	
	/**
	 * Test method for {@link net.orpiske.ssps.common.utils.URLUtils#getFilename(java.net.URI)}.
	 * @throws MalformedURLException
	 * @throws URISyntaxException 
	 */
	@Test
	public void testGetFilenameWithVariables() throws MalformedURLException, URISyntaxException {
		String filename = URLUtils.getFilename(
			"http://www.orpiske.net/abc.php?c=123&d=456");
		
		assertEquals("abc.php", filename);
	}
	
	
	/**
	 * Test method for {@link net.orpiske.ssps.common.utils.URLUtils#getFilename(java.net.URI)}.
	 * @throws MalformedURLException
	 * @throws URISyntaxException 
	 */
	@Test
	public void testRemoveParametersFromName() throws MalformedURLException, URISyntaxException {
		String filename = URLUtils.removeParametersFromName(
			"abc.php?c=123&d=456");
		
		assertEquals("abc.php", filename);
	}

}
