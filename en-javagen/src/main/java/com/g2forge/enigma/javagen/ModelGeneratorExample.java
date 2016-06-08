package com.g2forge.enigma.javagen;

import java.nio.file.Paths;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.enigma.javagen.codegen.JavaType;
import com.g2forge.enigma.javagen.model.Property;
import com.g2forge.enigma.javagen.model.Record;
import com.g2forge.enigma.javagen.model.Type;
import com.g2forge.enigma.javagen.model.generator.ModelGenerator;

public class ModelGeneratorExample {
	public static void main(String[] args) {
		final ModelGenerator<Object> generator = new ModelGenerator<>(type -> new JavaType(((Class<?>) type.getType()).getName()), (property, feature) -> property.getName());
		final Record parent = new Record("Parent", null, CollectionHelpers.asList(new Property(new Type(Integer.TYPE), "number")));
		generator.add(new Record("Child", CollectionHelpers.asList(parent), CollectionHelpers.asList(new Property(new Type(String.class), "string"))));
		generator.render(Paths.get(System.getProperty("user.dir"), "src/main/java"));
	}
}
