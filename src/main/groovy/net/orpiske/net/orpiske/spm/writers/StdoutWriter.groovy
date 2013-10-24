package net.orpiske.net.orpiske.spm.writers

import groovy.text.GStringTemplateEngine

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 10/24/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
class StdoutWriter implements Writer {
	void write(Map<String, String> binding, def template) {
		println template.toString();
	}
}
