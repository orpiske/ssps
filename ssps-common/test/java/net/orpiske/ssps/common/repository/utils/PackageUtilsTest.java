/**
   Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.ssps.common.repository.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class PackageUtilsTest {

	@Test
	public void testGetGroupId() {
		String groupId = PackageUtils.getGroupId("net.orpiske/sdm");
		
		assertEquals("net.orpiske", groupId);
	}
	
	@Test
	public void testGetGroupIdWithRepository() {
		String groupId = PackageUtils.getGroupId("default/net.orpiske/sdm");
		
		assertEquals("net.orpiske", groupId);
	}

	@Test
	public void testGetPackageName() {
		String packageName = PackageUtils.getPackageName("net.orpiske/sdm");
		
		assertEquals("sdm", packageName);
	}
	
	@Test
	public void testGetPackageNameWithRepository() {
		String packageName = PackageUtils.getPackageName("default/net.orpiske/sdm");
		
		assertEquals("sdm", packageName);
	}

}
