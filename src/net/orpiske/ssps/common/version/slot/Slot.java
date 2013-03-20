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
package net.orpiske.ssps.common.version.slot;

import net.orpiske.ssps.common.version.Version;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Slot {
	private SlotComparator slotComparator;
	
	public Slot() {
		slotComparator = SlotComparatorFactory.create(SlotComparatorFactory.DEFAULT_SLOT);
	}
	
	
	public Slot(SlotComparator slotComparator) {
		this.slotComparator = slotComparator;
	}
	
	public Slot(final String slotMask) {
		slotComparator = SlotComparatorFactory.create(slotMask);
	}
	
	
	public boolean fits(Version v1, Version v2) {
		return slotComparator.fits(v1.getValue(), v2.getValue());
	}

}
