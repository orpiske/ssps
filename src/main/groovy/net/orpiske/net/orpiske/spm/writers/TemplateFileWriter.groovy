package net.orpiske.net.orpiske.spm.writers

import groovy.text.GStringTemplateEngine

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 10/24/13
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
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
