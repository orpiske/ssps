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
package net.orpiske

import net.orpiske.spm.actions.TemplateParser
import net.orpiske.spm.actions.Eval;


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

		try { 
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
			
			System.exit(0)
		}
		catch (Throwable t) {
			println t.getMessage();
			t.printStackTrace();
			
			System.exit(1)
		}
	}

}