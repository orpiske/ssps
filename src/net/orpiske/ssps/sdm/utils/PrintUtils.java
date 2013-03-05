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

import net.orpiske.ssps.common.registry.SoftwareInventoryDto;
import net.orpiske.ssps.common.repository.PackageInfo;

/**
 * Print utilities
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class PrintUtils {
	
	private PrintUtils() {}

	/**
	 * Prints package information
	 * @param packageInfo
	 */
	public static void print(final PackageInfo packageInfo) {
		System.out.println("Group ID: " + packageInfo.getGroupId());
		System.out.println("Name: " + packageInfo.getName());
		System.out.println("Version: " + packageInfo.getVersion());
		System.out.println("Type: " + packageInfo.getPackageType());
		System.out.println("File: " + packageInfo.getPath());
	}
	
	
	/**
	 * Prints parseable package information
	 * @param packageInfo
	 */
	public static void printParseable(final PackageInfo packageInfo) {
		System.out.printf("%-15s => %-32s => %-9s => %-1s => %s\n", 
				packageInfo.getGroupId(), packageInfo.getName(), packageInfo.getVersion(),
				packageInfo.getPackageType(), packageInfo.getPath());
	}
	
	
	/**
	 * Prints a list of packages
	 * @param list
	 * @param parseable
	 */
	public static void printPackageList(final List<PackageInfo> list, boolean parseable) {
		if (parseable) {
			System.out.printf("%-15s    %-32s    %-9s    %-1s      %s\n", 
					"Group ID", "Package Name", "Version", "Type", "Path");
		}
		
		for (PackageInfo packageInfo : list) {
			
			if (!parseable) { 
				System.out.println("------");
				print(packageInfo);
			}
			else {
				printParseable(packageInfo);
			}
		}
	}
	
	
	/**
	 * Prints a package list
	 * @param list
	 */
	public static void printPackageList(final List<PackageInfo> list) {
		printPackageList(list, false);
	}
	
	
	
	/**
	 * Prints a software inventory record
	 * @param dto
	 */
	public static void print(final SoftwareInventoryDto dto) {
		System.out.println("Group ID: " + dto.getGroupId());
		System.out.println("Name: " + dto.getName());
		System.out.println("Version: " + dto.getVersion());
		System.out.println("Type: " + dto.getType());
		System.out.println("Installation date: " + dto.getInstallDate());
		System.out.println("Installation directory: " + dto.getInstallDir());
	}
	
	
	
	/**
	 * Prints parseable software inventory record information
	 * @param dto
	 */
	public static void printParseable(final SoftwareInventoryDto dto) {
		System.out.printf("%-15s => %-20s => %-9s => %-1s => %-23s => %s\n", 
				dto.getGroupId(), dto.getName(), dto.getVersion(),
				dto.getType(), dto.getInstallDate(), dto.getInstallDir());
	}
	
	
	
	/**
	 * Prints a list of software inventory records
	 * @param list
	 * @param parseable
	 */
	public static void printInventoryList(final List<SoftwareInventoryDto> list, 
			boolean parseable) {
		
		if (parseable) {
			System.out.printf("%-15s    %-20s    %-9s   %-1s  %-23s    %s\n", 
					"Group ID", "Package Name", "Version", "Type", "Install Date", 
					"Install Dir");
		}
		
		for (SoftwareInventoryDto dto : list) {
			if (!parseable) {
				System.out.println("------");
				print(dto);
			}
			else {
				printParseable(dto);
			}
		}
	}
	
	
	
	/**
	 * Prints a list of software inventory records
	 * @param list
	 */
	public static void printInventoryList(final List<SoftwareInventoryDto> list) {
		printInventoryList(list, false);
	}
}
