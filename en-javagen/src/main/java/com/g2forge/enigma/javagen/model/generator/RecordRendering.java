package com.g2forge.enigma.javagen.model.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.g2forge.enigma.javagen.TreeHelpers;
import com.g2forge.enigma.javagen.codegen.JavaAnnotation;
import com.g2forge.enigma.javagen.codegen.JavaClass;
import com.g2forge.enigma.javagen.codegen.JavaField;
import com.g2forge.enigma.javagen.codegen.JavaFile;
import com.g2forge.enigma.javagen.codegen.JavaProtection;
import com.g2forge.enigma.javagen.codegen.JavaType;
import com.g2forge.enigma.javagen.model.Record;
import com.g2forge.enigma.javagen.model.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRendering {
	protected JavaFile implementation;

	public RecordRendering(Record record, Function<? super Type, ? extends JavaType> types) {
		this();

		// Create the DFS post-order traversal of the parent classes
		final Stream<Record> records = TreeHelpers.dfs(record, Record::getParents, true);

		{ // Generate the implementation class for this record
			final List<JavaAnnotation> annotations = Arrays.asList(new JavaAnnotation(new JavaType(Data.class.getName())), new JavaAnnotation(new JavaType(AllArgsConstructor.class.getName())));
			final JavaClass implementation = new JavaClass(annotations, JavaProtection.Public, record.getName(), new ArrayList<>());
			records.flatMap(r -> r.getProperties().stream()).forEach(property -> {
				implementation.getMembers().add(new JavaField(null, JavaProtection.Protected, types.apply(property.getType()), property.getName()));
			});
			setImplementation(new JavaFile(null, null, implementation));
		}
	}
}