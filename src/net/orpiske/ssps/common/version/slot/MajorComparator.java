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

public class MajorComparator implements SlotComparator {

	@Override
	public int compare(String v1, String v2) {
		String[] parts1;
		String[] parts2;
		
		if (v1 == null && v2 != null) {
			return LESS_THAN;
		}
		else {
			if (v1 != null && v2 == null) {
				return GREATER_THAN;
			}
		}
		
		parts1 = v1.split("\\.");
		parts2 = v2.split("\\.");
		
		return parts1[0].compareTo(parts2[0]);
	}

}
