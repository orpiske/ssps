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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class NameConverterTest {

	@Test
	public void testNoChange() {
		String ret = NameConverter.sqlToProperty("test");
		
		assertEquals("test", ret);
	}
	
	@Test
	public void testUpperCaseSqlColumn() {
		String ret = NameConverter.sqlToProperty("TEST");
		
		assertEquals("test", ret);
	}
	
	
	@Test
	public void testDefaultComponsedSqlColumn() {
		String ret = NameConverter.sqlToProperty("test_id");
		
		assertEquals("testId", ret);
	}
	
	
	@Test
	public void testUpperCaseSqlComposedColumn() {
		String ret = NameConverter.sqlToProperty("TEST_ID");
		
		assertEquals("testId", ret);
	}
	
	
	@Test
	public void testCamelCaseSqlComposedColumn() {
		String ret = NameConverter.sqlToProperty("test_ID");
		
		assertEquals("testId", ret);
	}

}
