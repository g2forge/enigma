package org.stringtemplate.v4;

import java.util.function.Supplier;

@FunctionalInterface
public interface STAttributeGenerator extends Supplier<Object> {
	public static Object unwrap(Object value) {
		return (value instanceof STAttributeGenerator) ? ((STAttributeGenerator) value).get() : value;
	}
}
