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
package net.orpiske.sdm.adm.rules.support;

import java.io.File;

import org.apache.commons.io.filefilter.AbstractFileFilter;
import net.orpiske.sdm.adm.util.PrintUtils;

/**
 * Implements a file filter that checks whether a shield file exits. This file
 * filter works by checking whether a ${filenam}.shield file exists. If such 
 * file exists, then the input file is not accepted by the filter as it is 
 * "shielded".
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class ShieldFileFilter extends AbstractFileFilter {

	@Override
	public boolean accept(File dir, String name) {
		boolean isShielded = ShieldUtils.isShielded(dir, name);
		
		if (isShielded) {
			PrintUtils.printInfo("Ignoring shilded file " + name);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean accept(File file) {
		boolean isShielded = ShieldUtils.isShielded(file);
				
		if (isShielded) {
			PrintUtils.printInfo("Ignoring shilded file " + file.getName());
			return false;
		}
		
		return true;
	}

}
