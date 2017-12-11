package com.g2forge.enigma.javagen.codegen;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JavaFile {
	protected String packageName;

	protected Collection<JavaImport> imports;

	protected JavaClass declaration;
}
