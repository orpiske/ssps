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
package net.orpiske.ssps.common.version;

import static org.junit.Assert.*;

import org.junit.Test;

public class VersionComparatorTest {

	@Test
	public void testEquals() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.EQUALS);
	}
	
	
	@Test
	public void testLessThanFix() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0", "1.1.1");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	
	@Test
	public void testLessThanMinor() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0", "1.2.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	
	@Test
	public void testLessThanMajor() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0", "2.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	@Test
	public void testLessThanSize() {
		int ret = DefaultVersionComparator.compareStatic("1.1", "2.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	@Test
	public void testLessThanSnapshot() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0-SNAPSHOT", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	@Test
	public void testLessThanSnapshotSize() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0-SNAPSHOT", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	
	@Test
	public void testLessThanRC() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0-rc", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	@Test
	public void testLessThanRCSize() {
		int ret = DefaultVersionComparator.compareStatic("1.1.0-RC", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.LESS_THAN);
	}
	
	@Test
	public void testBiggerThanFix() {
		int ret = DefaultVersionComparator.compareStatic("1.1.1", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.GREATER_THAN);
	}
	
	
	@Test
	public void testBiggerThanMinor() {
		int ret = DefaultVersionComparator.compareStatic("1.2.0", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.GREATER_THAN);
	}
	
	
	@Test
	public void testBiggerThanMajor() {
		int ret = DefaultVersionComparator.compareStatic("2.1.0", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.GREATER_THAN);
	}
	
	@Test
	public void testBiggerThanSize() {
		int ret = DefaultVersionComparator.compareStatic("2.1", "1.1.0");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.GREATER_THAN);
	}
	
	@Test
	public void testBiggerThanSnapshot() {
		int ret = DefaultVersionComparator.compareStatic("2.1.0", "2.1.0-SNAPSHOT");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.GREATER_THAN);
	}
	
	@Test
	public void testBiggerThanSnapshotRc() {
		int ret = DefaultVersionComparator.compareStatic("2.1.0-RC", "2.1.0-SNAPSHOT");
		assertTrue("Versions did not match", ret == DefaultVersionComparator.GREATER_THAN);
	}

}
