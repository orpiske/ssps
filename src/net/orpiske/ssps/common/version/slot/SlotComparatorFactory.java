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
package net.orpiske.ssps.common.version.slot;

public final class SlotComparatorFactory {
	public static final String DEFAULT_SLOT = "*";
	public static final String MAJOR = "n.*";
	public static final String MAJOR_MINOR = "n.n.*";
	
	public static SlotComparator create(final String mask) {
		if (mask.equals(MAJOR)) {
			return new MajorComparator();
		}
		
		if (mask.equals(MAJOR_MINOR)) {
			return new MajorMinorComparator();
		}
		
		return new DefaultComparator();
	}

}
