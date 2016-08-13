package com.g2forge.enigma.record.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;
import com.g2forge.alexandria.java.core.helpers.StringHelpers;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.Tuple2G_;
import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaPackageSpecifier;
import com.g2forge.enigma.javagen.file.JavaFile;
import com.g2forge.enigma.javagen.file.JavaImport;
import com.g2forge.enigma.javagen.type.IJavaMember;
import com.g2forge.enigma.javagen.type.JavaClass;
import com.g2forge.enigma.javagen.type.JavaField;
import com.g2forge.enigma.javagen.type.JavaProtection;
import com.g2forge.enigma.javagen.type.JavaType;
import com.g2forge.enigma.record.IProperty;
import com.g2forge.enigma.record.Property;
import com.g2forge.enigma.record.Record;
import com.g2forge.enigma.record.core.Context;
import com.g2forge.enigma.record.core.IName;
import com.g2forge.enigma.record.core.IType;
import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class TestRecordGen {
	@AllArgsConstructor
	public static class Name implements IName<String> {
		@Getter(AccessLevel.PROTECTED)
		protected final String name;

		@Override
		public String getLanguageName(Context context) {
			switch (context.getUsage()) {
				case Field:
				case Argument:
					return StringHelpers.lowercase(getName());
				case Return:
					return "retVal";
				default:
					throw new UnsupportedOperationException();
			}
		}
	}

	@AllArgsConstructor
	public static class Type implements IType<String, JavaType> {
		@Getter(AccessLevel.PROTECTED)
		protected final JavaType type;

		@Override
		public JavaType getLanguageType(Context configuration) {
			return getType();
		}
	}

	public static ITuple2G_<String, String> breakTypeName(String string) {
		final int lastDot = string.lastIndexOf('.');
		if (lastDot >= 0) return new Tuple2G_<>(string.substring(0, lastDot), string.substring(lastDot + 1));
		return new Tuple2G_<>(null, string);
	}

	public static void main(String[] args) {
		final EmbeddedTemplateRenderer renderer = new EmbeddedTemplateRenderer("\n");

		final Record<String, JavaType> record = new Record<>(Arrays.asList(new Property<>(new Name("Property"), new Type(new JavaType(String.class)))), new Type(new JavaType("org.Foo")));

		final Context context = new Context(Context.Usage.Field);
		final Collection<IJavaMember> members = new ArrayList<>();
		for (IProperty<String, JavaType> property : record.getProperties()) {
			members.add(new JavaField(property.getType().getLanguageType(context), property.getName().getLanguageName(context)).setProtection(JavaProtection.Protected));
		}

		// TODO: Move this "break" functionality into JavaType?
		final ITuple2G_<String, String> type = breakTypeName(record.getLanguageType(new Context(Context.Usage.Declaration)).getString());
		// TODO: Automatically import types
		// TODO: For imported types, render them with their simple names (ideally should handle wildcards too!)
		final JavaType data = new JavaType(Data.class);
		// TODO: @AllArgsConstructor, @NoArgsConstructor, @Accessors(chain = true)
		final JavaClass javaClass = new JavaClass(null, JavaProtection.Public, type.get1(), members).setAnnotations(Arrays.asList(new JavaAnnotation(new JavaType("Data"))));
		System.out.println(renderer.render(new JavaFile(new JavaPackageSpecifier(type.get0()), Arrays.asList(new JavaImport(data)), CollectionHelpers.asList(javaClass))));
	}
}
