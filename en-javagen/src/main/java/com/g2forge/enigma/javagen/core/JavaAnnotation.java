package com.g2forge.enigma.javagen.core;

import com.g2forge.enigma.javagen.type.expression.JavaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaAnnotation {
	protected static final String TEMPLATE = "@<type>";

	protected JavaType type;
}
