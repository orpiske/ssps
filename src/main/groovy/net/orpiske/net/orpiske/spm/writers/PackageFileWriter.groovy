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
