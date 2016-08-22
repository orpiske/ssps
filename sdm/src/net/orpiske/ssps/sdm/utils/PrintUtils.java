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
package net.orpiske.ssps.sdm.utils;

import java.util.List;

import net.orpiske.ssps.common.dependencies.Dependency;
import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.sdm.update.Upgradeable;

/**
 * Print utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class PrintUtils {
	
	private PrintUtils() {}

		
	
	/**
	 * Prints parseable package information
	 * @param packageInfo package information object
	 */
	public static void printParseable(final PackageInfo packageInfo) {
		System.out.printf("%-15s => %-32s => %-15s => %s%n",
				packageInfo.getGroupId(), packageInfo.getName(), packageInfo.getVersion(),
				packageInfo.getPath());
	}
	
	
	/**
	 * Prints a list of packages
	 * @param list the list of packages
	 */
	public static void printPackageList(final List<PackageInfo> list) {
		System.out.printf("%-15s    %-32s    %-15s    %s%n",
					"Group ID", "Package Name", "Version", "Path");
				
		for (PackageInfo packageInfo : list) {
			printParseable(packageInfo);
		}
	}
	
	
	
	/**
	 * Prints parseable software inventory record information
	 * @param dto an inventory dto object
	 */
	public static void printParseable(final SoftwareInventoryDto dto) {
		System.out.printf("%-15s => %-20s => %-15s => %-1s => %-23s => %s%n",
				dto.getGroupId(), dto.getName(), dto.getVersion(),
				dto.getType(), dto.getInstallDate(), dto.getInstallDir());
	}
	
	
	/**
	 * Prints a list of packages candidate for upgrade
	 * @param list the list of packages
	 */
	public static void printUpgradeable(final List<Upgradeable> list) {
		if (list.size() == 0) {
			System.out.println("No packages to upgrade");
			
			return;
		}
		
		
		System.out.println("These are all the packages candidates for upgrade");
		
		System.out.printf("%-15s    %-32s    %-15s    %-15s%n",
				"Group ID", "Package Name", "Older", "Newer");
		
		
		for (Upgradeable up : list) { 
			for (PackageInfo candidate : up.getCandidates()) { 
				System.out.printf("%-15s => %-32s => %-9s => %-9s%n",
						up.getDto().getGroupId(), up.getDto().getName(), 
						up.getDto().getVersion(), candidate.getVersion());
			}
		}
		
	}
		
	
	/**
	 * Prints a list of software inventory records
	 * @param list a list of software inventory records
	 */
	public static void printInventoryList(final List<SoftwareInventoryDto> list) {
		
		System.out.printf("%-15s    %-20s    %-15s  %-23s%n",
				"Group ID", "Package Name", "Version", "Install Date", 
				"Install Dir");
	
		
		for (SoftwareInventoryDto dto : list) {
			printParseable(dto);
		}
	}



	/**
	 * Prints parseable package information
	 * @param packageInfo package information object
	 */
	private static void printParseableSub(final PackageInfo packageInfo, int level) {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < level; i++) {
			builder.append('-');
		} 
		
		System.out.printf("%s%-15s => %-32s => %-15s => %s%n",
				builder.toString(), packageInfo.getGroupId(), packageInfo.getName(), 
				packageInfo.getVersion(),
				packageInfo.getPath());
	}


	private static void printParseableSub(final Dependency parent, int level) {
		printParseableSub(parent.getPackageInfo(), level);
		
		for (Dependency dependency : parent.getDependencies()) {
			printParseableSub(dependency, level + 1);
		}
	}

	/**
	 * Prints a list of package dependencies
	 * @param parent a list of package dependencies
	 */
	public static void printDependencies(final Dependency parent) {

		System.out.printf("%-15s    %-20s    %-15s  %-23s%n",
				"Group ID", "Package Name", "Version", "Install Date",
				"Install Dir");

		printParseableSub(parent, 0);
	}
}
