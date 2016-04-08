package com.g2forge.enigma.javagen.codegen2;

import java.lang.reflect.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaType {
	protected static final String TEMPLATE = "<string>";

	protected Type type;

	protected String getString() {
		if (type instanceof Class) {
			final Class<?> klass = (Class<?>) type;
			if (klass.isPrimitive()) return klass.toString();
			if ("java.lang".equals(klass.getPackage().getName())) return klass.getSimpleName();
			return klass.getName();
		}
		return type.toString();
	}
}
