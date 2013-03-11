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

import net.orpiske.ssps.common.version.slot.SlotComparator;
import net.orpiske.ssps.common.version.slot.SlotComparatorFactory;

public class ComparisonStrategy {
	
	private String mask;
	
	public ComparisonStrategy(final String mask) {
		this.mask = mask;
	}
 	
	
	public boolean slotMatches(final String[] v1, final String[] v2) {
		SlotComparator comparator = SlotComparatorFactory.create(mask);
		
		return comparator.compare(v1, v2);
	}
	

}
