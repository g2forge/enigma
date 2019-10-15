package com.g2forge.enigma.stringtemplate;

import java.util.function.Function;

import com.g2forge.alexandria.adt.associative.cache.Cache;
import com.g2forge.alexandria.adt.associative.cache.LRUCacheEvictionPolicy;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.reflection.record.v2.reflection.ReflectedRecordType;

import lombok.AccessLevel;
import lombok.Getter;

public class EmbeddedTemplateRenderer {
	/** A function which is used to abstract a record from a java type. This must be initialized before {@link #DEFAULT} or {@link #STRING}. */
	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private static final IFunction1<? super Class<?>, ? extends ReflectedRecordType> recordFunction = new IFunction1<Class<?>, ReflectedRecordType>() {
		protected final IFunction1<? super Class<?>, ? extends ReflectedRecordType> cache = new Cache<>(ReflectedRecordType::new, new LRUCacheEvictionPolicy<>(30));

		@Override
		public synchronized ReflectedRecordType apply(Class<?> argument) {
			return cache.apply(argument);
		}
	};

	public final static EmbeddedTemplateRenderer DEFAULT = new EmbeddedTemplateRenderer();

	public final static EmbeddedTemplateRenderer STRING = new EmbeddedTemplateRenderer("\n");

	public static String toString(Object object) {
		return DEFAULT.render(object);
	}

	protected final STGroupJava group;

	public EmbeddedTemplateRenderer() {
		this(System.lineSeparator());
	}

	protected EmbeddedTemplateRenderer(char delimiterStartChar, char delimiterStopChar, String lineSeparator, Function<? super Object, ? extends Object> adapter) {
		this.group = new STGroupJava("UTF-8", delimiterStartChar, delimiterStopChar, lineSeparator, adapter, getRecordFunction());
	}

	public EmbeddedTemplateRenderer(String lineSeparator) {
		this(lineSeparator, null);
	}

	public EmbeddedTemplateRenderer(String lineSeparator, Function<? super Object, ? extends Object> adapter) {
		this('<', '>', lineSeparator, adapter);
	}

	public String render(Object object) {
		return group.render(object);
	}
}
