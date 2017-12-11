package com.g2forge.enigma.javagen.type.expression;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

import com.g2forge.enigma.javagen.file.JavaFile;
import com.g2forge.enigma.javagen.type.decl.JavaTypeDeclaration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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

	public JavaType(JavaFile file, JavaTypeDeclaration declaration) {
		this.string = file.getPackageDeclaration().getName().stream().collect(Collectors.joining(".")) + "." + declaration.getName();
	}
}
