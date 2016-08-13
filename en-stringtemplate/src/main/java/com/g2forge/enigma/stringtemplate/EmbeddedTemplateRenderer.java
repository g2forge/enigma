package com.g2forge.enigma.stringtemplate;

import com.g2forge.alexandria.java.associative.cache.Cache;
import com.g2forge.alexandria.java.associative.cache.LRUCacheEvictionPolicy;
import com.g2forge.alexandria.reflection.record.v2.reflection.ReflectedRecordType;

public class EmbeddedTemplateRenderer {
	protected final static EmbeddedTemplateRenderer singleton = new EmbeddedTemplateRenderer(System.lineSeparator());

	public static String toString(Object object) {
		return singleton.render(object);
	}

	protected final STGroupJava group;

	public EmbeddedTemplateRenderer() {
		this(System.getProperty("line.separator"));
	}

	protected EmbeddedTemplateRenderer(char delimiterStartChar, char delimiterStopChar, String lineSeparator) {
		this.group = new STGroupJava("UTF-8", delimiterStartChar, delimiterStopChar, lineSeparator, new Cache<>(ReflectedRecordType::new, new LRUCacheEvictionPolicy<>(30)));
	}

	public EmbeddedTemplateRenderer(String lineSeparator) {
		this('<', '>', lineSeparator);
	}

	public String render(Object object) {
		return group.render(object);
	}
}
