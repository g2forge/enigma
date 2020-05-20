package com.g2forge.enigma.diagram.plantuml.model.klass;

import java.util.List;

import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLClass {
	public enum MetaType {
		Class,
		Enum
	}

	public static class PUMLClassBuilder implements IBuilder<PUMLClass> {
		public PUMLClassBuilder stereotypeNamed(String name) {
			return stereotype(new PUMLNamedStereotype(name));
		}
		
		public PUMLClassBuilder stereotypeSpot(char letter, String color) {
			return stereotype(new PUMLSpotStereotype(letter, color));
		}
	}

	public static PUMLClassBuilder builder() {
		return new PUMLClassBuilder();
	}

	public static PUMLClassBuilder builder(String name) {
		return builder().name(name);
	}

	@Builder.Default
	protected final MetaType metaType = MetaType.Class;

	protected final String name;

	@Singular
	protected final List<IPUMLStereotype> stereotypes;

	@Singular
	protected final List<String> members;
}
