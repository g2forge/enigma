package com.g2forge.enigma.javagen.codegen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.io.RuntimeIOException;

public class JavaRenderer {
	protected static final Pattern JAVA_LANG_TYPE = Pattern.compile("java\\.lang\\.[A-Za-z0-9_]+");

	protected final STGroup group = new STGroupDir(getClass().getResource(""), "UTF-8", '<', '>');

	public void render(final Path root, final JavaFile file) {
		final Path path = root.resolve(file.getPackageName().replaceAll("\\.", "/")).resolve(file.getDeclaration().getName() + ".java");
		try {
			Files.createDirectories(path.getParent());
			try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
				writer.append(render(file));
			}
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}

	public String render(final JavaFile file) {
		final Set<JavaType> imported = (file.getImports() == null) ? Collections.emptySet() : file.getImports().stream().map(JavaImport::getType).collect(Collectors.toSet());
		try (final ICloseable temp = IJavaEnvironment.open(new IJavaEnvironment() {
			@Override
			public boolean isImported(JavaType type) {
				if (JAVA_LANG_TYPE.matcher(type.getType()).matches()) return true;
				return imported.contains(type);
			}
		})) {
			final ST template = group.getInstanceOf(file.getClass().getSimpleName());
			template.add("this", file);
			template.add("separator", null);
			return template.render();
		}
	}
}
