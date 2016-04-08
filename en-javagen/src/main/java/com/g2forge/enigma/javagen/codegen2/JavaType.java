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
		return type.toString();
	}
}
