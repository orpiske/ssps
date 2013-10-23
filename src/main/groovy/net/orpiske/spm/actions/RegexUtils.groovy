package net.orpiske.spm.actions

import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 10/23/13
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
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
