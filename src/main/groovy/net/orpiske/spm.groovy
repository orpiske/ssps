package net.orpiske

import net.orpiske.spm.actions.TemplateParser
import net.orpiske.spm.actions.Eval;

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 10/17/13
 * Time: 2:45 PM
 * To change this templateFile use File | Settings | File Templates.
 */
// @Grab(group='commons-cli', module='commons-cli', version='1.2')

public class spm {
	private static void help(int code) {
		System.out.println("Usage: spm <action>\n");

		System.out.println("Actions:");
		System.out.println("   templateFile");
		System.out.println("   eval");
		System.out.println("----------");
		System.out.println("   help");
		System.out.println("   --version");

		System.exit(code);
	}

	public static void main(String[] args) {

		if (args.length == 0) {
			help(1);
		}

		String first = args[0];
		String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

		if (first.equals("help")) {
			help(1);
		}
		
		if (args == null || args.length == 0) {
			println "Invalid parameters";
			
			System.exit(1);
		}
		
		if (first.equals("templateFile")) {
			TemplateParser templateParser = new TemplateParser(newArgs);

			templateParser.run();
		}

		if (first.equals("eval")) {
			Eval eval = new Eval(newArgs);
			
			eval.run();
		}
	}

}