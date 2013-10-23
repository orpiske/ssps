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
package net.orpiske.spm.actions

class RegexUtils {
	
	public static String getName(String input) {
		def matcher = (input =~ /([a-zA-Z]+)([-_]?)([a-zA-Z]+)/);

		if (matcher.getCount() > 0) {
			return matcher[0][0];
		}
		
		printf("Input %s does not match %s%n", input, /([a-zA-Z]+)([-_]?)([a-zA-Z]+)/);
		return null;
	}

	public static String getVersion(String input) {
		def matcher = (input =~ /([0-9]+).([0-9]+).([0-9]+)(-(SNAPSHOT|RELEASE|STABLE|BETA|ALPHA|RC[0-9]?))?/)

		if (matcher.getCount() > 0) {
			return matcher[0][0];
		}

		printf("Input %s does not match %s%n", input, /([0-9]+).([0-9]+).([0-9]+)(-(SNAPSHOT|RELEASE|STABLE|BETA|ALPHA|RC[0-9]?))?/);
		return null;
	}
}
