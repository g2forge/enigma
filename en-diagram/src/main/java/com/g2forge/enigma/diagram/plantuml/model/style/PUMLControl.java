package com.g2forge.enigma.diagram.plantuml.model.style;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class PUMLControl {
	protected final Integer dpi;

	protected final boolean shadowing;

	/**
	 * Set the size in pixels for the larger dimension. Setting this to a larger value will help you get a higher resolution image.
	 */
	protected final Integer px;

	protected final IPUMLColor background;
}
