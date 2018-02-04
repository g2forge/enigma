package com.g2forge.enigma.javagen.file;

import java.util.Collection;

import com.g2forge.enigma.javagen.core.JavaPackageSpecifier;
import com.g2forge.enigma.javagen.type.decl.IJavaTypeDeclaration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class JavaFile {
	protected static final String TEMPLATE = "package <packageDeclaration>;\n\n<if(imports)>\n<imports;separator=\"\\n\">\n\n<endif><if(types)>\n<types;separator=\"\\n\">\n<endif>";

	protected final JavaPackageSpecifier packageDeclaration;

	@Singular("import_")
	protected final Collection<JavaImport> imports;

	@Singular
	protected final Collection<IJavaTypeDeclaration> types;
}
