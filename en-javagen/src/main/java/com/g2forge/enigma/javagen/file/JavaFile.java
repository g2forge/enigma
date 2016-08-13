package com.g2forge.enigma.javagen.file;

import java.util.Collection;

import com.g2forge.enigma.javagen.core.JavaPackageSpecifier;
import com.g2forge.enigma.javagen.type.IJavaTypeDeclaration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaFile {
	protected static final String TEMPLATE = "package <packageDeclaration>;\n\n<if(imports)>\n<imports;separator=\"\\n\">\n\n<endif><if(types)>\n<types;separator=\"\\n\">\n<endif>";

	protected JavaPackageSpecifier packageDeclaration;

	protected Collection<JavaImport> imports;

	protected Collection<IJavaTypeDeclaration> types;
}
