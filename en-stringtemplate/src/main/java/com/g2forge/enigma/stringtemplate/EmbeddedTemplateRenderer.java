package com.g2forge.enigma.stringtemplate;

public class EmbeddedTemplateRenderer {
	protected final STGroupJava group;

	public EmbeddedTemplateRenderer() {
		this('<', '>');
	}

	public EmbeddedTemplateRenderer(char delimiterStartChar, char delimiterStopChar) {
		this.group = new STGroupJava("UTF-8", delimiterStartChar, delimiterStopChar);
	}

	public String render(Object object) {
		return group.render(object);
	}
}
