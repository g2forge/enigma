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

	protected String string;

	public JavaType(Type type) {
		if (type instanceof Class) {
			final Class<?> klass = (Class<?>) type;
			if (klass.isPrimitive()) this.string = klass.toString();
			else if ("java.lang".equals(klass.getPackage().getName())) this.string = klass.getSimpleName();
			else this.string = klass.getName();
		} else this.string = type.toString();
	}
}
