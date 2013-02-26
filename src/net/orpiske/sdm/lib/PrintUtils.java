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
package net.orpiske.sdm.lib;


/**
 * Standard out printing utilities for the rules
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class PrintUtils {
	
	public static void printStartStage(final String stageName) {
		System.out.println("\n** " + stageName + " Started **\n");
	}
	
	public static void printEndStage(final String stageName) {
		System.out.println("\n** " + stageName + " Completed **\n");
	}
	
	public static void printEndWithError(final String stageName, 
			final Exception e) 
	{
		System.out.println("\n** " + stageName + " Failed **\n ==> " + e.getMessage());
	}
	
	public static void printInfo(final String message) {
		System.out.println("[INFO]: " + message);
	}
	
	public static void staticInfoMessage(final String message) {
		System.out.print("\r[INFO]: " + message);
	}
	
	public static void staticWarningMessage(final String message) {
		System.out.println("[WARNING]: " + message);
	}

}
