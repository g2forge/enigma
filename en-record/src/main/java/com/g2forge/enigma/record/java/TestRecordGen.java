package com.g2forge.enigma.record.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.command.ICommand;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HString;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.implementations.Tuple2G_O;
import com.g2forge.enigma.javagen.core.JavaAnnotation;
import com.g2forge.enigma.javagen.core.JavaPackageSpecifier;
import com.g2forge.enigma.javagen.file.JavaFile;
import com.g2forge.enigma.javagen.file.JavaImport;
import com.g2forge.enigma.javagen.type.decl.IJavaMember;
import com.g2forge.enigma.javagen.type.decl.JavaField;
import com.g2forge.enigma.javagen.type.decl.JavaField.JavaFieldBuilder;
import com.g2forge.enigma.javagen.type.decl.JavaProtection;
import com.g2forge.enigma.javagen.type.decl.JavaTypeDeclaration;
import com.g2forge.enigma.javagen.type.expression.JavaType;
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

public class TestRecordGen implements ICommand {
	@AllArgsConstructor
	public static class Name implements IName<String> {
		@Getter(AccessLevel.PROTECTED)
		protected final String name;

		@Override
		public String getLanguageName(Context context) {
			switch (context.getUsage()) {
				case Field:
				case Argument:
					return HString.lowercase(getName());
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
		if (lastDot >= 0) return new Tuple2G_O<>(string.substring(0, lastDot), string.substring(lastDot + 1));
		return new Tuple2G_O<>(null, string);
	}

	public static void main(String[] args) {
		final EmbeddedTemplateRenderer renderer = EmbeddedTemplateRenderer.STRING;

		final Record<String, JavaType> record = new Record<>(Arrays.asList(new Property<>(new Name("Property"), new Type(new JavaType(String.class)))), new Type(new JavaType("org.Foo")));

		final Context context = new Context(Context.Usage.Field);
		final Collection<IJavaMember> members = new ArrayList<>();
		for (IProperty<String, JavaType> property : record.getProperties()) {
			final JavaFieldBuilder fieldBuilder = JavaField.builder();
			fieldBuilder.protection(JavaProtection.Protected);
			fieldBuilder.type(property.getType().getLanguageType(context));
			fieldBuilder.name(property.getName().getLanguageName(context));
			members.add(fieldBuilder.build());
		}

		// TODO: Move this "break" functionality into JavaType?
		final ITuple2G_<String, String> type = breakTypeName(record.getLanguageType(new Context(Context.Usage.Declaration)).getString());
		// TODO: Automatically import types
		// TODO: For imported types, render them with their simple names (ideally should handle wildcards too!)
		final JavaType data = new JavaType(Data.class);
		// TODO: @AllArgsConstructor, @NoArgsConstructor, @Accessors(chain = true)
		final JavaTypeDeclaration javaClass = JavaTypeDeclaration.standardBuilder().name(type.get1()).members(members).annotation(new JavaAnnotation(new JavaType("Data"))).build();
		System.out.println(renderer.render(new JavaFile(new JavaPackageSpecifier(type.get0()), Arrays.asList(new JavaImport(data)), HCollection.asList(javaClass))));
	}
}
