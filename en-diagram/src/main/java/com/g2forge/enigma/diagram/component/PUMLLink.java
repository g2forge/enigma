package com.g2forge.enigma.diagram.component;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class PUMLLink {
	public enum Modifier {
		Hidden;
	}

	protected static final String TEMPLATE = "<leftMangled> -<if(modifiers)>[<modifiers;separator=\",\">]<endif><if(vertical)>-<endif>\\> <rightMangled>";

	protected final String left;

	protected final boolean vertical;

	@Singular
	protected final Set<Modifier> modifiers;

	protected final String right;

	protected String getLeftMangled() {
		return PUMLComponent.mangle(getLeft());
	}

	protected String getRightMangled() {
		return PUMLComponent.mangle(getRight());
	}
}
