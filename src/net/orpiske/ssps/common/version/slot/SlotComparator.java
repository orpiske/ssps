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

/**
 * Describes the interface class for implementing slot comparators
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public interface SlotComparator {

	
	/**
	 * Checks whether a given version fits in the same slot as another. For 
	 * instance, given two versions "1.1.0" and "1.2.0", it checks whether the 
	 * the second should be considered an upgrade to the first. 
	 * 
	 * @param v1 The version to check against
	 * @param v2 The version to check into the slot
	 * @return true if v2 fits into the same slot as v1.
	 */
	boolean fits(final String v1, final String v2);
}
