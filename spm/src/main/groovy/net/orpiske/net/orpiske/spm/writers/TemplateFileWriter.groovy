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
package net.orpiske.net.orpiske.spm.writers

class TemplateFileWriter implements Writer {
	private File outputFile;
	
	public TemplateFileWriter(File outputFile) {
		this.outputFile = outputFile;
	}

	void write(Map<String, String> binding, def engine) {
		FileWriter fileWriter = null;
		
		try { 
		
			if (!outputFile.getParentFile().exists()) { 
				println "Creating parent directories: " + outputFile.getParentFile().getPath();
				
				outputFile.getParentFile().mkdirs();
			}
			
			fileWriter = new FileWriter(outputFile);
	
			String text = engine.toString();
			
			fileWriter.write(text);
			fileWriter.flush();
		}
		finally {
			
			if (fileWriter != null) { 
				fileWriter.close();
			}
		}
		
	}
}
