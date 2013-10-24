package net.orpiske.net.orpiske.spm.writers

import groovy.text.GStringTemplateEngine

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 10/24/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
class PackageFileWriter implements Writer {
	
	private String repositoryDir;
	
	public PackageFileWriter(String repositoryDir) {
		this.repositoryDir = repositoryDir;
	}
	
	private String getTargetFile(Map<String, String> binding) {
		StringBuilder builder = new StringBuilder();

		builder.append(repositoryDir);
		builder.append(File.separator);

		builder.append("packages");
		builder.append(File.separator);

		String groupId = binding.get("packageGroup");
		builder.append(groupId);
		builder.append(File.separator);

		String name = binding.get("packageName");
		builder.append(name);
		builder.append(File.separator);

		String version = binding.get("packageVersion");
		builder.append(version);
		builder.append(File.separator);

		builder.append("pkg");
		builder.append(File.separator);

		builder.append(name);
		builder.append(".groovy");
		
		return builder.toString();
	} 
	
	
	void write(Map<String, String> binding, def engine) {
		String path = getTargetFile(binding);
		File target = new File(path);
		
		TemplateFileWriter templateFileWriter = new TemplateFileWriter(target);

		templateFileWriter.write(binding, engine);
	}
}
