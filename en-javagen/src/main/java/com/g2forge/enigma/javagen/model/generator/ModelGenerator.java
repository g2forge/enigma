package com.g2forge.enigma.javagen.model.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.g2forge.enigma.javagen.codegen.JavaRenderer;
import com.g2forge.enigma.javagen.codegen.JavaType;
import com.g2forge.enigma.javagen.model.Property;
import com.g2forge.enigma.javagen.model.Record;
import com.g2forge.enigma.javagen.model.Type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModelGenerator<PropertyFeature> {
	public static void main(String[] args) {
		final ModelGenerator<Object> generator = new ModelGenerator<>(type -> new JavaType(((Class<?>) type.getType()).getName()), (property, feature) -> property.getName());
		final Record parent = new Record("Parent", null, Arrays.asList(new Property(new Type(Integer.TYPE), "number")));
		generator.add(new Record("Child", Arrays.asList(parent), Arrays.asList(new Property(new Type(String.class), "string"))));
		generator.render(Paths.get(System.getProperty("user.dir"), "src/main/java"));
	}

	protected final Function<? super Type, ? extends JavaType> types;

	protected final BiFunction<? super Property, ? super PropertyFeature, ? extends String> namer;

	protected final BiFunction<? super Record, ? super Function<? super Type, ? extends JavaType>, ? extends RecordRendering> renderer = RecordRendering::new;

	protected final Map<Record, RecordRendering> map = new LinkedHashMap<>();

	public ModelGenerator<PropertyFeature> add(Record... records) {
		for (Record record : records)
			if (!map.containsKey(record)) map.put(record, renderer.apply(record, types));
		return this;
	}

	public void render(Path root) {
		final JavaRenderer renderer = new JavaRenderer();
		map.values().forEach(rendering -> renderer.render(root, rendering.getImplementation()));
	}
}
