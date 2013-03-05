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

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

/**
 * Utility class to execute commands
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class Executable {
	

	/**
	 * Executes a command
	 * @param command the command to execute
	 * @param arguments the arguments to pass to the command
	 * @return the return code from the command
	 * @throws ExecuteException
	 * @throws IOException
	 */
	public static int exec(final String command, final String arguments) throws ExecuteException, IOException {
		CommandLine cmd = new CommandLine(command);
		
		cmd.addArguments(arguments);
		
		DefaultExecutor executor = new DefaultExecutor();
		
		int exitValue = executor.execute(cmd);
		return exitValue;
	}
}
