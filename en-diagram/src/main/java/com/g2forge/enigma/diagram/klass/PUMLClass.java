package com.g2forge.enigma.diagram.klass;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class PUMLClass {
	public enum MetaType {
		Class,
		Enum;

		protected static final String TEMPLATE = "<name;format=\"lower\">";
	}

	protected static final String TEMPLATE = "<metaType> <if(quoteName)>\"<endif><name><if(quoteName)>\"<endif><if(stereotypes)> \\<\\< <stereotypes;separator=\" \"> >\\><endif><if(members)> {<\\n><members:{m|<\\t><m><\\n>}>}<endif>";

	public static final String SPOT_C_COLOR = "#ADD1B2";

	public static String createSpotStereotype(String letter, String color) {
		return "(" + letter + "," + color + ")";
	}

	@Builder.Default
	protected final MetaType metaType = MetaType.Class;

	protected final String name;

	@Singular
	protected final List<String> stereotypes;

	@Singular
	protected final List<String> members;

	protected boolean isQuoteName() {
		return !name.matches("[a-zA-Z_][0-9a-zA-Z_]*");
	}
}
