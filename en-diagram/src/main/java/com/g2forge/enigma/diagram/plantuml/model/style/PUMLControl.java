package com.g2forge.enigma.diagram.plantuml.model.style;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PUMLControl {
	public enum Color {
		Transparent;

		protected static final String TEMPLATE = "<name;format=\"lower\">";
	}

	protected static final String TEMPLATE = "<if(dpi)>skinparam dpi <dpi><\\n><endif>skinparam shadowing <shadowing><\\n><if(background)>skinparam backgroundcolor <background><\\n><endif><if(scale)>scale <scale>x<scale><\\n><endif>";

	protected final Integer dpi;

	protected final boolean shadowing;

	/**
	 * Set the size in pixels for the larger dimension. Setting this to a larger value will help you get a higher resolution image.
	 */
	protected final Integer scale;

	protected final Color background;
}
