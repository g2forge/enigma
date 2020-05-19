package com.g2forge.enigma.diagram.plantuml.model.klass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PUMLClassName {
	protected static final String TEMPLATE = "<if(quoteName)>\"<endif><name><if(quoteName)>\"<endif>";

	protected final String name;

	protected boolean isQuoteName() {
		return !name.matches("[a-zA-Z_][0-9a-zA-Z_]*");
	}
}
