package com.g2forge.enigma.javagen.codegen2;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JavaFile {
	protected static final String TEMPLATE = "package <packageDeclaration>;\n\n<if(imports)>\n<imports;separator=\"\\n\">\n\n<endif><if(types)>\n<types;separator=\"\\n\">\n<endif>";
	
	protected String packageDeclaration;

	protected Collection<JavaImport> imports;

	protected Collection<IJavaTypeDeclaration> types;
}
